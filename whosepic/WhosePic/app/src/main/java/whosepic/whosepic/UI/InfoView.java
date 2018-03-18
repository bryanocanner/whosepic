package whosepic.whosepic.UI;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import whosepic.whosepic.R;

/**
 * Created by ecemafacan on 18.03.2018.
 */

public class InfoView extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.infowindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.7));
    }
}
