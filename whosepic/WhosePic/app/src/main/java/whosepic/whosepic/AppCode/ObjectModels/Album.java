package whosepic.whosepic.AppCode.ObjectModels;

import java.util.ArrayList;

/**
 * Created by aligunes on 08/04/2018.
 */

public class Album {
    private String name;
    private ArrayList<Image> images;

    public Album(String name) {
        this.name = name;
        this.images = null;
    }

    public Album(String name, ArrayList<Image> images) {
        this.name = name;
        this.images = images;
    }

    public String getName() {
        return name;
    }
}
