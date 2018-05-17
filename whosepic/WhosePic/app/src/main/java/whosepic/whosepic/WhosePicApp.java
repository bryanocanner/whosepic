package whosepic.whosepic;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import whosepic.whosepic.AppCode.ImageLearner.SimilarityFinder;
import whosepic.whosepic.AppCode.ImageProcessor.FaceDetector;
import whosepic.whosepic.AppCode.ImageProcessor.ImageAnalyzer;
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
        ImageAnalyzer.createInstance(this);
        SimilarityFinder.createInstance();
    }
}
