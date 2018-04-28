package whosepic.whosepic.AppCode.RealmObjectModels;

import io.realm.RealmObject;
import whosepic.whosepic.AppCode.ObjectModels.Image;

/**
 * Created by aligunes on 08/04/2018.
 */

public class RealmImage extends RealmObject {
    private int id;
    private String path;

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(Image img) {
        this.path = img.getPath();
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }
}
