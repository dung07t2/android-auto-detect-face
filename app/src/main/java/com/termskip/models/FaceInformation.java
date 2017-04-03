package com.termskip.models;

import android.graphics.Rect;

/**
 * Created by tntmr on 11/5/2016.
 */
public class FaceInformation {
    public PointFloat Center;
    public Double Height;
    public Double Width;
    public String FaceId;

    public FaceInformation(PointFloat center, Double height, Double width, String faceId){
        Center = center;
        Height = height;
        Width = width;
        FaceId = faceId;
    }

    public Rect GetFaceArea(Double imageWidth, Double imageHeight) {
        double centerX = Center.X / 100 * imageWidth;
        double centerY = Center.Y / 100 * imageHeight;
        double areaWidth = Width / 100 * imageWidth;
        double areaHeight = Height / 100 * imageHeight;
        double left = centerX - areaWidth / 2;
        double top = centerY - areaHeight / 2;
        double right = left + areaWidth;
        double bottom = top + areaHeight;

        return new Rect((int) left, (int) top, (int) right, (int) bottom);
    }
}
