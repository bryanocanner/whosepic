package whosepic.whosepic.AppCode.RealmObjectModels;

import io.realm.RealmObject;

/**
 * Created by aligunes on 08/04/2018.
 */

public class RealmPerson extends RealmObject {
    int id;
    String phoneNumber;
    String path;

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPath() {
        return path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhoneNumber(String number) {
        phoneNumber = number;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
