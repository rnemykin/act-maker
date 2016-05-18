package ru.act.service

import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import ru.act.common.ValidationException
import ru.act.model.Act

import static org.springframework.util.StringUtils.isEmpty

@Component
class ActValidator {
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
}
