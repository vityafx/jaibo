package aibo.systemextensions.core.dataserverprocessors;

import aibo.AIBO;
import aibo.systemextensions.core.Object;

import org.jaibo.api.dataserver.DataServerInfoObject;
import org.jaibo.api.dataserver.DataServerInfoPackage;
import org.jaibo.api.dataserver.DataServerInfoStatusCode;
import org.jaibo.api.dataserver.DataServerProcessor;

/**
 * Shutdown data server processor
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

public final class ShutdownProcessor extends DataServerProcessor {
    private Object extensionObject = null;

    public ShutdownProcessor(Object extensionObject) {
        this.extensionObject = extensionObject;

        this.setInfoPath("/core/shutdown");
    }

    @Override
    protected String action() {
        DataServerInfoPackage infoPackage = new DataServerInfoPackage();
        DataServerInfoObject infoObject = new DataServerInfoObject();

        if (this.getInfoObject() != null) {
            String adminHost = this.getInfoObject().get("token");

            if (adminHost != null && !adminHost.isEmpty()) {
                if (this.extensionObject.isApiAuthTokenCorrect(adminHost)) {
                    infoObject.putData("answer", "Bot will shut down in 5 seconds");

                    AIBO.Shutdown();
                } else {
                    infoPackage.setStatus(DataServerInfoStatusCode.ARGUMENT_ERROR);
                    infoPackage.setStatusMessage(String.format("'%s' unknown token", adminHost));
                }
            } else {
                infoPackage.setStatus(DataServerInfoStatusCode.ARGUMENT_ERROR);
                infoPackage.setStatusMessage("You must have a token");
            }
        }

        infoPackage.setInfoObject(infoObject);

        return infoPackage.toString();
    }
}