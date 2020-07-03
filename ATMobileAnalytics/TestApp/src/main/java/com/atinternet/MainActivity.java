package com.atinternet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Audio;
import com.atinternet.tracker.Gesture;
import com.atinternet.tracker.Screen;
import com.atinternet.tracker.Tracker;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Tracker tracker;
    private HashMap config = new HashMap<String, Object>() {{
        put("logSSL", "logs");
        put("log", "logp");
        put("domain", "xiti.com");
        put("pixelPath", "/hit.xiti");
        //put("identifier", "advertisingId");
        put("site", 552987);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.firstAction).setOnClickListener(this);
        findViewById(R.id.goToSecondScreen).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setConfig(config, null, true);
        tracker.setDefaultListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstAction:
                /// 3 hits
                tracker.setProp("fake_Prop", "vol", false);
                tracker.setProp("proP_1", "vol", false);
                tracker.setProp("proP_2", "pers", true);
                    tracker.setProps(new HashMap<String, String>() {{
                    put("K1", "v");
                    put("K2", "v");
                }}, true);
                tracker.delProp("fake_prop");
                tracker.Screens().add().sendView();
                tracker.Screens().add().sendView();
                tracker.delProps();
                tracker.Screens().add().sendView();

                /*Screen s = tracker.Screens().add("test");
                s.setLevel2("level2");
                s.sendView();*/

                /*s.setLevel2(6);
                tracker.Events().add("test", new HashMap<String, Object>() {{
                    put("k", "p");
                }});
                tracker.Events().send();

                s.setLevel2(-1);
                tracker.Events().add("test", new HashMap<String, Object>() {{
                    put("k", "p");
                }});
                tracker.Events().send();

                s.setLevel2("test");
                tracker.Events().add("test", new HashMap<String, Object>() {{
                    put("k", "p");
                }});
                tracker.Events().send();

                s.setLevel2("7");
                tracker.Events().add("test", new HashMap<String, Object>() {{
                    put("k", "p");
                }});
                tracker.Events().send();

                s.setLevel2(null);
                tracker.Events().add("test", new HashMap<String, Object>() {{
                    put("k", "p");
                }});
                tracker.Events().send();*/

                /*Gesture g = tracker.Gestures().add("Click");
                g.setLevel2(3).sendTouch();
                g.setLevel2(-1).sendTouch();
                g.setLevel2("test").sendTouch();
                g.setLevel2(null).sendTouch();

                Audio a = tracker.Players().add().Audios().add("audio", 4);
                a.setMediaLevel2(3).sendInfo(false);
                a.setMediaLevel2(-1).sendInfo(false);
                a.setMediaLevel2("test").sendInfo(false);
                a.setMediaLevel2(null).sendInfo(false);*/

                tracker.IdentifiedVisitor().set(123456, 12);
                tracker.dispatch();
                break;
            case R.id.goToSecondScreen:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}
