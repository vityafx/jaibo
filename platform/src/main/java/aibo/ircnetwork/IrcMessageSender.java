package aibo.ircnetwork;

import aibo.networkconnection.NetworkConnection;

import org.jaibo.api.IrcCommand;
import org.jaibo.api.IrcCommandSenderInterface;
import org.jaibo.api.IrcMessageSenderInterface;
import org.jaibo.api.NetworkConnectionInterface;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Message sender realization
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

public class IrcMessageSender implements IrcMessageSenderInterface {
    protected IrcCommandSenderInterface sender;
    private final Queue<IrcCommand> commandsQueue = new LinkedList<IrcCommand>();
    private short maxCommandsPerTime = 5;
    private short commandDelayTimeInSeconds = 3;
    private Timer timer = null;


    protected IrcMessageSender() {
        this.setSendByTimer();
    }

    public IrcMessageSender(NetworkConnectionInterface connection) {
        this();

        this.sender = new IrcCommandSender(connection);
    }

    private void setSendByTimer() {
        this.timer = new Timer();

        this.timer.schedule(new TimerTask(){
            @Override
            public void run() {
                for (short i = 0; i < maxCommandsPerTime; i++) {
                    if (commandsQueue.isEmpty()) {
                        break;
                    } else {
                        sender.sendIrcCommand(commandsQueue.remove());
                    }
                }
            }
        }, this.commandDelayTimeInSeconds * 1000, this.commandDelayTimeInSeconds * 1000);
    }

    private void stopSendTimer() {
        this.timer.cancel();
        this.timer.purge();
        this.timer = null;
    }

    private void resetSendTimer() {
        this.stopSendTimer();
        this.setSendByTimer();
    }

    public void setCommandDelayTime(short delayTimeInSeconds) {
        this.commandDelayTimeInSeconds = delayTimeInSeconds;

        this.resetSendTimer();
    }

    public void setMaxCommandsPerTime(short commandsCount) {
        this.maxCommandsPerTime = commandsCount;

        this.resetSendTimer();
    }

    @Override
    public void sendNotice(String username, String message) {
        this.commandsQueue.add(new IrcCommand("NOTICE", String.format("%s :%s", username, message)));
    }

    @Override
    public void sendPrivateMessage(String username, String message) {
        this.commandsQueue.add(new IrcCommand("PRIVMSG", String.format("%s :%s", username, message)));
    }

    @Override
    public void sendChannelMessage(String channel, String message) {
        this.commandsQueue.add(new IrcCommand("PRIVMSG", String.format("%s :%s", channel, message)));
    }

    @Override
    public void sendBroadcastMessage(String[] channels, String message) {
        for(String channel : channels) {
            this.commandsQueue.add(new IrcCommand("PRIVMSG", String.format("%s :%s", channel, message)));
        }
    }

    @Override
    public void setTopic(String channel, String topicContent) {
        this.commandsQueue.add(new IrcCommand("TOPIC", String.format("%s :%s", channel, topicContent)));
    }

    @Override
    public void setTopic(String[] channels, String topicContent) {
        for (String channel : channels) {
            this.setTopic(channel, topicContent);
        }
    }

    public IrcCommandSenderInterface getCommandSender() {
        return this.sender;
    }
}
