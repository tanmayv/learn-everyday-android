package com.tanmayvijayvargiya.factseveryday.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.tanmayvijayvargiya.factseveryday.R;
import com.tanmayvijayvargiya.factseveryday.models.Fact;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;
import com.tanmayvijayvargiya.factseveryday.services.LetterTileProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by tanmayvijayvargiya on 03/07/16.
 */
public class FactsListAdapter extends RecyclerView.Adapter<FactsListAdapter.ViewHolder> {

    private FactItemListener mListener;
    private List<Fact> factList;
    private Context context;

    public FactsListAdapter(Context context, FactItemListener listener){
        this.mListener = listener;
        this.context = context;
        this.factList = new ArrayList<Fact>();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fact_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.preBind(factList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return factList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, usernameText, timestampText, factContentText;
        CircleImageView profilePic;
        AppCompatImageView bannerImage, favImage, shareImage;

        private int bannerWidth;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText  = (TextView) itemView.findViewById(R.id.title_text);
            usernameText = (TextView) itemView.findViewById(R.id.username_text);
            timestampText = (TextView) itemView.findViewById(R.id.timestamp_text);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profile_pic);
            factContentText = (TextView) itemView.findViewById(R.id.fact_content_text);
            bannerImage = (AppCompatImageView) itemView.findViewById(R.id.bannerImage);
            favImage = (AppCompatImageView) itemView.findViewById(R.id.favButton);
            shareImage = (AppCompatImageView) itemView.findViewById(R.id.shareButton);

        }

        public void preBind(final Fact fact, final FactItemListener listener){
            if(fact.getTitle() == null && fact.get_id() != null){
                LearnEverydayService.getInstance().getApi()
                        .getFact(fact.get_id())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Fact>() {
                            @Override
                            public void call(Fact f) {
                                f.isFavorite = fact.isFavorite;
                                bind(f,listener);
                            }
                        });
            }
            else
                bind(fact,listener);
        }

        public void bind(final Fact fact, final FactItemListener listener){
            titleText.setText(fact.getTitle());
            if(fact.getBannerUrl() != null && fact.getBannerUrl() != ""){
                Transformation transformation = new Transformation() {

                    @Override
                    public Bitmap transform(Bitmap source) {
                        if(bannerWidth == 0){
                            bannerWidth = itemView.getWidth();
                        }
                        int targetWidth = 720;
                                Log.d("Shit", "Width " + targetWidth);
                        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                        int targetHeight = (int) (targetWidth * aspectRatio);
                        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                        if (result != source) {
                            // Same bitmap is returned if sizes are the same
                            source.recycle();
                        }
                        return result;
                    }

                    @Override
                    public String key() {
                        return "transformation" + " desiredWidth";
                    }
                };

                Picasso.with(context).load(fact.getBannerUrl()).transform(transformation).into(bannerImage);
            }
            else
                bannerImage.setImageDrawable(null);
            String authorName;
            if(fact.getAuthor() == null) {
                authorName = "Anonymous";
            }else{
                authorName = fact.getAuthor().getName();
                if(authorName == null || authorName == ""){
                    authorName = "Anonymous";
                }

            }
            usernameText.setText(authorName);
            final Resources res = context.getResources();
            final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);

            final LetterTileProvider tileProvider = new LetterTileProvider(context);
            final Bitmap letterTile = tileProvider.getLetterTile(authorName, "key", tileSize, tileSize);

            profilePic.setImageBitmap(letterTile);
            if(fact.getContent() != null)
                factContentText.setText(fact.getContent());

            if(fact.isFavorite){
                favImage.setImageResource(R.drawable.heartred);
            }else
                favImage.setImageResource(R.drawable.heartgrey);
            favImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                    fact.isFavorite = !fact.isFavorite;
                    listener.favButtonClick(fact);
                    notifyDataSetChanged();
                }
            });

            if(fact.getCreatedAt() != null){
                try{
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    Date tempDate = df.parse(fact.getCreatedAt());
                    DateFormat newDf = new SimpleDateFormat("dd/MM/yy hh:mm aaa");
                    timestampText.setText(newDf.format(tempDate));
                }catch(Exception e){
                    Log.d("Shit", "Unable to parse " + fact.getCreatedAt());
                }
            }
            shareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.shareButtonClick(fact);
                }
            });

        }
    }

    public void setFactList(List<Fact> list){
        this.factList = list;
        notifyDataSetChanged();
    }

    public interface FactItemListener{
        public void favButtonClick(Fact fact);
        public void shareButtonClick(Fact fact);
    }
}
