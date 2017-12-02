package whosepic.whosepic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;





public class MainActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        Button goList = (Button) findViewById(R.id.goListButton);
        Button goAlbums = (Button) findViewById(R.id.goAlbumsButton);
        goList.setText("ContactList");
        goAlbums.setText("Albums");
        goList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , AllContacts.class);
                startActivity(intent);
            }
        });
        goAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumsActivity.class);
                startActivity(intent);
            }
        });
    }
}
