package ru.act.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.act.Application;
import ru.act.model.Act;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ActServiceTest {

    @Autowired
    ActService actService;

    @Test
    public void makeAct() throws Exception {
        Act act = new Act();
        act.setActNumber(1);
        act.setDocNumber(3);
        act.setSalaryRate(BigDecimal.valueOf(500));
        act.setMainTaskHours(BigDecimal.valueOf(170));
        act.setMainTask("основная большая задача за месяц");
        act.setDocSignDate(LocalDate.of(2015, 3, 8));
        act.setDate(LocalDate.now());
        act.setUserName("Немыкин Роман Валерьевич");
        act.setAdditionalTask("asdasdas ahsgdjasghd asg yuags udyags udygas uygas ");
        act.setAdditionalTaskHours(BigDecimal.valueOf(16));


        XWPFDocument xwpfDocument = actService.makeAct(act);
        xwpfDocument.write(new FileOutputStream(new File("123.docx")));
    }
}