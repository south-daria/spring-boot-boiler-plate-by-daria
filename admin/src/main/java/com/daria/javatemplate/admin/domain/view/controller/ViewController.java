package com.daria.javatemplate.admin.domain.view.controller;

import com.daria.javatemplate.core.security.config.PrincipleDetail;
import org.apache.catalina.authenticator.SpnegoAuthenticator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("")
@Controller
public class ViewController {
    @GetMapping("/admin/v1/login")
    public String loginPage(){
        return "loginForm";
    }

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipleDetail principleDetail){
        return "세션 정보 확인 : " + authentication.getPrincipal();
    }
}
