package com.tanmayvijayvargiya.factseveryday.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tanmayvijayvargiya.factseveryday.R;
import com.tanmayvijayvargiya.factseveryday.services.LetterTileProvider;
import com.tanmayvijayvargiya.factseveryday.vo.Fact;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tanmayvijayvargiya on 03/07/16.
 */
public class FactsListAdapter extends RecyclerView.Adapter<FactsListAdapter.ViewHolder> {

    private FactItemListener mListener;
    private List<Fact> factList;
    private Context context;
    int lastPosition = -1;
    private boolean expandingTextAnimating = false;

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
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return factList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, usernameText, timestampText, factContentText, showMoreText;
        CircleImageView profilePic;
        AppCompatImageView  favImage, shareImage;
        RoundedImageView bannerImage;

        private int bannerWidth;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText  = (TextView) itemView.findViewById(R.id.title_text);
            usernameText = (TextView) itemView.findViewById(R.id.username_text);
            timestampText = (TextView) itemView.findViewById(R.id.timestamp_text);
            showMoreText = (TextView)itemView.findViewById(R.id.showMoreText);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profile_pic);
            factContentText = (TextView) itemView.findViewById(R.id.fact_content_text);
            bannerImage = (RoundedImageView) itemView.findViewById(R.id.bannerImage);
            favImage = (AppCompatImageView) itemView.findViewById(R.id.favButton);
            shareImage = (AppCompatImageView) itemView.findViewById(R.id.shareButton);
            showMoreText.setText("SHOW MORE");

        }

        public void preBind(final Fact fact, final FactItemListener listener){
            if(fact.getTitle() == null && fact.get_id() != null){
//                LearnEverydayService.getInstance().getApi()
//                        .getFact(fact.get_id())
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<Fact>() {
//                            @Override
//                            public void call(Fact f) {
//                                f.isFavorite = fact.isFavorite;
//                                bind(f,listener);
//                            }
//                        });
            }
            else
                bind(fact,listener);
        }

        public void bind(final Fact fact, final FactItemListener listener){
            titleText.setText(fact.getTitle());
            titleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                    mListener.navigateToFactViewActivity( fact);

                }
            });
            if(fact.getBannerUrl() != null && fact.getBannerUrl() != ""){
                Picasso.with(context)
                        .load(fact.getBannerUrl())
                        .placeholder(R.drawable.placeholder)
                        .resize(dp2px(220), 0)
                        .into(bannerImage);
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

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.pop_heart);
                if(fact.isSynced)

                    favImage.setImageResource(R.drawable.heartred);
                else
                    favImage.setImageResource(R.drawable.heart_cold);
                favImage.startAnimation(animation);

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

            showMoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleShowMoreLess();
                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
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

        private void toggleShowMoreLess() {
            Log.d("ANIM", showMoreText.getText().toString());
            if(showMoreText.getText().toString().equals("SHOW MORE")){
                Log.d("ANIM", "ANIMATING");

                ObjectAnimator animation = ObjectAnimator.ofInt(
                        factContentText,
                        "maxLines",
                        50);
                animation.setDuration(300);
                animation.start();
                animation.start();

                showMoreText.setText("SHOW LESS");

            }else{
                Log.d("ANIM", "ANIMATING LESS");
                ObjectAnimator animation = ObjectAnimator.ofInt(
                        factContentText,
                        "maxLines",
                        5);
                animation.setDuration(500);
                animation.start();
                showMoreText.setText("SHOW MORE");

            }
        }
    }

    public void setFactList(List<Fact> list){
        this.factList = list;
        notifyDataSetChanged();
    }

    public interface FactItemListener {
        public void favButtonClick(Fact fact);
        public void shareButtonClick(Fact fact);

        void navigateToFactViewActivity(Fact fact);
    }

    public int dp2px(int dp) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        return (int) (dp * displaymetrics.density + 0.5f);
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition && (position == 0 || position == 1))
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);

            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
