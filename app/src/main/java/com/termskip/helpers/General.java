package com.termskip.helpers;

import com.facepp.result.FaceppResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tntmr on 11/5/2016.
 */

public class General {
    public static final String EmptyString = "";
    public static final String ApiKey = "c40fafef7a35cd8cf73448fbda4990fa";
    public static final String ApiSecret = "TsIEik-Lv81U8tUU_ij2AYXpuUX8jD5z";
    public static final String Success = "success";

    public static class DetectMode{
        public static String Normal = "normal";
        public static String OneFace = "oneface";
    }

    public static String GetFaceppResultItem(FaceppResult result, String arrayKey, Integer index, String itemKey) throws JSONException {
        JSONObject jsonObject = new JSONObject(result.toString());
        JSONArray faces = jsonObject.getJSONArray(arrayKey);
        if (faces.length() < 1) {
            return EmptyString;
        }
        return faces.getJSONObject(index).get(itemKey).toString();
    }

    public static String GetFaceppResultValue(FaceppResult result, String key) throws JSONException {
        JSONObject jsonObject = new JSONObject(result.toString());
        return jsonObject.get(key).toString();
    }

    public static String GetFaceppResult(FaceppResult result, String arrayKey, Integer index, String itemKey) throws JSONException {
        JSONObject jsonObject = new JSONObject(result.toString());
        JSONArray faces = jsonObject.getJSONArray(arrayKey);
        if (faces.length() < 1) {
            return EmptyString;
        }
        return faces.getJSONObject(index).toString();
    }
}
