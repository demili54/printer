package com.dantsu.thermalprinter;


import android.graphics.Bitmap;
import android.widget.TextView;

public class TextToImageService {

    private TextView textViewL;
    private TextView textViewR;


    public TextToImageService(TextView textViewL,TextView textViewR) {
        this.textViewL = textViewL;
        this.textViewR = textViewR;
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
        textViewR.setText(text);
        textViewR.setDrawingCacheEnabled(true);
        textViewR.destroyDrawingCache();
        textViewR.buildDrawingCache();
        Bitmap bitmap = getTransparentBitmapCopy(textViewR.getDrawingCache());
        return bitmap;
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
}
