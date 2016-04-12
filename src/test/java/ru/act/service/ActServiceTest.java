package ru.act.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.act.Application;
import ru.act.model.Act;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ActServiceTest {

    @Autowired
    ActService actService;

    @Autowired
    RussianNameProcessor nameProcessor;

    @Test
    public void makeAct() throws Exception {
        Act act = new Act();
        act.setActNumber(8);
        act.setDocNumber(3);
        act.setSalaryRate(BigDecimal.valueOf(500));
        act.setMainTaskHours(BigDecimal.valueOf(160));
        act.setMainTask("основная большая задача за месяц");
        act.setDocSignDate(LocalDate.of(2015, 3, 8));
        act.setDate(LocalDate.of(2016,3,5));
        act.setUserName("Немыкин Роман Валерьевич");
//        act.setAdditionalTask("asdasdas ahsgdjasghd asg yuags udyags udygas uygas ");
//        act.setAdditionalTaskHours(BigDecimal.valueOf(16));

        XWPFDocument xwpfDocument = actService.makeAct(act);
        xwpfDocument.write(new FileOutputStream(new File("123.docx")));
    }

    @Test
    public void testJS() throws FileNotFoundException, ScriptException {
        System.out.println(nameProcessor.inCaseRod("Гадя Петрович Хренова"));
    }
}