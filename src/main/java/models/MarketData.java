package models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MarketData {
    private List<ItemHistory> History;
    private String ID;
    private Item Item;
    private long ItemID;
    private List<ItemAvailable> Prices;
    private long Server;
    private long Updated;
}
