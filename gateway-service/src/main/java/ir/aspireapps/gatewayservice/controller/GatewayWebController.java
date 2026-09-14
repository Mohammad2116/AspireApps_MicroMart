package ir.aspireapps.gatewayservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ir/aspireapps/micromart")
public class GatewayWebController {
    @GetMapping("/home")
    public String home(){
        return "home";
    }
}
