package com.atinternet.tracker;

import android.text.TextUtils;

import java.util.HashMap;

class Endpoints {

    enum Resource {
        Socket, GetConfig, CheckConfig
    }

    private static final String SOCKET_FORMAT = "https://smartsdk.%1$satinternet-solutions.com";
    private static final String GETCONFIG_FORMAT = "https://8me4zn67yd.execute-api.eu-west-1.amazonaws.com/%1$s/token/%2$s/version/%3$s";
    private static final String CHECKCONFIG_FORMAT = "https://8me4zn67yd.execute-api.eu-west-1.amazonaws.com/%1$s/token/%2$s/version/%3$s/lastUpdate";

    private final HashMap<Resource, String> endpointsMap;

    Endpoints(final String env, final String... args) {
        final String token = args[0];

        endpointsMap = new HashMap<Resource, String>() {{
            put(Resource.Socket, String.format(SOCKET_FORMAT, (env == null || env.equals("prod")) ? "" : env + ".aws."));
            put(Resource.GetConfig, TextUtils.isEmpty(token) ? "" : String.format(GETCONFIG_FORMAT, env, token, args[1]));
            put(Resource.CheckConfig, TextUtils.isEmpty(token) ? "" : String.format(CHECKCONFIG_FORMAT, env, args[0], args[1]));
        }};
    }

    String getResourceEndpoint(Resource res) {
        return endpointsMap.get(res);
    }
}