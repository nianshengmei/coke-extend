package org.needcoke.coke.web.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 */
public interface HandlerAdapter {

    void handle(HttpServletRequest request , HttpServletResponse response ,Handler handler);
}
