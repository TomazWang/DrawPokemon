package idv.tomazwang.app.pokemondraw;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by TomazWang on 2016/9/5.
 */

public class Utils {

    private static final int MARGIN_PX = 80;
    private static final float BORDER_THICK = 10;

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    // bmpA = pokemon pic, bmpB = use drawing.
    public static Bitmap combineImage(Bitmap bmpA, Bitmap bmpB){
        Log.d(TAG, "combineImage: checking if null");

        if(bmpA == null && bmpB != null){
            Log.d(TAG, "combineImage: bmpA == null");
            return bmpB;
        }


        if(bmpB == null && bmpA != null){
            Log.d(TAG, "combineImage: bmpB == null");
            return bmpA;
        }


        if(bmpA == null && bmpB == null){
            Log.d(TAG, "combineImage: both == null");
            return null;
        }

        Bitmap bmpCombine;

        int width, height = 0;

        float ya, yb, xa, xb;

        Log.d(TAG, String.format("combineImage: aW=%d, aH=%d; bW=%d, bH=%d", bmpA.getWidth(), bmpA.getHeight(), bmpB.getWidth(), bmpB.getHeight()));


        height = bmpB.getHeight() + bmpA.getHeight() + 3*MARGIN_PX;
        width = bmpB.getWidth() + 2*MARGIN_PX;

        xb = MARGIN_PX;
        yb = MARGIN_PX;

        xa = width - MARGIN_PX + BORDER_THICK - bmpA.getWidth();
        ya = MARGIN_PX*2 + bmpB.getHeight();

        bmpCombine = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImg = new Canvas(bmpCombine);

        comboImg.drawColor(Color.WHITE, PorterDuff.Mode.DST_OVER);

        Paint borderColor = new Paint();
        borderColor.setColor(Color.BLACK);

        comboImg.drawRect(
                xb-BORDER_THICK,
                yb-BORDER_THICK,
                MARGIN_PX+bmpB.getWidth()+ BORDER_THICK,
                MARGIN_PX+bmpB.getHeight()+ BORDER_THICK ,
                borderColor);


        comboImg.drawBitmap(bmpA, xa, ya, null);
        comboImg.drawBitmap(bmpB, xb, yb, null);

        return bmpCombine;

    }


}

