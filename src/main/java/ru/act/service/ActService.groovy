package ru.act.service

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.act.model.Act
import ru.act.model.ActDocument
import ru.act.model.ActProperty
import java.time.format.TextStyle

@Service
class ActService {
    private static final Logger log = LoggerFactory.getLogger(ActService.class)

    @Autowired
    private Act2ActPropertyMapper act2ActPropertyMapper

    @Autowired
    private ActValidator actValidator

    ActDocument makeAct(Act act) {
        actValidator.validate(act)

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
