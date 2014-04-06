package aibo.networkconnection;

import org.jaibo.api.NetworkConnectionInterface;
import org.jaibo.api.helpers.ConfigurationListener;
import org.jaibo.api.NetworkConnectionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

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

public class NetworkConnection implements NetworkConnectionInterface {
    private boolean isDebug;
    private int port;
    private String address;

    private Socket socket;
    private int timeout;
    private NetworkInterface networkInterface;

    private ArrayList<NetworkConnectionListener> listeners = new ArrayList<NetworkConnectionListener>();


    public NetworkConnection() {
    }

    public void setNetworkInterface(String interfaceName) {
        try {
            this.networkInterface = NetworkInterface.getByName(interfaceName);

            if (this.networkInterface == null) {
                System.out.println(String.format("No such interface: %s", interfaceName));
            }
        } catch (SocketException e) {
            System.out.println(String.format("Error while setting network interface(%s): %s", interfaceName,
                    e.getMessage()));

            System.exit(1);
        }
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getPort() {
        return this.port;
    }

    public String getAddress() {
        return this.address;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
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
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    private void notifyListeners(String[] data) {
        for (NetworkConnectionListener listener : this.listeners) {
            for (String dataString : data) {
                listener.dataReceived(dataString);
            }
        }
    }

    private void readLoop() throws IOException {
        this.socket.setSoTimeout(this.timeout);
        BufferedReader networkInputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        char[] inputChars = new char[1024];
        int charsRead;

        while (true) {
            if ((charsRead = networkInputStream.read(inputChars)) != -1) {
                this.socket.setSoTimeout(0);
                String dataString = new String(inputChars, 0, charsRead);
                String[] data = dataString.split("\r\n");

                if (this.isDebug) {
                    System.out.print(String.format("<< %s", dataString));
                }

                this.notifyListeners(data);
            }
        }
    }

    private void runLoop() {
        try {
            this.readLoop();
        } catch (IOException e) {
            System.out.println(String.format("Error while trying to read (%s)", e.getMessage()));
        }
    }

    @Override
    public void send(String data) {
        try {
            if (this.socket != null) {
                PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true);

                writer.write(data + "\r\n");
                writer.flush();

                if (this.isDebug) {
                    System.out.println(String.format(">> %s", data));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        int currentAttempt = 1;
        int maxAttempts = 3;

        if (this.port > 0 && this.address != null) {
            if (this.socket != null && this.socket.isConnected()) {
                try {
                    this.socket.close();
                } catch (IOException exception) {
                    System.out.printf("%s", exception.getMessage());
                }
            }

            try {
                this.socket = new Socket();

                if (this.networkInterface != null) {
                    Enumeration<InetAddress> addresses = this.networkInterface.getInetAddresses();
                    InetAddress address = null;

                    while (addresses.hasMoreElements())
                    {
                        InetAddress addr = addresses.nextElement();

                        if (addr instanceof Inet4Address && !addr.isLoopbackAddress())
                        {
                            address = addr;

                            break;
                        }
                    }

                    if (address != null) {
                        System.out.println(String.format("Binding to %s through %s", address.getHostName(),
                                this.networkInterface.getDisplayName()));

                        this.socket.bind(new InetSocketAddress(address, 0));
                    }
                }

                this.socket.connect(new InetSocketAddress(this.address, this.port), this.timeout);

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
            } catch (IOException exception) {
                System.out.printf("%s", exception.getMessage());
            }
        }
    }

}
