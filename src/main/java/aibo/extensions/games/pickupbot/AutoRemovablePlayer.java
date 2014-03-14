package aibo.extensions.games.pickupbot;

import helpers.ConfigurationListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Player with timer
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

public final class AutoRemovablePlayer extends Player {
    private final Timer timer = new Timer();
    private final AutoRemovablePlayerTask autoRemovalTask = new AutoRemovablePlayerTask(this);
    private final static int TimerDelay = 1 * 60 * 1000;
    private Game game;


    public AutoRemovablePlayer(Player player, Game game) {
        this(player.nick, player.host, game);
    }

    public AutoRemovablePlayer(String nick, String host, Game game) {
        super(nick, host);

        this.setGame(game);

        this.setTimer();
    }

    private void setTimer() {
        this.timer.scheduleAtFixedRate(this.autoRemovalTask, TimerDelay, TimerDelay);
    }

    private void setGame(Game game) {
        this.game = game;
    }

    public void removePlayer() {
        this.cancelPlayerTimer();

        this.game.automaticallyRemovePlayer(this);
    }

    public void cancelPlayerTimer() {
        this.timer.cancel();
        this.timer.purge();
    }

    @Override
    public void beforeRemove() {
        this.cancelPlayerTimer();
    }

    @Override
    public String toString() {
        return String.format("Nick: %s | Host: %s", this.nick, this.host);
    }

    @Override
    public String getFormattedNickName() {
        String gameProfileRequired = Object.Configuration.get("player.game_profile_required");

        String playerName;

        if (gameProfileRequired.equalsIgnoreCase("yes")) {
            playerName = this.gameProfile;
        } else {
            playerName = this.nick;
        }

        return String.format("%s(%dm)", playerName, this.autoRemovalTask.getMinutesAdded());
    }
}


class AutoRemovablePlayerTask extends TimerTask implements ConfigurationListener {

    private AutoRemovablePlayer player;
    private int minutesAdded = 0;
    private static int MaximumAddedTime;

    public AutoRemovablePlayerTask(AutoRemovablePlayer player) {
        this.player = player;

        this.configurationChanged();
    }

    @Override
    public void run() {
        this.minutesAdded++;

        if (this.minutesAdded >= MaximumAddedTime) {
            player.removePlayer();
        }
    }

    public int getMinutesAdded() {
        return this.minutesAdded;
    }

    @Override
    public void configurationChanged() {
        MaximumAddedTime = Integer.parseInt(Object.Configuration.get("Player.maximum_added_time"));
    }
}