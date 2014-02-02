package AIBO.Extensions;

import IrcNetwork.EventListener;
import IrcNetwork.MessageListener;
import IrcNetwork.ServerListener;

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

public abstract class Extension implements Runnable {
    private String topic;

    private ArrayList<MessageListener> messageListeners = new ArrayList<MessageListener>();
    private ArrayList<EventListener> eventListeners = new ArrayList<EventListener>();
    private ArrayList<ServerListener> serverListeners = new ArrayList<ServerListener>();


    public void setTopic(String topic) {
        this.topic = topic;
    }

    public abstract String getExtensionName();

    public void processTask() {

    }

    public void addMessageListener(MessageListener listener) {
        if(!this.messageListeners.contains(listener)) {
            this.messageListeners.add(listener);
        }
    }

    public void removeMessageListener(MessageListener listener) {
        this.messageListeners.remove(listener);
    }

    public void addEventListener(EventListener listener) {
        if(!this.eventListeners.contains(listener)) {
            this.eventListeners.add(listener);
        }
    }

    public void removeEventListener(EventListener listener) {
        this.eventListeners.remove(listener);
    }

    public void addServerListener(ServerListener listener) {
        if(!this.serverListeners.contains(listener)) {
            this.serverListeners.add(listener);
        }
    }

    public void removeServerListener(ServerListener listener) {
        this.serverListeners.remove(listener);
    }

    @Override
    public void run() {

    }


}
