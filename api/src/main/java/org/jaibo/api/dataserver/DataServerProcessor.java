package org.jaibo.api.dataserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Data server information provider
 * Copyright (C) 2014  Victor Polevoy (vityatheboss@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public abstract class DataServerProcessor implements HttpHandler {
    private String infoPath;
    private DataServerInfoObject infoObject;


    public String getInfoPath() {
        return infoPath;
    }

    public void setInfoPath(String infoPath) {
        this.infoPath = infoPath;
    }

    public DataServerInfoObject getInfoObject() {
        return infoObject;
    }

    private void setInfoObject(DataServerInfoObject infoObject) {
        this.infoObject = infoObject;
    }

    public void setParam(String jsonParam) {
        this.infoObject = new DataServerInfoObject(jsonParam);
    }

    public void setParam(DataServerInfoObject paramObject) {
        this.infoObject = paramObject;
    }

    public boolean checkPath(String path) {
        return path != null && !path.isEmpty() && path.equalsIgnoreCase(this.getInfoPath());
    }

    public String checkAndGetInfo(String path) {
        String answer = null;

        if (this.checkPath(path)) {
            answer = this.action();

            this.infoObject = null;
        }

        return answer;
    }

    protected abstract String action(); // returns json string with answer

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        DataServerRequestProcessor.processRequest(httpExchange, this);
    }
}



class DataServerRequestProcessor {
    public static void processRequest(HttpExchange httpExchange, DataServerProcessor processor) {
        showDebug(httpExchange);

        Map<String, String> attributes = getGetMethodParameters(httpExchange.getRequestURI().getQuery());

        if (!attributes.isEmpty()) {
            processor.setParam(new DataServerInfoObject(attributes));
        }

        String response = processor.checkAndGetInfo(httpExchange.getRequestURI().getPath());

        if (response == null || response.isEmpty()) {
            response = createError();
        }

        try {
            String jsonCallback = attributes.get("jsoncallback");
            if (jsonCallback != null && !jsonCallback.isEmpty()) {
                response = jsonCallback + "(" + response + ");";
            }

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createError() {
        DataServerInfoPackage errorPackage = new DataServerInfoPackage();
        errorPackage.setStatus(DataServerInfoStatusCode.INCORRECT_PATH);

        return errorPackage.toString();
    }

    private static void showDebug(HttpExchange httpExchange) {
        System.out.println(String.format("Web user requested: %s\nDate: %s\nFrom host: %s",
                httpExchange.getRequestURI(),
                new GregorianCalendar().getTime().toString(),
                httpExchange.getRemoteAddress()));
    }

    public static Map<String, String> getGetMethodParameters(String query){
        Map<String, String> result = new HashMap<String, String>();

        if (query != null && !query.isEmpty()) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                } else {
                    result.put(pair[0], "");
                }
            }
        }

        return result;
    }
}