package NetworkConnection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;

/**
 * Network connection realization
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

public class NetworkConnection {
    public static final NetworkConnection sharedInstance = new NetworkConnection();

    private int port;
    private String address;

    private Socket socket;

    private ArrayList<NetworkConnectionListener> listeners = new ArrayList<NetworkConnectionListener>();


    private NetworkConnection() {

    }


    public int getPort() {
        return this.port;
    }

    public String getAddress() {
        return this.address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void removeListener(NetworkConnectionListener listener) {
        this.listeners.remove(listener);
    }

    public void addListener(NetworkConnectionListener listener) {
        if (! this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    private void notifyListeners(String data) {
        for(NetworkConnectionListener listener : this.listeners) {
            listener.dataReceived(data);
        }
    }

    private void runLoop() {
        try {
            BufferedReader networkInputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            char[] inputChars = new char[1024];
            int charsRead = 0;

            while(true) {
                if ((charsRead = networkInputStream.read(inputChars)) != -1) {
                    String data = new String(inputChars, 0, charsRead);

                    System.out.print(data);

                    this.notifyListeners(data);
                }
            }
        }
        catch (IOException e) {
            System.out.println(String.format("I've got an exception: %s", e.getMessage()));
        }
    }

    public void send(String data) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(this.socket.getOutputStream());

            writer.write(data);

        } catch (IOException e) {
            System.out.println(String.format("Got an exception: %s", e.getMessage()));
        }
    }

    public void connect() {
        if (this.port > 0 && this.address != null) {
            if (this.socket != null && this.socket.isConnected()) {
                try {
                    this.socket.close();
                } catch (IOException exception) {
                    System.out.printf("%s", exception.getMessage());
                }
            }

            try {
                this.socket = new Socket(this.address, this.port);

                this.runLoop();
            } catch (UnknownHostException exception) {
                System.out.printf("%s", exception.getMessage());
            } catch (IOException exception) {
                System.out.printf("%s", exception.getMessage());
            }
        }
    }

    public void connect(String address, int port) {
        this.setAddress(address);
        this.setPort(port);

        this.connect();
    }

    public void reconnect() {
        if (this.socket != null && this.socket.isConnected()) {
            try {
                this.socket.close();
            } catch (IOException exception) {
                System.out.printf("%s", exception.getMessage());
            }
        }

        this.connect();
    }

    public void disconnect() {
        if (this.socket.isConnected()) {
            try {
                this.socket.close();
            } catch(IOException exception) {
                System.out.printf("%s", exception.getMessage());
            }
        }
    }
}
