package com.likesdogscam.cam.soundboardbase.cells;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.likesdogscam.cam.soundboardbase.R;
import com.likesdogscam.cam.soundboardbase.models.SoundClip;

/**
 * Created by likesdogscam on 1969-04-20.
 */

public class SoundClipCell extends RecyclerView.ViewHolder {
    private Button playSoundClipButton;

    private Context   context;
    private SoundClip soundClip;
    private Delegate  delegate;

    public interface Delegate {
        void onSingleTap(SoundClip soundClip);

        void onLongPress(SoundClip soundClip);
    }

    public SoundClipCell(View itemView, Delegate delegate, Context context) {
        super(itemView);
        this.delegate = delegate;
        this.playSoundClipButton = (Button) itemView.findViewById(R.id.playSoundClipButton);
        this.context = context;
    }

    public void bindSelf(final SoundClip soundClip){
        this.soundClip = soundClip;
        this.playSoundClipButton.setText(this.soundClip.getDescription());
        final GestureDetector gestureDetector = new GestureDetector(this.context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                if(delegate != null){
                    delegate.onLongPress(soundClip);
                }
                else {
                    super.onLongPress(e);
                }
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if(delegate != null){
                    delegate.onSingleTap(soundClip);
                    return true;
                }
                return super.onSingleTapUp(e);
            }
        });
        this.playSoundClipButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }
}
