package IrcNetwork;

/**
 * User modes in irc network
 * Copyright (C) 2014  Victor Polevoy (vityatheboss@gmail.com)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public enum IrcUserMode {
    /* Basic flags */
    Owner,          // +n OWNER     Can add or remove masters and all other flags (except personal flags)
    Master,         // +m MASTER    Can add or remove all access except master or owner
    Operator,       // +o OP        Can get ops on the channel
    Voice,          // +v VOICE     Can get voice on the channel
    Known,          // +k KNOWN     Known on the channel - can get invites to the channel via INVITE

    /* Punishment flags - these restrict the user on the channel in some way */
    Banned,         // +b BANNED    Banned from the channel
    Deopped,        // +d DEOP      Not allowed to be opped on the channel
    Devoiced,       // +q DEVOICE   Not allowed to be voiced on the channel

    /* Extra flags - these control specific behaviour on the channel */
    Autooperator,   // +a AUTOOP    Ops the user automatically when they join the channel (the user
                    // must also hold +o in order to have this flag)
    Autovoice,      // +g AUTOVOICE Voices the user automatically when they join the channel (the
                    // user must also hold +v in order to have this flag)
    Protect,        // +p PROTECT   If the user has +o or +v, this makes sure they will always have
                    // that status, they will be reopped/voiced if deopped/voiced
    Topic           // +t TOPIC     Can use SETTOPIC to alter the topic on the channel
}