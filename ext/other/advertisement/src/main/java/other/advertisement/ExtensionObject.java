package other.advertisement;

import other.advertisement.errors.AdvertisementError;
import other.advertisement.messagelisteners.RemoveAd;
import other.advertisement.messagelisteners.SetAd;

import org.jaibo.api.Extension;
import org.jaibo.api.helpers.Configuration;

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

    public final static org.jaibo.api.helpers.Configuration Configuration = new Configuration("Other.Advertisement.ini");
    private Advertisement advertisement;

    @Override
    public String getExtensionName() {
        return "other.advertisement";
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

    public void setAdvertisement(String advertisementText, short timePeriod) {
        if (this.advertisement != null) {
            throw new AdvertisementError("Advertisement is already set");
        }

        this.advertisement = new Advertisement(advertisementText, timePeriod, this);
    }

    public void removeAdvertisement() {
        if (this.advertisement != null) {
            this.advertisement.stopAdvertisementTimer();

            this.advertisement = null;
        } else {
            throw new AdvertisementError("No advertisement has been set before.");
        }
    }
}
