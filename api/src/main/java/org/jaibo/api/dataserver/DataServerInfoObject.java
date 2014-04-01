package org.jaibo.api.dataserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Data server info object
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

public final class DataServerInfoObject {
    private JSONObject jsonObject = new JSONObject();

    @Override
    public String toString() {
        return jsonObject.toString();
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void clear() {
        this.jsonObject = new JSONObject();
    }

    public void putData(String key, String stringData) {
        this.putDataInObject(this.jsonObject, key, stringData);
    }

    public void putArray(String key, Collection objectArray) {
        JSONArray jsonArray = new JSONArray(objectArray);

        try {
            this.jsonObject.put(key, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void putDataInObject(JSONObject jsonObject, String key, String stringData) {
        if (jsonObject != null) {

            try {
                jsonObject.put(key, stringData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void putSubObject(String key, DataServerInfoObject subObject) {
        try {
            if (subObject != null) {
                JSONObject jsonObject = null;

                if (this == subObject)
                    jsonObject = new JSONObject(subObject.getJsonObject().toString());
                else {
                    jsonObject = subObject.getJsonObject();
                }

                try {
                    this.jsonObject.put(key, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
