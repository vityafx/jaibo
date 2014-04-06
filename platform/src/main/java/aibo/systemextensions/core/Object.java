package aibo.systemextensions.core;

import aibo.ExtensionManager;
import aibo.systemextensions.core.commands.messagelisteners.*;
import aibo.systemextensions.core.commands.serverlisteners.*;
import aibo.systemextensions.core.dataserverprocessors.*;
import org.jaibo.api.Extension;
import org.jaibo.api.SimpleCommand;
import org.jaibo.api.errors.ExtensionError;

/**
 * core extension object
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

public class Object extends Extension {
    private ExtensionManager extensionManager;
    private String currentNickName;

    private static int ObjectCount = 0;
    private final static int MaxObjectCount = 1;

    public Object() {
        if (Object.ObjectCount >= Object.MaxObjectCount) {
            throw new ExtensionError(this.getExtensionName(),
                    String.format("Can't create core extension objects with count more than %s.",
                            Object.MaxObjectCount));
        } else {
            Object.ObjectCount++;
        }

        this.setShutdownHook();
    }

    private void setShutdownHook() {
        final QuitOnShutdown shutDownHandler = new QuitOnShutdown(this);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                shutDownHandler.execute();
            }
        });
    }

    @Override
    protected void finalize() throws Throwable {
        Object.ObjectCount--;

        super.finalize();
    }

    @Override
    public String getExtensionName() {
        return "core";
    }

    @Override
    public String getExtensionVersion() {
        return "0.4";
    }

    @Override
    public void setCommands() {
        this.addDataServerProcessor(new AboutProcessor());
        this.addDataServerProcessor(new ShutdownProcessor(this));
        this.addDataServerProcessor(new RestartProcessor(this));
        this.addDataServerProcessor(new LoadExtensionProcessor(this));
        this.addDataServerProcessor(new UnloadExtensionProcessor(this));
        this.addDataServerProcessor(new MotdProcessor(this));
        this.addDataServerProcessor(new UptimeProcessor(this));
        this.addDataServerProcessor(new UpdateConfigurationProcessor(this));

        this.addServerListener(new Pong(this));
        this.addServerListener(new Login(this));

        this.addMessageListener(new Restart(this));
        this.addMessageListener(new Uptime(this));
        this.addMessageListener(new Hello(this));
        this.addMessageListener(new Motd(this));
        this.addMessageListener(new LoadExtension(this));
        this.addMessageListener(new UnloadExtension(this));
        this.addMessageListener(new UpdateConfiguration(this));
        this.addMessageListener(new Shutdown(this));
        this.addMessageListener(new GetOp(this));
        this.addMessageListener(new APIToken(this));
        this.addMessageListener(new AddAdmin(this));
        this.addMessageListener(new RemoveAdmin(this));
        this.addMessageListener(new About(this));
        this.addMessageListener(new Bug(this));

        this.addServerListener(new ConnectedToServerEvent(new JoinChannels(this), new Auth(this)));
    }

    @Override
    public String getHelpPage() {
        return BotConfiguration.get("aibo.HelpPage");
    }

    @Override
    protected void beforeUnload() {
        throw new ExtensionError(this.getExtensionName(), "Can't unload extension");
    }

    public void setExtensionManager(ExtensionManager manager) {
        this.extensionManager = manager;
    }

    public ExtensionManager getExtensionManager() {
        return this.extensionManager;
    }


    public String getCurrentNickName() {
        return currentNickName;
    }

    public void setCurrentNickName(String currentNickName) {
        this.currentNickName = currentNickName;
    }
}
