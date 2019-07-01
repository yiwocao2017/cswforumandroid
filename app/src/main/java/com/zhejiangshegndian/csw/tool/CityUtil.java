package com.zhejiangshegndian.csw.tool;

import com.zhejiangshegndian.csw.model.CityModel;
import com.zhejiangshegndian.csw.model.CitysModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeiQ on 2017/4/12.
 */

public class CityUtil {

    private static List<CityModel> list;

    public static List<CityModel> getCity(CitysModel model){

        list = new ArrayList<>();

        // 查询对应List下面是否有站点
        if(model != null){

            if(model.getA() != null){
                for (CityModel cityModel : model.getA())
                    list.add(cityModel);
            }

            if(model.getB() != null){
                for (CityModel cityModel : model.getB())
                    list.add(cityModel);
            }

            if(model.getC() != null){
                for (CityModel cityModel : model.getC())
                    list.add(cityModel);
            }

            if(model.getD() != null){
                for (CityModel cityModel : model.getD())
                    list.add(cityModel);
            }

            if(model.getE() != null){
                for (CityModel cityModel : model.getE())
                    list.add(cityModel);
            }


            if(model.getF() != null){
                for (CityModel cityModel : model.getF())
                    list.add(cityModel);
            }

            if(model.getG() != null){
                for (CityModel cityModel : model.getG())
                    list.add(cityModel);
            }

            if(model.getH() != null){
                for (CityModel cityModel : model.getH())
                    list.add(cityModel);
            }

            if(model.getI() != null){
                for (CityModel cityModel : model.getI())
                    list.add(cityModel);
            }

            if(model.getJ() != null){
                for (CityModel cityModel : model.getJ())
                    list.add(cityModel);
            }

            if(model.getK() != null){
                for (CityModel cityModel : model.getK())
                    list.add(cityModel);
            }

            if(model.getL() != null){
                for (CityModel cityModel : model.getL())
                    list.add(cityModel);
            }

            if(model.getM() != null){
                for (CityModel cityModel : model.getM())
                    list.add(cityModel);
            }

            if(model.getN() != null){
                for (CityModel cityModel : model.getN())
                    list.add(cityModel);
            }

            if(model.getO() != null){
                for (CityModel cityModel : model.getO())
                    list.add(cityModel);
            }

            if(model.getP() != null){
                for (CityModel cityModel : model.getP())
                    list.add(cityModel);
            }

            if(model.getQ() != null){
                for (CityModel cityModel : model.getQ())
                    list.add(cityModel);
            }

            if(model.getR() != null){
                for (CityModel cityModel : model.getR())
                    list.add(cityModel);
            }

            if(model.getS() != null){
                for (CityModel cityModel : model.getS())
                    list.add(cityModel);
            }

            if(model.getT() != null){
                for (CityModel cityModel : model.getT())
                    list.add(cityModel);
            }

            if(model.getU() != null){
                for (CityModel cityModel : model.getU())
                    list.add(cityModel);
            }

            if(model.getV() != null){
                for (CityModel cityModel : model.getV())
                    list.add(cityModel);
            }

            if(model.getW() != null){
                for (CityModel cityModel : model.getW())
                    list.add(cityModel);
            }

            if(model.getX() != null){
                for (CityModel cityModel : model.getX())
                    list.add(cityModel);
            }

            if(model.getY() != null){
                for (CityModel cityModel : model.getY())
                    list.add(cityModel);
            }

            if(model.getZ() != null){
                for (CityModel cityModel : model.getZ())
                    list.add(cityModel);
            }
        }

        return list;
    }

}
