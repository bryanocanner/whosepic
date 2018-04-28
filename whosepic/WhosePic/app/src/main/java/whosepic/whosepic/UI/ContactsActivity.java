package whosepic.whosepic.UI;

/**
 * Created by ASUS on 2.12.2017.
 */

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;
import java.util.ArrayList;
import java.util.List;

import whosepic.whosepic.AppManagers.ContactsAdapter;
import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.R;

public class ContactsActivity extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener{

    RecyclerView rvContacts;
    List<Person> personList;
    private ContactsAdapter mAdapter;
    private SearchView searchView;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Contacts.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        personList = new ArrayList<Person>();
        // Todo: read contact permission will be asked.
        //getPermissionToReadUserContacts();
        getAllContacts();
        mAdapter = new ContactsAdapter(personList, getApplicationContext(), this);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                personList = mAdapter.getList();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                personList = mAdapter.getList();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAllContacts() {
        Query q = Contacts.getQuery();
        q.hasPhoneNumber();
        List<Contact> contacts = q.find();
        for (Contact c : contacts) {
            personList.add(new Person(c.getDisplayName(), c.getPhoneNumbers().get(0).getNumber(),
                    c.getPhotoUri() != null ? c.getPhotoUri() : null));
        }
        // Todo: sort personlist api 24 required.
        /*personList.sort(new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getContactName().compareToIgnoreCase(p2.getContactName());
            }
        });*/
    }

    @Override
    public void onContactSelected(Person person) {
        Intent intent = new Intent(getApplicationContext(), ImageOverviewActivity.class);
        intent.putExtra("Person", (Person) person);
        startActivity(intent);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
