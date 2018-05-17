package whosepic.whosepic.UI;


import android.content.ContentUris;

import android.content.Context;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;

import android.provider.MediaStore;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    ActionMode actionMode;
    ArrayList<Image> imageList = new ArrayList<>();
    ArrayList<Image> multiselect_list = new ArrayList<>();
    boolean isMultiSelect = false;
    Menu context_menu;
    ViewPager viewPager;

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
        imageList = getAllShownImagesPath(this);

        gridView = (GridView) findViewById(R.id.gridViewGallery);
        galleryAdapter = new GalleryAdapter(this.getApplicationContext(),imageList,multiselect_list);
        gridView.setAdapter(galleryAdapter);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (isMultiSelect) {
                    multi_select(position);
                    /*if (multiselect_list.size() == 0) {
                        mActionModeCallback.onDestroyActionMode(actionMode);
                        context_menu = null;
                    }*/
                } else {
                    if (galleryAdapter.getImages() != null && !galleryAdapter.getImages().isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), ImagePreviewActivity.class);
                        intent.putExtra("Image", (Image) galleryAdapter.getImages().get(position));
                        intent.putExtra("position",position);
                        intent.putExtra("person", person);
                        intent.putExtra("Adding", false);
                        intent.putExtra("images",imageList);
                        startActivity(intent);
                    }
                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<Image>();
                    isMultiSelect = true;

                    if (actionMode == null) {
                        actionMode = startActionMode(mActionModeCallback);
                    }
                }

                //multi_select(i);
                return false;
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

    public void multi_select(int position) {
        if (actionMode != null) {
            if (multiselect_list.contains(imageList.get(position)))
                multiselect_list.remove(imageList.get(position));
            else
                multiselect_list.add(imageList.get(position));

            if (multiselect_list.size() > 0)
                actionMode.setTitle("" + multiselect_list.size());
            else
                actionMode.setTitle("");

            refreshAdapter();

        }
    }

    public void refreshAdapter()
    {
        galleryAdapter.selectedImages = multiselect_list;
        galleryAdapter.images = imageList;
        galleryAdapter.notifyDataSetChanged();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_ok:
                    actionMode = null;
                    isMultiSelect = false;
                    refreshAdapter();
                    Intent intent = new Intent(getApplicationContext(), SimilarImagesActivity.class);
                    intent.putExtra("Images", multiselect_list);
                    intent.putExtra("person", person);
                    startActivity(intent);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Image>();
            refreshAdapter();
        }
    };

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