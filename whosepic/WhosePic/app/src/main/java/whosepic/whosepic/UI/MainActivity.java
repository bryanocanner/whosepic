package whosepic.whosepic.UI;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import whosepic.whosepic.R;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    Context context;
    public static final int RequestPermissionCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_main);
        /*
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
        if(!checkPermission()) {
            requestPermission();
        } else {
            startActivity(new Intent(MainActivity.this,InfoActivity.class));
        }
        */
        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_3);

        TextInsideCircleButton.Builder contactBuilder = new TextInsideCircleButton.Builder()
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(context , ContactsActivity.class);
                        startActivity(intent);
                    }
                });
        contactBuilder.normalImageRes(R.drawable.contacts);
        bmb.addBuilder(contactBuilder);

        TextInsideCircleButton.Builder albumsBuilder = new TextInsideCircleButton.Builder()
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(context, AlbumsActivity.class);
                        startActivity(intent);
                    }
                });
        albumsBuilder.normalImageRes(R.drawable.photogallery);
        bmb.addBuilder(albumsBuilder);

        TextInsideCircleButton.Builder infoBuilder = new TextInsideCircleButton.Builder()
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        startActivity(new Intent(MainActivity.this,InfoActivity.class));

                    }
                });
        infoBuilder.normalImageRes(R.drawable.about);
        bmb.addBuilder(infoBuilder);

        //goAlbums.setVisibility(View.VISIBLE);
        //goList.setVisibility(View.VISIBLE);
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        READ_EXTERNAL_STORAGE,
                        READ_CONTACTS
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        boolean ok = false;
        switch (requestCode) {
            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && ReadContactsPermission) {
                        ok = true;
                    }
                }

                break;
        }
        if(ok)
            startActivity(new Intent(MainActivity.this,InfoActivity.class));
        else {
            Toast.makeText(MainActivity.this,"You need to give all permissions",Toast.LENGTH_LONG).show();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

//    public  void requestReadStoragePermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted1");
//            } else {
//
//                Log.v(TAG,"Permission is revoked1");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted1");
//        }
//    }
//
//    protected void requestContactsPermission() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted1");
//            } else {
//
//                Log.v(TAG,"Permission is revoked1");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 2);
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted1");
//        }
//
//    }

}
