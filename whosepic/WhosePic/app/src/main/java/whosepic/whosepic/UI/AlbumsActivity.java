package whosepic.whosepic.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import whosepic.whosepic.AppCode.ObjectModels.Album;
import whosepic.whosepic.AppManagers.AlbumsAdapter;
import whosepic.whosepic.AppManagers.GalleryAdapter;
import whosepic.whosepic.DatabaseManager.DatabaseManager;
import whosepic.whosepic.R;

/**
 * Created by aligunes on 17/02/2018.
 */

public class AlbumsActivity extends AppCompatActivity {
    private RecyclerView gridView;
    private GridLayoutManager gridManager;
    private AlbumsAdapter gridAdapter;
    private TextView noAlbums;
    ArrayList<Album> albums;
    private String m_Text = "";
    DatabaseManager databaseManager = DatabaseManager.getInstance();
    ActionBar actionBar;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        noAlbums = (TextView) findViewById(R.id.no_albums);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        activity = this;
        gridView = (RecyclerView) findViewById(R.id.albums_grid);
        gridManager = new GridLayoutManager(this,2);
        gridView.setLayoutManager(gridManager);
        gridView.setHasFixedSize(true);
        albums = databaseManager.getDummyAlbums();
        gridAdapter = new AlbumsAdapter(this, albums, this);
        gridView.setAdapter(gridAdapter);
        noAlbums.setVisibility(gridView.getAdapter().getItemCount() > 0 ? View.GONE : View.VISIBLE);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Album Name");

// Set up the input
                final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNameAvailable(input.getText().toString())) {
                            Album album = new Album(input.getText().toString());
                            databaseManager.setAlbum(album);
                            albums.add(album);
                            gridAdapter.notifyDataSetChanged();
                            if(noAlbums.getVisibility() == View.VISIBLE)
                                noAlbums.setVisibility(View.GONE);
                            Toast.makeText(activity, "Album " + input.getText().toString() + " created", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(activity, "Album can not be created", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

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
    private boolean isNameAvailable(String name){
        boolean available = true;
        for (Album a : albums){
            if(a.getName().equals(name))
                available = false;
        }
        return available;
    }
}