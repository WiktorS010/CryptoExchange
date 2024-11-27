package pl.stepien.CryptoExchangeSpring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.stepien.CryptoExchangeSpring.connection.MyWebSocketClient;
import pl.stepien.CryptoExchangeSpring.service.OrderBookService;

import java.net.URI;

@Configuration
public class WebSocketConfig {
    @Bean
    public URI phemexWebSocketUri() {
        return URI.create("wss://testnet-api.phemex.com/ws");
    }

    @Bean
    public URI bybitWebsocketUri() {
        return URI.create("wss://stream.bybit.com/v5/public/spot");
    }
}
