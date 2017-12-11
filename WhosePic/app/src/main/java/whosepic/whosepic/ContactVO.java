package whosepic.whosepic;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

/**
 * Created by emintosun on 2.12.2017.
 */
public class ContactVO {
    private Bitmap ContactImage;
    private String ContactName;
    private String ContactNumber;

    public Bitmap getContactImageBitmap() {
        return ContactImage;
    }

    public void setContactImageBitmap(Bitmap contactImage) {
        this.ContactImage = contactImage;
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