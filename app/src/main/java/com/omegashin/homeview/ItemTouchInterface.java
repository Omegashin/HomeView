package com.omegashin.homeview;

/**
 * Created by gdesi on 12-Jun-17.
 */

interface ItemTouchInterface {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}