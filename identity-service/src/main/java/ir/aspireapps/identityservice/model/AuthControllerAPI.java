package ir.aspireapps.identityservice.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/ir/aspireapps/micromart/api/v1/auth")
public class AuthControllerAPI {
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
