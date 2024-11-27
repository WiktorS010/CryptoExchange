package pl.stepien.CryptoExchangeSpring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.TreeMap;

@Setter
@Getter
@AllArgsConstructor
public class OrderBook {

    TreeMap<Long, Long> bids = new TreeMap<>();
    TreeMap<Long, Long> asks = new TreeMap<>();
}
