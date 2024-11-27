package pl.stepien.CryptoExchangeSpring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stepien.CryptoExchangeSpring.connection.MyWebSocketClient;
import pl.stepien.CryptoExchangeSpring.model.JSONMessages;

import java.net.URI;


@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final OrderBookService orderBookService;

    public void connectAndSubscribe(String symbol, URI uri) {

        MyWebSocketClient myWebSocketClient = new MyWebSocketClient(uri, orderBookService);

        try {
            myWebSocketClient.setOnOpenListener(() -> subscribeOrderBook(symbol, myWebSocketClient));
            myWebSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribeOrderBook(String symbol, MyWebSocketClient myWebSocketClient) {
        myWebSocketClient.sendMessage(JSONMessages.createPhemexSubscribeMessage(symbol).toString());
        System.out.println("Subscribed to order book for: " + symbol);
    }



}
