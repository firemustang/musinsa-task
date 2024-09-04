package com.musinsa.api_task.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebController {

    @GetMapping
    public String taskWebViewPage(Model model) {
        return "task-web-view";
    }

}
