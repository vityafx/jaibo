package AIBO.Extensions.Games.LiveStreams;

import AIBO.Extensions.Extension;
import AIBO.Extensions.Games.LiveStreams.Errors.ProviderError;
import AIBO.Extensions.Games.LiveStreams.MessageListeners.Streams;
import Helpers.Configuration;
import Helpers.ConfigurationListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Shows currently running game video streams
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

public final class Object extends Extension implements ConfigurationListener {
    private final ArrayList<Provider> providers = new ArrayList<Provider>();

    public final static Helpers.Configuration Configuration = new Configuration("Games.LiveStreams.ini");

    public Object() {
        this.configurationChanged();
    }

    @Override
    public String getExtensionName() {
        return "Games.LiveStreams";
    }

    @Override
    protected void setCommands() {
        this.addMessageListener(new Streams(this));
    }

    @Override
    public String getHelpPage() {
        return Object.Configuration.getConfigurationHashMap().get("HelpPage");
    }

    @Override
    public void configurationChanged() {
        String[] providerNames = Configuration.getConfigurationHashMap().get("LiveStreams.providers").trim().split(" ");

        this.setProviders(providerNames);
    }

    private void setProviders(String[] providerNames) {
        this.providers.clear();

        for (String providerName : providerNames) {
            try {
                Class<?> providerClass = Class.forName("AIBO.Extensions." +
                        this.getExtensionName() +
                        ".Providers." +
                        providerName);

                try {
                    Provider provider = (Provider)providerClass.newInstance();

                    this.providers.add(provider);
                } catch (Exception e) {
                    throw new ProviderError(String.format("Can't create an instance of \"%s\" provider", providerName));
                }
            } catch (ClassNotFoundException e) {
                throw new ProviderError(String.format("No such provider \"%s\"", providerName));
            }
        }
    }

    public String[] getStreams(String providerName, String streamTag) {
        ArrayList<String> streams = new ArrayList<String>();
        String[] streamsArray = new String[]{};

        for (Provider provider : this.providers) {
            if (provider.getProviderName().equalsIgnoreCase(providerName)) {
                try {
                    streams.addAll(Arrays.asList(provider.getStreams(streamTag)));

                    break;
                } catch (ProviderError e) {
                    streams.add(e.getMessage());
                }
            }
        }

        return streams.toArray(streamsArray);
    }
}
