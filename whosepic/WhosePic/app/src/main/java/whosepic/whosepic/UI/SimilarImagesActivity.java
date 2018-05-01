package whosepic.whosepic.UI;

/**
 * Created by emintosun on 1.05.2018.
 */

import android.content.Intent;
import android.os.Bundle;
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
import whosepic.whosepic.R;

public class SimilarImagesActivity extends AppCompatActivity {
    private GridView gridView;
    private TextView textView;
    private SimilarImagesAdapter adapter;
    private Person person;
    ActionBar actionBar;
    ArrayList<Image> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_images);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        person = (Person) (getIntent().getExtras().getSerializable("person"));
        imageList = (ArrayList<Image>)(getIntent().getExtras().getSerializable("Images"));
        textView = (TextView) findViewById(R.id.textView);
        gridView = (GridView) findViewById(R.id.gridImages);
        String s = person.getContactName() + "'s photos.";
        textView.setText(s);
        adapter = new SimilarImagesAdapter(this,imageList);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                    if (adapter.getImages() != null && !adapter.getImages().isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), ImagePreviewActivity.class);
                        intent.putExtra("Image", (Image) adapter.getImages().get(position));
                        intent.putExtra("person", person);
                        intent.putExtra("Adding", false);
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