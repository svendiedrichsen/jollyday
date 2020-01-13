## Holidays in morocco
[ List of holidays (Wikipedia)](https://en.wikipedia.org/wiki/Public_holidays_in_Morocco)
In order to check if a date stands for a holiday in Morocco use this snippet :

``` java
final ManagerParameter managerParameter = ManagerParameters.create(HolidayCalendar.MOROCCO);
final HolidayManager holidayManager = HolidayManager.getInstance(managerParameter);
final LocalDate dateToTest = LocalDate.of(2020, 1, 11);
System.out.println(holidayManager.isHoliday(dateToTest));
```
