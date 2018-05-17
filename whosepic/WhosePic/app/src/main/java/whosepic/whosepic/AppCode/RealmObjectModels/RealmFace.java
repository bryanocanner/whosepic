package whosepic.whosepic.AppCode.RealmObjectModels;

import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import io.realm.RealmObject;
import io.realm.RealmResults;
import whosepic.whosepic.AppCode.ObjectModels.FaceInfo;

/**
 * Created by aligunes on 08/04/2018.
 */

public class RealmFace extends RealmObject {
    int id;
    int imageId;
    int personId;
    String info;

    public void setId(int id) {
        this.id = id;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setInfo(FaceInfo face) {
        this.info = convert2DArrayToString(face.getInfo());
    }

    public int getId() {
        return id;
    }

    public int getImageId() {
        return imageId;
    }

    public int getPersonId() {
        return personId;
    }

    public FaceInfo getInfo() {
        return new FaceInfo(convertStringTo2DArray(info));
    }

    private String convert2DArrayToString(double[][] info){
        ByteArrayOutputStream bo = null;
        ObjectOutputStream so = null;
        Base64OutputStream b64 = null;
        try {
            bo = new ByteArrayOutputStream();
            b64 = new Base64OutputStream(bo, Base64.DEFAULT);
            so = new ObjectOutputStream(b64);
            so.writeObject(info);
            return bo.toString("UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if (bo != null) { bo.close(); }
                if (b64 != null) { b64.close(); }
                if (so != null) { so.close(); }
            }catch (Exception ee){
                ee.printStackTrace();
            }

        }
        return null;
    }

    private double[][] convertStringTo2DArray(String s) {
        ByteArrayInputStream bi = null;
        ObjectInputStream si = null;
        Base64InputStream b64 = null;
        try {
            byte b[] = s.getBytes("UTF-8");
            bi = new ByteArrayInputStream(b);
            b64 = new Base64InputStream(bi, Base64.DEFAULT);
            si = new ObjectInputStream(b64);
            return (double[][]) si.readObject();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                if (bi != null) { bi.close(); }
                if (b64 != null) { b64.close(); }
                if (si != null) { si.close(); }
            }catch (Exception ee){
                ee.printStackTrace();
            }

        }
        return null;
    }
}
