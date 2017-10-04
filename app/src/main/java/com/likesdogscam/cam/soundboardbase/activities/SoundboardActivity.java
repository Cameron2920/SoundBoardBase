package com.likesdogscam.cam.soundboardbase.activities;

import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.likesdogscam.cam.soundboardbase.adapters.SoundClipAdapter;
import com.likesdogscam.cam.soundboardbase.cells.SoundClipCell;
import com.likesdogscam.cam.soundboardbase.helpers.SoundClipHelper;
import com.likesdogscam.cam.soundboardbase.models.SoundClip;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.likesdogscam.cam.soundboardbase.adapters.SoundClipAdapter.DEFAULT_SOUND_CLIP_CELL_ID;

public class SoundboardActivity extends AppCompatActivity implements SoundClipCell.Delegate{
    private static String TAG = "SoundboardActivity";
    private static int MAX_AUDIO_STREAMS = 20;

    private String soundClipDirectory;
    private String soundClipDescriptionPath;

    private List<SoundClip> soundClips;
    private Map<Integer, SoundClip> sampleIdSoundClipMap;
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initialize(RecyclerView soundClipRecyclerView, String soundClipDescriptionPath, String soundClipDirectory, int soundClipCellId){
        this.soundClipDirectory = soundClipDirectory;
        this.soundClipDescriptionPath = soundClipDescriptionPath;
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        sampleIdSoundClipMap = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createSoundPool();
        } else {
            createDeprecatedSoundPool();
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                SoundClip soundClip = sampleIdSoundClipMap.get(sampleId);

                if(soundClip != null){
                    soundClip.setSampleLoaded(true);
                    soundPool.play(soundClip.getSampleId(), 1, 1, 0, 0, 1);
                }
                else {
                    Log.e(TAG, "sound clip not found");
                }
            }
        });
        loadSoundClips();

        if(soundClips != null) {
            soundClipRecyclerView.setAdapter(new SoundClipAdapter(soundClips, this, soundClipCellId));
        }
        else {
            Log.e(TAG, "sound clips is null");
        }
    }

    public void initialize(RecyclerView soundClipRecyclerView, String soundClipDescriptionPath, String soundClipDirectory){
        initialize(soundClipRecyclerView, soundClipDescriptionPath, soundClipDirectory, DEFAULT_SOUND_CLIP_CELL_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(soundPool != null){
            soundPool.release();
            soundPool = null;
        }
    }

    @Override
    public void onPlaySoundClipButton(SoundClip soundClip) {
        if(soundClip.getSampleId() != -1) {
            if (soundClip.isSampleLoaded()) {
                soundPool.play(soundClip.getSampleId(), 1, 1, 0, 0, 1);
                Log.d(TAG, "playing sound clip " + soundClip.getDescription());
            }
        }
        else{
            try {
                soundClip.setSampleId(soundPool.load(getAssets().openFd(soundClip.getFilename()), 0));
                sampleIdSoundClipMap.put(soundClip.getSampleId(), soundClip);
            }
            catch (IOException e){
                Log.e(TAG, "unable to load sound clip", e);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(MAX_AUDIO_STREAMS)
                .build();
    }

    @SuppressWarnings("deprecation")
    protected void createDeprecatedSoundPool(){
        soundPool = new SoundPool(MAX_AUDIO_STREAMS, AudioManager.STREAM_MUSIC, 0);
    }

    private void loadSoundClips(){
        try {
            soundClips = SoundClipHelper.LoadSoundClips(getAssets().open(soundClipDescriptionPath), soundClipDirectory);
        }
        catch (IOException e){
            Log.e(TAG, "unable to load soundclips", e);
        }
    }
}
