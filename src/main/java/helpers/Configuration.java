package helpers;

import java.io.*;
import java.util.*;

/**
 * Class operates bot configuration
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

    private final static ArrayList<Configuration> _Configurations = new ArrayList<Configuration>();


    private Configuration() {
        Configuration._Configurations.add(this);
    }

    public Configuration(String configurationFileName) {
        this();

        this.loadFromFile(configurationFileName);
    }


    public void loadFromFile(String configurationFileName) {
        this.configurationFileName = configurationFileName;

        try {
            Properties properties = new Properties();

            File jarPath = new File(Configuration.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath = jarPath.getParentFile().getAbsolutePath();
            InputStream settingsFileInputStream = new FileInputStream(propertiesPath + "/settings/" + this.configurationFileName);

            try {
                properties.load(settingsFileInputStream);

                this.configurationHashMap = new HashMap<String, String>();

                for (final String name: properties.stringPropertyNames())
                    this.configurationHashMap.put(name, properties.getProperty(name));

                this.notifyListeners();


                settingsFileInputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void update() {
        for (Configuration configuration : Configuration._Configurations) {
            configuration.loadFromFile(configuration.configurationFileName);
        }
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

    public String getConfigurationFileName() {
        return this.configurationFileName;
    }

    public HashMap<String, String> getConfigurationHashMap() {
        return configurationHashMap;
    }

    public String get(String key) {
        if (this.configurationHashMap != null) {
            return this.configurationHashMap.get(key.toLowerCase());
        }

        return null;
    }

    public boolean getBoolean(String key) {
        if (this.configurationHashMap != null) {
            String value = this.configurationHashMap.get(key.toLowerCase());

            if (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true")) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void finalize() throws Throwable {
        _Configurations.remove(this);

        super.finalize();
    }
}
