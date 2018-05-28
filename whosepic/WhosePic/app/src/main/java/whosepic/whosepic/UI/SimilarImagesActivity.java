package whosepic.whosepic.UI;

/**
 * Created by emintosun on 1.05.2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.AppManagers.SimilarImagesAdapter;
import whosepic.whosepic.DatabaseManager.DatabaseManager;
import whosepic.whosepic.R;

public class SimilarImagesActivity extends AppCompatActivity {
    private GridView gridView;
    private TextView textView;
    private SimilarImagesAdapter adapter;
    private Activity activity;
    private Person person;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        setContentView(R.layout.activity_similar_images);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        person = (Person) (getIntent().getExtras().getSerializable("Person"));
        textView = (TextView) findViewById(R.id.textView);
        gridView = (GridView) findViewById(R.id.gridImages);
        textView.setText("Select a profile picture for " + person.getContactName());
        adapter = new SimilarImagesAdapter(this, getAllShownImagesPath(this));
        gridView.setAdapter(adapter);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (adapter.getImages() != null && !adapter.getImages().isEmpty()) {
                    person.setContactImagePath(adapter.getImages().get(position).getPath());
                    DatabaseManager.getInstance().setPerson(person);
                    activity.onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static ArrayList<Image> getAllShownImagesPath(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<Image> listOfAllImages = new ArrayList<Image>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = context.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(new Image(absolutePathOfImage));
        }
        return listOfAllImages;
    }

}