package pl.stepien.CryptoExchangeSpring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.stepien.CryptoExchangeSpring.connection.PhemexWebSocketClient;
import pl.stepien.CryptoExchangeSpring.service.OrderBookService;

import java.net.URI;

@Configuration
public class WebSocketConfig {
    @Bean
    public URI phemexWebSocketUri() {
        return URI.create("wss://testnet-api.phemex.com/ws");
    }

    @Bean
    public PhemexWebSocketClient phemexWebSocketClient(OrderBookService orderBookService, URI phemexWebSocketUri) {
        return new PhemexWebSocketClient(phemexWebSocketUri, orderBookService);
    }
}
