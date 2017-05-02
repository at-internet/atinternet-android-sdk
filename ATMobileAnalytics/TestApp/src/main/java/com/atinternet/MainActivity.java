package com.atinternet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Debugger;
import com.atinternet.tracker.Hit;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.TrackerConfigurationKeys;
import com.atinternet.tracker.TrackerListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ANDROID INTEGRATION";
    private static final String SUCCESS_REQUEST = "SuccessRequest";
    private static final String FAILURE_REQUEST = "FailureRequest";

    private static final String LOG = "Log";
    private static final String LOG_SSL = "LogSSL";
    private static final String SITE = "Site";
    private static final String CONFIG = "Config";
    private static final String PERSIST_PARAM_LIST = "PersistParamsList";
    private static final String JSON_FILE = "expectedAndroid.json";

    Tracker tracker;
    Integration integration;
    Operation currentOperation;

    JSONObject config;
    List<String> persistParamsKeysList;
    Map<String, Map<String, Object>> expectedResultsMap;

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*tracker = ATInternet.getInstance().getTracker("IntegrationTracker", new HashMap<String, Object>() {{
            put(TrackerConfigurationKeys.LOG, "logp");
            put(TrackerConfigurationKeys.LOG_SSL, "logs");
            put(TrackerConfigurationKeys.SITE, 552987);
        }});*/

        launchIntegration();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ATInternet.ALLOW_OVERLAY_INTENT_RESULT_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Debugger.create(this, tracker);
                }
            }
        }
    }

    private void launchIntegration() {
        try {
            expectedResultsMap = getExpectedResults();
            tracker = ATInternet.getInstance().getTracker("IntegrationTracker", new HashMap<String, Object>() {{
                put(TrackerConfigurationKeys.LOG, config.getString(LOG));
                put(TrackerConfigurationKeys.LOG_SSL, config.getString(LOG_SSL));
                put(TrackerConfigurationKeys.SITE, config.getString(SITE));
            }});
            tracker.setUserId("Custom-ClientID");

            tracker.setListener(new TrackerListener() {
                @Override
                public void trackerNeedsFirstLaunchApproval(String s) {

                }

                @Override
                public void buildDidEnd(HitStatus hitStatus, String s) {
                    try {
                        if (hitStatus == HitStatus.Failed) {
                            String content = "Test " + currentOperation.getHitId() + " failed : Build hit failed (" + s + ")";
                            Log.e(TAG, content);
                            URL url = new URL(config.getString(FAILURE_REQUEST));
                            HttpURLConnection connection = createConnection(url, content);
                            connection.connect();
                            connection.getResponseCode();
                            System.exit(0);
                        }
                    } catch (Exception ignored) {
                    }
                }

                @Override
                public void sendDidEnd(HitStatus hitStatus, String s) {
                    URL url;
                    HttpURLConnection connection;
                    try {
                        if (hitStatus == HitStatus.Failed) {
                            String content = "Test " + currentOperation.getHitId() + " failed : Sending hit failed";
                            Log.e(TAG, content);
                            url = new URL(config.getString(FAILURE_REQUEST));
                            connection = createConnection(url, content);
                            connection.connect();
                            connection.getResponseCode();
                            System.exit(0);
                        } else {
                            boolean diffDetected = false;
                            String content = "";
                            Map<String, Object> expectedResultObject = expectedResultsMap.get(String.valueOf(currentOperation.getHitId()));

                            Map<String, String> params;
                            params = getParameters(URLDecoder.decode(s, "UTF-8"));

                            for (String paramKey : persistParamsKeysList) {
                                if (params.get(paramKey) == null) {
                                    diffDetected = true;
                                    content = "Param \"" + paramKey + "\" missing in the hit";
                                    break;
                                }
                            }

                            if (!diffDetected) {
                                if (!String.valueOf(expectedResultObject.get("Host")).equals(params.get("Host"))) {
                                    diffDetected = true;
                                    content = "Diff detected in Host : ";
                                    content += "\n\tExpected : " + String.valueOf(expectedResultObject.get("Host"));
                                    content += "\n\tActual : " + params.get("Host");
                                } else if (!String.valueOf(expectedResultObject.get("Scheme")).equals(params.get("Scheme"))) {
                                    diffDetected = true;
                                    content = "Diff detected in Scheme : ";
                                    content += "\n\tExpected : " + String.valueOf(expectedResultObject.get("Scheme"));
                                    content += "\n\tActual : " + params.get("Scheme");
                                } else if (!String.valueOf(expectedResultObject.get("Path")).equals(params.get("Path"))) {
                                    diffDetected = true;
                                    content = "Diff detected in Path : ";
                                    content += "\n\tExpected : " + String.valueOf(expectedResultObject.get("Path"));
                                    content += "\n\tActual : " + params.get("Path");
                                } else {

                                    JSONObject stc = new JSONObject(params.remove("stc"));
                                    stc.remove("lifecycle");

                                    Map<String, Object> query = (Map<String, Object>) expectedResultObject.get("Query");
                                    Map<String, String> expectedStc = (Map<String, String>) query.remove("stc");
                                    if (expectedStc == null) {
                                        expectedStc = new LinkedHashMap<>();
                                    }

                                    // Comparaison des parametres
                                    for (Map.Entry<String, Object> entry : query.entrySet()) {
                                        String key = entry.getKey();
                                        String expectedValue = String.valueOf(entry.getValue());
                                        String actualValue = params.get(key);

                                        if (!expectedValue.equals(actualValue)) {
                                            diffDetected = true;
                                            content = "Diff detected in \"" + key + "\" parameter : ";
                                            content += "\n\tExpected : " + expectedValue;
                                            content += "\n\tActual : " + actualValue;
                                            break;
                                        }
                                    }
                                    // Comparaison du stc (cas particulier)
                                    if (!diffDetected) {
                                        Iterator<String> keys = stc.keys();
                                        if (stc.length() == expectedStc.size()) {
                                            while (keys.hasNext()) {
                                                String key = keys.next();
                                                String expectedValue = expectedStc.get(key);
                                                String actualValue = stc.getString(key);

                                                if (!expectedValue.equals(actualValue)) {
                                                    diffDetected = true;
                                                    content = "Diff detected in \"stc\" parameter : ";
                                                    content += "\n\tExpected : \"" + key + "\" : " + expectedValue;
                                                    content += "\n\tActual : " + key + "\" : " + actualValue;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (diffDetected) {
                                content = "Test " + currentOperation.getHitId() + " failed : " + content;
                                Log.e(TAG, content);
                                url = new URL(config.getString(FAILURE_REQUEST));
                                connection = createConnection(url, content);
                                connection.connect();
                                connection.getResponseCode();
                                System.exit(0);
                            } else {
                                Log.d(TAG, "Test " + currentOperation.getHitId() + " pass");
                                index++;
                                if (index < integration.getOperations().size()) {
                                    currentOperation = integration.getOperations().get(index);
                                    Thread.sleep(300);
                                    currentOperation.start();
                                } else {
                                    content = "Success !!";
                                    Log.d(TAG, content);
                                    url = new URL(config.getString(SUCCESS_REQUEST));
                                    connection = createConnection(url, content);
                                    connection.connect();
                                    connection.getResponseCode();
                                    System.exit(0);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void didCallPartner(String s) {

                }

                @Override
                public void warningDidOccur(String s) {

                }

                @Override
                public void saveDidEnd(String s) {

                }

                @Override
                public void errorDidOccur(String s) {

                }
            });

            integration = new Integration(tracker);
            currentOperation = integration.getOperations().get(index);
            currentOperation.start();
        } catch (JSONException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private HttpURLConnection createConnection(URL url, String content) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Length", Integer.toString(content.length()));
        conn.setUseCaches(false);
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

        return conn;
    }

    private LinkedHashMap<String, String> getParameters(String hit) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        try {
            URL url = new URL(hit);
            map.put("Scheme", url.getProtocol());
            map.put("Host", url.getHost());
            map.put("Path", url.getPath());
            String[] queryComponents = url.getQuery().split("&");
            for (String queryComponent : queryComponents) {
                String[] elem = queryComponent.split("=");
                if (elem.length > 1) {
                    if (parseJSON(elem[1]) instanceof JSONObject) {
                        JSONObject json = (JSONObject) parseJSON(elem[1]);
                        if (json != null && elem[0].equals(Hit.HitParam.JSON.stringValue())) {
                            map.put(elem[0], json.toString(3));
                        } else {
                            map.put(elem[0], elem[1]);
                        }
                    } else {
                        map.put(elem[0], elem[1]);
                    }
                } else {
                    map.put(elem[0], "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private Object parseJSON(String s) {
        try {
            return new JSONObject(s);
        } catch (JSONException e) {
            try {
                return new JSONArray(s);
            } catch (JSONException e1) {
                return null;
            }
        }
    }

    private Map<String, Map<String, Object>> getExpectedResults() {
        JSONObject result;
        String stringResult;
        try {
            InputStream inputStream = getAssets().open(JSON_FILE);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            stringResult = new String(buffer, "UTF-8");
            result = new JSONObject(stringResult);
        } catch (Exception ignored) {
            result = new JSONObject();
        }
        JSONArray ppList = (JSONArray) result.remove(PERSIST_PARAM_LIST);

        persistParamsKeysList = new Gson().fromJson(ppList.toString(), new TypeToken<ArrayList<String>>() {
        }.getType());

        config = (JSONObject) result.remove(CONFIG);

        return new Gson().fromJson(result.toString(), new TypeToken<Map<String, Map<String, Object>>>() {
        }.getType());
    }
}
