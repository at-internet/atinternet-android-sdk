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
import java.util.List;
import java.util.Map;

/**
 * Wrapper class to manage CustomVar instances
 */
public class CustomVars extends Helper {

    private AbstractScreen screen;

    CustomVars(Tracker tracker) {
        super(tracker);
    }

    CustomVars(AbstractScreen screen) {
        super(screen.tracker);
        this.screen = screen;
    }

    /**
     * Add a custom variable
     *
     * @param varId         custom variable identifier
     * @param value         custom variable value
     * @param customVarType custom variable type
     * @return a new CustomVar instance
     */
    public CustomVar add(int varId, String value, CustomVar.CustomVarType customVarType) {
        CustomVar customVar = new CustomVar(tracker)
                .setVarId(varId)
                .setValue(value)
                .setCustomVarType(customVarType);

        if (screen != null) {
            screen.getCustomVarsMap().put(customVar.getId(), customVar);
        } else {
            tracker.getBusinessObjects().put(customVar.getId(), customVar);
        }

        return customVar;
    }

    /**
     * Remove a custom var
     *
     * @param customVarId String
     */
    public void remove(String customVarId) {
        if (screen != null) {
            screen.getCustomVarsMap().remove(customVarId);
        } else {
            tracker.getBusinessObjects().remove(customVarId);
        }
    }

    /**
     * Remove all CustomVars
     */
    public void removeAll() {
        if (screen != null) {
            screen.getCustomVarsMap().clear();
        } else {
            List<String> ids = new ArrayList<>();
            for (Map.Entry<String, BusinessObject> entry : tracker.getBusinessObjects().entrySet()) {
                if (entry.getValue() instanceof CustomVar) {
                    ids.add(entry.getKey());
                }
            }
            for (String id : ids) {
                tracker.getBusinessObjects().remove(id);
            }
        }
    }
}
