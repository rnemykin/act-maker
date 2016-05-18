package ru.act.model

class ActProperty {
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