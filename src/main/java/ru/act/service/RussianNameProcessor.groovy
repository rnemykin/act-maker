package ru.act.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import java.nio.charset.Charset

@Service
class RussianNameProcessor {

    private static final ScriptEngine JS_ENGINE = new ScriptEngineManager().getEngineByName("javascript");
    private static final String GET_IN_ROD = "RussianNameProcessor.word('%s', '', '%s', RussianNameProcessor.gcaseRod)"

    @Value("RussianNameProcessor.js")
    private Resource resource;

    @PostConstruct
    init() {
        JS_ENGINE.eval(new InputStreamReader(resource.getInputStream(), Charset.forName("UTF-8")));
    }


    String inCaseRod(String userFio) {
        def values = userFio.split(' ')
        String lastName = values.size() > 0 ? values[0] : ''
        String firstName = values.size() > 1 ? values[1] : ''
        String middleName = values.size() > 2 ? values[2] : ''
        String.format("%s %s %s",
                JS_ENGINE.eval(String.format(GET_IN_ROD, lastName, 'lastName')),
                JS_ENGINE.eval(String.format(GET_IN_ROD, firstName, 'firstName')),
                JS_ENGINE.eval(String.format(GET_IN_ROD, middleName, 'middleName')))
    }

}
