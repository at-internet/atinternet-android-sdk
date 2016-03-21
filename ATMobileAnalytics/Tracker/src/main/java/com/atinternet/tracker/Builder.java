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

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.atinternet.tracker.ParamOption.RelativePosition.last;
import static com.atinternet.tracker.ParamOption.RelativePosition.none;
import static com.atinternet.tracker.Tool.CallbackType;

/**
 * Hit builder
 */
class Builder implements Runnable {

    // String Constants
    private static final String EMPTY_VALUE = "";
    private static final String PERCENT_VALUE = "%";
    private static final String MH_PARAMETER_FORMAT = "%1$s-%2$s-%3$s";
    private static final String MHID_FORMAT = "%02d%02d%02d%d";
    private static final String OPT_OUT = "opt-out";

    //Number of configuration part
    private static final int REFCONFIGCHUNKS = 4;

    // MH parameter max length
    private static final int MH_PARAMETER_MAX_LENGTH = 30;

    // MHERR parameter length
    private static final int MHERR_PARAMETER_LENGTH = 8;

    // hit max length
    private static final int HIT_MAX_LENGTH = 1600;

    // Max of subhits count
    private static final int HIT_MAX_COUNT = 999;

    /**
     * Contains Tracker configuration
     */
    private final Configuration configuration;

    /**
     * Collection to stock persistent parameters
     */
    private final ArrayList<Param> persistentParams;

    /**
     * Collection to stock volatile parameters
     */
    private final ArrayList<Param> volatileParams;

    /**
     * Tracker instance
     */
    private final Tracker tracker;

    /**
     * Init a Hit builder
     *
     * @param tracker Tracker
     */
    public Builder(Tracker tracker) {
        this.tracker = tracker;
        this.configuration = tracker.getConfiguration();
        this.volatileParams = new ArrayList<Param>(tracker.getBuffer().getVolatileParams());
        this.persistentParams = new ArrayList<Param>(tracker.getBuffer().getPersistentParams());
    }

    /**
     * Build the configuration
     *
     * @return String
     */
    private String buildConfiguration() {
        String conf = EMPTY_VALUE;
        int hitConfigChunks = 0;

        boolean isSecure = (Boolean) configuration.get(TrackerConfigurationKeys.SECURE);
        String log = String.valueOf(configuration.get(TrackerConfigurationKeys.LOG));
        String logSecure = String.valueOf(configuration.get(TrackerConfigurationKeys.LOG_SSL));
        String domain = String.valueOf(configuration.get(TrackerConfigurationKeys.DOMAIN));
        String pixelPath = String.valueOf(configuration.get(TrackerConfigurationKeys.PIXEL_PATH));
        String siteID = String.valueOf(configuration.get(TrackerConfigurationKeys.SITE));

        if (isSecure) {
            if (!TextUtils.isEmpty(logSecure)) {
                conf += "https://" + logSecure + ".";
                hitConfigChunks++;
            }
        } else {
            if (!TextUtils.isEmpty(log)) {
                conf += "http://" + log + ".";
                hitConfigChunks++;
            }
        }
        if (!TextUtils.isEmpty(domain)) {
            conf += domain;
            hitConfigChunks++;
        }
        if (!TextUtils.isEmpty(pixelPath)) {
            conf += pixelPath;
            hitConfigChunks++;
        }

        conf += "?s=" + siteID;
        hitConfigChunks++;


        if (hitConfigChunks != REFCONFIGCHUNKS) {
            Tool.executeCallback(tracker.getListener(), CallbackType.error, "There is something wrong with configuration : " + conf);
            conf = EMPTY_VALUE;
        }

        return conf;
    }

    /**
     * Build the hit
     */
    Object[] build() {
        ArrayList<String> prepareHitsList = new ArrayList<String>();
        ArrayList<String> hitsList = new ArrayList<String>();
        Integer countSplitHits = 1;
        int indexError = -1;
        String queryString = "";
        String idClient = "";
        String configuration = buildConfiguration();

        // Calcul pour connaitre la longueur maximum du hit
        String oltParameter = Tool.getTimeStamp().execute();
        int MAX_LENGTH_AVAILABLE = HIT_MAX_LENGTH - (configuration.length() + oltParameter.length() + MH_PARAMETER_MAX_LENGTH);

        LinkedHashMap<String, Object[]> dictionary = new LinkedHashMap<String, Object[]>();
        if (!TextUtils.isEmpty(configuration)) {
            dictionary = prepareQuery();
            Set<String> keySet = dictionary.keySet();

            // Outerloop est un label de référence si jamais une boucle doit être interrompue
            outerloop:

            for (String parameterKey : keySet) {
                Param p = (Param) dictionary.get(parameterKey)[0];
                String value = String.valueOf(dictionary.get(parameterKey)[1]);

                // Récupération de l'idclient pour éviter les redirections
                if (parameterKey.equals(Hit.HitParam.UserId.stringValue())) {
                    idClient = value;
                    MAX_LENGTH_AVAILABLE -= idClient.length();
                }
                // Si la valeur du paramètre est trop grande
                if (value.length() > MAX_LENGTH_AVAILABLE) {

                    // Si le paramètre est découpable
                    if (Lists.getSliceReadyParams().contains(parameterKey)) {

                        // Récupération du séparateur, de la clé et la valeur du paramètre courant
                        String separator = p.getOptions() != null ? p.getOptions().getSeparator() : ",";
                        String[] currentParameterString = value.split("=");
                        String currentKey = currentParameterString[0] + "=";
                        String currentValue = currentParameterString[1];

                        // On découpe la valeur du paramètre sur son séparateur
                        String[] valuesList = currentValue.split(separator);

                        for (int i = 0; i < valuesList.length; i++) {
                            String currentSplitValue = valuesList[i];

                            // Si la valeur courante est trop grande
                            if (currentSplitValue.length() > MAX_LENGTH_AVAILABLE) {
                                // Erreur : Valeur trop longue non découpable
                                indexError = countSplitHits;
                                queryString += currentKey;
                                int currentMaxLength = MAX_LENGTH_AVAILABLE - (MHERR_PARAMETER_LENGTH + queryString.length());

                                // On cherche la position du dernier % afin d'éviter les exceptions au moment de la découpe brutale
                                String splitError = currentSplitValue.substring(0, currentMaxLength);
                                int lastIndexOfPercent = splitError.lastIndexOf(PERCENT_VALUE);

                                if ((lastIndexOfPercent > currentMaxLength - 5) && (lastIndexOfPercent < currentMaxLength)) {
                                    queryString += splitError.substring(0, lastIndexOfPercent);
                                } else {
                                    queryString += splitError;
                                }
                                Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Multihits: Param " + parameterKey + " value still too long after slicing");

                                // On retourne à l'endroit du code où se trouve outerloop
                                break outerloop;
                            }
                            // Sinon si le hit déjà construit + la valeur courante est trop grand
                            else if (queryString.length() + currentSplitValue.length() > MAX_LENGTH_AVAILABLE) {
                                // On créé un nouveau tronçon
                                countSplitHits++;
                                prepareHitsList.add(queryString);
                                queryString = idClient + currentKey + (i == 0 ? currentSplitValue : separator + currentSplitValue);
                            }
                            // Sinon, on continue la construction normalement
                            else {
                                queryString += i == 0 ? currentKey + currentSplitValue : separator + currentSplitValue;
                            }
                        }
                    } else {
                        // Erreur : le paramètre n'est pas découpable
                        indexError = countSplitHits;
                        Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Multihits: parameter " + parameterKey + " value not allowed to be sliced");
                        break;
                    }
                }
                // Sinon, si le hit est trop grand, on le découpe entre deux paramètres
                else if (queryString.length() + value.length() > MAX_LENGTH_AVAILABLE) {
                    countSplitHits++;
                    prepareHitsList.add(queryString);
                    queryString = idClient + value;
                }
                //Sinon, on ne découpe pas
                else {
                    queryString += value;
                }
            }

            // Si un seul hit est construit
            if (countSplitHits == 1) {
                if (indexError == countSplitHits) {
                    hitsList.add(configuration + makeSubQuery("mherr", "1") + queryString);
                } else {
                    hitsList.add(configuration + queryString);
                }
            }
            // Sinon, si le nombre de tronçons > 999, erreur
            else if (countSplitHits > HIT_MAX_COUNT) {
                hitsList.add(configuration + makeSubQuery("mherr", "1") + queryString);
                Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Multihits: too much hit parts");
            }
            // Sinon, on ajoute les hits construits à la liste de hits à envoyer
            else {
                prepareHitsList.add(queryString);
                String mhID = mhIdSuffixGenerator();
                for (int i = 0; i < countSplitHits; i++) {
                    String countSplitHitsString = countSplitHits.toString();
                    String mhParameter = makeSubQuery("mh", String.format(MH_PARAMETER_FORMAT, Tool.formatNumberLength(Integer.toString(i + 1), countSplitHitsString.length()), countSplitHitsString, mhID));
                    if (indexError == (i + 1)) {
                        hitsList.add(configuration + mhParameter + makeSubQuery("mherr", "1") + prepareHitsList.get(i));
                    } else {
                        hitsList.add(configuration + mhParameter + prepareHitsList.get(i));
                    }
                }
            }
            if (tracker.getListener() != null) {
                String message = "";
                for (String hit : hitsList) {
                    message += hit + "\n";
                }
                Tool.executeCallback(tracker.getListener(), CallbackType.build, message, TrackerListener.HitStatus.Success);
            }
        }

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
            new Sender(tracker.getListener(), new Hit(url), false, oltParameter).send(true);
        }
    }

    /**
     * Organize the parameters to respect order
     *
     * @param completeBuffer ArrayList<Param>
     * @return ArrayList<Param>
     */
    private ArrayList<Param> organizeParameters(ArrayList<Param> completeBuffer) {
        Param lastParam = null;
        int position;
        boolean findFirst = false;
        boolean findLast = false;
        ArrayList<int[]> indexes;
        ArrayList<Param> params = new ArrayList<Param>();

        Param refParam = null;
        ArrayList<int[]> refParamPositions = Tool.findParameterPosition(Hit.HitParam.Refstore.stringValue(), completeBuffer);
        int indexRef = refParamPositions.isEmpty() ? -1 : refParamPositions.get(refParamPositions.size() - 1)[1];

        if (indexRef != -1) {
            refParam = completeBuffer.get(indexRef);
            completeBuffer.remove(indexRef);
            if (refParam.getOptions() != null &&
                    refParam.getOptions().getRelativePosition() != last &&
                    refParam.getOptions().getRelativePosition() != none) {
                Tool.executeCallback(tracker.getListener(), CallbackType.warning, "ref= parameter will be put in last position");
            }
        }

        for (Param param : completeBuffer) {

            ParamOption options = param.getOptions();

            if (options != null) {

                switch (options.getRelativePosition()) {
                    case first:
                        // Insertion au début
                        if (!findFirst) {
                            params.add(0, param);
                            findFirst = true;
                        } else {
                            Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Found more than one param with relative position set to first");
                            params.add(param);
                        }
                        break;
                    case last:
                        // Sauvegarde du dernier paramètre
                        if (!findLast) {
                            lastParam = param;
                            findLast = true;
                        } else {
                            Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Found more than one param with relative position set to last");
                            params.add(param);
                        }
                        break;
                    case before:
                        // Insertion d'un paramètre avant celui de référence
                        indexes = Tool.findParameterPosition(options.getRelativeParameterKey(), params);
                        position = indexes.isEmpty() ? -1 : indexes.get(indexes.size() - 1)[1];
                        if (position == -1) {
                            Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Relative param with key " + options.getRelativeParameterKey() + " could not be found. Param will be appended");
                            params.add(param);
                        } else {
                            params.add(position, param);
                        }
                        break;
                    case after:
                        // Insertion d'un paramètre après celui de référence
                        indexes = Tool.findParameterPosition(options.getRelativeParameterKey(), params);
                        position = indexes.isEmpty() ? -1 : indexes.get(indexes.size() - 1)[1];
                        if (position == -1) {
                            Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Relative param with key " + options.getRelativeParameterKey() + " could not be found. Param will be appended");
                            params.add(param);
                        } else {
                            params.add(position + 1, param);
                        }
                        break;
                    default:// Insertion à la suite
                        params.add(param);
                        break;
                }
            } else {
                params.add(param);
            }
        }
        // Insertion du dernier paramètre
        if (lastParam != null) {
            params.add(lastParam);
        }

        // Insertion du ref si existant
        if (refParam != null) {
            params.add(refParam);
        }

        return params;
    }

    /**
     * Prepare the hit queryString
     *
     * @return LinkedHashMap
     */
    private LinkedHashMap<String, Object[]> prepareQuery() {
        LinkedHashMap<String, Object[]> formattedParameters = new LinkedHashMap<String, Object[]>();

        ArrayList<Param> completeBuffer = new ArrayList<Param>();
        completeBuffer.addAll(persistentParams);
        completeBuffer.addAll(volatileParams);

        ArrayList<Param> params = organizeParameters(completeBuffer);

        for (Param p : params) {
            String value = p.getValue().execute();
            String key = p.getKey();

            HashMap<String, String> plugins = PluginParam.get(tracker);
            if (plugins.containsKey(key)) {
                String pluginClass = plugins.get(key);
                Plugin plugin = null;
                try {
                    plugin = (Plugin) Class.forName(pluginClass).newInstance();
                    plugin.execute(tracker);
                    value = plugin.getResponse();
                    p.setType(Param.Type.JSON);
                    key = Hit.HitParam.JSON.stringValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    value = null;
                }
            } else if (key.equals(Hit.HitParam.UserId.stringValue())) {
                if (TechnicalContext.doNotTrackEnabled(Tracker.getAppContext())) {
                    value = OPT_OUT;
                } else if (((Boolean) configuration.get(TrackerConfigurationKeys.HASH_USER_ID))) {
                    value = Tool.SHA_256(value);
                }
            }

            if (p.getType() == Param.Type.Closure && Tool.parseJSON(value) != null) {
                p.setType(Param.Type.JSON);
            }

            if (value != null) {
                if (p.getOptions() != null && p.getOptions().isEncode()) {
                    value = Tool.percentEncode(value);
                    p.getOptions().setSeparator(Tool.percentEncode(p.getOptions().getSeparator()));
                }
                int duplicateParamIndex = -1;
                String duplicateParamKey = null;

                Set<String> keys = formattedParameters.keySet();

                String[] keySet = keys.toArray(new String[keys.size()]);
                int length = keySet.length;
                for (int i = 0; i < length; i++) {
                    if (keySet[i].equals(key)) {
                        duplicateParamIndex = i;
                        duplicateParamKey = key;
                        break;
                    }
                }

                if (duplicateParamIndex != -1) {
                    List<Object[]> values = new ArrayList<Object[]>(formattedParameters.values());
                    Param duplicateParam = (Param) values.get(duplicateParamIndex)[0];
                    String str = ((String) formattedParameters.get(duplicateParamKey)[1]).split("=")[0] + "=";
                    String val = ((String) formattedParameters.get(duplicateParamKey)[1]).split("=")[1];

                    if (p.getType() == Param.Type.JSON) {
                        Object json = Tool.parseJSON(Tool.percentDecode(val));
                        Object newJson = Tool.parseJSON(Tool.percentDecode(value));

                        if (json != null && json instanceof JSONObject) {
                            Map dictionary = Tool.toMap((JSONObject) json);

                            if (newJson instanceof JSONObject) {
                                Map newDictionary = Tool.toMap((JSONObject) newJson);
                                dictionary.putAll(newDictionary);

                                JSONObject jsonData = new JSONObject(dictionary);
                                formattedParameters.put(key, new Object[]{duplicateParam, makeSubQuery(key, Tool.percentEncode(jsonData.toString()))});
                            } else {
                                Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Couldn't append value to a dictionary");
                            }
                        } else if (json != null && json instanceof JSONArray) {
                            try {
                                ArrayList<Object> array = new ArrayList<Object>();
                                JSONArray jArray = (JSONArray) json;
                                for (int i = 0; i < jArray.length(); i++) {
                                    array.add(jArray.get(i).toString());
                                }
                                if (newJson instanceof JSONArray) {
                                    jArray = (JSONArray) newJson;
                                    for (int i = 0; i < jArray.length(); i++) {
                                        array.add(jArray.get(i).toString());
                                    }
                                    JSONObject jsonData = new JSONObject(array.toString());
                                    formattedParameters.put(key, new Object[]{duplicateParam, makeSubQuery(key, Tool.percentEncode(jsonData.toString()))});
                                } else {
                                    Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Couldn't append value to an array");
                                }
                            } catch (JSONException e) {
                                Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Couldn't append value to an array");
                            }
                        } else {
                            Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Couldn't append value to a JSON Object");
                        }
                    } else if (duplicateParam.getType() == Param.Type.JSON) {
                        Tool.executeCallback(tracker.getListener(), CallbackType.warning, "Couldn't append value to a JSON Object");
                    } else {
                        formattedParameters.put(key, new Object[]{duplicateParam, str + val + p.getOptions().getSeparator() + value});
                    }
                } else {
                    formattedParameters.put(key, new Object[]{p, makeSubQuery(key, value)});
                }
            }
        }
        return formattedParameters;
    }

    /**
     * Generate mhIDSuffix
     *
     * @return String
     */
    private String mhIdSuffixGenerator() {
        Random random = new Random();
        int randId = random.nextInt(9000000) + 1000000;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return String.format(Locale.getDefault(), MHID_FORMAT, hour, minute, second, randId);
    }

    /**
     * Formatting a parameter
     *
     * @param key   String
     * @param value String
     * @return String
     */
    private String makeSubQuery(String key, String value) {
        return "&" + key + "=" + value;
    }
}
