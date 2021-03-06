package org.jaibo.api;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Command abstract class
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

public abstract class Command {
    private ArrayList<String> names = new ArrayList<String>();


    public void addNames(ArrayList<String> names) {
        this.names = names;
    }

    public void addName(String name) {
        this.names.add(name);
    }

    public void addNames(String[] names) {
        this.names.addAll(Arrays.asList(names));
    }

    public void clearNames() {
        this.names.clear();
    }

    public ArrayList<String> getNames() {
        return this.names;
    }


    // This is a simple check, developer usually have to reimplement check with regexps in implementations
    public boolean check(String message) {
        boolean checkPassed = false;

        for(String name : this.getNames()) {
            if(message.toLowerCase().startsWith(name.toLowerCase())) {
                checkPassed = true;

                break;
            }
        }

        return checkPassed;
    }

    public boolean checkExact(String message) {
        boolean checkPassed = false;

        for(String name : this.getNames()) {
            if(message.equalsIgnoreCase(name)) {
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

    protected void execute() {
        this.action();
    }


    protected abstract void action();
}
