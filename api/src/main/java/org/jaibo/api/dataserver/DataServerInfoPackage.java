package org.jaibo.api.dataserver;


import org.jaibo.api.dataserver.status.DataServerInfoArgumentErrorStatus;
import org.jaibo.api.dataserver.status.DataServerInfoIncorrectPathStatus;
import org.jaibo.api.dataserver.status.DataServerInfoParseErrorStatus;
import org.jaibo.api.dataserver.status.DataServerInfoSuccessStatus;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Data server json info package
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

public final class DataServerInfoPackage {
    private final DataServerInfoStatus[] statuses = {
        new DataServerInfoSuccessStatus(),
        new DataServerInfoArgumentErrorStatus(),
        new DataServerInfoIncorrectPathStatus(),
        new DataServerInfoParseErrorStatus()
    };

    private final String dataSection = "data";
    private DataServerInfoStatusCode status;

    private DataServerInfoObject infoObject;


    public DataServerInfoPackage() {
        this.clear();
    }


    public void clear() {
        this.status = DataServerInfoStatusCode.SUCCESS;
        this.infoObject = new DataServerInfoObject();
    }

    public void setInfoObject(DataServerInfoObject infoObject) {
        this.infoObject = infoObject;
    }

    private String getStatus() {
        String statusString = null;

        for(DataServerInfoStatus status : this.statuses) {
            if (status.getStatus() == this.status) {
                statusString = status.toInfoObject().toString();
            }
        }

        return statusString;
    }

    private DataServerInfoStatus getInfoStatusObject(DataServerInfoStatusCode statusCode) {
        DataServerInfoStatus statusObject = null;

        for(DataServerInfoStatus status : this.statuses) {
            if (status.getStatus() == statusCode) {
                statusObject = status;
            }
        }

        return statusObject;
    }

    public void setStatus(DataServerInfoStatusCode status) {
        this.status = status;
    }

    public void setStatusMessage(String statusMessage) {
        DataServerInfoStatus statusObject = this.getInfoStatusObject(this.status);

        if (statusObject != null) {
            statusObject.setMessage(statusMessage);
        }
    }

    @Override
    public String toString() {
        String jsonString = null;

        try {
            JSONObject jsonObject = new JSONObject(getStatus());

            if (this.status == DataServerInfoStatusCode.SUCCESS) {
                jsonObject.put(this.dataSection, infoObject.getJsonObject());
            }

            jsonString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}
