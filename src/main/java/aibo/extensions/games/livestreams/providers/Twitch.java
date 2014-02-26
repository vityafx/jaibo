package aibo.extensions.games.livestreams.providers;

import aibo.extensions.games.livestreams.errors.ProviderError;
import aibo.extensions.games.livestreams.Provider;

/**
 * Twitch.tv live streams extension provider
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

public class Twitch extends Provider {


    @Override
    public String[] getStreams(String tag) {
        throw new ProviderError(String.format("No streams found on \"%s\" service with tag \"%s\"",
                this.getProviderName(), tag));
    }

    @Override
    public String getProviderName() {
        return "Twitch";
    }
}