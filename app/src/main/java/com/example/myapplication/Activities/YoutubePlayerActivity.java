package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.myapplication.PlayerUIController.PlayerUIController;
import com.example.myapplication.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
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

//        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtubePlayer);
//        TextView videoName = findViewById(R.id.videoChapterName);
//
//        getLifecycle().addObserver(youTubePlayerView);
//        youTubePlayerView.setEnableAutomaticInitialization(false);
//
//        View controlsUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.custom_controls);

//        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer player) {
//                super.onReady(player);
//                PlayerUIController controller = new PlayerUIController(controlsUi, youTubePlayer, youTubePlayerView);
//                youTubePlayer = player;
//                player.addListener(controller);
//
//                for (int i =0;i<8;i++){
//                    if(position == i){
//                        YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), videoIds[i], 0F);
//                        videoName.setText(String.format("Video %s", chapter));
//                    }
//                }
//
//            }
//        });

//        YouTubePlayerListener youTubePlayerListener = new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                super.onReady(youTubePlayer);
//                PlayerUIController controller = new PlayerUIController(controlsUi, youTubePlayer, youTubePlayerView);
//                youTubePlayer.addListener(controller);
//                YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(),videoIds[1], 0F);
//            }
//        };
//
//        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();
//        youTubePlayerView.initialize(youTubePlayerListener, options);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        WebView webView = findViewById(R.id.webView);
        String video1 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/";
        String video2 = "title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";

        for (int i =0;i<8;i++){
            if(position == i){
                webView.loadData(video1+videoIds[i]+video2, "text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
//                videoName.setText(String.format("Video %s", chapter));
            }
        }

    }
//    private void loadNewVideo(String videoId) {
//        if (youTubePlayer != null) {
//            youTubePlayer.loadVideo(videoId, 0);
//        }
//    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}