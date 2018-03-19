package whosepic.whosepic.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.AppManagers.GalleryAdapter;
import whosepic.whosepic.R;

/**
 * Created by aligunes on 17/02/2018.
 */

public class ImageOverviewActivity extends AppCompatActivity {
    private GridView gridView;
    private TextView nameView;
    private TextView numberView;
    private GalleryAdapter galleryAdapter;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        person = (Person) (getIntent().getExtras().getSerializable("Person"));
        nameView = (TextView) findViewById(R.id.contactNameView);
        numberView = (TextView) findViewById(R.id.phoneNumberView);
        nameView.setText(person.getContactName());
        numberView.setText(person.getContactNumber());

        gridView = (GridView) findViewById(R.id.gridViewGallery);
        galleryAdapter = new GalleryAdapter(this.getApplicationContext());
        gridView.setAdapter(galleryAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (galleryAdapter.getImages() != null && !galleryAdapter.getImages().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), ImagePreviewActivity.class);
                    intent.putExtra("Image", (Image) galleryAdapter.getImages().get(position));
                    startActivity(intent);
                }
            }
        });

    }
}