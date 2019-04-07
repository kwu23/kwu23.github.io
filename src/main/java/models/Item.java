package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    private long ID;
    private String Icon;
    private long LevelItem;
    private String Name;
    private String Name_de;
    private String Name_en;
    private String Name_fr;
    private String Name_ja;
    private long Rarity;
}