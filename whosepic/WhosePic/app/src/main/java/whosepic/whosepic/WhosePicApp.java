package whosepic.whosepic;

import android.app.Application;

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
        ArrayList<Image> allImagesInGallery = GalleryAdapter.getAllShownImagesPath(getApplicationContext());
        ArrayList<Image> allImagesInDb = DatabaseManager.getInstance().getAllImages();
        ArrayList<Image> allImagesInGalleryCopy = (ArrayList<Image>) allImagesInGallery.clone();
        allImagesInGallery.removeAll(allImagesInDb);
        allImagesInDb.removeAll(allImagesInGalleryCopy);
        DatabaseManager.getInstance().setImageList(allImagesInGallery);
        DatabaseManager.getInstance().deleteImageList(allImagesInDb);
    }
}
