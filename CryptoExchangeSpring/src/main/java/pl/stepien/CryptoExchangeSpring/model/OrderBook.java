package pl.stepien.CryptoExchangeSpring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class OrderBook {

    private final List<Order> orders;
}
