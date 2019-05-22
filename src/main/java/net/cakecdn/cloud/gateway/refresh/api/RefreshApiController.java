package net.cakecdn.cloud.gateway.refresh.api;

import net.cakecdn.cloud.gateway.all.domain.dto.AjaxResult;
import net.cakecdn.cloud.gateway.refresh.service.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/refresh")
public class RefreshApiController {

    @Value("${cake.authorization-token-prefix:Bearer }")
    private String authorizationTokenPrefix;

    private final RefreshService refreshService;

    @Autowired
    public RefreshApiController(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @RequestMapping
    public AjaxResult doRefresh(@RequestHeader String authorization) {
        String oldToken = authorization.substring(authorizationTokenPrefix.length());
        return AjaxResult.success(refreshService.doRefresh(oldToken));
    }
}
