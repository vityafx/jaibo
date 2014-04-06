package other.advertisement;

import other.advertisement.dataserverprocessors.AdvertisementProcessor;
import other.advertisement.errors.AdvertisementError;
import other.advertisement.messagelisteners.RemoveAd;
import other.advertisement.messagelisteners.SetAd;

import org.jaibo.api.Extension;
import org.jaibo.api.helpers.Configuration;
import other.advertisement.messagelisteners.ShowAds;
import other.advertisement.messagelisteners.ViewAd;

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
    private final int maxAdvertisementCount = 10;

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
        this.addDataServerProcessor(new AdvertisementProcessor(this));

        this.addMessageListener(new SetAd(this));
        this.addMessageListener(new RemoveAd(this));
        this.addMessageListener(new ViewAd(this));
        this.addMessageListener(new ShowAds(this));
    }

    @Override
    public String getHelpPage() {
        return ExtensionObject.Configuration.get("helppage");
    }

    public int setAdvertisement(String advertisementText, short timePeriod) {
        if (this.advertisements.size() == this.maxAdvertisementCount) {
            throw new AdvertisementError("Maximum advertisement count reached");
        }

        short advertisementId = this.generateId();

        if (advertisementText != null && !advertisementText.isEmpty()) {
            Advertisement ad = new Advertisement(advertisementText, timePeriod, this);
            ad.setAdvertisementId(advertisementId);

            this.advertisements.add(ad);
        } else {
            throw new AdvertisementError("Advertisement text should not be empty");
        }

        return advertisementId;
    }

    public Advertisement removeAdvertisement(short advertisementId) {
        Advertisement advertisement = this.getAdvertisementById(advertisementId);

        if (advertisement != null) {
            advertisement.stopAdvertisementTimer();

            this.advertisements.remove(advertisement);
        } else {
            throw new AdvertisementError(String.format("No advertisement with id=[%d]", advertisementId));
        }

        return advertisement;
    }

    public Advertisement getAdvertisementById(short id) {
        Advertisement advertisement = null;

        for (Advertisement ad : this.advertisements) {
            if (ad.getAdvertisementId() == id) {
                advertisement = ad;

                break;
            }
        }

        return advertisement;
    }

    public Short[] getAdvertisementIds() {
        final ArrayList<Short> ids = new ArrayList<Short>();
        Short[] idArray = new Short[]{};

        for (Advertisement ad : this.advertisements) {
            ids.add(ad.getAdvertisementId());
        }

        return ids.toArray(idArray);
    }

    public Advertisement[] getAdvertisements() {
        Advertisement[] advertisements = new Advertisement[]{};

        return this.advertisements.toArray(advertisements);
    }

    private short generateId() {
        Random generator = new Random();
        short id = (short)((generator.nextInt(this.maxAdvertisementCount)) + 1);
        boolean idFound = false;

        while (!idFound) {
            if (this.getAdvertisementById(id) != null) {
                id = (short)((generator.nextInt(this.maxAdvertisementCount)) + 1);
            } else {
                idFound = true;
            }
        }

        return id;
    }
}
