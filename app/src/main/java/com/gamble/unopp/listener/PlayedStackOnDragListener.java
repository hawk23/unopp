package com.gamble.unopp.listener;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gamble.unopp.R;

/**
 * Created by Mario on 05.05.2015.
 */
public class PlayedStackOnDragListener implements View.OnDragListener {

    private int colorDragOver;

    public PlayedStackOnDragListener (int colorDragOver) {

        this.colorDragOver = colorDragOver;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        View view = (View) event.getLocalState();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(this.colorDragOver);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundColor(Color.TRANSPARENT);
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup

                LinearLayout owner = (LinearLayout) view.getParent();
                owner.removeView(view);

                FrameLayout container = (FrameLayout) v;
                container.addView(view);
                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(Color.TRANSPARENT);
                view.setVisibility(View.VISIBLE);
            default:
                break;
        }
        return true;
    }
}
