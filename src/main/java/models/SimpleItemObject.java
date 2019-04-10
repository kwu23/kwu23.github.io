package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SimpleItemObject {
    private long id;
    private String name;
    private List<ItemHistory> history;
    private List<ItemAvailable> prices;

    public long getCheapestInHistory() {
        if (history != null && !history.isEmpty()) {
            return history.get(0).getPricePerUnit();
        }
        return -1;
    }

    public long getCheapestAvailable() {
        if (prices != null && !prices.isEmpty()) {
            return prices.get(0).getPricePerUnit();
        }
        return -1;
    }

    public long getCheapestHQInHistory() {
        if (history != null && !history.isEmpty() && history.stream().anyMatch(x -> x.isIsHQ())) {
            return history.stream().filter(x -> x.isIsHQ()).findFirst().get().getPricePerUnit();
        }
        return -1;
    }

    public long getCheapestHQAvailable() {
        if (prices != null && !prices.isEmpty() && prices.stream().anyMatch(x -> x.isIsHQ())) {
            return prices.stream().filter(x -> x.isIsHQ()).findFirst().get().getPricePerUnit();
        }
        return -1;
    }

    public long getAverageHistory() {
        long sum = 0;
        long total = 0;
        if (history != null && !history.isEmpty()) {
            for (ItemHistory itemHistory : history) {
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
        if (history != null && !history.isEmpty()) {
            for (ItemHistory itemHistory : history) {
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
        for (ItemHistory itemHistory : history) {
            if ((System.currentTimeMillis() - itemHistory.getPurchaseDateMS()) < 604800000) {
                numSold += itemHistory.getQuantity();
            }
        }

        if (numSold >= 100) {
            double currentTime = System.currentTimeMillis();
            double oldestPurchaseRecorded = history.get(history.size() - 1).getPurchaseDateMS();
            double fractionOfWeek = ((currentTime - oldestPurchaseRecorded) / 604800000.0);
            numSold = (long) ((double) numSold / fractionOfWeek);
        }

        return numSold;
    }

    public boolean isValid() {
        return prices != null && !prices.isEmpty() && history != null && !history.isEmpty();
    }
}
