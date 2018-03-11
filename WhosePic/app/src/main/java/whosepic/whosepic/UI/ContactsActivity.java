package whosepic.whosepic.UI;

/**
 * Created by ASUS on 2.12.2017.
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import whosepic.whosepic.AppManagers.ContactsAdapter;
import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.R;

public class ContactsActivity extends AppCompatActivity {

    RecyclerView rvContacts;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);

        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        //getPermissionToReadUserContacts();
        getAllContacts();
        rvContacts.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),rvContacts, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), ImageOverviewActivity.class);
                //intent.putExtra("", (RecyclerView)v);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void getAllContacts() {
        List<Person> personList = new ArrayList();
        Person person;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null,
                null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Integer.parseInt(id));
                    Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    person = new Person();
                    person.setContactName(name);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
                            getContentResolver(), contactUri);

                    if (inputStream != null) {
                        Bitmap photo = BitmapFactory.decodeStream(inputStream);
                        person.setContactImageBitmap(photo);
                    } else {
                        person.setContactImageBitmap(null);
                    }

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(
                                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        person.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    personList.add(person);
                }
            }

            ContactsAdapter contactAdapter = new ContactsAdapter(personList, getApplicationContext());
            rvContacts.setLayoutManager(new LinearLayoutManager(this));
            rvContacts.setAdapter(contactAdapter);
        }
    }

    static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener
    {
        public interface OnItemClickListener
        {
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        private OnItemClickListener mListener;
        private GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener)
        {
            mListener = listener;

            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
            {
                @Override
                public boolean onSingleTapUp(MotionEvent e)
                {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e)
                {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(childView != null && mListener != null)
                    {
                        mListener.onItemLongClick(childView, recyclerView.getChildPosition(childView));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e)
        {
            View childView = view.findChildViewUnder(e.getX(), e.getY());

            if(childView != null && mListener != null && mGestureDetector.onTouchEvent(e))
            {
                mListener.onItemClick(childView, view.getChildPosition(childView));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent){}

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}