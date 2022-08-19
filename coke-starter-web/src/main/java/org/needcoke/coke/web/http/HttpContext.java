package org.needcoke.coke.web.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
}
