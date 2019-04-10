package models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
