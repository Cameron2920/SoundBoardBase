package com.likesdogscam.cam.soundboardbase.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.likesdogscam.cam.soundboardbase.R;
import com.likesdogscam.cam.soundboardbase.cells.SoundClipCell;
import com.likesdogscam.cam.soundboardbase.models.SoundClip;

import java.util.List;

/**
 * Created by likesdogscam on 1969-04-20.
 */

public class SoundClipAdapter extends RecyclerView.Adapter {
    public static int       DEFAULT_SOUND_CLIP_CELL_ID = R.layout.cell_sound_clip;

    private int             soundClipCellId;
    private Context         context;
    private List<SoundClip> soundClips;
    private SoundClipCell.Delegate soundClipCellDelegate;

    public SoundClipAdapter(List<SoundClip> soundClips, SoundClipCell.Delegate soundClipCellDelegate, int soundClipCellId, Context context) {
        this.soundClips = soundClips;
        this.soundClipCellDelegate = soundClipCellDelegate;
        this.soundClipCellId = soundClipCellId;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SoundClipCell(LayoutInflater.from(parent.getContext()).inflate(this.soundClipCellId, parent, false), soundClipCellDelegate, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SoundClipCell)holder).bindSelf(this.soundClips.get(position));
    }

    @Override
    public int getItemCount() {
        return soundClips.size();
    }
}
