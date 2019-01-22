package com.dino.dino;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    DrawView drawView;
    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkPermission()) requestPermissions();

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.dino);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        drawView = new DrawView(this);
        this.drawView.setBackgroundColor(-1);
        ((LinearLayout) this.findViewById(R.id.mainArea)).addView((View) this.drawView);

        final ToggleButton toggleButton = (ToggleButton) this.findViewById(R.id.filterButton);
        toggleButton.setChecked(true);
        this.drawView.setFilter(true);
        toggleButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                toggleButton.startAnimation(AnimationUtils.loadAnimation((Context) MainActivity.this, (int) R.anim.clickanim));
                MainActivity.this.drawView.setFilter(toggleButton.isChecked());
            }
        });
        final ImageButton imageButton3 = (ImageButton) this.findViewById(R.id.playButton);
        imageButton3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                imageButton3.startAnimation(AnimationUtils.loadAnimation((Context) MainActivity.this, (int) R.anim.clickanim));
                MainActivity.this.drawView.togglePlay();
                if (MainActivity.this.drawView.mIsPlaying) {
                    imageButton3.setImageResource(R.drawable.play);
                    return;
                }
                imageButton3.setImageResource(R.drawable.pause);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_a, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
        switch (paramMenuItem.getItemId()) {
            case R.id.menu_settings:
                finish();
                return true;
            default:
                return false;
        }
    }

    private boolean checkPermission() {

        int write_device_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_device_result == PackageManager.PERMISSION_GRANTED &&
                record_result == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);

    }
}
