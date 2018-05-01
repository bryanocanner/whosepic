package whosepic.whosepic.AppManagers;

/**
 * Created by emintosun on 1.05.2018.
 */
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.bumptech.glide.*;

import whosepic.whosepic.*;
import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.R;
import whosepic.whosepic.UI.Views.SquareImageView;

public class SimilarImagesAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<Image> images;

    public SimilarImagesAdapter(Context context,ArrayList<Image> images) {
        this.context = context;
        this.images = images;
    }


    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }

    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        SquareImageView picturesView;
        if (convertView == null) {
            picturesView = new SquareImageView(context);
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            picturesView.setPadding(8,8,8,8);
            notifyDataSetChanged();
        } else {
            picturesView = (SquareImageView) convertView;
            picturesView.setScaleType(ImageView.ScaleType.CENTER);
            picturesView.setPadding(8,8,8,8);
        }

        Glide.with(context).load(images.get(position).getPath())
                .into(picturesView);

        return picturesView;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    /**
     * Getting All Images Path.
     *
     * @param context
     *            the context
     * @return ArrayList with images Path
     */
}
