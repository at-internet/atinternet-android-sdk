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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class EndpointsTest extends AbstractTestClass {

    @Test
    public void getResourceEndpointDevTest() {
        Endpoints endpoints = new Endpoints("dev", "tokenTest", "1.0.0");
        assertEquals(endpoints.getResourceEndpoint(Endpoints.Resource.Socket), "https://smartsdk.dev.aws.atinternet-solutions.com");
        assertEquals(endpoints.getResourceEndpoint(Endpoints.Resource.GetConfig), "https://8me4zn67yd.execute-api.eu-west-1.amazonaws.com/dev/token/tokenTest/version/1.0.0");
        assertEquals(endpoints.getResourceEndpoint(Endpoints.Resource.CheckConfig), "https://8me4zn67yd.execute-api.eu-west-1.amazonaws.com/dev/token/tokenTest/version/1.0.0/lastUpdate");
    }

    @Test
    public void getResourceEndpointPreprodTest() {
        Endpoints endpoints = new Endpoints("preprod", "tokenTest", "1.0.0");
        assertEquals(endpoints.getResourceEndpoint(Endpoints.Resource.Socket), "https://smartsdk.preprod.aws.atinternet-solutions.com");
        assertEquals(endpoints.getResourceEndpoint(Endpoints.Resource.GetConfig), "https://8me4zn67yd.execute-api.eu-west-1.amazonaws.com/preprod/token/tokenTest/version/1.0.0");
        assertEquals(endpoints.getResourceEndpoint(Endpoints.Resource.CheckConfig), "https://8me4zn67yd.execute-api.eu-west-1.amazonaws.com/preprod/token/tokenTest/version/1.0.0/lastUpdate");
    }

    @Test
    public void getResourceEndpointProdTest() {
        Endpoints endpoints = new Endpoints("prod", "tokenTest", "1.0.0");
        assertEquals(endpoints.getResourceEndpoint(Endpoints.Resource.Socket), "https://smartsdk.atinternet-solutions.com");
        assertEquals(endpoints.getResourceEndpoint(Endpoints.Resource.GetConfig), "https://8me4zn67yd.execute-api.eu-west-1.amazonaws.com/prod/token/tokenTest/version/1.0.0");
        assertEquals(endpoints.getResourceEndpoint(Endpoints.Resource.CheckConfig), "https://8me4zn67yd.execute-api.eu-west-1.amazonaws.com/prod/token/tokenTest/version/1.0.0/lastUpdate");
    }
}