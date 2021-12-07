package com.firstapp.sastamusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    Animation topAnim,bottomAnim;
    ImageView logo, anim_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Load Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        // Load Images
        logo=findViewById(R.id.logo);
        anim_txt=findViewById(R.id.anim_txt);

        // Set Animations
        logo.setAnimation(topAnim);
        anim_txt.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,SongsActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}