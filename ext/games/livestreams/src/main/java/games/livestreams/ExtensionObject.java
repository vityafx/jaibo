package games.livestreams;

import games.livestreams.errors.ProviderError;
import games.livestreams.messagelisteners.Streams;

import org.jaibo.api.Extension;
import org.jaibo.api.helpers.Configuration;
import org.jaibo.api.helpers.ConfigurationListener;

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

public final class ExtensionObject extends Extension implements ConfigurationListener {
    private final ArrayList<Provider> providers = new ArrayList<Provider>();

    public final static Configuration Configuration = new Configuration("Games.LiveStreams.ini");

    public ExtensionObject() {
        this.configurationChanged();
    }

    @Override
    public String getExtensionName() {
        return "games.livestreams";
    }

    @Override
    protected void setCommands() {
        this.addMessageListener(new Streams(this));
    }

    @Override
    public String getHelpPage() {
        return ExtensionObject.Configuration.get("HelpPage");
    }

    @Override
    public void configurationChanged() {
        String[] providerNames = Configuration.get("livestreams.providers").trim().split(" ");

        this.setProviders(providerNames);
    }

    private void setProviders(String[] providerNames) {
        this.providers.clear();

        for (String providerName : providerNames) {
            if (!providerName.isEmpty()) {
                try {
                    Class<?> providerClass = Class.forName(this.getExtensionName() +
                            ".providers." +
                            providerName);

                    try {
                        Provider provider = (Provider) providerClass.newInstance();

                        this.providers.add(provider);
                    } catch (Exception e) {
                        throw new ProviderError(String.format("Can't create an instance of \"%s\" provider", providerName));
                    }
                } catch (ClassNotFoundException e) {
                    throw new ProviderError(String.format("No such provider \"%s\"", providerName));
                }
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
