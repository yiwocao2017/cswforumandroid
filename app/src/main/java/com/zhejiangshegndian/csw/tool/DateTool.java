package com.zhejiangshegndian.csw.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LeiQ on 2017/4/14.
 */

public class DateTool {

    public static String formatDate(String date,String formatStyle){
        SimpleDateFormat s = new SimpleDateFormat(formatStyle);
        Date d5 = new Date(date);

        return s.format(d5);
    }

}
