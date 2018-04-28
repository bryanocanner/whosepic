package whosepic.whosepic.AppManagers;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.R;
import whosepic.whosepic.UI.Views.SquareImageView;

/**
 * Created by ASUS on 18.03.2018.
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private Image image;

    public ImageAdapter(Context context, Image image) {
        this.context = context;
        this.image = image;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }

    public int getCount() {
        return 1;
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
            picturesView.setLayoutParams(new GridView.LayoutParams(270, 270));
            notifyDataSetChanged();
        } else {
            picturesView = (SquareImageView) convertView;
        }

        Glide.with(context).load(image.getPath())
                .into(picturesView);

        return picturesView;
    }

    public class ImagePreviewHolder {
        public ImageView image;

        public ImagePreviewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.previewedImage);
        }
    }
}
