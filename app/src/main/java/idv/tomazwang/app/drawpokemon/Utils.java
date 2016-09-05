package idv.tomazwang.app.drawpokemon;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by TomazWang on 2016/9/5.
 */

public class Utils {

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }



}
