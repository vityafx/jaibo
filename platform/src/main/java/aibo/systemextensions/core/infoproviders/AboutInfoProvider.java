package aibo.systemextensions.core.infoproviders;

import aibo.AIBO;

import org.jaibo.api.dataserver.DataServerInfoObject;
import org.jaibo.api.dataserver.DataServerInfoPackage;
import org.jaibo.api.dataserver.DataServerInfoProvider;

/**
 * Core extension about provider
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

public final class AboutInfoProvider extends DataServerInfoProvider {

    public AboutInfoProvider() {
        this.setInfoPath("/core/about");
    }

    @Override
    protected String action() {
        String aboutString = AIBO.Configuration.get("aibo.about");

        DataServerInfoPackage infoPackage = new DataServerInfoPackage();
        DataServerInfoObject infoObject = new DataServerInfoObject();
        infoObject.putData("about", aboutString);
        infoPackage.setInfoObject(infoObject);

        return infoPackage.toString();
    }
}
