package com.termskip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.termskip.helpers.FaceHelper;
import com.termskip.models.FaceInformation;
import com.termskip.models.PointFloat;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

import static com.termskip.helpers.General.ApiKey;
import static com.termskip.helpers.General.ApiSecret;
import static com.termskip.helpers.General.EmptyString;

public class TestDetectActivity extends AppCompatActivity implements View.OnClickListener {
    String ImgUrl = "http://f-davis.org/images/img10.jpg";
    ImageView imageView;
    EditText txtUrl;
    TextView lblStatus;
    Button btnSend;
    FaceInformation[] faceInformationArray;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detect);
        Intent myIntent = getIntent(); // gets the previously created intent
        ImgUrl = myIntent.getStringExtra("url");
        imageView = (ImageView) findViewById(R.id.imageView);
        lblStatus = (TextView) findViewById(R.id.lblStatus);
        txtUrl = (EditText) findViewById(R.id.urlInput);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Waiting...");

        txtUrl.setText(ImgUrl);

        faceInformationArray = new FaceInformation[0];
        new DownloadImageTask(imageView, faceInformationArray)
                .execute(ImgUrl);

        ExecuteDetection();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSend)
        {
            progressDialog.show();
            ImgUrl = txtUrl.getText().toString();
            lblStatus.setText(EmptyString);

            //send notify
            RequestParams params = new RequestParams();
            params.put("user_id", "Q6hJIEvM37gmt81XR0w2YBh7dmy2");
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://b2829bec.ngrok.io/api/v1/notifications/users/notify", params, new ResponseHandlerInterface() {
                @Override
                public void sendResponseMessage(HttpResponse response) throws IOException {

                }

                @Override
                public void sendStartMessage() {

                }

                @Override
                public void sendFinishMessage() {

                }

                @Override
                public void sendProgressMessage(long bytesWritten, long bytesTotal) {

                }

                @Override
                public void sendCancelMessage() {

                }

                @Override
                public void sendSuccessMessage(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void sendFailureMessage(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }

                @Override
                public void sendRetryMessage(int retryNo) {

                }

                @Override
                public URI getRequestURI() {
                    return null;
                }

                @Override
                public void setRequestURI(URI requestURI) {

                }

                @Override
                public Header[] getRequestHeaders() {
                    return new Header[0];
                }

                @Override
                public void setRequestHeaders(Header[] requestHeaders) {

                }

                @Override
                public boolean getUseSynchronousMode() {
                    return false;
                }

                @Override
                public void setUseSynchronousMode(boolean useSynchronousMode) {

                }

                @Override
                public boolean getUsePoolThread() {
                    return false;
                }

                @Override
                public void setUsePoolThread(boolean usePoolThread) {

                }

                @Override
                public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

                }

                @Override
                public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

                }

                @Override
                public Object getTag() {
                    return null;
                }

                @Override
                public void setTag(Object TAG) {

                }
            });

            ExecuteDetection();
        }
    }

    private void ExecuteDetection(){
        new Thread(new Runnable() {
            public void run() {
                HttpRequests httpRequests = new HttpRequests(ApiKey, ApiSecret);
                FaceHelper faceHelper;
                try {
                    faceHelper = new FaceHelper(httpRequests);
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                    return;
                }

                try {
                    faceInformationArray = faceHelper.GetInformationArray(ImgUrl);
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    lblStatus.post(new Runnable() {
                        @Override
                        public void run() {
                            lblStatus.setText("Faces cannot be detected due to slow connection.");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    lblStatus.post(new Runnable() {
                        @Override
                        public void run() {
                            lblStatus.setText("Faces cannot be detected due to slow connection.");
                        }
                    });
                }

                new DownloadImageTask(imageView, faceInformationArray)
                        .execute(ImgUrl);
            }
        }).start();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        FaceInformation[] faceInformationArray;

        public DownloadImageTask(ImageView bmImage, FaceInformation[] faceInformationArray) {
            this.bmImage = bmImage;
            this.faceInformationArray = faceInformationArray;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        private void AddFaceAreaToBitmap(Bitmap source, FaceInformation faceInformation){
            Paint p = new Paint();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(6);
            p.setAntiAlias(true);
            p.setFilterBitmap(true);
            p.setDither(true);
            p.setColor(Color.YELLOW);
            Canvas canvas = new Canvas(source);
            Double bitmapWidth = (double) source.getWidth();
            Double bitmapHeight = (double) source.getHeight();
            Rect faceArea = faceInformation.GetFaceArea(bitmapWidth, bitmapHeight);

            canvas.drawRect(faceArea, p);
        }

        protected void onPostExecute(Bitmap bitmap) {
            Bitmap drawableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            for (FaceInformation faceInformation :
                    faceInformationArray) {
                AddFaceAreaToBitmap(drawableBitmap, faceInformation);
            }
            bmImage.setImageBitmap(drawableBitmap);
            progressDialog.dismiss();
        }
    }
}
