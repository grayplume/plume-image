package com.plume.image.web;

import com.plume.image.annotation.OperationLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    @OperationLog(moudle = "test",operator = "测试")
    public String hello(Model model){
        model.addAttribute("hello", "hello welcome");
        return "upload";
    }
}
