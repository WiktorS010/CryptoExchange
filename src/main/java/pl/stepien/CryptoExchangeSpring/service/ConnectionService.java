package pl.stepien.CryptoExchangeSpring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.stepien.CryptoExchangeSpring.connection.MyWebSocketClient;
import pl.stepien.CryptoExchangeSpring.model.ExchangeType;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ExchangeOrderBookService exchangeOrderBookService;

    public void connectAndSubscribe(String symbol, URI uri) {

        MyWebSocketClient myWebSocketClient = new MyWebSocketClient(uri, exchangeOrderBookService);

        try {
            myWebSocketClient.setOnOpenListener(() -> subscribeOrderBook(symbol, myWebSocketClient));
            myWebSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribeOrderBook(String symbol, MyWebSocketClient myWebSocketClient) {
        if (myWebSocketClient.getExchangeType() == ExchangeType.PHEMEX) {
            myWebSocketClient.sendMessage(JSONMessagesService.createPhemexSubscribeMessage(symbol).toString());
        } else if (myWebSocketClient.getExchangeType() == ExchangeType.BYBIT) {
            myWebSocketClient.sendMessage(JSONMessagesService.createBybitSubscribeMessage(symbol).toString());
        } else if (myWebSocketClient.getExchangeType() == ExchangeType.BINANCE) {
            myWebSocketClient.sendMessage(JSONMessagesService.createBinanceSubscribeMessage(symbol).toString());
        }
        else {
            log.info("Problem with subscribing check symbol or exchange uri");
        }
        log.info("Subscribed to order book for: " + symbol);
    }


}
