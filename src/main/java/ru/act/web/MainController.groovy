package ru.act.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import ru.act.model.Act
import ru.act.model.ActDocument
import ru.act.service.ActService

@Controller
class MainController {

    @Autowired
    private ActService actService;

    @RequestMapping(value = "/acts", method = RequestMethod.POST)
    ResponseEntity<byte[]> makeAct(@RequestBody Act act) {
        ActDocument actDocument = actService.makeAct(act)

        HttpHeaders header = new HttpHeaders()
        header.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document"))

        String fileName = URLEncoder.encode(actDocument.fileName, "UTF-8")
        header.set("Content-Disposition", "attachment; filename=" + fileName)
        header.set("Content-Transfer-Encoding", "binary")
        header.setContentLength(actDocument.bytes.length)
        new ResponseEntity<>(actDocument.bytes, header, HttpStatus.OK)
    }

}
