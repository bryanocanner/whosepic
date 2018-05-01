package whosepic.whosepic.UI;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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
    private ImageView  ivContactImage;
    private GalleryAdapter galleryAdapter;
    private Person person;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        person = (Person) (getIntent().getExtras().getSerializable("Person"));
        nameView = (TextView) findViewById(R.id.contactName);
        numberView = (TextView) findViewById(R.id.phoneNumberView);
        nameView.setText(person.getContactName());
        numberView.setText(person.getContactNumber());
        ivContactImage = (ImageView) findViewById(R.id.contactImage);

        gridView = (GridView) findViewById(R.id.gridViewGallery);
        galleryAdapter = new GalleryAdapter(this.getApplicationContext());
        gridView.setAdapter(galleryAdapter);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (galleryAdapter.getImages() != null && !galleryAdapter.getImages().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), ImagePreviewActivity.class);
                    intent.putExtra("Image", (Image) galleryAdapter.getImages().get(position));
                    intent.putExtra("person",person);
                    startActivity(intent);
                }
            }
        });
        if (person.getContactImagePath() != null) {
            Uri uri = Uri.parse(person.getContactImagePath());
            ivContactImage.setImageURI(uri);
        } else {
            ivContactImage.setImageResource(R.drawable.default_profile);
            ivContactImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,person.getId()));
                    startActivity(intent);
                }
            });
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}