package pl.stepien.CryptoExchangeSpring.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.stepien.CryptoExchangeSpring.model.ExchangeType;
import pl.stepien.CryptoExchangeSpring.model.OrderBook;


import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
public class ExchangeOrderBookService {


    @Getter
    private final Map<ExchangeType, OrderBook> orderBooks = new EnumMap<>(ExchangeType.class);
    private final ObjectMapper objectMapper = new ObjectMapper();


    public ExchangeOrderBookService() {
        for (ExchangeType exchange : ExchangeType.values()) {
            orderBooks.put(exchange, new OrderBook());
        }
    }

    // PHEMEX
    public void processPhemexMessageToMap(String jsonMessage, ExchangeType exchangeType) {
        try {

            Map<String, Object> message = objectMapper.readValue(jsonMessage, Map.class);
            OrderBook orderBook = orderBooks.get(exchangeType);
            if (message.containsKey("book")) {
                Map<String, Object> book = (Map<String, Object>) message.get("book");

                List<List<Object>> askEntries = (List<List<Object>>) book.get("asks");
                processEntries(askEntries, orderBook.getAsks());

                List<List<Object>> bidEntries = (List<List<Object>>) book.get("bids");
                processEntries(bidEntries, orderBook.getBids());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //BYBIT

    public void processBybitMessageToMap(String jsonMessage, ExchangeType exchangeType) {
        try {
            Map<String, Object> message = objectMapper.readValue(jsonMessage, Map.class);
            OrderBook orderBook = orderBooks.get(exchangeType);

            if ("snapshot".equals(message.get("type")) || "delta".equals(message.get("type"))) {
                Map<String, Object> book = (Map<String, Object>) message.get("data");

                if (!book.isEmpty()) {
                    List<List<Object>> askEntries = (List<List<Object>>) book.get("a");
                    processEntries(askEntries, orderBook.getAsks());

                    List<List<Object>> bidEntries = (List<List<Object>>) book.get("b");
                    processEntries(bidEntries, orderBook.getBids());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // BINANCE
    public void processBinanceMessageToMap(String jsonMessage, ExchangeType exchangeType) {
        try {

            Map<String, Object> message = objectMapper.readValue(jsonMessage, Map.class);
            OrderBook orderBook = orderBooks.get(exchangeType);
            log.info("Processing message for {}: OrderBook hash: {}", exchangeType, System.identityHashCode(orderBook));

            if ("depthUpdate".equals(message.get("e"))) { // Bids and asks in exchange order book

                List<List<Object>> askEntries = (List<List<Object>>) message.get("a");
                log.info("Processing ASKS for Binance, depthUpdate");
                processEntries(askEntries, orderBook.getAsks());
                List<List<Object>> bidEntries = (List<List<Object>>) message.get("b");
                log.info("Processing BIDS for Binance, depthUpdate");
                processEntries(bidEntries, orderBook.getBids());

            } else if ("aggTrade".equals(message.get("e"))) {    // Executed transactions
                log.info("Processing aggTrade message: {}", message);
                processBinanceAggTradeMessage(message, orderBook);
            }
        } catch (Exception e) {
            log.error("Error processing Binance message: ", e);
        }
        log.info("Finished processing message for {}.", exchangeType);
    }


    private void processEntries(List<List<Object>> entries, Map<Double, Double> targetMap) {
        entries.forEach(entry -> {
            try {
                log.info("Processing entry: " + entry);
                double price = toDouble(entry.get(0));
                double quantity = toDouble(entry.get(1));

                if (quantity == 0) {
                    log.info("Removing price: " + price);
                    targetMap.remove(price);
                    return;
                } else {
                    log.info("Updating price: " + price + " with quantity: " + quantity);
                    targetMap.put(price, quantity);
                }
                log.info("Current state of targetMap: " + targetMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void processBinanceAggTradeMessage(Map<String, Object> message, OrderBook orderBook) {
        Boolean side = (Boolean) message.get("m");
        double price = toDouble(message.get("p"));
        double quantity = toDouble(message.get("q"));

        if (quantity != 0) {
            TreeMap<Double, Double> book = side ? orderBook.getBids() : orderBook.getAsks();

            synchronized (book) {
                Double currentQuantity = book.get(price);
                if (currentQuantity != null) {
                    double updatedQuantity = currentQuantity - quantity;

                    if (updatedQuantity > 0) {
                        book.put(price, updatedQuantity);
                        log.info("Updated {} at price {}: new quantity = {}", side ? "bid" : "ask", price, updatedQuantity);
                    } else {
                        book.remove(price);
                        log.info("Removed {} at price {}: quantity fully executed", side ? "bid" : "ask", price);
                    }
                } else {
                    log.warn("Price {} not found in {} book while processing aggTrade", price, side ? "bids" : "asks");
                }
            }
        } else {
            log.info("Received zero quantity for price: {}", price);
        }
    }


    private double toDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        } else {
            throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName());
        }
    }

    public void printOrderBook(ExchangeType exchangeType) {
        OrderBook orderBook = orderBooks.get(exchangeType);
        log.info("Exchange: " + exchangeType);
        log.info("Asks: " + orderBook.getAsks().size() + " asks size");
        orderBook.getAsks().forEach((key, value) ->
                log.info("Price: " + key + ", Quantity: " + value));
        log.info("Bids:" + orderBook.getBids().size() + " bids size");
        orderBook.getBids().forEach((key, value) ->
                log.info("Price: " + key + ",Quantity: " + value));
    }
}