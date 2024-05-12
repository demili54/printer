package com.dantsu.thermalprinter;


import android.graphics.Bitmap;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class TextToImageService {

    private TextView textViewL;
    private TextView textViewR;
    private TextView textViewC;
    private TextView imageTempLineFeed;

    private PrinterSettings printerSettings;


    public TextToImageService(TextView textViewL, TextView textViewR, TextView textViewC,TextView imageTempLineFeed,PrinterSettings printerSettings) {
        this.textViewL = textViewL;
        this.textViewR = textViewR;
        this.textViewC = textViewC;
        this.imageTempLineFeed = imageTempLineFeed;
        this.printerSettings = printerSettings;
    }

    public Bitmap createImageL(String text){
        textViewL.setText(text);
        textViewL.setDrawingCacheEnabled(true);
        textViewL.destroyDrawingCache();
        textViewL.buildDrawingCache();
        Bitmap bitmap = getTransparentBitmapCopy(textViewL.getDrawingCache());
        return bitmap;
    }
    public Bitmap createImageR(String text){
        textViewR.setText("  "+text);
        textViewR.setDrawingCacheEnabled(true);
        textViewR.destroyDrawingCache();
        textViewR.buildDrawingCache();
        Bitmap bitmap = getTransparentBitmapCopy(textViewR.getDrawingCache());
        return bitmap;
    }
    public Bitmap createImageC(String text){
        textViewC.setText("            "+text);
        textViewC.setDrawingCacheEnabled(true);
        textViewC.destroyDrawingCache();
        textViewC.buildDrawingCache();
        Bitmap bitmap = getTransparentBitmapCopy(textViewC.getDrawingCache());
        return bitmap;
    }
    public Bitmap createImageAuto(String text){
        if(printerSettings.getCheckA()&&printerSettings.getCheckB()){
            return this.createImageR(text);
        }else{
            return this.createImageC(text);
        }
    }
    private Bitmap getTransparentBitmapCopy(Bitmap source)
    {
        int width =  source.getWidth();
        int height = source.getHeight();
        Bitmap copy = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[width * height];
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        copy.setPixels(pixels, 0, width, 0, 0, width, height);
        return copy;
    }

    public Bitmap createImageLineFeed(){
        imageTempLineFeed.setText(" ");
        imageTempLineFeed.setDrawingCacheEnabled(true);
        imageTempLineFeed.destroyDrawingCache();
        imageTempLineFeed.buildDrawingCache();
        Bitmap bitmap = getTransparentBitmapCopy(imageTempLineFeed.getDrawingCache());
        return bitmap;
    }



}
