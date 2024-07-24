package spring.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class HttpController {

    @GetMapping({"/", "/index", "/home"})
    public String index(){
        return "index.jsp";
    }

}
