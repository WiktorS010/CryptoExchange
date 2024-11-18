package pl.stepien.CryptoExchangeSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import pl.stepien.CryptoExchangeSpring.service.PhemexConnectionService;

@SpringBootApplication
public class CryptoExchangeSpringApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CryptoExchangeSpringApplication.class, args);

        PhemexConnectionService connectionService = context.getBean(PhemexConnectionService.class);
        connectionService.connectAndSubscribePhemex("BTCUSD");
    }
    }


