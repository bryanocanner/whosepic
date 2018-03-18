package whosepic.whosepic.AppManagers;

/**
 * Created by ASUS on 2.12.2017.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import whosepic.whosepic.AppCode.ObjectModels.Person;
import whosepic.whosepic.R;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>{

    private List<Person> personList;
    private Context mContext;
    public ContactsAdapter(List<Person> personList, Context mContext){
        this.personList = personList;
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Person person = personList.get(position);
        Uri uri = person.getContactImageUri();
        if (uri != null)
            holder.ivContactImage.setImageURI(uri);
        else
            holder.ivContactImage.setImageResource(R.mipmap.ic_launcher);
        holder.tvContactName.setText(person.getContactName());
        holder.tvPhoneNumber.setText(person.getContactNumber());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
        }
    }
}