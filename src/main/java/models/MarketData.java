package models;

import lombok.Getter;
import lombok.Setter;

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

    public long getAverageHistory() {
        long sum = 0;
        long total = 0;
        if (History != null && !History.isEmpty()) {
            for (ItemHistory itemHistory : History) {
                if (((System.currentTimeMillis() - itemHistory.getPurchaseDateMS()) < 604800000)) {
                    sum += itemHistory.getPricePerUnit();
                    total++;
                }
            }
        }
        if (total == 0) {
            return 0;
        }
        return sum / total;
    }

    public long getAverageHQHistory() {
        long sum = 0;
        long total = 0;
        if (History != null && !History.isEmpty()) {
            for (ItemHistory itemHistory : History) {
                if (itemHistory.isIsHQ() && ((System.currentTimeMillis() - itemHistory.getPurchaseDateMS()) < 604800000)) {
                    sum += itemHistory.getPricePerUnit();
                    total++;
                }
            }
        }
        if (total == 0) {
            return 0;
        }
        return sum / total;
    }

    public long getAmountSoldLastWeek() {
        long numSold = 0;
        for (ItemHistory itemHistory : History) {
            if ((System.currentTimeMillis() - itemHistory.getPurchaseDateMS()) < 604800000) {
                numSold++;
            }
        }
        return numSold;
    }
}
