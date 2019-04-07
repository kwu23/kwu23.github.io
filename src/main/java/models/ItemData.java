package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemData {
    private long itemId;
    private String singularName;
    private String pluralName;
    private int startsWithVowel;
    private String description;
    private String name;
    private String imageId;
    private long itemLevel;
    private long rarity;
}
