package script.library;/*
@Origin: dsrc.script.library
@Author: BubbaJoeX
@Purpose: Date and Time functions for SWG
@Created: Sunday, 10/1/2023, at 12:56 PM.
@Requirements: <no requirements>
@Copyright © SWG-OR 2024.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/

import script.obj_id;

import java.time.LocalDate;

public class date extends script.base_script
{
    public static String[] months = {
            "January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"
    };
    public static String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    static LocalDate date = LocalDate.now();
    public static int getDay = date.getDayOfMonth();
    public static int getMonth = date.getMonthValue();
    public static int getYear = date.getYear();
    public static int getTime = getGameTime();

    public static String getFullDate(obj_id player, String locale)
    {
        if (locale.equals("en_US"))
        {
            return days[getDay] + ", " + months[getMonth] + " " + getDay + ", " + getYear;
        }
        else if (locale.equals("en_GB") || locale.equals("en_AU"))
        {
            return days[getDay] + ", " + getDay + " " + months[getMonth] + ", " + getYear;
        }
        return days[getDay] + ", " + months[getMonth] + " " + getDay + ", " + getYear;
    }

    public static String getLocale(obj_id player)
    {
        String locale = "en_US";
        if (hasObjVar(player, "localization"))
        {
            locale = getStringObjVar(player, "localization");
        }
        return locale;
    }

    public static String getShortDate(obj_id player, String locale)
    {
        if (locale.equals("en_US"))
        {
            return getMonth + "/" + getDay + "/" + getYear;
        }
        else if (locale.equals("en_GB") || locale.equals("en_AU"))
        {
            return getDay + "/" + getMonth + "/" + getYear;
        }
        return getMonth + "/" + getDay + "/" + getYear;
    }

    public static String getFullDateWithSuffix(obj_id target)
    {
        return days[getDay] + ", " + months[getMonth] + " " + getDay + getDayWithSuffix(getDay) + ", " + getYear;
    }

    public static String getDayWithSuffix(int dayOfMonth)
    {
        if (dayOfMonth >= 1 && dayOfMonth <= 31)
        {
            if (dayOfMonth >= 11 && dayOfMonth <= 13)
            {
                return dayOfMonth + "th";
            }
            switch (dayOfMonth % 10)
            {
                case 1:
                    return dayOfMonth + "st";
                case 2:
                    return dayOfMonth + "nd";
                case 3:
                    return dayOfMonth + "rd";
                default:
                    return dayOfMonth + "th";
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid day of the month: " + dayOfMonth);
        }
    }
}
