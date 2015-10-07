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

import java.util.HashMap;
import java.util.HashSet;

class Lists {

    /**
     * Get a map
     *
     * @return HashMap
     */
    static HashMap<String, Hit.HitType> getProcessedTypes() {
        return new HashMap<String, Hit.HitType>() {{
            put("audio", Hit.HitType.Audio);
            put("video", Hit.HitType.Video);
            put("vpre", Hit.HitType.Video);
            put("vmid", Hit.HitType.Video);
            put("vpost", Hit.HitType.Video);
            put("animation", Hit.HitType.Animation);
            put("anim", Hit.HitType.Animation);
            put("podcast", Hit.HitType.PodCast);
            put("rss", Hit.HitType.RSS);
            put("email", Hit.HitType.Email);
            put("pub", Hit.HitType.Publicite);
            put("ad", Hit.HitType.Publicite);
            put("click", Hit.HitType.Touch);
            put("clic", Hit.HitType.Touch);
            put("AT", Hit.HitType.AdTracking);
            put("pdt", Hit.HitType.ProduitImpression);
            put("mvt", Hit.HitType.MvTesting);
            put("wbo", Hit.HitType.Weborama);
            put("screen", Hit.HitType.Screen);
        }};
    }

    /**
     * Get a set <key>
     *
     * @return HashSet<String>
     */
    static HashSet<String> getReadOnlyConfigs() {
        return new HashSet<String>() {{
        }};
    }

    /**
     * Get a set <key>
     *
     * @return HashSet<String>
     */
    static HashSet<String> getReadOnlyParams() {
        return new HashSet<String>() {{
            add("vtag");
            add("ptag");
            add("lng");
            add("mfmd");
            add("os");
            add("apvr");
            add("hl");
            add("r");
            add("car");
            add("cn");
            add("ts");
        }};
    }

    /**
     * Get a set <key>
     *
     * @return HashSet<String>
     */
    static HashSet<String> getSliceReadyParams() {
        return new HashSet<String>() {{
            add("ati");
            add("atc");
            add("pdtl");
            add("stc");
        }};
    }
}
