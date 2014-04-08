package aibo.systemextensions.core.dataserverprocessors;

import aibo.systemextensions.core.Object;

import org.jaibo.api.dataserver.DataServerInfoObject;
import org.jaibo.api.dataserver.DataServerInfoPackage;
import org.jaibo.api.dataserver.DataServerProcessor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Extension list processor
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

public final class ExtensionListProcessor extends DataServerProcessor {
    private aibo.systemextensions.core.Object extensionObject = null;

    public ExtensionListProcessor(Object extensionObject) {
        this.extensionObject = extensionObject;

        this.setInfoPath("/core/extensions");
    }

    @Override
    protected String action() {
        DataServerInfoPackage infoPackage = new DataServerInfoPackage();
        DataServerInfoObject infoObject = new DataServerInfoObject();

        String[] extensionNames = this.extensionObject.getExtensionManager().getAllExtensionList();
        ArrayList<DataServerInfoObject> extensions = new ArrayList<DataServerInfoObject>();

        for (int i = 0; i < extensionNames.length; i++) {
            DataServerInfoObject extensionObject = new DataServerInfoObject();
            String extensionName = extensionNames[i];
            boolean isRunning = this.extensionObject.getExtensionManager().isExtensionAlreadyAdded(extensionName);

            extensionObject.putData("running", isRunning);
            extensionObject.putData("extension", extensionName);

            extensions.add(extensionObject);
        }

        infoObject.putArrayWithoutEscaping("extensions", extensions);

        infoPackage.setInfoObject(infoObject);

        return infoPackage.toString();
    }
}