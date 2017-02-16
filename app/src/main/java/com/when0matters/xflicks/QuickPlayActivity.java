package com.when0matters.xflicks;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;

public class QuickPlayActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_play);


    }
}
