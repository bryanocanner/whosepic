package whosepic.whosepic.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.AppManagers.ImageAdapter;
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_album:
                // User chose the "Settings" item, show the app settings UI...
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
}
