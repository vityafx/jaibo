package AIBO.Extensions;

import java.util.ArrayList;

/**
 * Command abstract class
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

public abstract class Command {
    private ArrayList<String> names;


    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public void addName(String name) {
        this.names.add(name);
    }

    public ArrayList<String> getNames() {
        return this.names;
    }

    protected abstract void action();

    protected void execute() {
        this.action();
    }

    public boolean check(String message) {
        boolean checkPassed = false;

        for(String name : this.names) {
            if(message.startsWith(name)) {
                checkPassed = true;

                break;
            }
        }

        return checkPassed;
    }

    public void checkAndExecute(String message) {
        if (this.check(message)) {
            this.execute();
        }
    }
}
