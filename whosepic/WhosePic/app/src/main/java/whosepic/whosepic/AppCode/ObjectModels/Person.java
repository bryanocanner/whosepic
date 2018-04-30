package whosepic.whosepic.AppCode.ObjectModels;

import java.io.Serializable;
import io.realm.RealmObject;

/**
 * Created by emintosun on 2.12.2017.
 */
public class Person implements Serializable {
    private String ContactImagePath;
    private String ContactName;
    private String ContactNumber;
    private long     id;

    public Person (String name, String number, String path, long id) {
        ContactImagePath = path;
        ContactName = name;
        ContactNumber = number;
        this.id=id;
    }

    public String getContactImagePath() {
        return ContactImagePath;
    }

    public void setContactImagePath(String contactImagePath) {
        this.ContactImagePath = contactImagePath;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}