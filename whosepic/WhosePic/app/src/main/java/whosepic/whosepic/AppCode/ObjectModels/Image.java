package whosepic.whosepic.AppCode.ObjectModels;

import java.io.Serializable;
import io.realm.RealmObject;

/**
 * Created by aligunes on 11/03/2018.
 */

public class Image implements Serializable {
    private String path;

    public Image(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object img) {
        if (!(img instanceof Image))
            return false;
        return this.path.equals(((Image)img).getPath());
    }
}
