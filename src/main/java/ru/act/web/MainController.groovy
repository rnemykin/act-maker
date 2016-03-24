package ru.act.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import ru.act.service.ActService

import java.time.LocalDateTime

@Controller
class MainController {

    @Autowired
    private ActService actService;

    @RequestMapping("/act")
    ResponseEntity<byte[]> makeAct(/*@RequestBody Act act*/) {
        new ResponseEntity<>(new byte[0], HttpStatus.OK)
    }

    @RequestMapping("/now")
    @ResponseBody
    LocalDateTime now() {
        LocalDateTime.now()
    }

    @RequestMapping("/")
    String index() {
        "index"
    }

}
