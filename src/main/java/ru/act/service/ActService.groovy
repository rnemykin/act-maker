package ru.act.service

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.stereotype.Service
import ru.act.model.Act

@Service
class ActService {

    XWPFDocument makeAct(Act act) {
        def docTemplate = ActService.class.getResource("/test.docx")
        XWPFDocument doc = new XWPFDocument(OPCPackage.open(docTemplate.file))
        doc.paragraphs.each {
            it.runs.each {
                String text = it.getText(0)
                it.setText(replaceFromProperty(text, act), 0)
            };
        }

        doc.tables.each {
            it.rows.each {
                it.tableCells.each {
                    it.paragraphs.each {
                        it.runs.each {
                            String text = it.getText(0);
                            text = replaceFromProperty(text, act)
                            it.setText(text, 0)
                        }
                    }
                }
            }
        }


//        for (XWPFTable tbl : doc.getTables()) {
//            for (XWPFTableRow row : tbl.getRows()) {
//                for (XWPFTableCell cell : row.getTableCells()) {
//                    for (XWPFParagraph p : cell.getParagraphs()) {
//                        for (XWPFRun r : p.getRuns()) {
//                            String text = r.getText(0);
//                            if (text.contains("needle")) {
//                                text = text.replace("needle", "haystack");
//                                r.setText(text);
//                            }
//                        }
//                    }
//                }
//            }
//        }
        doc
    }

    String replaceFromProperty(String text, act) {
        act.properties.each {key, value ->
            if(!(key.toString()  in ['metaClass', 'class'])) {
                text = text?.replace(key.toString(), value.toString()?:'')
            }
        }
        text
    }

}
