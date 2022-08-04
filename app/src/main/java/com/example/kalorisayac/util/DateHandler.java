package com.example.kalorisayac.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DateHandler {
    private static final String PATTERN = "dd MMM yy";
    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern(PATTERN);
    private String choosedDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DateHandler(){
        this.choosedDate = LocalDate.now().format(FORMATTER);
    }

    public String getChoosedDate() {
        return choosedDate;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String nextDay(){
        this.choosedDate = LocalDate.parse(choosedDate, FORMATTER).plusDays(1).format(FORMATTER).toString();
        return this.choosedDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String previousDay(){
        this.choosedDate = LocalDate.parse(choosedDate, FORMATTER).minusDays(1).format(FORMATTER).toString();
        return this.choosedDate;
    }
}
