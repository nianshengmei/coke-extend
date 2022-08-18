package org.needcoke.coke.web.util;

import cn.hutool.core.io.IoUtil;
import lombok.experimental.UtilityClass;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author warren
 * @date 2022/4/2
 */
@UtilityClass
public class IOUtil {

    /**
     * 获取post请求的body
     */
    public String getBody(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        return IoUtil.read(inputStream, StandardCharsets.UTF_8);
    }

    public String getBody(HttpServletRequest request, Charset charset) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        return IoUtil.read(inputStream, charset);
    }

    public String getHttpType(HttpServletRequest request){
        return request.getMethod();
    }

}
