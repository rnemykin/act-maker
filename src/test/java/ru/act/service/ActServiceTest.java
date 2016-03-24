package ru.act.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.act.Application;
import ru.act.model.Act;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ActServiceTest {

    @Autowired
    ActService actService;

    @Test
    public void makeAct() throws Exception {
        actService.makeAct(new Act());
    }
}