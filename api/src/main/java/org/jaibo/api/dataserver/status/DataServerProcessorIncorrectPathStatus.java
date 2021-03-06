package org.jaibo.api.dataserver.status;

import org.jaibo.api.dataserver.DataServerProcessorStatus;
import org.jaibo.api.dataserver.DataServerInfoStatusCode;
import org.jaibo.api.errors.DataServerError;

/**
 * Data Server Process Incorrect Path Status
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

public final class DataServerProcessorIncorrectPathStatus extends DataServerProcessorStatus {
    private final String messagePrefix = "Incorrect path";
    private String message = this.messagePrefix;


    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        throw new DataServerError(String.format("Can't set custom message to \"%s\" status", this.getMessage()));
    }

    @Override
    public DataServerInfoStatusCode getStatus() {
        return DataServerInfoStatusCode.INCORRECT_PATH;
    }
}