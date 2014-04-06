package other.advertisement;

import other.advertisement.errors.AdvertisementError;
import other.advertisement.messagelisteners.RemoveAd;
import other.advertisement.messagelisteners.SetAd;

import org.jaibo.api.Extension;
import org.jaibo.api.helpers.Configuration;

import java.util.ArrayList;
import java.util.Random;

/**
 * Greets people when they are joins the channel
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

public final class ExtensionObject extends Extension {

    public final static Configuration Configuration = new Configuration("Other.Advertisement.ini");
    private final ArrayList<Advertisement> advertisements = new ArrayList<Advertisement>();

    @Override
    public String getExtensionName() {
        return "other.advertisement";
    }

    @Override
    public String getExtensionVersion() {
        return "1.0";
    }

    @Override
    protected void setCommands() {
        this.addMessageListener(new SetAd(this));
        this.addMessageListener(new RemoveAd(this));
    }

    @Override
    public String getHelpPage() {
        return ExtensionObject.Configuration.get("helppage");
    }

    public int setAdvertisement(String advertisementText, short timePeriod) {
        int advertisementId = this.generateId();

        if (advertisementText != null && !advertisementText.isEmpty()) {
            Advertisement ad = new Advertisement(advertisementText, timePeriod, this);
            ad.setAdvertisementId(advertisementId);

            this.advertisements.add(ad);
        } else {
            throw new AdvertisementError("Advertisement text should not be empty");
        }

        return advertisementId;
    }

    public Advertisement removeAdvertisement(int advertisementId) {
        Advertisement advertisement = this.getAdvertisementById(advertisementId);

        if (advertisement != null) {
            advertisement.stopAdvertisementTimer();

            this.advertisements.remove(advertisement);
        } else {
            throw new AdvertisementError(String.format("No advertisement with id=[%d]", advertisementId));
        }

        return advertisement;
    }

    private Advertisement getAdvertisementById(int id) {
        Advertisement advertisement = null;

        for (Advertisement ad : this.advertisements) {
            if (ad.getAdvertisementId() == id) {
                advertisement = ad;
            }
        }

        return advertisement;
    }

    private int generateId() {
        Random generator = new Random();
        int id = generator.nextInt() + 1;
        boolean idFound = false;

        while (!idFound) {
            if (this.getAdvertisementById(id) != null) {
                id = generator.nextInt() + 1;

                break;
            }

            idFound = true;
        }

        return id;
    }
}
