package net.cakecdn.cloud.gateway.all.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import net.cakecdn.cloud.gateway.all.repository.UserRepository;
import net.cakecdn.cloud.gateway.all.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AccessFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AccessFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String filterType() {
        //前置过滤器
        return "pre";
    }

    @Override
    public int filterOrder() {
        //优先级，数字越大，优先级越低
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //是否执行该过滤器，true代表需要过滤
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        String token = "";
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            token = authorization.substring(7);
        } else {
            token = request.getParameter("token");
        }

        Map<String, Object> subMap = jwtTokenUtil.getSubFromToken(token);
        String uid = subMap.get("uid").toString();

        MultiValueMap<String, String> params;
        log.debug("request params: {}", request.getParameterMap());
        log.debug("context params: {}", ctx.getRequestQueryParams());
        if (null != ctx.getRequestQueryParams()) {
            params = new LinkedMultiValueMap<>(ctx.getRequestQueryParams());
        } else {
            params = new LinkedMultiValueMap<>();
        }
        params.remove("userId");
        params.add("userId", uid);

        ctx.setRequestQueryParams(params);
        ctx.setSendZuulResponse(true); // 对该请求进行路由
        ctx.set("isSuccess", true);

        // 这里return的值没有意义，zuul没有使用该返回值
        return null;
    }

}
