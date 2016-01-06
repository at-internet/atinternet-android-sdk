package com.atinternet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.atinternet.tracker.Tracker;

public class FirstScreenActivity extends Activity {

    Button goToSecondScreen;
    Button loopToFirstScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Tracker mySpecificTracker = new Tracker(this);

        mySpecificTracker.Screens().add("FirstScreen").sendView();
        loopToFirstScreen = (Button) findViewById(R.id.loopToFirstScreen);
        loopToFirstScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this, FirstScreenActivity.class));
            }
        });

        goToSecondScreen = (Button) findViewById(R.id.goToSecondScreenId);
        goToSecondScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this, SecondScreenActivity.class));
            }
        });
    }
}
