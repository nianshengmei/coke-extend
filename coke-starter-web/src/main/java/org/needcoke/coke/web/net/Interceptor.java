package org.needcoke.coke.web.net;

import org.needcoke.coke.web.http.HttpContext;

public interface Interceptor {

    void interceptor(HttpContext context);
}
