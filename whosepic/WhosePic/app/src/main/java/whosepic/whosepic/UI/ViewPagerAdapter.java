package whosepic.whosepic.UI;

/**
 * Created by ASUS on 1.05.2018.
 */
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.R;

import static whosepic.whosepic.AppManagers.GalleryAdapter.getAllShownImagesPath;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Image> images;

    public ViewPagerAdapter(Context context) {
        this.context = context;
        this.images = getAllShownImagesPath(context);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_image_preview, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.previewedImage);
        imageView.setImageURI(Uri.parse(images.get(position).getPath()));

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}