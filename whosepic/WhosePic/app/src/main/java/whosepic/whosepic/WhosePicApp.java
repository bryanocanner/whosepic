package whosepic.whosepic;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.AppManagers.GalleryAdapter;
import whosepic.whosepic.DatabaseManager.DatabaseManager;

/**
 * Created by aligunes on 07/04/2018.
 */

public class WhosePicApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseManager.createInstance(this);
        checkImages();
    }

    public void checkImages() {
        ArrayList<Image> allImagesInGallery = getAllShownImagesPath(getApplicationContext());
        ArrayList<Image> allImagesInDb = DatabaseManager.getInstance().getAllImages();
        ArrayList<Image> allImagesInGalleryCopy = (ArrayList<Image>) allImagesInGallery.clone();
        allImagesInGallery.removeAll(allImagesInDb);
        allImagesInDb.removeAll(allImagesInGalleryCopy);
        DatabaseManager.getInstance().setImageList(allImagesInGallery);
        DatabaseManager.getInstance().deleteImageList(allImagesInDb);
    }

    public static ArrayList<Image> getAllShownImagesPath(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<Image> listOfAllImages = new ArrayList<Image>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = context.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(new Image(absolutePathOfImage));
        }
        return listOfAllImages;
    }
}
