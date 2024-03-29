package com.circle.circle_games.main;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.circle.circle_games.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideosFullScreenActivity extends AppCompatActivity {

    // id of the video which we are playing.
    String linkVideo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            linkVideo = b.getString("link_video");
        }

        // below two lines are used to set our screen orientation in landscape mode.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fullscreen_videos);

        // below line of code is to hide our action bar.
        getSupportActionBar().hide();

        // declaring variable for youtubePlayer view
        final YouTubePlayerView youTubePlayerView = findViewById(R.id.videoPlayer);

        // below line is to place your youtube player in a full screen mode (i.e landscape mode)
        youTubePlayerView.enterFullScreen();
        youTubePlayerView.toggleFullScreen();

        // here we are adding observer to our youtubeplayerview.
        getLifecycle().addObserver(youTubePlayerView);

        // below method will provides us the youtube player ui controller such
        // as to play and pause a video to forward a video and many more features.
        //youTubePlayerView.getPlayerUiController();

        // below line is to enter full screen mode.
        youTubePlayerView.enterFullScreen();
        youTubePlayerView.toggleFullScreen();

        // adding listener for our youtube player view.
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // loading the selected video into the YouTube Player
                youTubePlayer.loadVideo(linkVideo, 0);
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                // this method is called if video has ended,
                super.onStateChange(youTubePlayer, state);
            }
        });
    }
}
