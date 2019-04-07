package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemAvailable {
    private long Added;
    private String CreatorSignatureID;
    private String CreatorSignatureName;
    private String ID;
    private boolean IsCrafted;
    private boolean IsHQ;
    //private List<Materia> Materia;
    private long PricePerUnit;
    private long PriceTotal;
    private long Quantity;
    private String RetainerID;
    private String RetainerName;
    private long StainID;
    private long TownID;
}
