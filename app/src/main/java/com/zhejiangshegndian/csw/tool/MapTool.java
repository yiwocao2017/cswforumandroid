package com.zhejiangshegndian.csw.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.model.CityModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LeiQ on 2017/4/13.
 */

public class MapTool {

    private static SharedPreferences appConfigSp;


    private static String latitude = "";
    private static String longitude = "";
    private static AMapLocationClient mLocationClient;

    private static Map<String, String> map;

    public static void getLocation(final Context context, final locationCallBack callBack) {
        if (map == null) {
            map = new HashMap();
        }

        mLocationClient = new AMapLocationClient(context);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {

                        latitude = aMapLocation.getLatitude() + "";
                        longitude = aMapLocation.getLongitude() + "";

                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        String province = aMapLocation.getProvince();
                        String location = extractLocation(city, district);

                        Log.i("location_success","latitude"+latitude);
                        Log.i("location_success","longitude"+longitude);
                        Log.i("location_success","location"+location);
                        Log.i("location_success","province"+province);
                        Log.i("location_success","city"+city);
                        Log.i("location_success","district"+district);

                        callBack.onSuccess(latitude,longitude,location,province,city,district);

                        // 根据省市区查询站点
//                        getCity(province, city, district, callBack, context);
                    } else {
                        //定位失败
                        callBack.onFailed("定位失败");
                    }
                }
            }
        });
        mLocationClient.startLocation();
    }

    /***
     *
     * @param province
     * @param city
     * @param district
     */
    public static void getCity(String province, String city, String district, final locationCallBack callBack, final Context context) {
        JSONObject object = new JSONObject();
        try {
            object.put("status", "2");
            object.put("province", province);
            object.put("city", city);
            object.put("area", district);
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_806012, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Gson gson = new Gson();
                    CityModel model = gson.fromJson(jsonObject.toString(),new TypeToken<CityModel>(){}.getType());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(context, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * 提取出城市或者县
     *
     * @param city
     * @param district
     * @return
     */
    public static String extractLocation(final String city, final String district) {
        return district.contains("县") ? district.substring(0, district.length() - 1) : city.substring(0, city.length() - 1);
    }

    public interface locationCallBack {

        void onSuccess(String latitude, String longitude, String location,
                       String province,String city, String district);

        void onFailed(String tip);
    }
}
