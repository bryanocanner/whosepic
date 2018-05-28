package whosepic.whosepic.UI;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.ContactsContract;

import android.provider.MediaStore;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import whosepic.whosepic.AppCode.ImageLearner.SimilarityFinder;
import whosepic.whosepic.AppCode.ImageProcessor.ImageAnalyzer;
import whosepic.whosepic.AppCode.ObjectModels.Album;
import whosepic.whosepic.AppCode.ObjectModels.FaceInfo;
import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.AppManagers.GalleryAdapter;
import whosepic.whosepic.DatabaseManager.DatabaseManager;
import whosepic.whosepic.R;

/**
 * Created by aligunes on 17/02/2018.
 */

public class ImageOverviewActivity extends AppCompatActivity {
    private GridView gridView;
    private TextView nameView;
    private TextView numberView;
    private TextView text;
    private ImageView  ivContactImage;
    private GalleryAdapter galleryAdapter;
    private Person person;
    ActionBar actionBar;
    private ProgressDialog progress;
    private boolean isFirstTime;
    ArrayList<Image> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        person = (Person) (getIntent().getExtras().getSerializable("Person"));
        isFirstTime = true;
        nameView = (TextView) findViewById(R.id.contactName);
        numberView = (TextView) findViewById(R.id.phoneNumberView);
        text = (TextView) findViewById(R.id.text);
        nameView.setText(person.getContactName());
        numberView.setText(person.getContactNumber());
        text.setText("Photos that are similar to " + person.getContactName());

        ivContactImage = (ImageView) findViewById(R.id.contactImage);
        gridView = (GridView) findViewById(R.id.gridViewGallery);
        galleryAdapter = new GalleryAdapter(this,imageList);
        gridView.setAdapter(galleryAdapter);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (galleryAdapter.getImages() != null && !galleryAdapter.getImages().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), ImagePreviewActivity.class);
                    intent.putExtra("Image", (Image) galleryAdapter.getImages().get(position));
                    intent.putExtra("position", position);
                    intent.putExtra("person", person);
                    intent.putExtra("Adding", false);
                    intent.putExtra("images", imageList);
                    startActivity(intent);
                }
            }
        });
        ivContactImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (!person.getContactImagePath().equals("")) {
            Uri uri = Uri.parse(person.getContactImagePath());
            ivContactImage.setImageURI(uri);
        } else {
            ivContactImage.setImageResource(R.drawable.default_profile);
        }
        ivContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickProfilePicture();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        String newContactImagePath = DatabaseManager.getInstance().getProfilePhotoPath(person);
        boolean isPhotoChanged = !person.getContactImagePath().equals(newContactImagePath);
        if (isPhotoChanged) {
            isFirstTime = true;
            person.setContactImagePath(newContactImagePath);
            Uri uri = Uri.parse(person.getContactImagePath());
            ivContactImage.setImageURI(uri);
        }
        if (isFirstTime) {
            new HandleImageMapping(this).execute("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overview_menu, menu);
        menu.getItem(0).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_all_to_album:
                // User chose the "Settings" item, show the app settings UI...
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
                builderSingle.setTitle("Choose the album");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
                final ArrayList<Album> albums = DatabaseManager.getInstance().getDummyAlbums();
                for (Album a : albums)
                    arrayAdapter.add(a.getName());

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Album album = DatabaseManager.getInstance().getAlbum(arrayAdapter.getItem(which));
                        DatabaseManager.getInstance().setImagesToAlbum(album, imageList);
                        Toast.makeText(ImageOverviewActivity.this, "Photos are added to album", Toast.LENGTH_LONG).show();
                    }
                });
                builderSingle.show();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void clickProfilePicture() {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Would you like to change profile picture?");
        dlgAlert.setCancelable(true);
        dlgAlert.setNegativeButton("Cancel", null);
        dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), SimilarImagesActivity.class);
                intent.putExtra("Person", person);
                startActivity(intent);
            }
        });
        dlgAlert.create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getSimilarImagesPath(ArrayList<Image> list) {
        ArrayList<Image> listOfAllImages = new ArrayList<>();
        Image contactImage = new Image(person.getContactImagePath());
        FaceInfo contactFace = DatabaseManager.getInstance().getFaces(contactImage).get(0);
        ArrayList<Image> processedImages = DatabaseManager.getInstance().getProcessedImages();
        for (Image img : processedImages) {
            ArrayList<FaceInfo> faces = DatabaseManager.getInstance().getFaces(img);
            for (FaceInfo face : faces) {
                double d = SimilarityFinder.getInstance().getSimilarity(face, contactFace);
                if (d < 0.4) {
                    listOfAllImages.add(img);
                }
            }
        }
        list.clear();
        list.addAll(listOfAllImages);
    }

    private class HandleImageMapping extends AsyncTask<String, Integer, String> {

        private Context activity;

        public HandleImageMapping(Context context) {
            this.activity = context;
        }

        @Override
        protected String doInBackground(String... params) {
            getSimilarImagesPath(imageList);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
            isFirstTime = false;
            galleryAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(activity);
            progress.setTitle("Mapping");
            progress.setMessage("Wait for mapping...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == 0) {
                Log.d("Processed", "" + values[1]);
            } else {
                Log.d("Mapped", "" + values[1]);
            }

        }
    }
}