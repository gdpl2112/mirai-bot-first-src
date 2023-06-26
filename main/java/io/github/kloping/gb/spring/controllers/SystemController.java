package io.github.kloping.gb.spring.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author github.kloping
 */
@RestController
public class SystemController {
    @RequestMapping("/")
    public String index() {
        return "this is index!";
    }
}
