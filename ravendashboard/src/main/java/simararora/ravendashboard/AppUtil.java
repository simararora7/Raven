package simararora.ravendashboard;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by nateshrelhan on 3/15/18.
 */

public class AppUtil {

    public static boolean isEmptyOrNullString(String input) {
        if ((input != null) && (!input.trim().isEmpty())) {
            return false;
        } else {
            return true;
        }
    }
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
