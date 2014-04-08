package aibo;

import aibo.systemextensions.ExtensionMessenger;
import aibo.ircnetwork.IrcMessageSender;

import org.jaibo.api.Extension;
import org.jaibo.api.errors.ExtensionManagerError;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

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
    private final ArrayList<Extension> extensions = new ArrayList<Extension>();
    private final ArrayList<Extension> extensionToBeDeleted = new ArrayList<Extension>();
    private final ArrayList<Extension> extensionToBeAdded = new ArrayList<Extension>();
    private ExtensionMessenger messenger;


    public ExtensionManager(String[] extensionNames, IrcMessageSender messageSender) {
        Extension.BotConfiguration = AIBO.Configuration;

        this.setupExtensionMessenger(messageSender);
        this.createCoreExtension();
        this.addExtensionsByNames(extensionNames);
    }


    public void addExtensionsByNames(String[] extensionNames) {
        if (extensionNames != null) {
            for (String name : extensionNames) {
                this.addExtensionByName(name);
            }
        }
    }

    public void addExtensionByName(String extensionName) {
        if (!this.isExtensionAlreadyAdded(extensionName)) {
            try {
                Extension extension = this.findAndCreateExtensionByName(extensionName);

                if (extension != null) {
                    this.addExtension(extension);
                }
            } catch (ExtensionManagerError e) {
                System.out.println(e.getMessage());
            }
        } else {
            throw new ExtensionManagerError(
                    String.format("Extension \"%s\" is already loaded", extensionName));
        }
    }

    private void addExtension(Extension extension) {
        if (extension != null) {
            extension.setExtensionMessenger(this.messenger);

            this.extensionToBeAdded.add(extension);

            System.out.println(String.format("Extension added: %s (version: %s)",
                    extension.getExtensionName(),
                    extension.getExtensionVersion()));
        }
    }

    public void removeExtensionByName(String extensionName) {
        if (this.isExtensionAlreadyAdded(extensionName)) {
            for(Extension extension : this.extensions) {
                if (extension.getExtensionName().equals(extensionName)) {
                    this.removeExtension(extension);

                    break;
                }
            }
        } else {
            throw new ExtensionManagerError(String.format("Extension \"%s\" was not loaded before", extensionName));
        }
    }

    private void removeExtension(Extension extension) {
        if (extension != null) {
            extension.prepareToUnload();

            this.extensionToBeDeleted.add(extension);

            System.out.println(String.format("Extension removed: %s (version: %s)",
                    extension.getExtensionName(),
                    extension.getExtensionVersion()));
        }
    }

    public List<Extension> getExtensions() {
        return this.extensions;
    }


    public Extension findAndCreateExtensionByName(String extensionName) {
        if (extensionName != null && !extensionName.isEmpty()) {
            try {
                File jarPath = new File(ExtensionManager.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                String jarPathAsString = jarPath.getParentFile().getAbsolutePath();
                String extensionPath = "file://" + jarPathAsString + "/libs/" +extensionName + ".jar";
                URLClassLoader child = new URLClassLoader (new URL[]{new URL(extensionPath)},
                        ExtensionManager.class.getClassLoader());

                Class<?> extensionClass = Class.forName(extensionName + ".ExtensionObject", true, child);

                try {
                    return (Extension)extensionClass.newInstance();
                } catch (Exception e) {
                    throw new ExtensionManagerError(
                            String.format("Can't create an object of extension \"%s\"",
                            extensionName));
                }
            } catch (ClassNotFoundException e) {
                throw new ExtensionManagerError(String.format("Can't find extension \"%s\"", extensionName));
            } catch (MalformedURLException e) {
                throw new ExtensionManagerError(String.format("Can't find extension \"%s\"", extensionName));
            }
        } else {
            return null;
        }
    }

    public static boolean IsExtensionObjectExists(String extensionName) {
        boolean extensionExists;

        if (!extensionName.equalsIgnoreCase("core")) {
            try {
                File jarPath = new File(ExtensionManager.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                String jarPathAsString = jarPath.getParentFile().getAbsolutePath();
                String extensionPath = "file://" + jarPathAsString + "/libs/" + extensionName + ".jar";
                URLClassLoader child = new URLClassLoader(new URL[]{new URL(extensionPath)},
                        ExtensionManager.class.getClassLoader());

                Class.forName(extensionName + ".ExtensionObject", true, child);

                extensionExists = true;
            } catch (ClassNotFoundException e) {
                extensionExists = false;
            } catch (MalformedURLException e) {
                extensionExists = false;
            }
        } else {
            extensionExists = true;
        }

        return extensionExists;
    }

    public Extension getCurrentlyRunningExtensionByName(String extensionName) {
        for(Extension extension : this.extensions) {
            if (extension.getExtensionName().equalsIgnoreCase(extensionName)) {
                return extension;
            }
        }

        return null;
    }

    public void setupExtensionMessenger(IrcMessageSender messageSender) {
        this.messenger = new ExtensionMessenger(messageSender, this);
    }

    private void createCoreExtension() {
        aibo.systemextensions.core.Object coreExtensionObject = new aibo.systemextensions.core.Object();
        coreExtensionObject.setExtensionManager(this);

        this.addExtension(coreExtensionObject);
    }

    private boolean isExtensionAlreadyAdded(String extensionName) {
        boolean extensionAlreadyAdded = false;

        for(Extension extension : this.extensions) {
            if (extension.getExtensionName().equals(extensionName)) {
                extensionAlreadyAdded = true;

                break;
            }
        }

        return extensionAlreadyAdded;
    }

    private boolean isExtensionAlreadyAdded(Extension extension) {
        return this.extensions.contains(extension);
    }

    @Override
    protected void finalize() throws Throwable {
        this.extensions.clear();

        super.finalize();
    }

    private void delayedDelete() {
        for (Extension extensionToDelete : this.extensionToBeDeleted) {
            Iterator<Extension> extensionIterator = this.extensions.iterator();

            while(extensionIterator.hasNext()) {
                Extension extension = extensionIterator.next();

                if (extension.getExtensionName().equals(extensionToDelete.getExtensionName())) {
                    extensionIterator.remove();
                }
            }
        }

        this.extensionToBeDeleted.clear();
    }

    private void delayedAdd() {
        this.extensions.addAll(this.extensionToBeAdded);

        this.extensionToBeAdded.clear();
    }

    public void performDelayedOperations() {
        this.delayedDelete();
        this.delayedAdd();
    }
}