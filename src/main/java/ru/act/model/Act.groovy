package ru.act.model

import java.time.LocalDate

class Act {
    String userName
    LocalDate date
    Integer actNumber
    Integer docNumber
    LocalDate docSignDate
    BigDecimal salaryRate = BigDecimal.ZERO
    String mainTask
    BigDecimal mainTaskHours = BigDecimal.ZERO
    String additionalTask
    BigDecimal additionalTaskHours
}
