package whosepic.whosepic.DatabaseManager;

import android.app.Application;

import java.sql.ResultSet;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
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
    }

    public static void createInstance(Application app) {
        dbManager = new DatabaseManager(app);
    }

    public static DatabaseManager getInstance() {
        dbManager.realm = Realm.getDefaultInstance();
        return dbManager;
    }

    public void setAlbum(Album album) {
        realm.beginTransaction();
        RealmAlbum ra = realm.createObject(RealmAlbum.class);
        ra.setId(generateId(RealmAlbum.class));
        ra.setAlbum(album);
        realm.commitTransaction();
        realm.close();
    }

    public boolean checkImage(Image img) {
        realm.beginTransaction();
        RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
        if (ri == null) {
            realm.close();
            return false;
        }
        realm.close();
        return true;
    }

    public void setImage(Image img, boolean isProcessed, boolean isMapped) {
        realm.beginTransaction();
        RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
        if (ri == null) {
            ri = new RealmImage();
            ri.setId(generateId(RealmImage.class));
        }
        ri.setImage(img);
        ri.setProcessed(isProcessed);
        ri.setMapped(isMapped);
        realm.insertOrUpdate(ri);
        realm.commitTransaction();
        realm.close();
    }

    public void deleteImage(Image img) {
        realm.beginTransaction();
        RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
        ri.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public void setImageToAlbum(Album album, Image img) {
        RealmAlbum ra = realm.where(RealmAlbum.class).equalTo("name", album.getName()).findFirst();
        RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
        realm.beginTransaction();
        RealmAlbumImage rai = realm.createObject(RealmAlbumImage.class);
        rai.setAlbumId(ra.getId());
        rai.setImageId(ri.getId());
        realm.commitTransaction();
        realm.close();
    }

    public void setImagesToAlbum(Album album, ArrayList<Image> imgs) {
        RealmAlbum ra = realm.where(RealmAlbum.class).equalTo("name", album.getName()).findFirst();
        realm.beginTransaction();
        for (Image img : imgs) {
            RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
            RealmAlbumImage rai = realm.where(RealmAlbumImage.class).equalTo("imageId", ri.getId())
                    .and().equalTo("albumId", ra.getId()).findFirst();
            if (rai == null) {
                rai = new RealmAlbumImage();
                rai.setAlbumId(ra.getId());
                rai.setImageId(ri.getId());
                realm.insertOrUpdate(rai);
            }
        }
        realm.commitTransaction();
        realm.close();
    }

    public void setPerson(Person person) {
        realm.beginTransaction();
        RealmPerson rp = realm.where(RealmPerson.class).equalTo("phoneNumber", person.getContactNumber()).findFirst();
        if (rp == null) {
            rp = new RealmPerson();
            rp.setId(generateId(RealmImage.class));
        }
        rp.setPhoneNumber(person.getContactNumber());
        rp.setPath(person.getContactImagePath());
        realm.insertOrUpdate(rp);
        realm.commitTransaction();
        realm.close();
    }

    public void setFace(FaceInfo face, Image img) {
        RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
        realm.beginTransaction();
        RealmFace rf = realm.createObject(RealmFace.class);
        rf.setId(generateId(RealmFace.class));
        rf.setImageId(ri.getId());
        rf.setInfo(face);
        realm.commitTransaction();
        realm.close();
    }

    public void updateFace(FaceInfo face, Person person) {
        RealmFace rf = realm.where(RealmFace.class).equalTo("id", face.getId()).findFirst();
        realm.beginTransaction();
        rf.setPersonId((int)person.getId());
        realm.commitTransaction();
        realm.close();
    }

    public void setSimilarity(Image img1, Image img2) {
        RealmImage ri1 = realm.where(RealmImage.class).equalTo("path", img1.getPath()).findFirst();
        RealmImage ri2 = realm.where(RealmImage.class).equalTo("path", img2.getPath()).findFirst();
        realm.beginTransaction();
        RealmSimilarity rs = realm.createObject(RealmSimilarity.class);
        rs.setImageId1(ri1.getId());
        rs.setImageId2(ri2.getId());
        realm.commitTransaction();
        realm.close();
    }

    public ArrayList<Image> getSimilarImages(ArrayList<Image> images) {
        ArrayList<Image> resultSet = new ArrayList<Image>();
        for (Image img : images) {
            RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
            RealmResults<RealmSimilarity> similarities
                    = realm.where(RealmSimilarity.class).equalTo("imageId1", ri.getId()).findAll();
            ArrayList<Image> result1 = new ArrayList<Image>();
            ArrayList<Image> result2 = (ArrayList<Image>) resultSet.clone();
            for (int i = 0; i < similarities.size(); i++) {
                RealmImage ri2 = realm.where(RealmImage.class).equalTo("id", similarities.get(i).getImageId2()).findFirst();
                result1.add(new Image(ri2.getPath()));
            }
            resultSet.addAll(result1);
            result2.removeAll(result1);
            //resultSet.removeAll(result2);
        }
        resultSet.removeAll(images);
        resultSet.addAll(0, images);
        realm.close();
        return resultSet;
    }

    public ArrayList<FaceInfo> getFaces(Image img) {
        ArrayList<FaceInfo> faceInfos = new ArrayList<>();
        RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
        RealmResults<RealmFace> faces = realm.where(RealmFace.class).equalTo("imageId", ri.getId()).findAll();
        for (RealmFace rf : faces) {
            FaceInfo face = rf.getInfo();
            face.setId(rf.getId());
            faceInfos.add(face);
        }
        realm.close();
        return faceInfos;
    }

    public ArrayList<Album> getDummyAlbums() {
        ArrayList<Album> albums = new ArrayList<Album>();
        RealmResults<RealmAlbum> realmAlbums = realm.where(RealmAlbum.class).findAll();
        for (RealmAlbum ra : realmAlbums) {
            albums.add(new Album(ra.getName()));
        }
        realm.close();
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
        String newName = ra.getName();
        realm.close();
        return new Album(newName, images);
    }

    public ArrayList<Image> getImagesOfPerson() {
        return null;
    }

    public ArrayList<Image> getUnprocessedImages() {
        RealmResults<RealmImage> images = realm.where(RealmImage.class).equalTo("isProcessed", false).findAll();
        ArrayList<Image> allImages = new ArrayList<Image>();
        for (RealmImage ri : images) {
            allImages.add(new Image(ri.getPath()));
        }
        realm.close();
        return allImages;
    }

    public ArrayList<Image> getProcessedImages() {
        RealmResults<RealmImage> images = realm.where(RealmImage.class).equalTo("isProcessed", true).findAll();
        ArrayList<Image> allImages = new ArrayList<Image>();
        for (RealmImage ri : images) {
            allImages.add(new Image(ri.getPath()));
        }
        realm.close();
        return allImages;
    }

    public ArrayList<Image> getUnmappedImages() {
        RealmResults<RealmImage> images = realm.where(RealmImage.class).equalTo("isMapped", false).findAll();
        ArrayList<Image> allImages = new ArrayList<Image>();
        for (RealmImage ri : images) {
            allImages.add(new Image(ri.getPath()));
        }
        realm.close();
        return allImages;
    }

    private int generateId(Class c) {
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
        realm.close();
        return allImages;
    }

    public void setInitialImageList(ArrayList<Image> images) {
        realm.beginTransaction();
        for (Image img : images) {
            RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
            if (ri == null) {
                ri = new RealmImage();
                ri.setId(generateId(RealmImage.class));
            }
            ri.setImage(img);
            ri.setProcessed(false);
            ri.setMapped(false);
            realm.insertOrUpdate(ri);
        }
        realm.commitTransaction();
        realm.close();
    }

    public void deleteImageList(ArrayList<Image> images) {
        realm.beginTransaction();
        for (Image img : images) {
            RealmImage ri = realm.where(RealmImage.class).equalTo("path", img.getPath()).findFirst();
            ri.deleteFromRealm();
        }
        realm.commitTransaction();
        realm.close();
    }

    public void deleteAlbum(Album album){
        realm.beginTransaction();
        RealmAlbum ra = realm.where(RealmAlbum.class).equalTo("name", album.getName()).findFirst();
        RealmResults<RealmAlbumImage> rai = realm.where(RealmAlbumImage.class).equalTo("albumId", ra.getId()).findAll();
        ra.deleteFromRealm();
        rai.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public String getProfilePhotoPath(Person p) {
        RealmPerson rp = realm.where(RealmPerson.class).equalTo("phoneNumber", p.getContactNumber()).findFirst();
        if (rp == null) {
            realm.close();
            return "";
        }
        String path = rp.getPath();
        realm.close();
        return path;
    }
}
