package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){

        for (Integer i = 0; i<10; i++) {
            System.out.println(i);
        }
        model.addAttribute("data", "hello!");
        return "hello";
    }
}
