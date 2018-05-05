package whosepic.whosepic.DatabaseManager;

import android.app.Application;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import whosepic.whosepic.AppCode.ObjectModels.*;
import whosepic.whosepic.AppCode.RealmObjectModels.*;

/**
 * Created by aligunes on 11/03/2018.
 */

public class DatabaseManager {
    private Realm realm;
    private static DatabaseManager dbManager;

    private DatabaseManager(Application app) {
        Realm.init(app);
        RealmConfiguration configuration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);
        realm = Realm.getDefaultInstance();
    }

    public static void createInstance(Application app) {
        dbManager = new DatabaseManager(app);
    }

    public static DatabaseManager getInstance() {
        return dbManager;
    }

    public void setAlbum(Album album) {
        realm.beginTransaction();
        RealmAlbum ra = realm.createObject(RealmAlbum.class);
        ra.setId(generateId(RealmAlbum.class));
        ra.setAlbum(album);
        realm.commitTransaction();
    }

    public void setImage(Image img) {
        realm.beginTransaction();
        RealmImage ri = realm.createObject(RealmImage.class);
        ri.setId(generateId(RealmImage.class));
        ri.setImage(img);
        realm.commitTransaction();
    }

    public void deleteImage(Image img) {
        realm.beginTransaction();
        RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
        ri.deleteFromRealm();
        realm.commitTransaction();
    }

    public void setImageToAlbum(Album album, Image img) {
        RealmAlbum ra = realm.where(RealmAlbum.class).equalTo("name", album.getName()).findFirst();
        RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
        realm.beginTransaction();
        RealmAlbumImage rai = realm.createObject(RealmAlbumImage.class);
        rai.setAlbumId(ra.getId());
        rai.setImageId(ri.getId());
        realm.commitTransaction();
    }

    public void setPerson(Person person) {

    }

    public ArrayList<Album> getDummyAlbums() {
        ArrayList<Album> albums = new ArrayList<Album>();
        RealmResults<RealmAlbum> realmAlbums = realm.where(RealmAlbum.class).findAll();
        for (RealmAlbum ra : realmAlbums) {
            albums.add(new Album(ra.getName()));
        }
        return albums;
    }

    public Album getAlbum(String name) {
        ArrayList<Image> images = new ArrayList<Image>();
        RealmAlbum ra = realm.where(RealmAlbum.class).equalTo("name", name).findFirst();
        RealmResults<RealmAlbumImage> rais = realm.where(RealmAlbumImage.class).equalTo("albumId", ra.getId()).findAll();
        for (RealmAlbumImage rai : rais) {
            RealmImage img = realm.where(RealmImage.class).equalTo("id", rai.getImageId()).findFirst();
            images.add(new Image(img.getPath()));
        }
        return new Album(ra.getName(), images);
    }

    public ArrayList<Image> getImagesOfPerson() {
        return null;
    }

    public int generateId(Class c) {
        Number currentIdNum = realm.where(c).max("id");
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    public ArrayList<Image> getAllImages() {
        RealmResults<RealmImage> images = realm.where(RealmImage.class).findAll();
        ArrayList<Image> allImages = new ArrayList<Image>();
        for (RealmImage ri : images) {
            allImages.add(new Image(ri.getPath()));
        }
        return allImages;
    }

    public void setImageList(ArrayList<Image> images) {
        for (Image img : images) {
            setImage(img);
        }
    }

    public void deleteImageList(ArrayList<Image> images) {
        for (Image img : images) {
            deleteImage(img);
        }
    }

    public void deleteAlbum(Album album){
        realm.beginTransaction();
        RealmAlbum ra = realm.where(RealmAlbum.class).equalTo("name", album.getName()).findFirst();
        RealmResults<RealmAlbumImage> rai = realm.where(RealmAlbumImage.class).equalTo("albumId", ra.getId()).findAll();
        ra.deleteFromRealm();
        rai.deleteAllFromRealm();
        realm.commitTransaction();
    }
}
