package ru.act.service

import com.ibm.icu.text.RuleBasedNumberFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.act.model.Act
import ru.act.model.ActProperty

import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.format.TextStyle

import static java.time.temporal.ChronoField.DAY_OF_MONTH
import static java.time.temporal.ChronoField.MONTH_OF_YEAR
import static java.time.temporal.ChronoField.YEAR

@Component
class Act2ActPropertyMapper {

    private static def RU_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('.')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('.')
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .toFormatter()


    @Autowired
    private RussianNameProcessor nameProcessor;


    ActProperty map(Act act) {
        ActProperty actProperty = new ActProperty();
        actProperty.userName = nameProcessor.inCaseRod(act.userName)
        actProperty.shortUserName = getShortName(act.userName)

        actProperty.actDay = act.createDate.dayOfMonth
        actProperty.actMonth = act.createDate.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))
        actProperty.actYear = act.createDate.year
        actProperty.actStartDate = act.createDate.withDayOfMonth(1).format(RU_DATE_FORMATTER)
        actProperty.actEndDate = act.createDate.withDayOfMonth(act.createDate.toLocalDate().lengthOfMonth()).format(RU_DATE_FORMATTER)
        actProperty.actNumber = act.actNumber

        actProperty.docNumber = act.docNumber
        actProperty.docSignYear = act.docSignDate.year
        actProperty.docSignDate = act.docSignDate.format(RU_DATE_FORMATTER)
        actProperty.certSerial = act.certSerial
        actProperty.certNumber = act.certNumber

        actProperty.mainTask = act.mainTask
        actProperty.mainTaskHours = act.mainTaskHours
        actProperty.salaryRate = act.salaryRate

        if(act.additionalTask) {
            actProperty.addTask = act.additionalTask
            actProperty.salaryRate2 = act.salaryRate.multiply(BigDecimal.valueOf(1.5)).setScale(2)
            actProperty.addSum = act.additionalTaskHours?.multiply(actProperty.salaryRate2)
            actProperty.addTaskHours = act.additionalTaskHours
            actProperty.actAddStartDate = actProperty.actStartDate + '-'
            actProperty.actAddEndDate = actProperty.actEndDate
        }

        actProperty.mainSum = act.salaryRate.multiply(act.mainTaskHours)
        actProperty.allSum = actProperty.mainSum.add(actProperty.addSum ?: BigDecimal.ZERO)

        def numberFormat = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT)
        actProperty.sumStr = numberFormat.format(actProperty.allSum);

        actProperty
    }

    String getShortName(String userName) {
        String[] values = userName.split(' ');
        String lastName = values.size() > 0 ? values[0] : null
        String firstName = values.size() > 1 ? values[1] : null
        String middleName = values.size() > 2 ? values[2] : null

        String.format("%s %s. %s.", lastName, firstName?.charAt(0) ?: '', middleName?.charAt(0) ?: '')
    }
}
