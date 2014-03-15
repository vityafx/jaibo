package aibo.extensions;

import aibo.AIBO;
import aibo.extensions.core.MainDatabaseManager;
import helpers.ConfigurationListener;
import ircnetwork.EventListener;
import ircnetwork.IrcMessage;
import ircnetwork.ircevent.IrcEvent;
import ircnetwork.MessageListener;
import ircnetwork.ServerListener;

import java.util.ArrayList;

/**
 * Extension Interface
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

public abstract class Extension extends Thread implements ConfigurationListener {
    public final static MainDatabaseManager MainDatabaseManager = new MainDatabaseManager();

    private ArrayList<MessageListener> messageListeners = new ArrayList<MessageListener>();
    private ArrayList<EventListener> eventListeners = new ArrayList<EventListener>();
    private ArrayList<ServerListener> serverListeners = new ArrayList<ServerListener>();

    private ExtensionMessenger messenger;

    private String topic;

    private String[] channels;


    protected Extension() {
        AIBO.Configuration.addListener(this);

        this.setChannelsFromConfiguration();

        this.setCommands();
    }

    protected Extension(ExtensionMessenger messenger) {
        this.setExtensionMessenger(messenger);
    }

    public void setTopic(String topic) {
        this.topic = topic;

        this.getExtensionMessenger().setTopicForExtension(this.getChannels(), this.getExtensionName(), this.topic);
    }

    public String getTopic() {
        return this.topic;
    }

    public abstract String getExtensionName();

    public void processTask(IrcMessage ircMessage) {
        for(MessageListener listener : messageListeners) {
            listener.messageReceived(ircMessage);
        }
    }

    public void processTask(IrcEvent ircEvent) {
        for(EventListener listener : eventListeners) {
            listener.eventReceived(ircEvent);
        }
    }

    public void processTask(String serverMessage) {
        for(ServerListener listener : serverListeners) {
            listener.serverMessageReceived(serverMessage);
        }
    }

    public void processTask(ExtensionMessage extensionMessage) {
        // currently a placeholder
    }

    public void addMessageListener(MessageListener listener) {
        if(!this.messageListeners.contains(listener)) {
            this.messageListeners.add(listener);
        }
    }

    public void removeMessageListener(MessageListener listener) {
        this.messageListeners.remove(listener);
    }

    public void deleteAllMessageListeners() {
        this.messageListeners.clear();
    }

    public void addEventListener(EventListener listener) {
        if(!this.eventListeners.contains(listener)) {
            this.eventListeners.add(listener);
        }
    }

    public void removeEventListener(EventListener listener) {
        this.eventListeners.remove(listener);
    }

    public void deleteAllEventListeners() {
        this.eventListeners.clear();
    }

    public void addServerListener(ServerListener listener) {
        if(!this.serverListeners.contains(listener)) {
            this.serverListeners.add(listener);
        }
    }

    public void removeServerListener(ServerListener listener) {
        this.serverListeners.remove(listener);
    }

    public void deleteAllServerListeners() {
        this.serverListeners.clear();
    }

    public ExtensionMessenger getExtensionMessenger() {
        return this.messenger;
    }

    public void setExtensionMessenger(ExtensionMessenger messenger) {
        this.messenger = messenger;
    }

    public void prepareToUnload() {
        this.beforeUnload();
    }

    /* This method will be called before unloading extension */
    protected void beforeUnload() {
        System.out.println(String.format("Extension %s will be unloaded", this.getExtensionName()));
    }

    private void setChannelsFromConfiguration() {
        this.channels = AIBO.Configuration.get("aibo.channels").split(" ");
    }

    @Override
    public void configurationChanged() {
        this.setChannelsFromConfiguration();
    }

    protected abstract void setCommands();

    public String[] getChannels() {
        return this.channels;
    }

    public abstract String getHelpPage();

    public boolean isAdminHost(String host) {
        String rootAdminHost = AIBO.Configuration.get("aibo.root_admin_host");

        return rootAdminHost.equalsIgnoreCase(host)
                || MainDatabaseManager.isAdminExists(this.getExtensionName(), host);
    }

    @Override
    public boolean equals(Object obj) {
        if (this != obj) {
            Extension extensionObject = (Extension)obj;

            if (extensionObject != null) {
                return extensionObject.getExtensionName().equalsIgnoreCase(this.getExtensionName());
            }
        }

        return false;
    }
}
