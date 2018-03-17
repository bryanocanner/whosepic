package whosepic.whosepic.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import whosepic.whosepic.R;
import whosepic.whosepic.UI.AlbumsActivity;
import whosepic.whosepic.UI.ContactsActivity;


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
        goList.setTextSize(22);
        goList.setBackgroundColor(Color.parseColor("#ffffff"));
        goList.setTextColor(Color.parseColor("#3B3264"));
        goAlbums.setText("Albums");
        goAlbums.setTextSize(22);
        goAlbums.setBackgroundColor(Color.parseColor("#ffffff"));
        goList.setTextColor(Color.parseColor("#3B3264"));
        goList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , ContactsActivity.class);
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

        startActivity(new Intent(MainActivity.this,InfoView.class));

    }
}
