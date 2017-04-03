package com.termskip.helpers;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.facepp.result.FaceppResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.termskip.helpers.General.EmptyString;
import static com.termskip.helpers.General.GetFaceppResultValue;
import static com.termskip.helpers.General.Success;

/**
 * Created by tntmr on 11/5/2016.
 */

public class FaceSetHelper {
    private static final String FaceSet = "faceset";
    private static final String FaceSetId = "faceset_id";
    private HttpRequests _httpRequests;

    public FaceSetHelper(HttpRequests httpRequests) throws FaceppParseException {
        _httpRequests = httpRequests;
    }

    private FaceppResult GetFacesetList() throws FaceppParseException {
        return _httpRequests.request("info", "get_faceset_list");
    }

    public String GetFacesetId() {
        try {
            FaceppResult result = GetFacesetList();
            JSONObject jsonObject = new JSONObject(result.toString());
            JSONArray faces = jsonObject.getJSONArray(FaceSet);
            if (faces.length() < 1) {
                return CreateFaceset();
            }
            return faces.getJSONObject(0).get(FaceSetId).toString();

        } catch (FaceppParseException e) {
            e.printStackTrace();
            return EmptyString;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return EmptyString;
    }

    private String CreateFaceset() {
        FaceppResult result;
        try {
            result = _httpRequests.request(FaceSet, "create");
            JSONObject jsonObject = new JSONObject(result.toString());
            return jsonObject.get(FaceSetId).toString();
        } catch (FaceppParseException e) {
            e.printStackTrace();
            return EmptyString;
        } catch (JSONException e) {
            e.printStackTrace();
            return EmptyString;
        }
    }

    public Boolean AddFaceToFaceSet(String faceSetId, String faceIds) throws FaceppParseException, JSONException {
        PostParameters postParameters = new PostParameters();
        postParameters.setFaceSetId(faceSetId);
        postParameters.setFaceId(faceIds);
        FaceppResult result = _httpRequests.faceSetAddFace(postParameters);
        return GetFaceppResultValue(result, Success) == "true";
    }
}
