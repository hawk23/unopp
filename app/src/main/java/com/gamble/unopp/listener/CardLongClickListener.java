package com.gamble.unopp.listener;

import android.content.ClipData;
import android.view.View;
import android.widget.ImageView;

import com.gamble.unopp.R;

/**
 * Created by Mario on 05.05.2015.
 */
public class CardLongClickListener implements View.OnLongClickListener {

    private ImageView card;

    public CardLongClickListener(ImageView card) {

        this.card = card;
    }

    @Override
    public boolean onLongClick(View v) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(this.card);
        this.card.startDrag(data, shadowBuilder, this.card, 0);
        this.card.setVisibility(View.INVISIBLE);
        return true;
    }
}
