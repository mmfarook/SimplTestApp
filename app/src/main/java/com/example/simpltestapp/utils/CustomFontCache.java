package com.example.simpltestapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class CustomFontCache {
    private static final String TAG = "CustomFontCache";

    private static final Map<String, Typeface> name2Typeface = new HashMap<>();

    public static Typeface lookForTypeface(Context ctx, String fontName) {
        try {
            if (fontName == null)
                return null;
            Typeface loadedInstance = name2Typeface.get(fontName);
            if (loadedInstance == null) {
                String assetName = fontName;
                for (; ; ) {
                    try {
                        loadedInstance = Typeface.createFromAsset(ctx.getAssets(), assetName);
                        if (loadedInstance != null) {
                            name2Typeface.put(fontName, loadedInstance);
                            break;
                        }
                    } catch (Throwable ex) {
                        //probably asset not found
                        Log.e(TAG, "Asset not found: " + ex.getMessage());
                    }
                    if (loadedInstance == null) {
                        if (!assetName.endsWith(".ttf")) {
                            assetName = fontName + ".ttf";
                        } else
                            break;
                    }
                }
            }
            return loadedInstance;
        } catch (Exception e) {
            Log.e(TAG, "Custom font not found: " + e.getMessage());
            return null;
        }
    }
}
