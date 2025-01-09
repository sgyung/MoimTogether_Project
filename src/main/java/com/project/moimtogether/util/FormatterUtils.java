package com.project.moimtogether.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatterUtils {

    public static String convertBirth(String birth){
        StringBuilder convertBirth = new StringBuilder();
        convertBirth.append(birth, 0, 4).append("-")
                .append(birth,4,6).append("-")
                .append(birth,6,birth.length());
        return convertBirth.toString();
    }

    public static String convertPhone(String phone){
        StringBuilder convertPhone = new StringBuilder();
        convertPhone.append(phone, 0, 3).append("-")
                .append(phone,3,7).append("-")
                .append(phone,7,phone.length());
        return convertPhone.toString();
    }

    public static String convertTimestampToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateFormat.format(localDateTime);
    }

}
