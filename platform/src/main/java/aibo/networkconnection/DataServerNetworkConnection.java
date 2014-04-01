package aibo.networkconnection;

import org.jaibo.api.NetworkConnectionInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Data server network connection
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

public final class DataServerNetworkConnection {
    private boolean isDebug;
    private ArrayList<DataServerNetworkConnectionListener> listeners = new ArrayList<DataServerNetworkConnectionListener>();
    private ServerSocket serverSocket;


    public DataServerNetworkConnection() {
        this.setShutdownHook();
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }


    public void removeListener(DataServerNetworkConnectionListener listener) {
        this.listeners.remove(listener);
    }

    public void addListener(DataServerNetworkConnectionListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    private void notifyListeners(Socket clientSocket, String dataServerData) {
        for (DataServerNetworkConnectionListener listener : this.listeners) {
            listener.dataServerRequestReceived(clientSocket, dataServerData);
        }
    }

    public void startListenOn(String address, int port) {
        try {
            if (address != null && !address.isEmpty()) {
                this.serverSocket = new ServerSocket(port, 0, InetAddress.getByName(address));
            } else {
                this.serverSocket = new ServerSocket(port);
            }

            if (this.isDebug) {
                String addressString = null;

                if (address != null && !address.isEmpty()) {
                    addressString = address;
                } else {
                    addressString = "*";
                }

                System.out.println(String.format("Data server connection opened, listening %s:%s", addressString, port));
            }

            this.runLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runLoop() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                BufferedReader networkInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                char[] inputChars = new char[1024];
                int charsRead;

                if ((charsRead = networkInputStream.read(inputChars)) != -1) {
                    String dataString = new String(inputChars, 0, charsRead);
                    String[] request = dataString.split("\r\n");

                    String infoPath = parseInfoPath(request);

                    if (infoPath != null) {
                        this.notifyListeners(clientSocket, infoPath);

                        if (this.isDebug) {
                            System.out.println(String.format("<< %s", infoPath));
                        }
                    } else {
                        clientSocket.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Socket clientSocket, String data) {
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            writer.write(data);
            writer.close();

            if (this.isDebug) {
                System.out.println(String.format(">> %s", data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String parseInfoPath(String[] request) {
        String infoPath = null;

        for (String requestLine : request) {
            if (requestLine.startsWith("GET ")) {
                Pattern p = Pattern.compile(String.format("^GET (.*) (.*)$"));

                CharSequence sequence = requestLine.subSequence(0, requestLine.length());
                Matcher matcher = p.matcher(sequence);

                if (matcher.matches()) {
                    infoPath = matcher.group(1);

                    if (infoPath.equals("/")) {
                        infoPath = null;
                    }

                    break;
                }

                break;
            }
        }

        return infoPath;
    }

    private void setShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
            try {
                if (serverSocket != null) {
                    serverSocket.close();

                    System.out.println("Closing data server socket.");
                }
            } catch (IOException e) {
                System.out.println("Error while closing data server socket.");
            }
        }});
    }
}
