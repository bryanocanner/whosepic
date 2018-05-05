package whosepic.whosepic.UI;

/**
 * Created by emintosun on 30.04.2018.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import whosepic.whosepic.AppCode.ObjectModels.Album;
import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.AppManagers.AlbumAdapter;
import whosepic.whosepic.AppManagers.GalleryAdapter;
import whosepic.whosepic.DatabaseManager.DatabaseManager;
import whosepic.whosepic.R;

/**
 * Created by aligunes on 17/02/2018.
 */

public class AlbumActivity extends AppCompatActivity {
    Context context;
    private GridView gridView;
    private TextView nameView;
    private AlbumAdapter albumAdapter;
    ActionBar actionBar;
    Album album;
    DatabaseManager databaseManager = DatabaseManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        nameView = (TextView) findViewById(R.id.albumName2);
        album = (Album)(getIntent().getSerializableExtra("Album"));
        nameView.setText(album.getName());
        context = this;

        gridView = (GridView) findViewById(R.id.gridViewAlbum);
        album.setImages(databaseManager.getAlbum(album.getName()).getImages());
        albumAdapter = new AlbumAdapter(this, album.getImages());
        gridView.setAdapter(albumAdapter);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (albumAdapter.getImages() != null && !albumAdapter.getImages().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), ImagePreviewActivity.class);
                    intent.putExtra("Image", (Image) albumAdapter.getImages().get(position));
                    intent.putExtra("Adding", true);
                    intent.putExtra("images",album.getImages());
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.album_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                databaseManager.deleteAlbum(album);
                Intent intent = new Intent(context,AlbumsActivity.class);
                context.startActivity(intent);

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