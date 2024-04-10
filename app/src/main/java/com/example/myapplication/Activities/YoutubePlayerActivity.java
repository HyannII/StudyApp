package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
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

        videoName.setText(String.format("Video %s", chapter));

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                super.onReady(player);
                youTubePlayer = player;
                for (int i =0;i<8;i++){
                    if(position == i){
                        loadNewVideo(videoIds[i]);
                    }
                }

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }
    private void loadNewVideo(String videoId) {
        if (youTubePlayer != null) {
            youTubePlayer.loadVideo(videoId, 0);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}