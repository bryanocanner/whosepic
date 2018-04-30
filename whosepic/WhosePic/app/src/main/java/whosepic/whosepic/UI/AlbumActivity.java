package whosepic.whosepic.UI;

/**
 * Created by emintosun on 30.04.2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

        gridView = (GridView) findViewById(R.id.gridViewAlbum);
        albumAdapter = new AlbumAdapter(this, databaseManager.getAlbum(album.getName()).getImages());
        gridView.setAdapter(albumAdapter);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (albumAdapter.getImages() != null && !albumAdapter.getImages().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), ImagePreviewActivity.class);
                    intent.putExtra("Image", (Image) albumAdapter.getImages().get(position));
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}