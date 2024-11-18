package pl.stepien.CryptoExchangeSpring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class MyOrderBook {

    private final List<MyOrder> orders;
}
