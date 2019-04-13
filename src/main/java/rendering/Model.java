package rendering;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Model {
    String crafter;
    String name;
    String cheapest;
    String cheapestHQ;
    String priceToCraft;
    String nqProfit;
    String hqProfit;
    String averageNQHistory;
    String averageHQHistory;
    String numSoldInPastWeek;
    String historicalNQProfit;
    String historicalHQProfit;
    String profitScore;
    List<RecipeModel> recipeModels;
    String id;
}
