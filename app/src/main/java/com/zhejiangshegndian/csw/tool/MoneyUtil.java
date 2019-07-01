package com.zhejiangshegndian.csw.tool;

import java.text.DecimalFormat;

/**
 * Created by dell1 on 2016/12/20.
 */

public class MoneyUtil {

    public static String moneyFormatDouble(double money){
        DecimalFormat df = new DecimalFormat("#######0.000");
        String showMoney = df.format((money/1000));

        return showMoney.substring(0,showMoney.length()-1);
    }

}
