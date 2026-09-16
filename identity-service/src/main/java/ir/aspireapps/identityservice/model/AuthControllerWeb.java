package ir.aspireapps.identityservice.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/ir/aspireapps/micromart/web/v1/auth")
public class AuthControllerWeb {
    @PostMapping("/register")
    public void register(){

    }

    @PostMapping("/login")
    public void login(){

    }

    @GetMapping("/refresh")
    public void refresh(){

    }

    @PostMapping("/logout")
    public void logout(){

    }

    @PostMapping("/logout/all")
    public void logoutAll(){

    }
}
