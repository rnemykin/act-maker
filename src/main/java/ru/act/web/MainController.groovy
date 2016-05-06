package ru.act.web

import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import ru.act.model.Act
import ru.act.service.ActService

import javax.servlet.http.HttpServletResponse
import java.time.format.TextStyle

@Controller
class MainController {

    @Autowired
    private ActService actService;

    @RequestMapping(value = "/acts", method = RequestMethod.POST)
    void makeAct(@RequestBody Act act, HttpServletResponse response) {
        XWPFDocument actDoc = actService.makeAct(act)

        String fileName = URLEncoder.encode(getFileName(act), "UTF-8")
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName)
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
//        actDoc.getProperties().coreProperties.contentType
        actDoc.write(response.getOutputStream())
        response.flushBuffer()
//        ByteArrayOutputStream stream = new ByteArrayOutputStream()
//        actDoc.write(stream)
        actDoc.write(new FileOutputStream(new File("1.docx")))
//        actDoc.close()
//
//        HttpHeaders header = new HttpHeaders()
//        header.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document"))
//        header.set("Content-Disposition", "attachment; filename=" + getFileName(act))
//        header.setContentLength(stream.size())
//        new ResponseEntity<>(stream.toByteArray(), header, HttpStatus.OK)
    }

    String getFileName(Act act) {
        String month = act.getCreateDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))
        String userName = act.getUserName().replaceAll(" ", "_")
        String.format("Акт_%s_%s_%s.docx", month, act.getCreateDate().getYear(), userName)
    }

    @RequestMapping("/str")
    ResponseEntity<String> getStr() {
        new ResponseEntity<>("Я Русский", HttpStatus.OK)
    }

}
