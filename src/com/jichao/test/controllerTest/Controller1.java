package com.jichao.test.controllerTest;

import com.jichao.spring.framework.annotation.Controller;
import com.jichao.spring.framework.annotation.RequestMapping;

@Controller
@RequestMapping("/jichao1")
public class Controller1 {

    @RequestMapping("/say")
    public void doSome() {
        System.out.println("what are you 弄啥嘞");
    }
}
