package net.cakecdn.cloud.gateway.register.api;

import net.cakecdn.cloud.gateway.all.domain.dto.AjaxResult;
import net.cakecdn.cloud.gateway.register.service.RegisterForm;
import net.cakecdn.cloud.gateway.register.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/public/register")
public class RegisterApiController {

    private final RegisterService registerService;

    @Autowired
    public RegisterApiController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping
    public AjaxResult doRegister(@RequestBody RegisterForm form) {
        registerService.doRegister(form);
        return AjaxResult.success();
    }
}
