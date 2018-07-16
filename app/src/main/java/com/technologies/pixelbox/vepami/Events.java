package com.technologies.pixelbox.vepami;

public class Events {
    LoadInventoryDataListener eventListener;
    ItemEventListener itemEventListener;

    public void setOnLoadInventoryData(LoadInventoryDataListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setOnClickInventoryItem(ItemEventListener itemEventListener) {
        this.itemEventListener = itemEventListener;
    }
}
