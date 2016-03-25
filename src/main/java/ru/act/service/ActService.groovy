package ru.act.service

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.stereotype.Service
import ru.act.model.Act

import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.format.TextStyle

import static java.time.temporal.ChronoField.DAY_OF_MONTH
import static java.time.temporal.ChronoField.MONTH_OF_YEAR
import static java.time.temporal.ChronoField.YEAR

@Service
class ActService {

    private static def RU_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('.')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('.')
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .toFormatter()


    XWPFDocument makeAct(Act act) {

        def actProperty = buildProperties(act)
        def docTemplate = ActService.class.getResource("/test.docx")
        XWPFDocument doc = new XWPFDocument(OPCPackage.open(docTemplate.file))
        doc.paragraphs.each {
            it.runs.each {
                String text = it.getText(0)
                it.setText(replaceFromProperty(text, actProperty), 0)
            };
        }

        doc.tables.each {
            it.rows.each {
                it.tableCells.each {
                    it.paragraphs.each {
                        it.runs.each {
                            String text = it.getText(0);
                            text = replaceFromProperty(text, actProperty)
                            it.setText(text, 0)
                        }
                    }
                }
            }
        }

        doc
    }

    String replaceFromProperty(String text, Object source) {
        source.properties.each {key, value ->
            if(!(key.toString()  in ['metaClass', 'class'])) {
                String exactKey = String.format("\\b%s\\b", key.toString())
                String valueToReplace = value?.toString() ?: ''
                text = text?.replaceAll(exactKey, valueToReplace)
            }
        }

        text
    }

    ActProperty buildProperties(Act act) {
        ActProperty actProperty = new ActProperty();
        actProperty.userName = act.userName  //  padezh
        actProperty.shortUserName = act.userName  //  short

        actProperty.actDay = act.date.withDayOfMonth(1).dayOfMonth
        actProperty.actMonth = act.docSignDate.month.getDisplayName(TextStyle.FULL, Locale.default)
        actProperty.actYear = act.date.year
        actProperty.actStartDate = act.date.withDayOfMonth(1).format(RU_DATE_FORMATTER)
        actProperty.actEndDate = act.date.withDayOfMonth(act.date.lengthOfMonth()).format(RU_DATE_FORMATTER)
        actProperty.actNumber = act.actNumber

        actProperty.docNumber = act.docNumber
        actProperty.docSignYear = act.docSignDate.year
        actProperty.docSignDate = act.docSignDate.format(RU_DATE_FORMATTER)

        actProperty.mainTask = act.mainTask
        actProperty.mainTaskHours = act.mainTaskHours
        actProperty.salaryRate = act.salaryRate

        if(act.additionalTask) {
            actProperty.addTask = act.additionalTask
            actProperty.salaryRate2 = act.salaryRate.multiply(BigDecimal.valueOf(1.5))
            actProperty.addSum = act.additionalTaskHours.multiply(actProperty.salaryRate2)
            actProperty.addTaskHours = act.additionalTaskHours
            actProperty.actAddStartDate = actProperty.actStartDate + '-'
            actProperty.actAddEndDate = actProperty.actEndDate
        }

        actProperty.mainSum = act.salaryRate.multiply(act.mainTaskHours)
        actProperty.allSum = actProperty.mainSum.add(actProperty.addSum ?: BigDecimal.ZERO)
        actProperty.sumStr = "ввосемьдесят тыщ стопятьсот рублей"

        actProperty
    }

    static class ActProperty {
        String userName
        String shortUserName

        Integer actDay
        String actMonth
        Integer actYear
        String actStartDate
        String actEndDate
        String actAddStartDate
        String actAddEndDate
        Integer actNumber

        Integer docNumber
        Integer docSignYear
        String docSignDate

        BigDecimal salaryRate = BigDecimal.ZERO
        String mainTask
        BigDecimal mainTaskHours = BigDecimal.ZERO
        String addTask
        BigDecimal addTaskHours
        BigDecimal salaryRate2
        BigDecimal mainSum = BigDecimal.ZERO
        BigDecimal addSum
        BigDecimal allSum = BigDecimal.ZERO
        String sumStr
    }

}
