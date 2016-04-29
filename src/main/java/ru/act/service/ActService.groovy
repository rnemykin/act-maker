package ru.act.service

import com.ibm.icu.text.RuleBasedNumberFormat
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.act.model.Act

import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.format.TextStyle

import static java.time.temporal.ChronoField.*

@Service
class ActService {

    private static def RU_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('.')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('.')
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .toFormatter()

    @Autowired
    private RussianNameProcessor nameProcessor;


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

    String replaceFromProperty(String text, ActProperty source) {
        source.properties.each {key, value ->
            if(!(key.toString()  in ['metaClass', 'class'])) {
                String keyWord = String.format("\\b%s\\b", key.toString())
                String replacement = value?.toString() ?: ''
                text = text?.replaceAll(keyWord, replacement)
            }
        }

        text
    }

    ActProperty buildProperties(Act act) {
        ActProperty actProperty = new ActProperty();
        actProperty.userName = nameProcessor.inCaseRod(act.userName)
        actProperty.shortUserName = getShortName(act.userName)

        actProperty.actDay = act.date.withDayOfMonth(act.date.lengthOfMonth()).dayOfMonth
        actProperty.actMonth = act.date.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))
        actProperty.actYear = act.date.year
        actProperty.actStartDate = act.date.withDayOfMonth(1).format(RU_DATE_FORMATTER)
        actProperty.actEndDate = act.date.withDayOfMonth(act.date.lengthOfMonth()).format(RU_DATE_FORMATTER)
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
            actProperty.salaryRate2 = act.salaryRate.multiply(BigDecimal.valueOf(1.5)).setScale(0)
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

        String.format("%s %s. %s.", lastName, firstName?.charAt(0), middleName?.charAt(0))
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

        Integer certSerial;
        String certNumber;
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
