package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemHistory {
    private long Added;
    private String CharacterID;
    private String CharacterName;
    private String ID;
    private boolean IsHQ;
    private long PricePerUnit;
    private long PriceTotal;
    private long PurchaseDate;
    private long PurchaseDateMS;
    private long Quanitity;
}
