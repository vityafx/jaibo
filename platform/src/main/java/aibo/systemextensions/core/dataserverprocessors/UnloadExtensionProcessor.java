package aibo.systemextensions.core.dataserverprocessors;

import aibo.systemextensions.core.Object;

import org.jaibo.api.dataserver.DataServerInfoObject;
import org.jaibo.api.dataserver.DataServerInfoPackage;
import org.jaibo.api.dataserver.DataServerInfoStatusCode;
import org.jaibo.api.dataserver.DataServerProcessor;
import org.jaibo.api.errors.ExtensionManagerError;

/**
 * Unload extension data server processor
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

public final class UnloadExtensionProcessor extends DataServerProcessor {
    private Object extensionObject = null;

    public UnloadExtensionProcessor(Object extensionObject) {
        this.extensionObject = extensionObject;

        this.setInfoPath("/core/unload");
    }

    @Override
    protected String action() {
        DataServerInfoPackage infoPackage = new DataServerInfoPackage();
        DataServerInfoObject infoObject = new DataServerInfoObject();

        if (this.getInfoObject() == null || this.getInfoObject().get("token") == null
                || this.getInfoObject().get("token").isEmpty()) {

            infoPackage.setStatus(DataServerInfoStatusCode.ARGUMENT_ERROR);
            infoPackage.setStatusMessage("You must have a token");

        } else {
            String adminHost = this.getInfoObject().get("token");
            String extensionName = this.getInfoObject().get("extension");

            if (this.extensionObject.isApiAuthTokenCorrect(adminHost)) {
                if (extensionName != null && !extensionName.isEmpty()) {
                    try {
                        this.extensionObject.getExtensionManager().removeExtensionByName(extensionName);

                        infoObject.putData("answer", "Extension has been unloaded: " + extensionName);
                    } catch (ExtensionManagerError e) {
                        infoObject.putData("answer", e.getMessage());
                    }
                } else {
                    infoObject.putData("answer", "Can't remove extension: empty string provided");
                }

            } else {
                infoPackage.setStatus(DataServerInfoStatusCode.ARGUMENT_ERROR);
                infoPackage.setStatusMessage(String.format("'%s' unknown token", adminHost));
            }
        }

        infoPackage.setInfoObject(infoObject);

        return infoPackage.toString();
    }
}