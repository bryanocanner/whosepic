package whosepic.whosepic.AppCode.ObjectModels;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

/**
 * Created by emintosun on 2.12.2017.
 */
public class Person {
    private Uri ContactImageUri;
    private String ContactName;
    private String ContactNumber;

    public Person (String name, String number, Uri uri) {
        ContactImageUri = uri;
        ContactName = name;
        ContactNumber = number;
    }

    public Uri getContactImageUri() {
        return ContactImageUri;
    }

    public void setContactImageUri(Uri contactImageUri) {
        this.ContactImageUri = contactImageUri;
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
}