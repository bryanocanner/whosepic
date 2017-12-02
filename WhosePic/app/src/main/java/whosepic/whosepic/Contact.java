package whosepic.whosepic;

import android.media.Image;

/**
 * Created by emintosun on 2.12.2017.
 */

public class Contact {
    String name;
    String surname;
    String midName;
    String number;
    Image image;
    public Contact(String name, String midName,String surname, String number, Image image){
        this.image = image;
        this.name = name;
        this.midName = midName;
        this.surname = surname;
        this.number = number;
    }

    public Image getImage() {
        return image;
    }

    public String getMidName() {
        return midName;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getSurname() {
        return surname;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
