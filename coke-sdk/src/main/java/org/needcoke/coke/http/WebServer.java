package org.needcoke.coke.http;

import org.apache.catalina.LifecycleException;

public interface WebServer {

    /**
     * Starts the web server. Calling this method on an already started server has no
     * effect.
     * @throws WebServerException if the server cannot be started
     */
    void start() throws WebServerException;

    /**
     * Stops the web server. Calling this method on an already stopped server has no
     * effect.
     * @throws WebServerException if the server cannot be stopped
     */
    void stop() throws WebServerException, LifecycleException;

    /**
     * Return the port this server is listening on.
     * @return the port (or -1 if none)
     */
    int getPort();
}
