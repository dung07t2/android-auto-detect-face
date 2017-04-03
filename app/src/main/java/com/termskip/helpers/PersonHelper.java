package com.termskip.helpers;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.facepp.result.FaceppResult;

import org.json.JSONException;

import static com.termskip.helpers.General.GetFaceppResultValue;
import static com.termskip.helpers.General.Success;

/**
 * Created by tntmr on 11/5/2016.
 */

public class PersonHelper {
    private static final String PersonId = "person_id";

    private HttpRequests _httpRequests;

    public PersonHelper(HttpRequests httpRequests) throws FaceppParseException {
        _httpRequests = httpRequests;
    }

    public String CreatePerson(String name, String email) throws FaceppParseException, JSONException {
        PostParameters postParameters = new PostParameters();
        postParameters.setPersonName(name);
        postParameters.setTag(email);
        FaceppResult result = _httpRequests.personCreate(postParameters);
        return GetFaceppResultValue(result, PersonId);
    }

    public Boolean AddFaceToPerson(String personId, String faceIds) throws FaceppParseException, JSONException {
        PostParameters postParameters = new PostParameters();
        postParameters.setPersonId(personId);
        postParameters.setFaceId(faceIds);
        FaceppResult result = _httpRequests.personAddFace(postParameters);
        return GetFaceppResultValue(result, Success) == "true";
    }
}
