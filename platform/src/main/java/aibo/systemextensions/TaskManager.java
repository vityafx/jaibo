package aibo.systemextensions;

import aibo.ExtensionManager;
import aibo.ircnetwork.IrcMessageSender;

import org.jaibo.api.Extension;
import org.jaibo.api.IrcEvent;
import org.jaibo.api.IrcMessage;
import org.jaibo.api.dataserver.DataServerProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Sends tasks to each extension
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

public final class TaskManager {
    private ExtensionManager extensionManager;

    public TaskManager(IrcMessageSender messageSender) {
        this.extensionManager = new ExtensionManager(null, messageSender);
    }


    public ExtensionManager getExtensionManager() {
        return this.extensionManager;
    }


    public void notifyMessageListeners(final IrcMessage ircMessage) {
        for (final Extension extension : this.extensionManager.getExtensions()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    extension.processTask(ircMessage);
                }
            }).start();
        }
    }

    public void notifyEventListeners(final IrcEvent ircEvent) {
        for (final Extension extension : this.extensionManager.getExtensions()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    extension.processTask(ircEvent);
                }
            }).start();
        }
    }

    public void notifyServerListeners(final String serverMessage) {
        for (final Extension extension : this.extensionManager.getExtensions()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    extension.processTask(serverMessage);
                }
            }).start();
        }
    }

    public DataServerProcessor[] getDataServerProcessors() {
        ArrayList<DataServerProcessor> dataServerProcessors = new ArrayList<DataServerProcessor>();
        DataServerProcessor[] dataServerProcessorsArray = new DataServerProcessor[]{};
        Iterator<Extension> extensionIterator = extensionManager.getExtensions().iterator();

        while(extensionIterator.hasNext()) {
            Extension extension = extensionIterator.next();

            dataServerProcessors.addAll(extension.getDataServerProcessors());
        }

        dataServerProcessorsArray = dataServerProcessors.toArray(dataServerProcessorsArray);

        return dataServerProcessorsArray;
    }
}
