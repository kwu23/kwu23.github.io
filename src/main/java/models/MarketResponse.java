package models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MarketResponse {
    private ServerAndMarketDataHolder serverAndMarketDataHolder;

    @JsonAnySetter
    public void set(String server, MarketData marketData) {
        serverAndMarketDataHolder = new ServerAndMarketDataHolder(server, marketData);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class ServerAndMarketDataHolder {
        private String server;
        private MarketData marketData;
    }
}
