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

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import whosepic.whosepic.AppManagers.ContactsAdapter;
import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.R;

public class ContactsActivity extends AppCompatActivity {

    RecyclerView rvContacts;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Contacts.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);

        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        // Todo: read contact permission will be asked.
        //getPermissionToReadUserContacts();
        getAllContacts();
        rvContacts.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),rvContacts,
                                        new RecyclerItemClickListener.OnItemClickListener() {
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
        Query q = Contacts.getQuery();
        q.hasPhoneNumber();
        List<Contact> contacts = q.find();
        for (Contact c : contacts) {
            personList.add(new Person(c.getDisplayName(), c.getPhoneNumbers().get(0).getNumber(),
                                    c.getPhotoUri() != null ? Uri.parse(c.getPhotoUri()) : null));
        }
        // Todo: sort personlist api 24 required.
        /*personList.sort(new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getContactName().compareToIgnoreCase(p2.getContactName());
            }
        });*/
        ContactsAdapter contactAdapter = new ContactsAdapter(personList, getApplicationContext());
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(contactAdapter);
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