package pl.stepien.CryptoExchangeSpring.service;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONMessagesService {

    // PHEMEX
    public static JSONObject createPhemexPingMessage() {
        JSONObject pingMessage = new JSONObject();
        JSONArray paramsArray = new JSONArray();
        pingMessage.put("id", 0);
        pingMessage.put("method", "server.ping");
        pingMessage.put("params", paramsArray);
        return pingMessage;
    }

    public static JSONObject createPhemexSubscribeMessage(String symbol) {
        JSONObject subscribeMessage = new JSONObject();
        subscribeMessage.put("id", 1);
        subscribeMessage.put("method", "orderbook.subscribe");
        JSONArray paramsArray = new JSONArray();
        paramsArray.put(symbol);
        subscribeMessage.put("params", paramsArray);
        return subscribeMessage;
    }

    //BYBIT
    public static JSONObject createBybitPingMessage() {
        JSONObject pingMessage = new JSONObject();
        pingMessage.put("req_id", 0);
        pingMessage.put("op", "ping");
        return pingMessage;
    }

    public static JSONObject createBybitSubscribeMessage(String symbol) {
        JSONObject subscribeMessage = new JSONObject();
        JSONArray paramsArray = new JSONArray();
        paramsArray.put("orderbook.1." + symbol);
        subscribeMessage.put("req_id", "test");
        subscribeMessage.put("op", "subscribe");
        subscribeMessage.put("args", paramsArray);
        return subscribeMessage;
    }

    //BINANCE
    public static JSONObject createBinancePingMessage() {
        JSONObject pingMessage = new JSONObject();
        pingMessage.put("id", "922bcc6e-9de8-440d-9e84-7c80933a8d0d");
        pingMessage.put("method", "ping");
        return pingMessage;
    }

    public static JSONObject createBinanceSubscribeMessage(String symbol) {
        JSONObject subscribeMessage = new JSONObject();
        JSONArray paramsMessage = new JSONArray();
        paramsMessage.put(symbol.toLowerCase() + "@depth");
        paramsMessage.put(symbol.toLowerCase() + "@aggTrade");
        subscribeMessage.put("method", "SUBSCRIBE");
        subscribeMessage.put("params", paramsMessage);
        subscribeMessage.put("id", 1);
        return subscribeMessage;
    }

}
