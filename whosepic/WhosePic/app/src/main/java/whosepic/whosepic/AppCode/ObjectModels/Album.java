package whosepic.whosepic.AppCode.ObjectModels;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aligunes on 08/04/2018.
 */

public class Album implements Serializable {
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

    public void setImages(ArrayList<Image> images){
        this.images = images;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Image> getImages(){
        return images;
    }
}
