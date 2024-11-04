package pl.stepien.CryptoExchangeSpring.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pl.stepien.CryptoExchangeSpring.connection.PhemexWebSocketClien;

import java.net.URI;

@Service
public class OrderBookService {

    private  PhemexWebSocketClien phemexWebSocketClien;

    public OrderBookService(PhemexWebSocketClien phemexWebSocketClien) {
        this.phemexWebSocketClien = phemexWebSocketClien;
    }


    public void connectAndSubscribe(String symbol) {
        // dealing with "WebSocketClient objects are not reuseable" Exception. Nie wiem czy to ok.
        if(phemexWebSocketClien !=  null) {
            phemexWebSocketClien.close();
        }
        // Create new websocket client
        try {
            URI uri = new URI("wss://testnet-api.phemex.com/ws");
            phemexWebSocketClien = new PhemexWebSocketClien(uri);

            phemexWebSocketClien.setOnOpenListener(() -> subscribeOrderBook(symbol));
            phemexWebSocketClien.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void subscribeOrderBook(String symbol) {
        JSONObject subscribeMessage = new JSONObject();
        subscribeMessage.put("id", 1); // Set an ID for the request, could be any integer
        subscribeMessage.put("method", "orderbook.subscribe");

        // Add parameters
        JSONArray paramsArray = new JSONArray();
        paramsArray.put(symbol);
        subscribeMessage.put("params", paramsArray);

        // Send the subscription request as a JSON string
        phemexWebSocketClien.sendMessage(subscribeMessage.toString());
        System.out.println("Subscribed to order book for: " + symbol);
    }

}
