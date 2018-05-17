package whosepic.whosepic.AppCode.RealmObjectModels;

import io.realm.RealmObject;

/**
 * Created by aligunes on 09/05/2018.
 */

public class RealmSimilarity extends RealmObject {
    int imageId1;
    int imageId2;

    public void setImageId1(int id) {
        imageId1 = id;
    }

    public void setImageId2(int id) {
        imageId2 = id;
    }

    public int getImageId1() {
        return imageId1;
    }

    public int getImageId2() {
        return imageId2;
    }
}
