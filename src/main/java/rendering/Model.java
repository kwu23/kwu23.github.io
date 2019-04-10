package rendering;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Model {
    String name;
    String cheapest;
    String cheapestHQ;
    String priceToCraft;
    String profit;
    String averageHistory;
    String numSoldInPastWeek;
    String historicalProfit;
    String profitScore;
}
