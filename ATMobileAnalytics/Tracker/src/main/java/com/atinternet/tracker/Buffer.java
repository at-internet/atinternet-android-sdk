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

import java.util.ArrayList;

import static com.atinternet.tracker.Param.Type;

/**
 * Object to stock parameters to build hit
 */
class Buffer {

    /**
     * Collection to stock persistent parameters
     */
    private final ArrayList<Param> persistentParams;

    /**
     * Collection to stock volatile parameters
     */
    private final ArrayList<Param> volatileParams;

    /**
     * Identifier Key
     */
    private String identifierKey;

    /**
     * Constant closures
     */
    private String os;
    private Closure osClosure;

    private String device;
    private Closure deviceClosure;

    private String diagonal;
    private Closure diagonalClosure;

    private String apid;
    private String apvr;
    private Closure apidClosure;
    private Closure apvrClosure;

    /**
     * Get persistent parameters
     *
     * @return ArrayList<Param>
     */
    public ArrayList<Param> getPersistentParams() {
        return persistentParams;
    }

    /**
     * Get volatile parameters
     *
     * @return ArrayList<Param>
     */
    public ArrayList<Param> getVolatileParams() {
        return volatileParams;
    }

    /**
     * Set a new user id key
     *
     * @param identifierKey String
     */
    public void setIdentifierKey(String identifierKey) {
        this.identifierKey = identifierKey;
        int i = 0, length = persistentParams.size();
        boolean find = false;
        if (length > 0) {
            while (i < length || !find) {
                if (persistentParams.get(i).getKey().equals(Hit.HitParam.UserId.stringValue())) {
                    persistentParams.get(i).setValue(TechnicalContext.getUserId(identifierKey));
                    find = true;
                }
                i++;
            }
        }
    }

    /**
     * Default constructor
     */
    public Buffer(Tracker tracker) {
        persistentParams = new ArrayList<Param>();
        volatileParams = new ArrayList<Param>();
        identifierKey = String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.IDENTIFIER));

        initConstantClosures();
        addContextVariables(tracker);
    }

    /**
     * Add context variables in persistent parameters list
     */
    private void addContextVariables(Tracker tracker) {
        // Boolean isPersistent have to be true in all cases
        ParamOption persistent = new ParamOption().setPersistent(true);
        ParamOption persistentWithEncoding = new ParamOption().setPersistent(true).setEncode(true);

        persistentParams.add(new Param("vtag", TechnicalContext.VTAG, Type.String, persistent));
        persistentParams.add(new Param("ptag", TechnicalContext.PTAG, Type.String, persistent));
        persistentParams.add(new Param("lng", TechnicalContext.getLanguage(), Type.String, persistent));
        persistentParams.add(new Param("mfmd", deviceClosure, Type.String, persistent));
        persistentParams.add(new Param("os", osClosure, Type.String, persistent));
        persistentParams.add(new Param("apid", apidClosure, Type.String, persistent));
        persistentParams.add(new Param("apvr", apvrClosure, Type.String, persistentWithEncoding));
        persistentParams.add(new Param("hl", TechnicalContext.getLocalHour(), Type.String, persistent));
        persistentParams.add(new Param("r", TechnicalContext.getResolution(), Type.String, persistent));
        persistentParams.add(new Param("dg", diagonalClosure, Type.String, persistent));
        persistentParams.add(new Param("car", TechnicalContext.getCarrier(), Type.String, persistentWithEncoding));
        persistentParams.add(new Param("cn", TechnicalContext.getConnectionType(), Type.String, persistentWithEncoding));
        persistentParams.add(new Param("ts", Tool.getTimeStamp(), Type.String, persistent));
        persistentParams.add(new Param("dls", TechnicalContext.getDownloadSource(tracker), Type.String, persistent));
        persistentParams.add(new Param("idclient", TechnicalContext.getUserId(identifierKey), Type.String, persistent));
    }

    /**
     * Init the constant closures
     */
    private void initConstantClosures() {
        os = TechnicalContext.getOS().execute();
        device = TechnicalContext.getDevice().execute();
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
