package ru.act.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

class Act {
    String userName
    ZonedDateTime createDate = ZonedDateTime.now()
    Integer actNumber
    Integer docNumber
    Integer certSerial
    String certNumber
    LocalDate docSignDate
    BigDecimal salaryRate
    String mainTask
    BigDecimal mainTaskHours
    String additionalTask
    BigDecimal additionalTaskHours
}
