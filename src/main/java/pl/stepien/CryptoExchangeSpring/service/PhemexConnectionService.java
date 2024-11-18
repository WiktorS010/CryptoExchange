package pl.stepien.CryptoExchangeSpring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stepien.CryptoExchangeSpring.connection.PhemexWebSocketClient;
import pl.stepien.CryptoExchangeSpring.model.JSONMessages;


@Service
@RequiredArgsConstructor
public class PhemexConnectionService {

    private final PhemexWebSocketClient phemexWebSocketClient;

    public void connectAndSubscribePhemex(String symbol) {
        try {
            phemexWebSocketClient.setOnOpenListener(() -> subscribePhemexOrderBook(symbol));
            phemexWebSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribePhemexOrderBook(String symbol) {
        phemexWebSocketClient.sendMessageToPhemex(JSONMessages.createPhemexSubscribeMessage(symbol).toString());
        System.out.println("Subscribed to order book for: " + symbol);
    }

}
