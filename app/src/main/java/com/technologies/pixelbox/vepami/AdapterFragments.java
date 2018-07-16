package com.technologies.pixelbox.vepami;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;



public class AdapterFragments extends FragmentStatePagerAdapter {
    FragmentWishlist wishlistTab;
    FragmentCart cartTab;
    FragmentInventory inventoryTab;
    LoadInventoryDataListener eventListener;
    int mNumOfTabs;

    public AdapterFragments(FragmentManager fm, int NumOfTabs, LoadInventoryDataListener eventListener) {
        super(fm);
        this.eventListener = eventListener;
        inventoryTab = new FragmentInventory();
        inventoryTab.setEventListener(eventListener);
        cartTab = new FragmentCart();
        wishlistTab = new FragmentWishlist();
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return inventoryTab;
            case 1:
                return cartTab;
            case 2:
                return wishlistTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}