package org.trinity_services.cleaningchecklist.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.trinity_services.cleaningchecklist.R;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread animationThread = new Thread(new Runnable() {

            @Override
            public void run() {
                ImageView imgRotate = (ImageView) findViewById(R.id.imgSplashLogo);
                Log.d("Splash", "Image View Status:" + (imgRotate == null));
                imgRotate.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_animation));
            }
        });

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };
        Timer schedule = new Timer();
        animationThread.start();
        schedule.schedule(task,3500);
    }
}
