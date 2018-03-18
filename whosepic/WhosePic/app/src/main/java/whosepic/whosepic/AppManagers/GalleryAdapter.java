package whosepic.whosepic.AppManagers;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.bumptech.glide.*;

import whosepic.whosepic.AppCode.ObjectModels.Image;

/**
 * Created by aligunes on 18/02/2018.
 */

public class GalleryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Image> images;

    public GalleryAdapter(Context context) {
        this.context = context;
        this.images = getAllShownImagesPath(context);
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
        ImageView picturesView;
        if (convertView == null) {
            picturesView = new ImageView(context);
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            picturesView.setLayoutParams(new GridView.LayoutParams(270, 270));
        } else {
            picturesView = (ImageView) convertView;
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
    private ArrayList<Image> getAllShownImagesPath(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<Image> listOfAllImages = new ArrayList<Image>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = context.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(new Image(absolutePathOfImage));
        }
        return listOfAllImages;
    }
}
