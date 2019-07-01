package com.zhejiangshegndian.csw.tool;

/**
 * Created by LeiQ on 2017/5/3.
 */

public class SystemTool {

    /**
     *
     * @param level
     * @return
     */
    public static String levelFormat(String level){

        switch (level){
            case "1":
                return "新手上路";

            case "2":
                return "初级会员";

            case "3":
                return "中级会员";

            case "4":
                return "高级会员";

            case "5":
                return "金牌会员";

            case "6":
                return "论坛元老";
        }

        return "";
    }
}
