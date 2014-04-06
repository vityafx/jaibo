package aibo.networkconnection;

import com.sun.net.httpserver.HttpServer;
import org.jaibo.api.dataserver.DataServerProcessor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
    private HttpServer server = null;


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

    public void startListenOn(String address, int port) {
        try {
            if (address != null && !address.isEmpty()) {
                this.server = HttpServer.create(new InetSocketAddress(address, port), 0);
            } else {
                this.server = HttpServer.create(new InetSocketAddress(port), 0);
            }

            this.setUpdateContextHandlersTimer();

            server.setExecutor(null);
            server.start();

            if (this.isDebug) {
                String addressString;

                if (address != null && !address.isEmpty()) {
                    addressString = address;
                } else {
                    addressString = "*";
                }

                System.out.println(String.format("Data server connection opened, listening %s:%s", addressString, port));
            }
        } catch (IOException e) {
            System.out.println("Can't launch data server: " + e.getMessage());
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

    private void setShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
            if (server != null) {
                server.stop(0);

                System.out.println("Data server stopped.");
            }
        }});
    }

    private void setContextHandlers() {
        if (this.server != null) {
            for (DataServerNetworkConnectionListener listener : this.listeners) {
                for (DataServerProcessor processor : listener.getDataProcessors()) {
                    this.server.createContext(processor.getInfoPath(), processor);
                }
            }
        }
    }

    public void updateContextHandlers() {
        this.setContextHandlers();
    }

    private void setUpdateContextHandlersTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                updateContextHandlers();
            }
        }, 5000);
    }
}
