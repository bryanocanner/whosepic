package whosepic.whosepic.AppManagers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import whosepic.whosepic.AppCode.ObjectModels.Album;
import whosepic.whosepic.AppCode.ObjectModels.Image;
import whosepic.whosepic.R;
import whosepic.whosepic.UI.AlbumActivity;
import whosepic.whosepic.UI.ImagePreviewActivity;

/**
 * Created by aligunes on 18/02/2018.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumsViewHolder> {
    private ArrayList<Bitmap> data = new ArrayList<Bitmap>();
    private ArrayList<Album> albums;
    Context mContext;

    public AlbumsAdapter(Context context, ArrayList<Album> albums, Activity activity) {
        this.albums = albums;
        this.mContext = context;
    }

    // Todo: viewholder will be coded.
    public  class AlbumsViewHolder extends RecyclerView.ViewHolder{

        ImageView albumImageView;
        TextView albumName;

        public AlbumsViewHolder(View itemView) {
            super(itemView);
            albumImageView = (ImageView) itemView.findViewById(R.id.albumImage);
            albumName = (TextView) itemView.findViewById(R.id.albumName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AlbumActivity.class);
                    intent.putExtra("Album" , (Album) albums.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public AlbumsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_album_view, null);
        AlbumsViewHolder albumsViewHolder = new AlbumsViewHolder(view);
        return albumsViewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumsViewHolder holder, int position) {
        if(!albums.isEmpty()) {
            Album album = albums.get(position);
            holder.albumImageView.setImageResource(R.mipmap.if_folder_33694);
            holder.albumName.setText(album.getName());
        }
    }

    
    @Override
    public int getItemCount() {
        return albums.size();
    }

}