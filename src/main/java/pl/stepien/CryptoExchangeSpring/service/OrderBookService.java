package pl.stepien.CryptoExchangeSpring.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class OrderBookService {


    private final TreeMap<Long, Long> bids = new TreeMap<>();
    private final TreeMap<Long, Long> asks = new TreeMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void processMessage(String jsonMessage) {

        try {
            Map<String, Object> message = objectMapper.readValue(jsonMessage, Map.class);

            if(message.containsKey("book")) {
                Map<String, Object> book = (Map<String, Object>) message.get("book");

                List<List<Integer>> askEntries = (List<List<Integer>>) book.get("asks");
                for(List<Integer> entry : askEntries) {
                    long price = entry.get(0);
                    long quantity = entry.get(1);

                    if (quantity == 0) {
                        asks.remove(price);
                    } else {
                        asks.put(price, quantity);
                    }
                }

                List<List<Integer>> bidEntries = (List<List<Integer>>) book.get("bids");
                for (List<Integer> entry : bidEntries) {
                    long price = entry.get(0);
                    long quantity = entry.get(1);

                    if (quantity == 0) {
                        bids.remove(price);
                    } else {
                        bids.put(price, quantity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void printOrderBook() {
        System.out.println("Asks:");
        for (Map.Entry<Long, Long> entry : asks.entrySet()) {
            System.out.println("Price: " + entry.getKey() + ", Quantity: " + entry.getValue());
        }

        System.out.println("Bids:");
        for (Map.Entry<Long, Long> entry : bids.entrySet()) {
            System.out.println("Price: " + entry.getKey() + ", Quantity: " + entry.getValue());
        }
    }



}
