package Helpers;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Bot configuration parser and keeper
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

public final class Configuration {
    private String configurationFileName;

    private ArrayList<ConfigurationListener> listeners = new ArrayList<ConfigurationListener>();

    private HashMap<String, String> configurationHashMap;


    private Configuration() {

    }

    public Configuration(String configurationFileName) {
        this();

        this.loadFromFile(configurationFileName);
    }


    public void loadFromFile(String configurationFileName) {
        this.configurationFileName = configurationFileName;

        try {
            URL resource = this.getClass().getClassLoader().getResource(String.format("%s",
                    this.configurationFileName));

            try {
                FileInputStream settingsFileInputStream = new FileInputStream(new File(resource.toURI()));

                Properties properties = new Properties();

                try {
                    properties.load(settingsFileInputStream);

                    this.configurationHashMap = new HashMap<String, String>();

                    for (final String name: properties.stringPropertyNames())
                        this.configurationHashMap.put(name, properties.getProperty(name));

                    this.notifyListeners();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        this.loadFromFile(this.configurationFileName);
    }

    public ArrayList<ConfigurationListener> getListeners() {
        return listeners;
    }

    public void addListener(ConfigurationListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ConfigurationListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    private void notifyListeners() {
        for (ConfigurationListener listener : this.listeners) {
            listener.configurationChanged();
        }
    }

    public HashMap<String, String> getConfigurationHashMap() {
        return configurationHashMap;
    }
}
