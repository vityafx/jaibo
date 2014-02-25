package AIBO.Extensions.Games.PickupBot.Commands.EventListeners;

import AIBO.Extensions.Command;
import AIBO.Extensions.Games.PickupBot.Object;
import AIBO.Extensions.Games.PickupBot.Player;
import IrcNetwork.EventListener;
import IrcNetwork.IrcEvent.IrcEvent;
import IrcNetwork.IrcEvent.IrcEventType;

/**
 * Change nickname in pickupbot game
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

public final class NickChange extends Command implements EventListener {
    private Object object;
    private Player oldPlayer;
    private Player newPlayer;

    public NickChange(Object object) {
        this.object = object;
    }

    @Override
    public void eventReceived(IrcEvent ircEvent) {
        if (ircEvent.getEventType() == IrcEventType.Nick) {
            this.oldPlayer = new Player(ircEvent.getUser(), null);
            this.newPlayer = new Player(ircEvent.getArgument("NewNickName"), null);

            this.execute();
        }
    }

    @Override
    protected void action() {
        this.object.substitutePlayer(this.oldPlayer, this.newPlayer);
    }
}