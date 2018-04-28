package whosepic.whosepic.AppCode.RealmObjectModels;

import io.realm.RealmObject;

/**
 * Created by aligunes on 08/04/2018.
 */

public class RealmAlbumImage extends RealmObject {
    private int imageId;
    private int albumId;

    public int getImageId() {
        return imageId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
