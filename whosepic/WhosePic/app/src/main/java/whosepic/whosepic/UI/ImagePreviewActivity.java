package whosepic.whosepic.UI;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import whosepic.whosepic.AppCode.ObjectModels.Album;
import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.AppManagers.ImageAdapter;
import whosepic.whosepic.DatabaseManager.DatabaseManager;
import whosepic.whosepic.R;
import whosepic.whosepic.UI.Views.SquareImageView;

/**
 * Created by aligunes on 11/03/2018.
 */

public class ImagePreviewActivity extends AppCompatActivity {
    private ImageView imageView;
    private Image image;
    ActionBar actionBar;
    Person person;
    boolean adding;
    DatabaseManager databaseManager = DatabaseManager.getInstance();
    ViewPager viewPager;
    ArrayList<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        image = (Image) (getIntent().getExtras().getSerializable("Image"));
        person = (Person) (getIntent().getExtras().getSerializable("person"));
        imageView = findViewById(R.id.previewedImage);
        imageView.setImageURI(Uri.parse(image.getPath()));
        adding = (boolean) (getIntent().getBooleanExtra("Adding",true));
       // imageView = findViewById(R.id.previewedImage);
        //imageView.setImageURI(Uri.parse(image.getPath()));

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getApplicationContext());

        viewPager.setAdapter(viewPagerAdapter);
        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
        });*/

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preview_menu, menu);
        if(adding)
            menu.getItem(0).setVisible(false);
        else
            menu.getItem(0).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_album:
                // User chose the "Settings" item, show the app settings UI...
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ImagePreviewActivity.this);
                builderSingle.setTitle("Choose the album");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ImagePreviewActivity.this, android.R.layout.select_dialog_item);
                final ArrayList<Album> albums = databaseManager.getDummyAlbums();
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
                        Album album = databaseManager.getAlbum(arrayAdapter.getItem(which));
                        boolean added = true;
                        if(album.getImages() != null) {
                            for (int i = 0; i < album.getImages().size(); i++) {
                                String s = album.getImages().get(i).getPath();
                                if (s.equals(image.getPath()))
                                    added = false;
                            }
                        }
                        if (added) {
                            databaseManager.setImageToAlbum(findAlbum(arrayAdapter.getItem(which), albums), image);
                            Toast.makeText(ImagePreviewActivity.this, "Picture is added to album", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ImagePreviewActivity.this, "Picture cant be added to album", Toast.LENGTH_LONG).show();
                        }
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private Album findAlbum(String name, ArrayList<Album> albums) {
        for(Album a : albums)
            if(a.getName().equals(name))
                return a;
        return null;
    }
}
