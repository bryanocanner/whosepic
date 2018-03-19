package whosepic.whosepic.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.AppManagers.ImageAdapter;
import whosepic.whosepic.R;

/**
 * Created by aligunes on 11/03/2018.
 */

public class ImagePreviewActivity extends AppCompatActivity {
    private ImageView imageView;
    private Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        image = (Image) (getIntent().getExtras().getSerializable("Image"));
        imageView = (ImageView) findViewById(R.id.previewedImage);
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
}
