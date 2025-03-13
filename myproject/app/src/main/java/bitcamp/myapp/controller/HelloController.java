package bitcamp.myapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller // 이 클래스의 객체를 생성해서 보관해 두라라는 의미. 역할은 페이지 컨트롤러 역할을 수행하는 의미.
public class HelloController {

    public HelloController(){
        System.out.println("HelloController 생성됨!");
    }

    @GetMapping(value = "/hello", produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String hello(){return "<html><body><h1>하하하하</h1></body></html>";}
}
