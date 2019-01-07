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

/**
 * Wrapper class for mm testing tracking
 */
public class MvTesting extends BusinessObject {

    private String test, creation;
    private int waveId;
    private MvTestingVars vars;

    MvTesting(Tracker tracker) {
        super(tracker);
        test = "";
        waveId = -1;
        creation = "";
    }

    /**
     * Get the mvtesting test
     *
     * @return the mvtesting test
     */
    public String getTest() {
        return test;
    }

    /**
     * Get the mvtesting creation
     *
     * @return the creation
     */
    public String getCreation() {
        return creation;
    }

    /**
     * Get the wave identifier
     *
     * @return the wave identifier
     */
    public int getWaveId() {
        return waveId;
    }

    /**
     * Get mvtesting variables
     *
     * @return MVTestingVars instance
     */
    public MvTestingVars Vars() {
        if (vars == null) {
            vars = new MvTestingVars();
        }
        return vars;
    }

    /**
     * Set a new mvtesting test
     *
     * @param test /
     * @return MVTesting instance
     */
    public MvTesting setTest(String test) {
        this.test = test;

        return this;
    }

    /**
     * Set a new mvtesting waveId
     *
     * @param waveId /
     * @return MVTesting instance
     */
    public MvTesting setWaveId(int waveId) {
        this.waveId = waveId;

        return this;
    }

    /**
     * Set a new mvtesting creation
     *
     * @param creation /
     * @return MVTesting instance
     */
    public MvTesting setCreation(String creation) {
        this.creation = creation;

        return this;
    }

    /**
     * Send
     */
    public void send() {
        tracker.getDispatcher().dispatch(this);
    }


    @Override
    void setParams() {
        ParamOption encoding = new ParamOption().setEncode(true);

        tracker.setParam(Hit.HitParam.HitType.stringValue(), "mvt")
                .setParam(Hit.HitParam.MvTestingTest.stringValue(), String.format("%s-%d-%s", test, waveId, creation), encoding);

        if (vars != null) {
            for (int i = 0; i < vars.size(); i++) {
                MvTestingVar mvtc = vars.get(i);
                tracker.setParam("abmv" + String.valueOf(i + 1), String.format("%s-%s", mvtc.getVariable(), mvtc.getVersion()), encoding);
            }
        }
    }
}
