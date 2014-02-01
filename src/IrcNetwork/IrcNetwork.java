package IrcNetwork;

import java.util.ArrayList;

/**
 * Class that introduces an Irc Network
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

public class IrcNetwork {
    private ArrayList<ChannelListener> channelListeners = new ArrayList<ChannelListener>();
    private ArrayList<ServerListener> serverListeners = new ArrayList<ServerListener>();

    private ArrayList<IrcChannel> channels = new ArrayList<IrcChannel>();
}
