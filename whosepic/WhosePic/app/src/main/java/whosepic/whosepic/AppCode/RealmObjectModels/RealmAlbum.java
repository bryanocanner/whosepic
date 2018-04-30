package whosepic.whosepic.AppCode.RealmObjectModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import whosepic.whosepic.AppCode.ObjectModels.Album;

/**
 * Created by aligunes on 08/04/2018.
 */

public class RealmAlbum extends RealmObject {
    private int id;
    private String name;

    public void setId(int id) {
        this.id = id;
    }

    public void setAlbum(Album album) {
        this.name = album.getName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
