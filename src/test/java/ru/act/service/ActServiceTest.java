package ru.act.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.act.Application;
import ru.act.model.Act;
import ru.act.model.ActDocument;
import ru.act.service.ActService;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@SuppressWarnings("SpringJavaAutowiringInspection")
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
        act.setMainTask("Доработка функционала COD-GATEWAY");
        act.setDocSignDate(LocalDate.of(2015, 8, 1));
        act.setCreateDate(ZonedDateTime.now());
        act.setUserName("Немыкин Роман Валерьевич");
        act.setCertSerial(31);
        act.setCertNumber("002439785");

        ActDocument actDocument = actService.makeAct(act);
        assertNotNull(actDocument);
        assertNotNull(actDocument.getBytes());
        assertNotNull(actDocument.getFileName());
        Files.write(Paths.get(actDocument.getFileName()), actDocument.getBytes());
    }

    @Test
    public void testJS() throws FileNotFoundException, ScriptException {
        System.out.println(nameProcessor.inCaseRod("Гадя Петрович Хренова"));
    }
}