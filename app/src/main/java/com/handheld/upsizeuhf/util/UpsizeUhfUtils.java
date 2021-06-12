package com.handheld.upsizeuhf.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class UpsizeUhfUtils {
    private static Typeface fontKanitBlack;
    private static Typeface fontKanitBlackItalic;
    private static Typeface fontKanitBold;
    private static Typeface fontKanitBoldItalic;
    private static Typeface fontKanitExtraBold;
    private static Typeface fontKanitExtraBoldItalic;
    private static Typeface fontKanitExtraLight;
    private static Typeface fontKanitExtraLightItalic;
    private static Typeface fontKanitItalic;
    private static Typeface fontKanitLight;
    private static Typeface fontKanitLightItalic;
    private static Typeface fontKanitMedium;
    private static Typeface fontKanitMediumItalic;
    private static Typeface fontKanitRegular;
    private static Typeface fontKanitSemiBold;
    private static Typeface fontKanitSemiBoldItalic;
    private static Typeface fontKanitThin;
    private static Typeface fontKanitThinItalic;

    public static void loadFonts(AssetManager assetManager) {
        fontKanitBlack = Typeface.createFromAsset(assetManager, "fonts/Kanit-Black.ttf");
        fontKanitBlackItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-BlackItalic.ttf");
        fontKanitBold = Typeface.createFromAsset(assetManager, "fonts/Kanit-Bold.ttf");
        fontKanitBoldItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-BoldItalic.ttf");
        fontKanitExtraBold = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraBold.ttf");
        fontKanitExtraBoldItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraBoldItalic.ttf");
        fontKanitExtraLight = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraLight.ttf");
        fontKanitExtraLightItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraLightItalic.ttf");
        fontKanitItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-Italic.ttf");
        fontKanitLight = Typeface.createFromAsset(assetManager, "fonts/Kanit-Light.ttf");
        fontKanitLightItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-LightItalic.ttf");
        fontKanitMedium = Typeface.createFromAsset(assetManager, "fonts/Kanit-Medium.ttf");
        fontKanitMediumItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-MediumItalic.ttf");
        fontKanitRegular = Typeface.createFromAsset(assetManager, "fonts/Kanit-Regular.ttf");
        fontKanitSemiBold = Typeface.createFromAsset(assetManager, "fonts/Kanit-SemiBold.ttf");
        fontKanitSemiBoldItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-SemiBoldItalic.ttf");
        fontKanitThin = Typeface.createFromAsset(assetManager, "fonts/Kanit-Thin.ttf");
        fontKanitThinItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-ThinItalic.ttf");
    }

    public static Typeface getFontKanitBlack() {
        return fontKanitBlack;
    }

    public static Typeface getFontKanitBlackItalic() {
        return fontKanitBlackItalic;
    }

    public static Typeface getFontKanitBold() {
        return fontKanitBold;
    }

    public static Typeface getFontKanitBoldItalic() {
        return fontKanitBoldItalic;
    }

    public static Typeface getFontKanitExtraBold() {
        return fontKanitExtraBold;
    }

    public static Typeface getFontKanitExtraBoldItalic() {
        return fontKanitExtraBoldItalic;
    }

    public static Typeface getFontKanitExtraLight() {
        return fontKanitExtraLight;
    }

    public static Typeface getFontKanitExtraLightItalic() {
        return fontKanitExtraLightItalic;
    }

    public static Typeface getFontKanitItalic() {
        return fontKanitItalic;
    }

    public static Typeface getFontKanitLight() {
        return fontKanitLight;
    }

    public static Typeface getFontKanitLightItalic() {
        return fontKanitLightItalic;
    }

    public static Typeface getFontKanitMedium() {
        return fontKanitMedium;
    }

    public static Typeface getFontKanitMediumItalic() {
        return fontKanitMediumItalic;
    }

    public static Typeface getFontKanitRegular() {
        return fontKanitRegular;
    }

    public static Typeface getFontKanitSemiBold() {
        return fontKanitSemiBold;
    }

    public static Typeface getFontKanitSemiBoldItalic() {
        return fontKanitSemiBoldItalic;
    }

    public static Typeface getFontKanitThin() {
        return fontKanitThin;
    }

    public static Typeface getFontKanitThinItalic() {
        return fontKanitThinItalic;
    }

}
