package whosepic.whosepic.AppCode.ObjectModels;

import java.io.Serializable;

/**
 * Created by aligunes on 11/03/2018.
 */

public class Image implements Serializable {
    private String path;

    public Image(String path) {
        this.path = path;
    }

    public String getPath() {
        return  path;
    }
}
