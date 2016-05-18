package ru.act.service

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import ru.act.common.ValidationException
import ru.act.model.Act
import ru.act.model.ActDocument
import ru.act.model.ActProperty
import java.time.format.TextStyle

import static org.springframework.util.StringUtils.isEmpty

@Service
class ActService {
    private static final Logger log = LoggerFactory.getLogger(ActService.class)

    @Autowired
    private Act2ActPropertyMapper act2ActPropertyMapper

    ActDocument makeAct(Act act) {
        validate(act)

        def actProperty = act2ActPropertyMapper.map(act)
        def docTemplate = ActService.class.getResource("/template.docx")
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

        log.info("Act for user {} has been created", act.userName)

        def stream = new ByteArrayOutputStream()
        doc.write(stream)
        doc.getPackage().revert()

        new ActDocument(stream.toByteArray(), getFileName(act))
    }

    void validate(Act act) {
        if(act == null) {
            throw new ValidationException("Акт не мб пустым")
        }
        if(act.docNumber == null || act.docSignDate == null) {
            throw new ValidationException("Номер или дата подписания договора не мб пустым")
        }
        if(isEmpty(act.certNumber) || act.certSerial == null) {
            throw new ValidationException("Серия или номер ИП не мб пустым")
        }
        if(isEmpty(act.userName)) {
            throw new ValidationException("ФИО юзера не мб пустым")
        }
        if(isEmpty(act.mainTask)) {
            throw new ValidationException("Название задачи не мб пустым")
        }
        if(act.mainTaskHours == null || act.mainTaskHours.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Количество часов по задаче должно быть > 0")
        }
        if(act.actNumber == null) {
            throw new ValidationException("Номер акта не мб пустым")
        }
        if(act.salaryRate == null || act.salaryRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Ставка должна быть > 0")
        }
        if(StringUtils.hasText(act.additionalTask)) {
            if(act.additionalTaskHours == null || act.additionalTaskHours.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Количество часов по дополнительной задаче должно быть > 0")
            }
        }
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

    String getFileName(Act act) {
        String month = act.getCreateDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))
        String userName = act.getUserName().replaceAll(" ", "_")
        String.format("Акт_%s_%s_%s.docx", month, act.getCreateDate().getYear(), userName)
    }

}
