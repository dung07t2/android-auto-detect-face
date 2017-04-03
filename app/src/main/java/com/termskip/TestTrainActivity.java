package com.termskip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.termskip.helpers.FaceHelper;
import com.termskip.helpers.FaceSetHelper;
import com.termskip.helpers.PersonHelper;

import org.json.JSONException;

import static com.termskip.helpers.General.ApiKey;
import static com.termskip.helpers.General.ApiSecret;
import static com.termskip.helpers.General.EmptyString;

public class TestTrainActivity extends AppCompatActivity {

    private String GetFaceIds(String[] imageUrls, FaceHelper faceHelper) {
        String faceIds = EmptyString;
        for (String imageUrl :
                imageUrls) {
            try {
                faceIds += faceHelper.DetectOneFace(imageUrl) + ",";
            } catch (FaceppParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return faceIds.substring(0, faceIds.length()-1); // remove the comma at the end
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_train);

        final String[] imageUrls = {"http://conma.net/photos/dung/01.jpg",
                "http://conma.net/photos/dung/02.jpg",
                "http://conma.net/photos/dung/03.jpg",
                "http://conma.net/photos/dung/04.jpg",};

        final String personName = "Dung";



        new Thread(new Runnable() {
            public void run() {
                HttpRequests httpRequests = new HttpRequests(ApiKey, ApiSecret);
                FaceSetHelper faceSetHelper;
                FaceHelper faceHelper;
                PersonHelper personHelper;
                String personId;

                //Train for one person
                try {
                    faceSetHelper = new FaceSetHelper(httpRequests);
                    faceHelper = new FaceHelper(httpRequests);
                    personHelper = new PersonHelper(httpRequests);
                    personId = personHelper.CreatePerson(personName, EmptyString);
                    String faceIds = GetFaceIds(imageUrls, faceHelper);
                    personHelper.AddFaceToPerson(personId, faceIds);
                    String faceSetId = faceSetHelper.GetFacesetId();
                    faceSetHelper.AddFaceToFaceSet(faceSetId, faceIds);
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }
}
