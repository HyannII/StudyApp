package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class YoutubePlayerActivity extends AppCompatActivity {
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        String [] videoIds = getResources().getStringArray(R.array.video_id);

        int position = getIntent().getIntExtra("position", 0);
        String chapter = getIntent().getStringExtra("name");

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtubePlayer);
        TextView videoName = findViewById(R.id.videoChapterName);

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                super.onReady(player);
                youTubePlayer = player;
                for (int i =0;i<8;i++){
                    if(position == i){
                        loadNewVideo(videoIds[i]);
                        videoName.setText(String.format("Video %s", chapter));
                    }
                }

            }
        });
    }
    private void loadNewVideo(String videoId) {
        if (youTubePlayer != null) {
            youTubePlayer.loadVideo(videoId, 0);
        }
    }
}