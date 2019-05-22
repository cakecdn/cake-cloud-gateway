package net.cakecdn.cloud.gateway.refresh.service;

import net.cakecdn.cloud.gateway.all.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshServiceImpl implements RefreshService {

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public RefreshServiceImpl(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String doRefresh(String oldToken) {
        return jwtTokenUtil.refreshToken(oldToken);
    }
}
