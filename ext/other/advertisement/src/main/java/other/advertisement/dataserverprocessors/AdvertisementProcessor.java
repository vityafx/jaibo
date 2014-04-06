package other.advertisement.dataserverprocessors;

import org.jaibo.api.dataserver.DataServerInfoStatusCode;
import other.advertisement.Advertisement;
import other.advertisement.ExtensionObject;

import org.jaibo.api.dataserver.DataServerInfoObject;
import org.jaibo.api.dataserver.DataServerInfoPackage;
import org.jaibo.api.dataserver.DataServerProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Shows currently live advertisements
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

public final class AdvertisementProcessor extends DataServerProcessor {
    ExtensionObject object;

    public AdvertisementProcessor(ExtensionObject object) {
        this.object = object;

        this.setInfoPath("/advertisement/list");
    }

    @Override
    protected String action() {
        DataServerInfoPackage infoPackage = new DataServerInfoPackage();
        DataServerInfoObject infoObject = new DataServerInfoObject();

        Advertisement[] advertisements = this.object.getAdvertisements();
        ArrayList<DataServerInfoObject> advertisementList = new ArrayList<DataServerInfoObject>();

        for (Advertisement advertisement : advertisements) {
            String text = advertisement.getAdvertisementText();
            short id = advertisement.getAdvertisementId();
            String timePeriod = String.format("%d minutes", advertisement.getTimePeriod());

            DataServerInfoObject adObject = new DataServerInfoObject();
            adObject.putData("id", id);
            adObject.putData("text", text);
            adObject.putData("period", timePeriod);
            advertisementList.add(adObject);
        }

        infoObject.putArrayWithoutEscaping("advertisements", advertisementList);

        infoPackage.setInfoObject(infoObject);

        return infoPackage.toString();
    }
}
