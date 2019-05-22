package net.cakecdn.cloud.gateway.all.config.handler;

import net.cakecdn.cloud.gateway.all.domain.dto.AjaxResult;
import net.sf.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(401);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject responseJSONObject = JSONObject.fromObject(AjaxResult.failure("Unauthorized."));
        OutputStream oStream = response.getOutputStream();
        byte[] responseJSONBytes = responseJSONObject.toString().getBytes();
        oStream.write(responseJSONBytes);
    }
}
