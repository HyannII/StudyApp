package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myapplication.FirebaseStorageUtils;
import com.example.myapplication.R;
import com.example.myapplication.Receiver.ConnectionReceiver;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class YoutubePlayerActivity extends AppCompatActivity {
    private YouTubePlayer youTubePlayer;

    String videoId;
    private ConnectionReceiver connectionReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        connectionReceiver = new ConnectionReceiver();

        ConnectionReceiver.registerReceiver(this, connectionReceiver);

        String uri = getIntent().getStringExtra("uri");
        String chapter = getIntent().getStringExtra("name");


        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtubePlayer);
        TextView videoName = findViewById(R.id.videoChapterName);

        videoName.setText(String.format(chapter));

        getLifecycle().addObserver(youTubePlayerView);
        FirebaseStorageUtils.downloadStringFromUrl(uri, new FirebaseStorageUtils.StringDownloadListener() {
            @Override
            public void onSuccess(String result) {
                System.out.println("Downloaded string: " + result);
                videoId = result;
            }

            @Override
            public void onFailure(Exception e) {
                // Xử lý lỗi khi tải dữ liệu
                e.printStackTrace();
            }
        });

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                super.onReady(player);
                youTubePlayer = player;
                loadNewVideo(videoId);
//                for (int i =0;i<8;i++){
//                    if(position == i){
//                        loadNewVideo(videoIds[i]);
//                    }
//                }

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
    @Override
    protected void onDestroy() {
        super.onDestroy();

        ConnectionReceiver.unregisterReceiver(this, connectionReceiver);
    }
}