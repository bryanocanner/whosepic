package whosepic.whosepic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by aligunes on 17/02/2018.
 */

public class GalleryViewActivity extends AppCompatActivity {
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ArrayList<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this,images,this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty())
                    Toast.makeText(
                            getApplicationContext(),
                            "position " + position + " " + images.get(position),
                            Toast.LENGTH_LONG).show();
                ;

            }
        });

    }

    // Prepare some dummy data for gridview
//    private ArrayList<Bitmap> getData() {
//        final ArrayList<Bitmap> imageItems = new ArrayList<>();
//        //TypedArray imgs = getResources().obtainTypedArray(R.mipmap.ic_launcher);
//        int a = (int)(Math.random() * 15);
//        for (int i = 0; i < a; i++) {//imgs.length(); i++) {
//            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);;
//            imageItems.add(bitmap);
//        }
//        return imageItems;
//    }
}
//public class GallarySample extends Activity {
//
//    /** The images. */
//    private ArrayList<String> images;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.gallery_activity);
//
//        GridView gallery = (GridView) findViewById(R.id.galleryGridView);
//
//        gallery.setAdapter(new ImageAdapter(this));
//
//        gallery.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,
//                                    int position, long arg3) {
//                if (null != images && !images.isEmpty())
//                    Toast.makeText(
//                            getApplicationContext(),
//                            "position " + position + " " + images.get(position),
//                            300).show();
//                ;
//
//            }
//        });
//
//    }
