package IrcNetwork;

/**
 * Irc Channel realization
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

public class IrcChannel {
    private String topic;
    private String name;


    public IrcChannel(String name) {
        this.name = name;
    }


    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;

        if (this.name.charAt(0) != '#') {
            this.name = String.format("#%s", this.name);
        }
    }

    public String toString() {
        return this.name;
    }
}
