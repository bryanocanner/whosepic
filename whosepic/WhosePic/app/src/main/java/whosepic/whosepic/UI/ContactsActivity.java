package whosepic.whosepic.UI;

/**
 * Created by ASUS on 2.12.2017.
 */

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
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

import whosepic.whosepic.AppCode.ImageProcessor.ImageAnalyzer;
import whosepic.whosepic.AppCode.ObjectModels.FaceInfo;
import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.AppManagers.ContactsAdapter;
import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.DatabaseManager.DatabaseManager;
import whosepic.whosepic.R;

public class ContactsActivity extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener{

    RecyclerView rvContacts;
    List<Person> personList;
    private ContactsAdapter mAdapter;
    private SearchView searchView;
    ActionBar actionBar;
    Person clicked;
    ProgressDialog progress;
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
    protected void onResume() {
        super.onResume();
        if (clicked != null) {
            clicked.setContactImagePath(DatabaseManager.getInstance().getProfilePhotoPath(clicked));
            personList.set(personList.indexOf(clicked), clicked);
        }
        mAdapter.notifyDataSetChanged();
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
            Person p = new Person(c.getDisplayName(), c.getPhoneNumbers().get(0).getNumber(), null, c.getId());
            p.setContactImagePath(DatabaseManager.getInstance().getProfilePhotoPath(p));
            personList.add(p);
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
    public void onContactSelected(final Person person) {
        clicked = person;
        if (person.getContactImagePath().equals("")) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("This contact does not have profile picture. Would you like to assign one?");
            dlgAlert.setCancelable(true);
            dlgAlert.setNegativeButton("Cancel", null);
            dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), SimilarImagesActivity.class);
                    intent.putExtra("Person", person);
                    startActivity(intent);
                }
            });
            dlgAlert.create().show();
            return;
        }
        Image img = new Image(person.getContactImagePath());
        if (!DatabaseManager.getInstance().checkImage(img)) {
            new HandleImageProcessing().execute(img);
            progress = new ProgressDialog(this);
            progress.setTitle("Processing");
            progress.setMessage("Assigned profile picture is processing...");
            progress.setCancelable(false);
            progress.show();
            return;
        }
        if (DatabaseManager.getInstance().getFaces(img).size() != 1) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Assigned profile picture does not in a proper format. Would you like to change it?");
            dlgAlert.setCancelable(true);
            dlgAlert.setNegativeButton("Cancel", null);
            dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), SimilarImagesActivity.class);
                    intent.putExtra("Person", person);
                    startActivity(intent);
                }
            });
            dlgAlert.create().show();
            return;
        }
        Intent intent = new Intent(getApplicationContext(), ImageOverviewActivity.class);
        intent.putExtra("Person", (Person) person);
        startActivity(intent);
    }

    private class HandleImageProcessing extends AsyncTask<Image, Integer, String> {

        @Override
        protected String doInBackground(Image... params) {
            Image img = params[0];
            ArrayList<FaceInfo> faces = ImageAnalyzer.getInstance().processImage(img);
            DatabaseManager.getInstance().setImage(img, true, false);
            for (FaceInfo face : faces) {
                DatabaseManager.getInstance().setFace(face, img);
            }
            if (progress != null) {
                progress.dismiss();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {}

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Integer... values) {}
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
