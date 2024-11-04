package pl.stepien.CryptoExchangeSpring.connection;


import pl.stepien.CryptoExchangeSpring.service.OrderBookService;

import java.net.URI;

public class WebSocketTest {
    public static void main(String[] args) {
        try {
            URI uri = new URI("wss://testnet-api.phemex.com/ws");
            PhemexWebSocketClien client = new PhemexWebSocketClien(uri);
            OrderBookService orderBookService = new OrderBookService(client);
            orderBookService.connectAndSubscribe("BTCUSD");
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
