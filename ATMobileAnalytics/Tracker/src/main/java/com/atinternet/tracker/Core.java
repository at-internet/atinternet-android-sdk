/*
This SDK is licensed under the MIT license (MIT)
Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.atinternet.tracker;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

interface Closure {
    String execute();
}

class Param {

    private String key;
    private List<Closure> values;
    private ParamOption paramOption;

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    List<Closure> getValues() {
        return values;
    }

    void setValue(Closure value) {
        values.clear();
        values.add(value);
    }

    void setValues(List<Closure> values) {
        this.values = values;
    }

    ParamOption getOptions() {
        return paramOption;
    }

    void setOptions(ParamOption paramOption) {
        this.paramOption = paramOption;
    }

    boolean isPersistent() {
        return paramOption != null && paramOption.isPersistent();
    }

    Param() {
        key = "";
        values = new ArrayList<>();
        paramOption = null;
    }

    Param(String key, final Closure value) {
        this();
        this.key = key;
        this.values.add(value);
    }

    Param(String key, Closure value, ParamOption paramOption) {
        this(key, value);
        this.paramOption = paramOption;
    }
}

class Buffer {

    private final LinkedHashMap<String, Param> persistentParams;
    private final LinkedHashMap<String, Param> volatileParams;

    private String identifierKey;

    private String os;
    private Closure osClosure;

    private String device;
    private Closure deviceClosure;

    private String manufacturer;
    private Closure manufacturerClosure;

    private String model;
    private Closure modelClosure;

    private String diagonal;
    private Closure diagonalClosure;

    private String apid;
    private String apvr;
    private Closure apidClosure;
    private Closure apvrClosure;

    LinkedHashMap<String, Param> getPersistentParams() {
        return persistentParams;
    }

    LinkedHashMap<String, Param> getVolatileParams() {
        return volatileParams;
    }

    void setIdentifierKey(String identifierKey) {
        volatileParams.remove(Hit.HitParam.UserId.stringValue());
        ParamOption persistent = new ParamOption().setPersistent(true);
        persistentParams.put(Hit.HitParam.UserId.stringValue(), new Param(Hit.HitParam.UserId.stringValue(), TechnicalContext.getUserId(identifierKey), persistent));
    }

    Buffer(Tracker tracker) {
        persistentParams = new LinkedHashMap<>();
        volatileParams = new LinkedHashMap<>();
        identifierKey = String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.IDENTIFIER));

        initConstantClosures();
        addContextVariables(tracker);
    }

    private void addContextVariables(Tracker tracker) {
        // Boolean isPersistent have to be true in all cases
        ParamOption persistent = new ParamOption().setPersistent(true);
        ParamOption persistentWithEncoding = new ParamOption().setPersistent(true).setEncode(true);

        persistentParams.put("vtag", new Param("vtag", TechnicalContext.VTAG, persistent));
        persistentParams.put("ptag", new Param("ptag", TechnicalContext.PTAG, persistent));
        persistentParams.put("lng", new Param("lng", TechnicalContext.getLanguage(), persistent));
        persistentParams.put("mfmd", new Param("mfmd", deviceClosure, persistentWithEncoding));
        persistentParams.put("manufacturer", new Param("manufacturer", manufacturerClosure, persistentWithEncoding));
        persistentParams.put("model", new Param("model", modelClosure, persistentWithEncoding));
        persistentParams.put("os", new Param("os", osClosure, persistent));
        persistentParams.put("apid", new Param("apid", apidClosure, persistent));
        persistentParams.put("apvr", new Param("apvr", apvrClosure, persistentWithEncoding));
        persistentParams.put("hl", new Param("hl", TechnicalContext.getLocalHour(), persistent));
        persistentParams.put("r", new Param("r", TechnicalContext.getResolution(), persistent));
        persistentParams.put("dg", new Param("dg", diagonalClosure, persistent));
        persistentParams.put("car", new Param("car", TechnicalContext.getCarrier(), persistentWithEncoding));
        persistentParams.put("cn", new Param("cn", TechnicalContext.getConnectionType(), persistentWithEncoding));
        persistentParams.put("ts", new Param("ts", Tool.getTimeStamp(), persistent));
        persistentParams.put("dls", new Param("dls", TechnicalContext.getDownloadSource(tracker), persistent));
        persistentParams.put("idclient", new Param("idclient", TechnicalContext.getUserId(identifierKey), persistent));
    }

    private void initConstantClosures() {
        os = TechnicalContext.getOS().execute();
        device = TechnicalContext.getDevice().execute();
        manufacturer = TechnicalContext.getManufacturer().execute();
        model = TechnicalContext.getModel().execute();
        apid = TechnicalContext.getApplicationIdentifier().execute();
        apvr = TechnicalContext.getApplicationVersion().execute();
        diagonal = TechnicalContext.getDiagonal().execute();

        osClosure = new Closure() {
            @Override
            public String execute() {
                return os;
            }
        };

        deviceClosure = new Closure() {
            @Override
            public String execute() {
                return device;
            }
        };

        manufacturerClosure = new Closure() {
            @Override
            public String execute() {
                return manufacturer;
            }
        };

        modelClosure = new Closure() {
            @Override
            public String execute() {
                return model;
            }
        };

        apidClosure = new Closure() {
            @Override
            public String execute() {
                return apid;
            }
        };

        apvrClosure = new Closure() {
            @Override
            public String execute() {
                return apvr;
            }
        };

        diagonalClosure = new Closure() {
            @Override
            public String execute() {
                return diagonal;
            }
        };
    }
}

@SuppressWarnings("unchecked")
class Builder implements Runnable {

    private static final String PERCENT_VALUE = "%";
    private static final String MH_PARAMETER_FORMAT = "%1$s-%2$s-%3$s";
    private static final String MHID_FORMAT = "%02d%02d%02d%d";
    private static final String[] MH_PARAMS_ALL_PARTS = new String[]{"idclient", "col"};
    private static final String OPT_OUT = "opt-out";
    private static final String MHERR = "mherr";

    private static final int REFCONFIGCHUNKS = 4;
    private static final int MH_PARAMETER_MAX_LENGTH = 30;
    private static final int MHERR_PARAMETER_LENGTH = 8;
    private static final int HIT_MAX_LENGTH = 1600;
    private static final int HIT_MAX_COUNT = 999;

    private final Configuration configuration;
    private final LinkedHashMap<String, Param> persistentParams;
    private final LinkedHashMap<String, Param> volatileParams;
    private final Tracker tracker;

    Builder(Tracker tracker) {
        this.tracker = tracker;
        this.configuration = tracker.getConfiguration();
        this.volatileParams = new LinkedHashMap<>(tracker.getBuffer().getVolatileParams());
        this.persistentParams = new LinkedHashMap<>(tracker.getBuffer().getPersistentParams());
    }

    String buildConfiguration() {
        StringBuilder conf = new StringBuilder();
        int hitConfigChunks = 0;

        boolean isSecure = (Boolean) configuration.get(TrackerConfigurationKeys.SECURE);
        String log = String.valueOf(configuration.get(TrackerConfigurationKeys.LOG));
        String logSecure = String.valueOf(configuration.get(TrackerConfigurationKeys.LOG_SSL));
        String domain = String.valueOf(configuration.get(TrackerConfigurationKeys.DOMAIN));
        String pixelPath = String.valueOf(configuration.get(TrackerConfigurationKeys.PIXEL_PATH));
        String siteID = String.valueOf(configuration.get(TrackerConfigurationKeys.SITE));

        if (isSecure) {
            if (!TextUtils.isEmpty(logSecure)) {
                conf.append("https://")
                        .append(logSecure)
                        .append(".");
                hitConfigChunks++;
            }
        } else {
            if (!TextUtils.isEmpty(log)) {
                conf.append("http://")
                        .append(log)
                        .append(".");
                hitConfigChunks++;
            }
        }
        if (!TextUtils.isEmpty(domain)) {
            conf.append(domain);
            hitConfigChunks++;
        }
        if (!TextUtils.isEmpty(pixelPath)) {
            conf.append(pixelPath);
            hitConfigChunks++;
        }

        conf.append("?s=")
                .append(siteID);
        hitConfigChunks++;


        if (hitConfigChunks != REFCONFIGCHUNKS) {
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.ERROR, "There is something wrong with configuration : " + conf);
            conf = new StringBuilder();
        }

        return conf.toString();
    }

    Object[] build() {
        ArrayList<String> hitsList = new ArrayList<>();

        String configStr = buildConfiguration();
        String oltParameter = Tool.getTimeStamp().execute();

        if (TextUtils.isEmpty(configStr)) {
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.BUILD, "Empty configuration", TrackerListener.HitStatus.Failed);
            return new Object[]{hitsList, oltParameter};
        }

        ArrayList<String> prepareHitsList = new ArrayList<>();
        int countSplitHits = 1;
        int indexError = -1;
        StringBuilder queryString = new StringBuilder();
        String mhCommonQueryContent;

        // Calcul pour connaitre la longueur maximum du hit
        int maxLengthAvailable = HIT_MAX_LENGTH - (configStr.length() + oltParameter.length() + MH_PARAMETER_MAX_LENGTH);
        LinkedHashMap<String, Pair<String, String>> dictionary = prepareQuery();
        Set<String> keySet = dictionary.keySet();

        StringBuilder mhCommonQueryContentSb = new StringBuilder();
        for (String paramKey : MH_PARAMS_ALL_PARTS) {
            Pair<String, String> pair = dictionary.remove(paramKey);
            if (pair != null) {
                mhCommonQueryContentSb.append(pair.first);
            }
        }
        maxLengthAvailable -= mhCommonQueryContentSb.length();
        mhCommonQueryContent = mhCommonQueryContentSb.toString();
        queryString.append(mhCommonQueryContent);

        // Outerloop est un label de référence si jamais une boucle doit être interrompue
        outerloop:

        for (String parameterKey : keySet) {
            String value = dictionary.get(parameterKey).first;
            String separator = dictionary.get(parameterKey).second;


            // Si la valeur du paramètre est trop grande
            if (value.length() > maxLengthAvailable) {

                // Si le paramètre n'est pas découpable
                if (!Lists.getSliceReadyParams().contains(parameterKey)) {
                    // Erreur : le paramètre n'est pas découpable
                    indexError = countSplitHits;
                    Tool.executeCallback(tracker.getListener(), Tool.CallbackType.WARNING, "Multihits: parameter " + parameterKey + " value not allowed to be sliced");
                    break;
                }

                // Récupération du séparateur, de la clé et la valeur du paramètre courant
                String[] currentParameterString = value.split("=");
                String currentKey = currentParameterString[0] + "=";
                String currentValue = currentParameterString[1];

                // On découpe la valeur du paramètre sur son séparateur
                String[] valuesList = currentValue.split(separator);

                for (int i = 0; i < valuesList.length; i++) {
                    String currentSplitValue = valuesList[i];

                    // Si la valeur courante est trop grande
                    if (currentSplitValue.length() > maxLengthAvailable) {
                        // Erreur : Valeur trop longue non découpable
                        indexError = countSplitHits;
                        queryString.append(currentKey);
                        int currentMaxLength = maxLengthAvailable - (MHERR_PARAMETER_LENGTH + queryString.length());

                        // On cherche la position du dernier % afin d'éviter les exceptions au moment de la découpe brutale
                        String splitError = currentSplitValue.substring(0, currentMaxLength);
                        int lastIndexOfPercent = splitError.lastIndexOf(PERCENT_VALUE);

                        if ((lastIndexOfPercent > currentMaxLength - 5) && (lastIndexOfPercent < currentMaxLength)) {
                            queryString.append(splitError.substring(0, lastIndexOfPercent));
                        } else {
                            queryString.append(splitError);
                        }
                        Tool.executeCallback(tracker.getListener(), Tool.CallbackType.WARNING, "Multihits: Param " + parameterKey + " value still too long after slicing");

                        // On retourne à l'endroit du code où se trouve outerloop
                        break outerloop;
                    }
                    // Sinon si le hit déjà construit + la valeur courante est trop grand
                    else if (queryString.length() + currentSplitValue.length() > maxLengthAvailable) {
                        // On créé un nouveau tronçon
                        countSplitHits++;
                        prepareHitsList.add(queryString.toString());
                        queryString = new StringBuilder()
                                .append(mhCommonQueryContent)
                                .append(currentKey)
                                .append(i == 0 ? currentSplitValue : separator + currentSplitValue);
                    }
                    // Sinon, on continue la construction normalement
                    else {
                        queryString.append(i == 0 ? currentKey + currentSplitValue : separator + currentSplitValue);
                    }
                }
            }
            // Sinon, si le hit est trop grand, on le découpe entre deux paramètres
            else if (queryString.length() + value.length() > maxLengthAvailable) {
                countSplitHits++;
                prepareHitsList.add(queryString.toString());
                queryString = new StringBuilder()
                        .append(mhCommonQueryContent)
                        .append(value);
            }
            //Sinon, on ne découpe pas
            else {
                queryString.append(value);
            }
        }

        // Si un seul hit est construit
        if (countSplitHits == 1) {
            if (indexError == countSplitHits) {
                hitsList.add(configStr + makeSubQuery(MHERR, "1") + queryString);
            } else {
                hitsList.add(configStr + queryString);
            }
        }
        // Sinon, si le nombre de tronçons > 999, erreur
        else if (countSplitHits > HIT_MAX_COUNT) {
            hitsList.add(configStr + makeSubQuery(MHERR, "1") + queryString);
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.WARNING, "Multihits: too much hit parts");
        }
        // Sinon, on ajoute les hits construits à la liste de hits à envoyer
        else {
            prepareHitsList.add(queryString.toString());
            String mhID = mhIdSuffixGenerator();
            String countSplitHitsString = String.valueOf(countSplitHits);
            for (int i = 0; i < countSplitHits; i++) {
                String mhParameter = makeSubQuery("mh", String.format(MH_PARAMETER_FORMAT, Tool.formatNumberLength(Integer.toString(i + 1), countSplitHitsString.length()), countSplitHitsString, mhID));
                if (indexError == (i + 1)) {
                    hitsList.add(configStr + mhParameter + makeSubQuery(MHERR, "1") + prepareHitsList.get(i));
                } else {
                    hitsList.add(configStr + mhParameter + prepareHitsList.get(i));
                }
            }
        }

        StringBuilder message = new StringBuilder();
        for (String hit : hitsList) {
            message.append(hit).append('\n');
        }
        Tool.executeCallback(tracker.getListener(), Tool.CallbackType.BUILD, message.toString(), TrackerListener.HitStatus.Success);

        return new Object[]{hitsList, oltParameter};
    }

    @Override
    public void run() {
        // Récupération des éléments issus du build
        Object[] buildResult = build();
        ArrayList<String> urls = (ArrayList<String>) buildResult[0];
        String oltParameter = (String) buildResult[1];

        // Envoi du(des) hit(s) construit(s)
        for (String url : urls) {
            new Sender(tracker, new Hit(url), false, oltParameter).send(tracker.getOfflineMode() != Tracker.OfflineMode.never);
        }
    }

    ArrayList<Param> organizeParameters(LinkedHashMap<String, Param> completeBuffer) {
        ArrayList<Param> params = new ArrayList<>();
        String firstParamKey = null;
        String lastParamKey = null;

        Param refstore = completeBuffer.remove(Hit.HitParam.Refstore.stringValue());
        Param ref = completeBuffer.remove(Hit.HitParam.Referrer.stringValue());

        for (Map.Entry<String, Param> entry : completeBuffer.entrySet()) {
            Param param = entry.getValue();
            ParamOption options = param.getOptions();

            if (options == null) {
                params.add(param);
                continue;
            }

            switch (options.getRelativePosition()) {
                case first:
                    firstParamKey = entry.getKey();
                    break;
                case last:
                    lastParamKey = entry.getKey();
                    break;
                default:
                    params.add(param);
                    break;
            }
        }

        // Insertion du premier paramètre
        if (firstParamKey != null) {
            params.add(0, completeBuffer.get(firstParamKey));
        }

        // Insertion du dernier paramètre
        if (lastParamKey != null) {
            params.add(completeBuffer.get(lastParamKey));
        }

        // Insertion du refstore si existant
        if (refstore != null) {
            params.add(refstore);
        }

        // Insertion du ref si existant
        if (ref != null) {
            params.add(ref);
        }

        return params;
    }

    LinkedHashMap<String, Pair<String, String>> prepareQuery() {
        LinkedHashMap<String, Pair<String, String>> formattedParameters = new LinkedHashMap<>();

        LinkedHashMap<String, Param> completeBuffer = new LinkedHashMap<>();
        completeBuffer.putAll(persistentParams);
        completeBuffer.putAll(volatileParams);

        // ORGANISE PARAMS
        ArrayList<Param> params = organizeParameters(completeBuffer);

        // PREPARE
        for (final Param p : params) {
            List<Closure> paramValues = new ArrayList<>(p.getValues());

            String strValue = paramValues.remove(0).execute();
            if (strValue == null) {
                continue;
            }

            ParamOption options = p.getOptions();
            if (options != null) {
                try {
                    switch (options.getType()) {
                        case JSON:
                            Map map = new HashMap(Tool.toMap(new JSONObject(strValue)));
                            for (Closure closureValue : paramValues) {
                                String appendValue = closureValue.execute();
                                if (Tool.isJSON(appendValue)) {
                                    map.putAll(Tool.toMap(new JSONObject(appendValue)));
                                } else {
                                    Tool.executeCallback(tracker.getListener(), Tool.CallbackType.WARNING, "Couldn't append value to a JSONObject");
                                }
                            }
                            strValue = new JSONObject(map).toString();
                            break;
                        case ARRAY:
                            List array = new ArrayList();
                            JSONArray valArray = new JSONArray(strValue);
                            for (int i = 0; i < valArray.length(); i++) {
                                array.add(valArray.get(i));
                            }
                            for (Closure closureValue : paramValues) {
                                String appendValue = closureValue.execute();
                                if (Tool.isArray(appendValue)) {
                                    valArray = new JSONArray(appendValue);
                                    for (int i = 0; i < valArray.length(); i++) {
                                        array.add(valArray.get(i));
                                    }
                                } else {
                                    array.add(appendValue);
                                }
                            }
                            strValue = array.toString();
                            break;
                        default:
                            // NOT JSON
                            StringBuilder strBuilder = new StringBuilder(strValue);
                            for (Closure closureValue : paramValues) {
                                strBuilder.append(p.getOptions().getSeparator())
                                        .append(closureValue.execute());
                            }
                            strValue = strBuilder.toString();
                            break;
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } else {
                // NOT JSON
                StringBuilder strBuilder = new StringBuilder(strValue);
                for (Closure closureValue : paramValues) {
                    strBuilder.append(',')
                            .append(closureValue.execute());
                }
                strValue = strBuilder.toString();
            }

            String key = p.getKey();

            if (key.equals(Hit.HitParam.UserId.stringValue())) {
                if (TechnicalContext.optOutEnabled(Tracker.getAppContext())) {
                    strValue = OPT_OUT;
                } else if (((Boolean) configuration.get(TrackerConfigurationKeys.HASH_USER_ID))) {
                    strValue = Tool.SHA256(strValue);
                }
                tracker.setInternalUserId(strValue);
            } else if (key.equals(Hit.HitParam.Referrer.stringValue())) {
                strValue = strValue.replace("&", "$")
                        .replaceAll("[<>]", "");
            }

            String separator = ",";
            if (options != null) {
                separator = options.getSeparator();
                if (options.isEncode()) {
                    strValue = Tool.percentEncode(strValue);
                    separator = Tool.percentEncode(separator);
                }
            }

            formattedParameters.put(key, new Pair<>(makeSubQuery(key, strValue), separator));
        }
        return formattedParameters;
    }

    private String mhIdSuffixGenerator() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return String.format(Locale.getDefault(), MHID_FORMAT, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), new Random().nextInt(9000000) + 1000000);
    }

    String makeSubQuery(String key, String value) {
        return "&" + key + "=" + value;
    }
}

class Sender implements Runnable {

    private static final int RETRY_COUNT = 3;
    private static final int TIMEOUT = 15000;
    private static final String RECEIVE_FROM_SERVER_ERROR = "recvfrom";

    private static boolean OfflineHitProcessing = false;

    private final Tracker tracker;
    private final Storage storage;

    private final Hit hit;
    private final String oltParameter;
    private final boolean forceSendOfflineHits;

    Sender(Tracker tracker, Hit hit, boolean forceSendOfflineHits, String... oltParameter) {
        this.tracker = tracker;
        this.storage = Storage.getInstance(Tracker.getAppContext());
        this.hit = hit;
        this.forceSendOfflineHits = forceSendOfflineHits;
        this.oltParameter = oltParameter.length > 0 ? oltParameter[0] : "";
    }

    private void send(final Hit hit) {

        switch (tracker.getOfflineMode()) {
            case always:
                if (!forceSendOfflineHits) {
                    saveHitDatabase(hit);
                }
                break;
            case required:
                // Si pas de connexion
                if (TechnicalContext.getConnection() == TechnicalContext.ConnectionType.OFFLINE || (!hit.isOffline() && storage.getCountOfflineHits() > 0)) {
                    // Si le hit ne provient pas du offline
                    if (!hit.isOffline()) {
                        saveHitDatabase(hit);
                    }
                    break;
                }
            default:
                HttpURLConnection connection = null;
                try {
                    // Execution de la requête
                    URL url = new URL(hit.getUrl());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(TIMEOUT);
                    connection.setConnectTimeout(TIMEOUT);
                    connection.connect();

                    int statusCode = connection.getResponseCode();
                    final String message = connection.getResponseMessage();

                    // Le hit n'a pas pu être envoyé
                    if (statusCode != 200) {
                        if (tracker.getOfflineMode() != Tracker.OfflineMode.never) {
                            if (!hit.isOffline()) {
                                saveHitDatabase(hit);
                            } else {
                                updateRetryCount(hit);
                            }
                        }
                        Tool.executeCallback(tracker.getListener(), Tool.CallbackType.SEND, message, TrackerListener.HitStatus.Failed);
                        updateDebugger(message, "error48", false);
                    }
                    // Le hit a été envoyé
                    else {
                        // Si le hit provient du stockage, on le supprime de la base
                        if (hit.isOffline()) {
                            storage.deleteHit(hit.getUrl());
                        }
                        Tool.executeCallback(tracker.getListener(), Tool.CallbackType.SEND, hit.getUrl(), TrackerListener.HitStatus.Success);
                        updateDebugger(hit.getUrl(), "sent48", true);
                    }
                } catch (final Exception e) {
                    updateDebugger(e.getMessage(), "error48", false);
                    // Si une erreur est survenue au moment de la récupération du pixel de marquage mais que le hit est bien envoyé
                    if (checkExceptionServerReceiveData(e)) {
                        // Si il s'agissait d'un hit offline, on le supprime
                        if (hit.isOffline()) {
                            storage.deleteHit(hit.getUrl());
                        }
                    } else if (tracker.getOfflineMode() != Tracker.OfflineMode.never) {
                        if (!hit.isOffline()) {
                            saveHitDatabase(hit);
                        } else {
                            updateRetryCount(hit);
                        }
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                break;
        }
    }

    @Override
    public void run() {
        send(false);
    }

    void send(boolean includeOfflineHits) {
        if (includeOfflineHits) {
            Sender.sendOfflineHits(tracker, storage, false, forceSendOfflineHits);
        }
        send(hit);
    }

    static void sendOfflineHits(Tracker tracker, Storage storage, boolean forceSendOfflineHits, boolean async) {
        if ((tracker.getOfflineMode() != Tracker.OfflineMode.always || forceSendOfflineHits)
                && TechnicalContext.getConnection() != TechnicalContext.ConnectionType.OFFLINE
                && !OfflineHitProcessing && TrackerQueue.getEnabledFillQueueFromDatabase()
                && storage.getCountOfflineHits() > 0) {

            ArrayList<Hit> offlineHits = storage.getOfflineHits();
            if (async) {
                TrackerQueue.setEnabledFillQueueFromDatabase(false);
                for (Hit hit : offlineHits) {
                    Sender sender = new Sender(tracker, hit, forceSendOfflineHits);
                    TrackerQueue.getInstance().put(sender);
                }
                TrackerQueue.getInstance().put(new Runnable() {
                    @Override
                    public void run() {
                        TrackerQueue.setEnabledFillQueueFromDatabase(true);
                    }
                });
            } else {
                OfflineHitProcessing = true;
                for (Hit hit : offlineHits) {
                    Sender sender = new Sender(tracker, hit, forceSendOfflineHits);
                    sender.send(false);
                }
                OfflineHitProcessing = false;
            }
        }
    }

    private void updateRetryCount(Hit hit) {
        int retryCount = hit.getRetry();
        if (retryCount < RETRY_COUNT) {
            storage.updateRetry(hit.getUrl(), retryCount + 1);
        } else {
            storage.deleteHit(hit.getUrl());
        }
    }

    void saveHitDatabase(final Hit hit) {
        final String url = storage.saveHit(hit.getUrl(), System.currentTimeMillis(), oltParameter);
        if (!TextUtils.isEmpty(url)) {
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.SAVE, url);
            updateDebugger(url, "save48", true);
        } else {
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.WARNING, "Hit could not be saved : " + hit.getUrl());
            updateDebugger("Hit could not be saved : " + hit.getUrl(), "warning48", false);
        }
    }

    private boolean checkExceptionServerReceiveData(Exception e) {
        int index = 0;
        do {
            if (e.getStackTrace()[index].getMethodName().equals(RECEIVE_FROM_SERVER_ERROR)) {
                return true;
            }
            index++;
        }
        while (index < e.getStackTrace().length);
        return false;
    }

    private void updateDebugger(final String message, final String type, final boolean isHit) {
        if (Debugger.getContext() != null) {
            Tool.runOnMainThread((Activity) Debugger.getContext(), new Runnable() {
                @Override
                public void run() {
                    Debugger.getDebuggerEvents().add(0, new Debugger.DebuggerEvent(message, type, isHit));
                    if (Debugger.getCurrentViewVisibleId() == com.atinternet.tracker.R.id.eventViewer) {
                        Debugger.getDebuggerEventListAdapter().notifyDataSetChanged();
                    }
                }
            });
        }
    }
}

class Dispatcher {

    private final Tracker tracker;

    Dispatcher(Tracker tracker) {
        this.tracker = tracker;
    }

    void dispatch(BusinessObject... businessObjects) {
        ArrayList<BusinessObject> trackerObjects;
        for (BusinessObject businessObject : businessObjects) {
            businessObject.setParams();

            if (businessObject instanceof AbstractScreen) {
                trackerObjects = new ArrayList<>(tracker.getBusinessObjects().values());

                boolean hasOrder = false;

                for (BusinessObject object : trackerObjects) {
                    if (((object instanceof OnAppAd && ((OnAppAd) object).getAction() == OnAppAd.Action.View)
                            || object instanceof Order
                            || object instanceof InternalSearch
                            || object instanceof ScreenInfo) &&
                            object.getTimestamp() < businessObject.getTimestamp()) {

                        if (object instanceof Order) {
                            hasOrder = true;
                        }

                        object.setParams();
                        tracker.getBusinessObjects().remove(object.getId());
                    }
                }

                if (tracker.Cart().getCartId() != null && (((Screen) businessObject).isBasketScreen() || hasOrder)) {
                    tracker.Cart().setParams();
                }
            } else if (businessObject instanceof Gesture) {
                trackerObjects = new ArrayList<>(tracker.getBusinessObjects().values());

                if (((Gesture) businessObject).getAction() == Gesture.Action.InternalSearch) {
                    for (BusinessObject object : trackerObjects) {
                        if ((object instanceof InternalSearch) && object.getTimestamp() < businessObject.getTimestamp()) {
                            object.setParams();
                            tracker.getBusinessObjects().remove(object.getId());
                        }
                    }
                }
            }

            tracker.getBusinessObjects().remove(businessObject.getId());
            trackerObjects = new ArrayList<>(tracker.getBusinessObjects().values());

            for (BusinessObject object : trackerObjects) {
                if ((object instanceof CustomObject || object instanceof NuggAd) && object.getTimestamp() < businessObject.getTimestamp()) {
                    object.setParams();
                    tracker.getBusinessObjects().remove(object.getId());
                }
            }
        }

        if (Hit.getHitType(tracker.getBuffer().getVolatileParams(), tracker.getBuffer().getPersistentParams()) == Hit.HitType.Screen) {
            SharedPreferences preferences = Tracker.getPreferences();
            if (!preferences.getBoolean(TrackerConfigurationKeys.CAMPAIGN_ADDED_KEY, false)) {
                final String xtor = preferences.getString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, null);
                if (xtor != null) {
                    ParamOption beforeStcPositionWithEncoding = new ParamOption()
                            .setRelativePosition(ParamOption.RelativePosition.before)
                            .setRelativeParameterKey(Hit.HitParam.JSON.stringValue())
                            .setEncode(true);

                    if (preferences.getBoolean(TrackerConfigurationKeys.IS_FIRST_AFTER_INSTALL_HIT_KEY, true)) {
                        tracker.setParam(Hit.HitParam.Source.stringValue(), xtor, beforeStcPositionWithEncoding);
                        preferences.edit().putBoolean(TrackerConfigurationKeys.IS_FIRST_AFTER_INSTALL_HIT_KEY, false).apply();
                    } else {
                        tracker.setParam(Hit.HitParam.RemanentSource.stringValue(), xtor, beforeStcPositionWithEncoding);
                    }
                    preferences.edit().putBoolean(TrackerConfigurationKeys.CAMPAIGN_ADDED_KEY, true).apply();
                }
            }
        }

        setIdentifiedVisitorInfos();


        ParamOption stcOptions = new ParamOption()
                .setAppend(true)
                .setEncode(true)
                .setType(ParamOption.Type.JSON)
                .setRelativePosition(ParamOption.RelativePosition.last);
        tracker.setParam(Hit.HitParam.JSON.stringValue(), LifeCycle.getMetrics(Tracker.getPreferences()), stcOptions);
        if ((Boolean) tracker.getConfiguration().get(TrackerConfigurationKeys.ENABLE_CRASH_DETECTION)) {
            tracker.setParam(Hit.HitParam.JSON.stringValue(), CrashDetectionHandler.getCrashInformation(), stcOptions);
        }

        final String referrer = Tracker.getPreferences().getString(TrackerConfigurationKeys.REFERRER, null);
        if (!TextUtils.isEmpty(referrer)) {
            tracker.setParam(Hit.HitParam.Refstore.stringValue(), referrer);
            Tracker.getPreferences().edit().putString(TrackerConfigurationKeys.REFERRER, null).apply();
        }

        Builder builder = new Builder(tracker);
        tracker.getBuffer().getVolatileParams().clear();
        TrackerQueue.getInstance().put(builder);

        tracker.Context().setLevel2(tracker.Context().getLevel2());
    }

    void setIdentifiedVisitorInfos() {
        if (Boolean.parseBoolean(String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.PERSIST_IDENTIFIED_VISITOR)))) {
            ParamOption beforeStcPosition = new ParamOption()
                    .setRelativePosition(ParamOption.RelativePosition.before)
                    .setRelativeParameterKey(Hit.HitParam.JSON.stringValue());

            ParamOption beforeStcPositionWithEncoding = new ParamOption()
                    .setRelativePosition(ParamOption.RelativePosition.before)
                    .setRelativeParameterKey(Hit.HitParam.JSON.stringValue()).setEncode(true);

            String visitorNumericID = Tracker.getPreferences().getString(IdentifiedVisitor.VISITOR_NUMERIC, null);
            String visitorCategory = Tracker.getPreferences().getString(IdentifiedVisitor.VISITOR_CATEGORY, null);
            String visitorTextID = Tracker.getPreferences().getString(IdentifiedVisitor.VISITOR_TEXT, null);
            if (visitorNumericID != null) {
                tracker.setParam(Hit.HitParam.VisitorIdentifierNumeric.stringValue(), visitorNumericID, beforeStcPosition);
            }
            if (visitorTextID != null) {
                tracker.setParam(Hit.HitParam.VisitorIdentifierText.stringValue(), visitorTextID, beforeStcPositionWithEncoding);
            }
            if (visitorCategory != null) {
                tracker.setParam(Hit.HitParam.VisitorCategory.stringValue(), visitorCategory, beforeStcPosition);
            }
        }
    }
}

class TrackerQueue extends LinkedBlockingQueue<Runnable> {

    private static boolean ENABLED_FILL_QUEUE_FROM_DATABASE = true;
    private static TrackerQueue instance;
    private final ScheduledExecutorService scheduledExecutorService;

    static void setEnabledFillQueueFromDatabase(boolean enabled) {
        ENABLED_FILL_QUEUE_FROM_DATABASE = enabled;
    }

    static boolean getEnabledFillQueueFromDatabase() {
        return ENABLED_FILL_QUEUE_FROM_DATABASE;
    }

    private TrackerQueue() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Runnable runnable;
                    while ((runnable = take()) != null) {
                        scheduledExecutorService.execute(runnable);
                    }
                } catch (InterruptedException e) {
                    Log.e(ATInternet.TAG, e.toString());
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    static TrackerQueue getInstance() {
        if (instance == null) {
            instance = new TrackerQueue();
        }
        return instance;
    }

    @Override
    public void put(Runnable runnable) {
        try {
            if (runnable != null) {
                super.put(runnable);
            }
        } catch (InterruptedException e) {
            Log.e(ATInternet.TAG, e.toString());
            Thread.currentThread().interrupt();
        }
    }
}

class TechnicalContext {

    private static String screenName = "";
    private static int level2 = -1;

    static void resetScreenContext() {
        screenName = null;
        level2 = -1;
    }

    static void setScreenName(String sn) {
        screenName = sn;
    }

    static String getScreenName() {
        return screenName;
    }

    static void setLevel2(int lv) {
        level2 = lv;
    }

    static int getLevel2() {
        return level2;
    }


    private static final int RETRY_GET_ADVERTISING_COUNT = 3;
    private static final String ANDROID_ID_KEY = "androidId";
    private static final String UUID_KEY = "UUID";

    static final Closure VTAG = new Closure() {
        @Override
        public String execute() {
            return "2.11.0";
        }
    };

    static final Closure PTAG = new Closure() {
        @Override
        public String execute() {
            return "Android";
        }
    };

    enum ConnectionType {
        GPRS, EDGE, TWOG, THREEG, THREEGPLUS, FOURG, WIFI, OFFLINE, UNKNOWN
    }

    static ConnectionType getConnection() {
        android.content.Context context = Tracker.getAppContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return ConnectionType.OFFLINE;
        }

        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return ConnectionType.WIFI;
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return ConnectionType.UNKNOWN;
        }

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return ConnectionType.GPRS;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return ConnectionType.EDGE;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return ConnectionType.TWOG;
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return ConnectionType.THREEG;
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return ConnectionType.THREEGPLUS;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_LTE:
                return ConnectionType.FOURG;
            default:
                return ConnectionType.UNKNOWN;
        }
    }

    static Closure getConnectionType() {
        return new Closure() {
            @Override
            public String execute() {
                switch (getConnection()) {
                    case GPRS:
                        return "gprs";
                    case EDGE:
                        return "edge";
                    case TWOG:
                        return "2g";
                    case THREEG:
                        return "3g";
                    case THREEGPLUS:
                        return "3g+";
                    case FOURG:
                        return "4g";
                    case WIFI:
                        return "wifi";
                    case UNKNOWN:
                        return "unknown";
                    default:
                        return "offline";
                }
            }
        };
    }

    static Closure getCarrier() {
        return new Closure() {
            @Override
            public String execute() {
                android.content.Context context = Tracker.getAppContext();
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE);
                return telephonyManager != null ? telephonyManager.getNetworkOperatorName() : "";
            }
        };
    }

    static Closure getLanguage() {
        return new Closure() {
            @Override
            public String execute() {
                Locale localeDefault = Locale.getDefault();
                if (localeDefault == null) {
                    return "";
                }
                return String.format("%1$s-%2$s-%3$s", localeDefault.getLanguage(), localeDefault.getCountry(), localeDefault.getVariant());
            }
        };
    }

    static Closure getDevice() {
        return new Closure() {
            @Override
            public String execute() {
                return String.format("[%1$s]-[%2$s]", Build.BRAND, Tool.removeCharacters(Build.MODEL, " ", "-", ".").toLowerCase());
            }
        };
    }

    static Closure getManufacturer() {
        return new Closure() {
            @Override
            public String execute() {
                return Build.MANUFACTURER;
            }
        };
    }

    static Closure getModel() {
        return new Closure() {
            @Override
            public String execute() {
                return Build.MODEL;
            }
        };
    }

    static Closure getOS() {
        return new Closure() {
            @Override
            public String execute() {
                return String.format("[android]-[%s]", Build.VERSION.RELEASE);
            }
        };
    }

    static Closure getLocalHour() {
        return new Closure() {
            @Override
            public String execute() {
                return new SimpleDateFormat("HH'x'mm'x'ss", Locale.getDefault()).format(new Date());
            }
        };
    }

    static Closure getApplicationIdentifier() {
        return new Closure() {
            @Override
            public String execute() {
                return Tracker.getAppContext().getPackageName();
            }
        };
    }

    static Closure getApplicationVersion() {
        return new Closure() {
            @Override
            public String execute() {
                android.content.Context context = Tracker.getAppContext();
                String versionName = "";
                try {
                    if (context.getPackageManager() != null &&
                            context.getPackageName() != null &&
                            context.getPackageManager().getPackageInfo(context.getPackageName(), 0) != null) {

                        versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                return String.format("[%s]", versionName);
            }
        };
    }

    static Closure getResolution() {
        return new Closure() {
            @Override
            public String execute() {
                android.content.Context context = Tracker.getAppContext();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((WindowManager) context.getApplicationContext().getSystemService(android.content.Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
                return displayMetrics.widthPixels + "x" + displayMetrics.heightPixels;
            }
        };
    }

    @TargetApi(17)
    static Closure getDiagonal() {
        return new Closure() {
            @Override
            public String execute() {
                DisplayMetrics metrics = new DisplayMetrics();
                Display d = ((WindowManager) Tracker.getAppContext().getSystemService(Context.WINDOW_SERVICE))
                        .getDefaultDisplay();
                d.getMetrics(metrics);

                int widthPixels = metrics.widthPixels;
                int heightPixels = metrics.heightPixels;

                try {
                    // includes window decorations (statusbar bar/menu bar)
                    if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
                        widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                        heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                    }
                    // includes window decorations (statusbar bar/menu bar)
                    if (Build.VERSION.SDK_INT >= 17) {
                        Point realSize = new Point();
                        d.getRealSize(realSize);

                        widthPixels = realSize.x;
                        heightPixels = realSize.y;

                    }
                } catch (Exception e) {
                    Log.e(ATInternet.TAG, e.toString());
                }

                double x = Math.pow(widthPixels / metrics.xdpi, 2);
                double y = Math.pow(heightPixels / metrics.ydpi, 2);


                return String.format(Locale.getDefault(), "%.1f", Math.sqrt(x + y));
            }
        };
    }

    static Closure getDownloadSource(final Tracker tracker) {
        return new Closure() {
            @Override
            public String execute() {
                Object dls = tracker.getConfiguration().get(TrackerConfigurationKeys.DOWNLOAD_SOURCE);
                if (dls != null) {
                    return String.valueOf(dls);
                }
                return "ext";
            }
        };
    }

    static Closure getUserId(final String identifier) {
        return new Closure() {
            @SuppressLint("HardwareIds")
            @Override
            public String execute() {
                final android.content.Context context = Tracker.getAppContext();
                SharedPreferences preferences = context.getSharedPreferences(TrackerConfigurationKeys.PREFERENCES, Context.MODE_PRIVATE);

                if (preferences.getBoolean(TrackerConfigurationKeys.OPT_OUT_ENABLED, false)) {
                    return "opt-out";
                } else if (identifier.equals(ANDROID_ID_KEY)) {
                    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                } else if (identifier.equals(UUID_KEY)) {
                    String uuid = preferences.getString(TrackerConfigurationKeys.IDCLIENT_UUID, null);
                    if (uuid == null) {
                        uuid = UUID.randomUUID().toString();
                        preferences.edit().putString(TrackerConfigurationKeys.IDCLIENT_UUID, uuid).apply();
                    }
                    return uuid;
                } else {
                    try {
                        com.google.android.gms.ads.identifier.AdvertisingIdClient.Info adInfo = null;
                        int count = 0;
                        while (adInfo == null && count < RETRY_GET_ADVERTISING_COUNT) {
                            adInfo = com.google.android.gms.ads.identifier.AdvertisingIdClient.getAdvertisingIdInfo(context);
                            count++;
                        }
                        if (adInfo != null && !adInfo.isLimitAdTrackingEnabled()) {
                            return adInfo.getId();
                        } else {
                            return "opt-out";
                        }
                    } catch (Exception e) {
                        Log.e(ATInternet.TAG, e.toString());
                        return "";
                    }
                }
            }
        };
    }

    static void optOut(android.content.Context context, boolean enabled) {
        context.getSharedPreferences(TrackerConfigurationKeys.PREFERENCES, android.content.Context.MODE_PRIVATE).edit().putBoolean(TrackerConfigurationKeys.OPT_OUT_ENABLED, enabled).apply();
    }

    static boolean optOutEnabled(android.content.Context context) {
        return context.getSharedPreferences(TrackerConfigurationKeys.PREFERENCES, android.content.Context.MODE_PRIVATE).getBoolean(TrackerConfigurationKeys.OPT_OUT_ENABLED, false);
    }
}

class Tool {

    enum CallbackType {
        FIRST_LAUNCH, BUILD, SEND, PARTNER, WARNING, SAVE, ERROR
    }

    static String percentEncode(String s) {
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(ATInternet.TAG, e.toString());
        }
        return s.replace("+", "%20")
                .replace("*", "%2A")
                .replace("-", "%2D")
                .replace(".", "%2E")
                .replace("_", "%5F");
    }

    static String percentDecode(String s) {
        try {
            s = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(ATInternet.TAG, e.toString());
        }
        return s;
    }

    static String removeCharacters(String s, String... charTobeRemoved) {
        String result = s;
        for (String c : charTobeRemoved) {
            result = result.replace(c, "");
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    static String convertToString(Object value, String separator) {
        separator = (!TextUtils.isEmpty(separator)) ? separator : ",";
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;

        if (value != null) {
            if (value instanceof List) {
                List<Object> listResult = (List<Object>) value;
                for (Object object : listResult) {
                    value = object;
                    if (isFirst) {
                        result = new StringBuilder(convertToString(value, separator));
                        isFirst = false;
                    } else {
                        result.append(separator).append(convertToString(value, separator));
                    }
                }
            } else if (value instanceof Object[]) {
                Object[] objects = (Object[]) value;
                value = Arrays.asList(objects);
                return convertToString(value, separator);
            } else if (value instanceof Map) {
                result = new StringBuilder(new JSONObject((Map) value).toString());
            } else {
                result = new StringBuilder(String.valueOf(value));
            }
        }

        return result.toString();
    }

    static Closure getTimeStamp() {
        return new Closure() {
            @Override
            public String execute() {

                double result = System.currentTimeMillis() / 1000.0;
                long d = (long) result;
                String afterZero = Double.toString(result - d);

                return afterZero.length() > 1 ? Long.toString(d) + afterZero.substring(1) : "";
            }
        };
    }

    static boolean isTablet(android.content.Context context) {
        return (context.getResources().getConfiguration().screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) >= android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    static Tracker.OfflineMode convertStringToOfflineMode(String offlineMode) {
        switch (offlineMode) {
            case "always":
                return Tracker.OfflineMode.always;
            case "required":
                return Tracker.OfflineMode.required;
            default:
                return Tracker.OfflineMode.never;
        }
    }

    static void executeCallback(TrackerListener trackerListener, CallbackType callbackType, String message, TrackerListener.HitStatus... hitStatuses) {
        if (trackerListener != null) {
            switch (callbackType) {
                case FIRST_LAUNCH:
                    trackerListener.trackerNeedsFirstLaunchApproval(message);
                    break;
                case BUILD:
                    trackerListener.buildDidEnd(hitStatuses[0], message);
                    break;
                case SEND:
                    trackerListener.sendDidEnd(hitStatuses[0], message);
                    break;
                case PARTNER:
                    trackerListener.didCallPartner(message);
                    break;
                case WARNING:
                    trackerListener.warningDidOccur(message);
                    break;
                case SAVE:
                    trackerListener.saveDidEnd(message);
                    break;
                default://error
                    trackerListener.errorDidOccur(message);
                    break;
            }
        }
    }

    static int getDaysBetweenTimes(long latestTimeMillis, long oldestTimeMillis) {
        return (int) TimeUnit.DAYS.convert((latestTimeMillis - oldestTimeMillis), TimeUnit.MILLISECONDS);
    }

    static int getSecondsBetweenTimes(long latestTimeMillis, long oldestTimeMillis) {
        return (int) TimeUnit.SECONDS.convert((latestTimeMillis - oldestTimeMillis), TimeUnit.MILLISECONDS);
    }

    static String formatNumberLength(String s, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < length; i++) {
            sb.append('0');
        }
        return sb.append(s).toString();
    }

    static boolean isJSON(String s) {
        if (s == null) return false;

        try {
            JSONObject obj = new JSONObject(s);
            if (obj.toString().equals(s)) {
                return true;
            }
            return false;
        } catch (JSONException e) {
            return false;
        }
    }

    static boolean isArray(String s) {
        if (s == null) return false;

        try {
            JSONArray array = new JSONArray(s);
            if (array.toString().equals(s)) {
                return true;
            }
            return false;
        } catch (JSONException e) {
            return false;
        }
    }

    static Map<String, Object> toMap(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = jsonObject.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = null;
            try {
                value = jsonObject.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put(key, value);
        }
        return map;
    }

    static SparseIntArray sortSparseIntArrayByKey(SparseIntArray arr) {
        int length = arr.size();
        int[] keys = new int[length];
        for (int i = 0; i < length; i++) {
            keys[i] = arr.keyAt(i);
        }

        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < length - 1; i++) {
                int k = keys[i - 1];
                int k1 = keys[i];
                if (k > k1) {
                    keys[i - 1] = k1;
                    keys[i] = k;
                    swapped = true;
                }
            }
        } while (swapped);

        SparseIntArray res = new SparseIntArray();
        for (int k : keys) {
            res.append(k, arr.get(k));
        }

        return res;
    }

    static String appendParameterValues(Param param) {
        boolean isFirst = true;
        StringBuilder result = new StringBuilder();

        if (param == null) {
            return result.toString();
        }
        for (Closure closure : param.getValues()) {
            if (isFirst) {
                result.append(closure.execute());
                isFirst = false;
            } else {
                if (param.getOptions() != null) {
                    result.append(param.getOptions().getSeparator());
                } else {
                    result.append(",");
                }
                result.append(closure.execute());
            }
        }

        return result.toString();
    }

    static String SHA256(String s) {
        String baseString = "AT" + s;
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-256");
            md.update(baseString.getBytes());

            byte[] byteData = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
        } catch (Exception e) {
            Log.e(ATInternet.TAG, e.toString());
        }
        return sb.toString();
    }

    static Drawable getResizedImage(int imageID, android.content.Context context, int width, int height) {
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), imageID);
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }

    static void setVisibleViewWithAnimation(View view, boolean visible) {
        AlphaAnimation animation;
        if (visible) {
            view.setVisibility(View.VISIBLE);
            animation = new AlphaAnimation(0.f, 1.f);
        } else {
            view.setVisibility(View.GONE);
            animation = new AlphaAnimation(1.f, 0.f);
        }
        animation.setDuration(400);
        view.startAnimation(animation);
    }

    static LinkedHashMap<String, String> getParameters(String hit) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        try {
            URL url = new URL(hit);
            map.put("ssl", url.getProtocol().equals("http") ? "Off" : "On");
            map.put("log", url.getHost());
            String[] queryComponents = url.getQuery().split("&");
            for (String queryComponent : queryComponents) {
                String[] elem = queryComponent.split("=");
                if (elem.length > 1) {
                    elem[1] = Tool.percentDecode(elem[1]);
                    if (Tool.isJSON(elem[1])) {
                        JSONObject json = new JSONObject(elem[1]);
                        if (elem[0].equals(Hit.HitParam.JSON.stringValue())) {
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
            Log.e(ATInternet.TAG, e.toString());
        }
        return map;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("deprecation")
    @TargetApi(23)
    static int getColor(android.content.Context context, int colorId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getResources().getColor(colorId, null);
        } else {
            return context.getResources().getColor(colorId);
        }
    }

    static void runOnMainThread(Activity act, Runnable r) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
        } else {
            act.runOnUiThread(r);
        }
    }
}

class Storage extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String HITS_STORAGE_TABLE = "StoredOfflineHit";
    private static final String ID = "id";
    private static final String HIT = "hit";
    private static final String RETRY = "retry";
    private static final String DATE = "date";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM " + HITS_STORAGE_TABLE + " ";
    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + HITS_STORAGE_TABLE + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    HIT + " TEXT NOT NULL , " +
                    DATE + " INTEGER NOT NULL , " +
                    RETRY + " INTEGER NOT NULL);";


    private static Storage instance;
    private static boolean initialized;
    private static String DATABASE_PATH;

    static void setDatabasePath(String path) {
        if (!initialized) {
            DATABASE_PATH = path;
        } else {
            Log.w(ATInternet.TAG, "Changing path when database is already initialized");
        }
    }

    static String getDatabasePath() {
        return DATABASE_PATH;
    }

    private Storage(Context context) {
        super(context, DATABASE_PATH, null, DATABASE_VERSION);
        initialized = true;
    }

    static Storage getInstance(Context context) {
        if (instance == null) {
            if (TextUtils.isEmpty(DATABASE_PATH)) {
                DATABASE_PATH = "TrackerDatabase";
            }
            instance = new Storage(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HITS_STORAGE_TABLE);
        onCreate(db);
    }

    String saveHit(String hit, long time, String oltParameter) {
        hit = buildHitToStore(hit, oltParameter);
        ContentValues values = new ContentValues();
        values.put(HIT, hit);
        values.put(RETRY, 0);
        values.put(DATE, time);
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return null;
        }
        db.insert(HITS_STORAGE_TABLE, null, values);
        db.close();
        return hit;
    }

    void deleteHit(String hit) {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(HITS_STORAGE_TABLE, HIT + "='" + hit + "'", null);
        db.close();
    }

    String buildHitToStore(String hit, String olt) {
        String[] hitComponents = hit.split("&");
        StringBuilder newHitBuilder = new StringBuilder(hitComponents[0]);

        for (int i = 1; i < hitComponents.length; i++) {
            String[] parameterComponents = hitComponents[i].split("=");

            if (parameterComponents[0].equals("cn")) {
                newHitBuilder.append("&cn=offline");
            } else {
                newHitBuilder.append('&').append(hitComponents[i]);
            }

            if (parameterComponents[0].equals("ts") || parameterComponents[0].equals("mh")) {
                newHitBuilder.append("&olt=").append(olt);
            }
        }
        return newHitBuilder.toString();
    }

    void updateRetry(String hit, int retry) {
        ContentValues values = new ContentValues();
        values.put(RETRY, retry);
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.update(HITS_STORAGE_TABLE, values, HIT + "='" + hit + "'", null);
        db.close();
    }

    int getCountOfflineHits() {
        int result = -1;
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return result;
        }
        Cursor c = db.rawQuery(SELECT_ALL_QUERY, null);
        if (c != null) {
            result = c.getCount();
            c.close();
        }
        db.close();
        return result;
    }

    void removeAllOfflineHits() {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(HITS_STORAGE_TABLE, null, null);
        db.close();
    }

    void removeOldOfflineHits(int storageDuration) {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -storageDuration);
        long maxOldDate = cal.getTime().getTime();
        db.delete(HITS_STORAGE_TABLE, DATE + " < " + maxOldDate, null);
        db.close();
    }

    ArrayList<Hit> getOfflineHits() {
        ArrayList<Hit> hits = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SELECT_ALL_QUERY + "ORDER BY " + ID + " ASC", null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                String hit = c.getString(c.getColumnIndex(HIT));
                String time = c.getString(c.getColumnIndex(DATE));
                int retry = c.getInt(c.getColumnIndex(RETRY));
                hits.add(new Hit(hit, new Date(Long.parseLong(time)), retry, true));
            }
            while (c.moveToNext());
            c.close();
        }
        db.close();
        return hits;
    }

    Hit getOldestOfflineHit() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SELECT_ALL_QUERY + "WHERE " + DATE + " = (SELECT MIN(" + DATE + ") FROM " + HITS_STORAGE_TABLE + " )", null);
        if (c != null && c.moveToFirst()) {
            String hit = c.getString(c.getColumnIndex(HIT));
            String time = c.getString(c.getColumnIndex(DATE));
            int retry = c.getInt(c.getColumnIndex(RETRY));
            if (hit != null && time != null) {
                c.close();
                db.close();
                return new Hit(hit, new Date(Long.parseLong(time)), retry, true);
            }
            c.close();
        }
        db.close();
        return null;
    }

    Hit getLatestOfflineHit() {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor c = db.rawQuery(SELECT_ALL_QUERY + "WHERE " + DATE + " = (SELECT MAX(" + DATE + ") FROM " + HITS_STORAGE_TABLE + " )", null);
        if (c != null && c.moveToFirst()) {
            String hit = c.getString(c.getColumnIndex(HIT));
            String time = c.getString(c.getColumnIndex(DATE));
            int retry = c.getInt(c.getColumnIndex(RETRY));
            if (hit != null && time != null) {
                c.close();
                db.close();
                return new Hit(hit, new Date(Long.parseLong(time)), retry, true);
            }
            c.close();
        }
        db.close();
        return null;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        try {
            return super.getReadableDatabase();
        } catch (Exception e) {
            Log.e(ATInternet.TAG, "Cannot getReadableDatabase : " + e);
            return null;
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        try {
            return super.getWritableDatabase();
        } catch (Exception e) {
            Log.e(ATInternet.TAG, "Cannot getWritableDatabase : " + e);
            return null;
        }
    }
}

class Lists {

    private Lists() {
        throw new IllegalStateException("Private class");
    }

    static HashMap<String, Hit.HitType> getProcessedTypes() {
        HashMap<String, Hit.HitType> map = new HashMap<>();
        map.put("audio", Hit.HitType.Audio);
        map.put("video", Hit.HitType.Video);
        map.put("vpre", Hit.HitType.Video);
        map.put("vmid", Hit.HitType.Video);
        map.put("vpost", Hit.HitType.Video);
        map.put("animation", Hit.HitType.Animation);
        map.put("anim", Hit.HitType.Animation);
        map.put("podcast", Hit.HitType.PodCast);
        map.put("rss", Hit.HitType.RSS);
        map.put("email", Hit.HitType.Email);
        map.put("pub", Hit.HitType.Publicite);
        map.put("ad", Hit.HitType.Publicite);
        map.put("click", Hit.HitType.Touch);
        map.put("clic", Hit.HitType.Touch);
        map.put("AT", Hit.HitType.AdTracking);
        map.put("pdt", Hit.HitType.ProduitImpression);
        map.put("mvt", Hit.HitType.MvTesting);
        map.put("wbo", Hit.HitType.Weborama);
        map.put("screen", Hit.HitType.Screen);
        return map;
    }

    static HashSet<String> getReadOnlyParams() {
        HashSet<String> set = new HashSet<>();
        set.add("vtag");
        set.add("ptag");
        set.add("lng");
        set.add("mfmd");
        set.add("manufacturer");
        set.add("model");
        set.add("os");
        set.add("apvr");
        set.add("hl");
        set.add("r");
        set.add("car");
        set.add("cn");
        set.add("ts");
        set.add("olt");
        return set;
    }


    static HashSet<String> getSliceReadyParams() {
        HashSet<String> set = new HashSet<>();
        set.add("ati");
        set.add("atc");
        set.add("pdtl");
        set.add("stc");
        set.add("events");
        return set;
    }
}