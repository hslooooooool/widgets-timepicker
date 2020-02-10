# qsos-timepicker
安卓时间选择器

![](https://)

```
    api 'com.github.hslooooooool:qsos-timepicker:[version]'
```

## 功能描述
提供时间选择的功能，可定制时间选择格式和范围。

## 使用案例
```
                when (p) {
                    /**普通时间
                    格式：年月日时分
                    限制：当前时间上下【10年】*/
                    0 -> {
                        TimePickHelper.picker(
                            context = mContext, timeType = CustomDatePicker.Type.YMDHM,
                            onDateListener = onDateListener
                        )
                    }
                    /**定制时间
                    格式：年月日时
                    限制：当前时间上下【10年】*/
                    1 -> {
                        TimePickHelper.picker(
                            context = mContext, timeType = CustomDatePicker.Type.YMDH,
                            onDateListener = onDateListener
                        )
                    }
                    /**定制时间
                    格式：年月日
                    限制：当前时间上下【10年】*/
                    2 -> {
                        TimePickHelper.picker(
                            context = mContext, timeType = CustomDatePicker.Type.YMD,
                            onDateListener = onDateListener
                        )
                    }
                    /**定制时间
                    格式：年月
                    限制：当前时间上下【10年】*/
                    3 -> {
                        TimePickHelper.picker(
                            context = mContext, timeType = CustomDatePicker.Type.YM,
                            onDateListener = onDateListener
                        )
                    }
                    /**定制时间
                    格式：年
                    限制：当前时间上下【10年】*/
                    4 -> {
                        TimePickHelper.picker(
                            context = mContext, timeType = CustomDatePicker.Type.Y,
                            onDateListener = onDateListener
                        )
                    }
                    /**定制时间
                    格式：【今月】日时分
                    限制：当前时间【今月】*/
                    5 -> {
                        mCalendar.set(nowYear, nowMonth, 0, 0, 0)
                        val limitDateStart = mCalendar.time

                        val mMaxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                        mCalendar.set(nowYear, nowMonth, mMaxDay, 23, 59)
                        val limitDateEnd = mCalendar.time
                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.DHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**定制时间
                    格式：【今月】日时
                    限制：当前时间【今月】*/
                    6 -> {
                        mCalendar.set(nowYear, nowMonth, 0, 0, 0)
                        val limitDateStart = mCalendar.time

                        val mMaxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                        val mMaxHour = mCalendar.getActualMaximum(Calendar.HOUR_OF_DAY)
                        mCalendar.set(nowYear, nowMonth, mMaxDay, mMaxHour, 0)
                        val limitDateEnd = mCalendar.time
                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.DH,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**定制时间
                    格式：【今日】时分
                    限制：当前时间【今日】*/
                    7 -> {
                        mCalendar.set(nowYear, nowMonth, nowDay, 0, 0)
                        val limitDateStart = mCalendar.time

                        val mMaxHour = mCalendar.getActualMaximum(Calendar.HOUR_OF_DAY)
                        mCalendar.set(nowYear, nowMonth, nowDay, mMaxHour, 59)
                        val limitDateEnd = mCalendar.time
                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.HM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**限制时间
                    格式：年月日时分
                    限制：设定时间上下【1年】*/
                    8 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 - 1)
                        val limitDateStart = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 + 1)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**限制时间
                    格式：年月日时分
                    限制：当前时间上下【1年】*/
                    9 -> {
                        mCalendar.set(Calendar.YEAR, nowYear - 1)
                        val limitDateStart = mCalendar.time

                        mCalendar.set(Calendar.YEAR, nowYear + 1)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            onDateListener = onDateListener
                        )
                    }
                    /**限制时间
                    格式：年月日时分
                    限制：设定时间上下【5年】*/
                    10 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 - 5)
                        val limitDateStart = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 + 5)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**限制时间
                    格式：年月日时分
                    限制：当前时间上下【5年】*/
                    11 -> {
                        mCalendar.set(Calendar.YEAR, nowYear - 5)
                        val limitDateStart = mCalendar.time

                        mCalendar.set(Calendar.YEAR, nowYear + 5)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**限制时间
                    格式：年月日时分
                    限制：设定时间往前【5年】*/
                    12 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 - 5)
                        val limitDateStart = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDate,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**限制时间
                    格式：年月日时分
                    限制：当前时间往前【5年】*/
                    13 -> {
                        mCalendar.set(Calendar.YEAR, nowYear - 5)
                        val limitDateStart = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = nowDate,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**限制时间
                    格式：年月日时分
                    限制：设定时间往后【5年】*/
                    14 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 + 5)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDate,
                            limitMax = limitDateEnd,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**限制时间
                    格式：年月日时分
                    限制：当前时间往后【5年】*/
                    15 -> {
                        mCalendar.set(Calendar.YEAR, nowYear + 5)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = nowDate,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**限制时间
                    格式：年月日时分
                    限制：设定时间往前【5年】，往后【1年】*/
                    16 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time
                        mCalendar.set(Calendar.YEAR, 2020 - 5)
                        val limitDateStart = mCalendar.time
                        mCalendar.set(Calendar.YEAR, 2020 + 1)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**限制时间
                    格式：年月日时分
                    限制：当前时间往前【5年】，往后【1年】*/
                    17 -> {
                        mCalendar.set(Calendar.YEAR, nowYear - 5)
                        val limitDateStart = mCalendar.time
                        mCalendar.set(Calendar.YEAR, nowYear + 1)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = nowDate,
                            onDateListener = onDateListener
                        )
                    }
                    /**错误时间
                    格式：年月日时分
                    错误：开始时间大于设定时间或结束时间小于设定时间*/
                    18 -> {
                        mCalendar.set(2020, 2 - 1, 2, 2, 2)
                        val limitDate = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 + 1)
                        val limitDateStart = mCalendar.time

                        mCalendar.set(Calendar.YEAR, 2020 - 1)
                        val limitDateEnd = mCalendar.time

                        TimePickHelper.picker(
                            context = mContext,
                            timeType = CustomDatePicker.Type.YMDHM,
                            limitMin = limitDateStart,
                            limitMax = limitDateEnd,
                            selected = limitDate,
                            onDateListener = onDateListener
                        )
                    }
                }

```
## 部分视图
![image1](../doc/image1.jpg)
![image2](../doc/image2.jpg)
![image3](../doc/image3.jpg)
![image4](../doc/image4.jpg)