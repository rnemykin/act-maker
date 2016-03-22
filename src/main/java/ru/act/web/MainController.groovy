package ru.act.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import ru.act.service.ActService

import java.time.LocalDateTime

@Controller
class MainController {

    @Autowired
    private ActService actService;

    @RequestMapping("/groovy")
    ResponseEntity<String> main() {
        new ResponseEntity<>("real work", HttpStatus.OK)
    }

    @RequestMapping("/now")
    @ResponseBody
    LocalDateTime now() {
        LocalDateTime.now();
    }

    @RequestMapping("/")
    String index(Model model) {
        "index";
    }

}
