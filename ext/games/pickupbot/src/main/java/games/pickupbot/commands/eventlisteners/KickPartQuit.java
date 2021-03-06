package games.pickupbot.commands.eventlisteners;

import games.pickupbot.ExtensionObject;
import org.jaibo.api.Command;
import games.pickupbot.Player;
import org.jaibo.api.EventListener;
import org.jaibo.api.IrcEvent;
import org.jaibo.api.ircevent.IrcEventType;

/**
 * On kick/part/quit event - remove player
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

public final class KickPartQuit extends Command implements EventListener {
    private ExtensionObject object;
    private Player player;

    public KickPartQuit(ExtensionObject object) {
        this.object = object;
    }

    @Override
    public void eventReceived(IrcEvent ircEvent) {
        if (ircEvent.getEventType() == IrcEventType.Kick) {
            this.player = new Player(ircEvent.getArgument("UserKicked"), null);
        } else if (ircEvent.getEventType() == IrcEventType.Part || ircEvent.getEventType() == IrcEventType.Quit) {
            this.player = new Player(ircEvent.getUser(), null);
        }

        if (this.player != null) {
            this.execute();
        }
    }

    @Override
    protected void action() {
        this.object.removePlayerFromEachGameType(this.player, true);

        this.player = null;
    }
}
