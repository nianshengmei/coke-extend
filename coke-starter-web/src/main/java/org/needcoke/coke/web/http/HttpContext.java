package org.needcoke.coke.web.http;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.needcoke.coke.web.util.IOUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author warren
 * @date 2022/4/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpContext {

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;

    public Map<String, String[]> getParamMap() {
        return httpServletRequest.getParameterMap();
    }

    public String body() throws IOException {
        return IOUtil.getBody(httpServletRequest);
    }

    public void writeJson(Object o) throws IOException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        if (null == o) {
            httpServletResponse.getWriter().flush();
        } else {
            httpServletResponse.getWriter().write(JSONUtil.toJsonStr(o));
        }
    }
}
