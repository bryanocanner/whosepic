package whosepic.whosepic.AppCode.ObjectModels;

import io.realm.RealmObject;

/**
 * Created by aligunes on 11/03/2018.
 */

public class FaceInfo {
    double [][] info;
    int id;

    public FaceInfo(double [][] info) {
        this.info = info;
    }

    public double [][] getInfo() {
        return info;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
