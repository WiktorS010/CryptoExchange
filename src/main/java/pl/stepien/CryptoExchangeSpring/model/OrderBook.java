package pl.stepien.CryptoExchangeSpring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.TreeMap;

@Setter
@Getter
@AllArgsConstructor
public class OrderBook {

   private final TreeMap<Double, Double> bids = new TreeMap<>(Comparator.reverseOrder());
   private final TreeMap<Double, Double> asks = new TreeMap<>();
}
