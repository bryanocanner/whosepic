package whosepic.whosepic.AppManagers;

/**
 * Created by ASUS on 2.12.2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.R;
import whosepic.whosepic.UI.Views.SquareImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> implements Filterable{

    private List<Person> personList;
    private List<Person> contactListFiltered;
    private Context mContext;
    private ContactsAdapterListener listener;

    public ContactsAdapter(List<Person> personList, Context mContext, ContactsAdapterListener listener){
        this.personList = personList;
        contactListFiltered = personList;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Person person = contactListFiltered.get(position);
        if (!person.getContactImagePath().equals("")) {
            Uri uri = Uri.parse(person.getContactImagePath());
            holder.ivContactImage.setImageURI(uri);
        } else {
            holder.ivContactImage.setImageResource(R.drawable.default_profile);
        }
        holder.ivContactImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.tvContactName.setText(person.getContactName());
        holder.tvPhoneNumber.setText(person.getContactNumber());
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = personList;
                } else {
                    List<Person> filteredList = new ArrayList<>();
                    for (Person row : personList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getContactName().toLowerCase().contains(charString.toLowerCase()) || row.getContactNumber().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Person>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public  List<Person> getList(){
        return contactListFiltered;
    }
    public  class ContactViewHolder extends RecyclerView.ViewHolder{

        SquareImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (SquareImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    public interface ContactsAdapterListener {
        void onContactSelected(Person person);
    }
}