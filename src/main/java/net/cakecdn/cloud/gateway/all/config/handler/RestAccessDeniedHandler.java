package net.cakecdn.cloud.gateway.all.config.handler;

import net.cakecdn.cloud.gateway.all.domain.dto.AjaxResult;
import net.sf.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(403);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject responseJSONObject = JSONObject.fromObject(AjaxResult.failure("Forbidden."));
        OutputStream oStream = response.getOutputStream();
        byte[] responseJSONBytes = responseJSONObject.toString().getBytes();
        oStream.write(responseJSONBytes);
    }
}
