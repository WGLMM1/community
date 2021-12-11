package life.majiang.community.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")//一个反斜杠都不写，表示根目录s
    public String index(){
        return "index";
    }

}
