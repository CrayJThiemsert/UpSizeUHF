package com.handheld.upsizeuhf.util

import android.content.res.AssetManager
import android.graphics.Typeface

abstract class UhfUtils {
    companion object {
        private var TAG : String = "UhfUtils"

        var fontKanitBlack: Typeface? = null
            private set
        var fontKanitBlackItalic: Typeface? = null
            private set
        var fontKanitBold: Typeface? = null
            private set
        var fontKanitBoldItalic: Typeface? = null
            private set
        var fontKanitExtraBold: Typeface? = null
            private set
        var fontKanitExtraBoldItalic: Typeface? = null
            private set
        var fontKanitExtraLight: Typeface? = null
            private set
        var fontKanitExtraLightItalic: Typeface? = null
            private set
        var fontKanitItalic: Typeface? = null
            private set
        var fontKanitLight: Typeface? = null
            private set
        var fontKanitLightItalic: Typeface? = null
            private set
        var fontKanitMedium: Typeface? = null
            private set
        var fontKanitMediumItalic: Typeface? = null
            private set
        var fontKanitRegular: Typeface? = null
            private set
        var fontKanitSemiBold: Typeface? = null
            private set
        var fontKanitSemiBoldItalic: Typeface? = null
            private set
        var fontKanitThin: Typeface? = null
            private set
        var fontKanitThinItalic: Typeface? = null
            private set

        fun loadFonts(assetManager: AssetManager?) {
            fontKanitBlack = Typeface.createFromAsset(assetManager, "fonts/Kanit-Black.ttf")
            fontKanitBlackItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-BlackItalic.ttf")
            fontKanitBold = Typeface.createFromAsset(assetManager, "fonts/Kanit-Bold.ttf")
            fontKanitBoldItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-BoldItalic.ttf")
            fontKanitExtraBold = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraBold.ttf")
            fontKanitExtraBoldItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraBoldItalic.ttf")
            fontKanitExtraLight = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraLight.ttf")
            fontKanitExtraLightItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraLightItalic.ttf")
            fontKanitItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-Italic.ttf")
            fontKanitLight = Typeface.createFromAsset(assetManager, "fonts/Kanit-Light.ttf")
            fontKanitLightItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-LightItalic.ttf")
            fontKanitMedium = Typeface.createFromAsset(assetManager, "fonts/Kanit-Medium.ttf")
            fontKanitMediumItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-MediumItalic.ttf")
            fontKanitRegular = Typeface.createFromAsset(assetManager, "fonts/Kanit-Regular.ttf")
            fontKanitSemiBold = Typeface.createFromAsset(assetManager, "fonts/Kanit-SemiBold.ttf")
            fontKanitSemiBoldItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-SemiBoldItalic.ttf")
            fontKanitThin = Typeface.createFromAsset(assetManager, "fonts/Kanit-Thin.ttf")
            fontKanitThinItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-ThinItalic.ttf")
        }


//        fun loadFonts(assetManager: AssetManager) {
//            fontKanitBlack = Typeface.createFromAsset(assetManager, "fonts/Kanit-Black.ttf")
//            fontKanitBlackItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-BlackItalic.ttf")
//            fontKanitBold = Typeface.createFromAsset(assetManager, "fonts/Kanit-Bold.ttf")
//            fontKanitBoldItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-BoldItalic.ttf")
//            fontKanitExtraBold = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraBold.ttf")
//            fontKanitExtraBoldItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraBoldItalic.ttf")
//            fontKanitExtraLight = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraLight.ttf")
//            fontKanitExtraLightItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-ExtraLightItalic.ttf")
//            fontKanitItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-Italic.ttf")
//            fontKanitLight = Typeface.createFromAsset(assetManager, "fonts/Kanit-Light.ttf")
//            fontKanitLightItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-LightItalic.ttf")
//            fontKanitMedium = Typeface.createFromAsset(assetManager, "fonts/Kanit-Medium.ttf")
//            fontKanitMediumItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-MediumItalic.ttf")
//            fontKanitRegular = Typeface.createFromAsset(assetManager, "fonts/Kanit-Regular.ttf")
//            fontKanitSemiBold = Typeface.createFromAsset(assetManager, "fonts/Kanit-SemiBold.ttf")
//            fontKanitSemiBoldItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-SemiBoldItalic.ttf")
//            fontKanitThin = Typeface.createFromAsset(assetManager, "fonts/Kanit-Thin.ttf")
//            fontKanitThinItalic = Typeface.createFromAsset(assetManager, "fonts/Kanit-ThinItalic.ttf")
//        }
    }

}
