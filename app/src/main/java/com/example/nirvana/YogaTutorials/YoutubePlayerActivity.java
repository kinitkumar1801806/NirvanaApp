package com.example.nirvana.YogaTutorials;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.nirvana.Model.PlayerConfig;
import com.example.nirvana.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;
    public String link;
    private boolean mAutoRotation = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        mAutoRotation = Settings.System.getInt(getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        link=intent.getStringExtra("link");
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubeView.initialize(PlayerConfig.API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if(mAutoRotation) {
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                    | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        }else {
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        }
        if (!wasRestored) {
            player.loadVideo(link);
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(PlayerConfig.API_KEY, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_in_bottom);
    }
}