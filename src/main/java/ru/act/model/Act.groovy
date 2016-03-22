package ru.act.model

import java.time.LocalDate

class Act {
    String userName;
    LocalDate startDate;
    Short actNumber;
    Short docNumber;
    LocalDate docSignDate;
    String mainTask;
    Integer mainTaskHours;
    String additionalTask;
    Integer additionalTaskHours;
    BigDecimal salaryRate;
}
