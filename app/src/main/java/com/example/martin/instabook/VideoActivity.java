package com.example.martin.instabook;


import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class VideoActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private Boolean shouldAutoplay = true;
    private Handler mainHandler;


    private BandwidthMeter bandwidthMeter;

    private String videoUrl = null;

    private MediaSource buildMediaSource(Uri uri) {
        DefaultDataSourceFactory dataSource = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "Playlist"));
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSource).createMediaSource(uri);
        return mediaSource;
    }

    private void initPlayer() {
        playerView.requestFocus();
        TrackSelection.Factory videoTrackSelection = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelection);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        playerView.setPlayer(player);
        playerView.showController();

        MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl));

        player.prepare(mediaSource);
        player.setPlayWhenReady(shouldAutoplay);
    }

    private void releasePlayer() {
        shouldAutoplay = player.getPlayWhenReady();
        player.release();
        player = null;
        trackSelector = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (getIntent() != null){
            videoUrl = getIntent().getStringExtra(RecyclerAdapterVertical.URL_KEY).toString();
        }

        playerView = (PlayerView) findViewById(R.id.player_view);

        // TOTO ZREJME POJDE DO PICI
        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(Util.SDK_INT > 23) {
            initPlayer();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initPlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(Util.SDK_INT > 23) {
            releasePlayer();
        }

    }

}
