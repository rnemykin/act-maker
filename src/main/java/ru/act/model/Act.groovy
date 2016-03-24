package ru.act.model

import java.time.LocalDate

class Act {
    String userName;
    LocalDate startDate;
    Short actNumber;
    Short docNumber;
    LocalDate docSignDate;
    BigDecimal salaryRate;
    String mainTask;
    BigDecimal mainTaskHours;
    BigDecimal mainSum;
    String additionalTask;
    BigDecimal additionalTaskHours;
    BigDecimal additionalSum;
    BigDecimal allSum;
}
