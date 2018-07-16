package com.technologies.pixelbox.vepami;

import android.view.View;

interface LoadInventoryDataListener {
    void callback(AdapterInventoryList inventoryList);


}

interface ItemEventListener {
    void onClickInventoryItem(View view);
}
