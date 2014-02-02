package AIBO;

import AIBO.Extensions.Extension;
import IrcNetwork.MessageListener;

import java.util.ArrayList;

/**
 * Extension manager realization
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

public final class ExtensionManager {
    private ArrayList<Extension> extensions = new ArrayList<Extension>();


    public ExtensionManager() {

    }

    public ExtensionManager(String[] extensionNames) {
        this.addExtensionsByNames(extensionNames);
    }


    public void addExtensionsByNames(String[] extensionNames) {
        for (String name : extensionNames) {
            this.addExtensionByName(name);
        }
    }

    public void addExtensionByName(String extensionName) {
        Extension extension = this.findExtensionByName(extensionName);

        if (extension != null) {
            this.extensions.add(extension);
        }
    }

    public void removeExtensionByName(String extensionName) {
        for(Extension extension : this.extensions) {
            if (extension.getExtensionName().equals(extensionName)) {
                this.extensions.remove(extension);

                break;
            }
        }
    }

    public ArrayList<MessageListener> getMessageListeners() {
        ArrayList<MessageListener> messageListeners = null;

        for(int i = 0; i < this.extensions.size()) {
            //this.messageListeners.addAll(this.extensions.get(0).getMessageListeners());
        }

        return messageListeners;
    }

    private Extension findExtensionByName(String extensionName) {
        return null;
    }
}
