package pl.stepien.CryptoExchangeSpring.util;


import pl.stepien.CryptoExchangeSpring.model.Order;
import pl.stepien.CryptoExchangeSpring.model.OrderBook;

import java.util.ArrayList;
import java.util.List;

public class OrderBookManager implements OrderBookHandler {
    private final OrderBook orderBook;

    public OrderBookManager(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public void updateOrderBook(OrderBook newOrderBook) {
        orderBook.getAsks().clear();
        orderBook.getBids().clear();
        orderBook.setAsks(newOrderBook.getAsks());
        orderBook.setBids(newOrderBook.getBids());

    }

    @Override
    public void onOrderAddad(Order order, String bidOrAsk) {
        if (bidOrAsk.equals("ask")) {
            orderBook.getAsks().put(order.getPrice(), order.getQuantity());
        } else if (bidOrAsk.equals("bid")){
            orderBook.getBids().put(order.getPrice(), order.getQuantity());
        }

    }

    @Override
    public void onOrderRemoved(Order order) {
        //TODO
    }

    @Override
    public OrderBook getCurrentOrders() {
        return orderBook;
    }
}
