package ru.act.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ViewController {
    @RequestMapping("/")
    String index() {
        "index"
    }

    @RequestMapping("part/{name}")
    String partIndex(@PathVariable String name) {
        "part/" + name + "-view";
    }
}
