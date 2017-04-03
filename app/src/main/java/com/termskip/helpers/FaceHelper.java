package com.termskip.helpers;

import android.util.Log;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.facepp.result.FaceppResult;
import com.termskip.models.FaceInformation;
import com.termskip.models.PointFloat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.termskip.helpers.General.DetectMode.OneFace;
import static com.termskip.helpers.General.GetFaceppResultItem;

/**
 * Created by tntmr on 11/5/2016.
 */

public class FaceHelper {
    private static final String Face = "face";
    private static final String FaceId = "face_id";
    private static final String Position = "position";

    private HttpRequests _httpRequests;

    public FaceHelper(HttpRequests httpRequests) throws FaceppParseException {
        _httpRequests = httpRequests;
    }

    public String DetectOneFace(String imgUrl) throws FaceppParseException, JSONException {
        PostParameters postParameters = new PostParameters();
        postParameters.setMode(OneFace);
        postParameters.setUrl(imgUrl);
        FaceppResult result = _httpRequests.detectionDetect(postParameters);
        return GetFaceppResultItem(result, Face, 0, FaceId);
    }

    public String DetectFace(String imgUrl) throws FaceppParseException, JSONException {
        PostParameters postParameters = new PostParameters();
        postParameters.setMode(OneFace);
        postParameters.setUrl(imgUrl);
        FaceppResult result = _httpRequests.detectionDetect(postParameters);
        //return GetFaceppResult(result, Face, 0, FaceId);
        Log.d("FaceHelper:::", result.toString());
        return result.toString();
    }

    public FaceInformation[] GetInformationArray(String imgUrl) throws FaceppParseException, JSONException {
        PostParameters postParameters = new PostParameters();
        postParameters.setUrl(imgUrl);
        FaceppResult result = _httpRequests.detectionDetect(postParameters);
        JSONObject jsonObject = new JSONObject(result.toString());
        JSONArray jsonFaceArray = jsonObject.getJSONArray(Face);
        if (jsonFaceArray.length() < 1)
            return null;

        FaceInformation[] faceInformationArray = new FaceInformation[jsonFaceArray.length()];

        for (Integer index = 0; index< jsonFaceArray.length();index++) {
            JSONObject jsonFace = jsonFaceArray.getJSONObject(index);
            String faceId = jsonFace.getString(FaceId);

            JSONObject jsonPosition = jsonFace.getJSONObject(Position);
            Double height = jsonPosition.getDouble("height");
            Double width = jsonPosition.getDouble("width");

            JSONObject jsonCenter = jsonPosition.getJSONObject("center");
            Double centerX = jsonCenter.getDouble("x");
            Double centerY = jsonCenter.getDouble("y");

            FaceInformation faceInformation = new FaceInformation(new PointFloat(centerX, centerY), height, width, faceId);
            faceInformationArray[index] = faceInformation;
        }

        return faceInformationArray;
    }
}
