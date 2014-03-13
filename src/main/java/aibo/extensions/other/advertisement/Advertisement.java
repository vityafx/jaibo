package aibo.extensions.other.advertisement;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Advertisement realization
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

public final class Advertisement extends TimerTask {
    private String advertisementText;
    private short timePeriod;
    Object object;

    private final Timer timer = new Timer();



    public Advertisement(String advertisementText, short timePeriod, Object object) {
        this.advertisementText = advertisementText;
        this.timePeriod = timePeriod;
        this.object = object;

        this.setTimer();
    }

    public String getAdvertisementText() {
        return advertisementText;
    }

    public void setAdvertisementText(String advertisementText) {
        this.advertisementText = advertisementText;
    }

    public short getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(short timePeriod) {
        this.timePeriod = timePeriod;
    }

    @Override
    public void run() {
        if (this.object != null) {
            this.object.getExtensionMessenger().sendBroadcastMessage(this.object.getChannels(),
                    "Advertisement: " + this.advertisementText);
        }
    }


    private void setTimer() {
        this.timer.scheduleAtFixedRate(this, this.timePeriod * 60 * 1000, this.timePeriod * 60 * 1000);
    }

    private void cancelTimer() {
        this.timer.cancel();
        this.timer.purge();
    }

    public void stopAdvertisementTimer() {
        this.cancelTimer();
    }
}