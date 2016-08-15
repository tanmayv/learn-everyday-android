package com.tanmayvijayvargiya.factseveryday.views;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.tanmayvijayvargiya.factseveryday.R;
import com.tanmayvijayvargiya.factseveryday.models.Fact;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;
import com.tanmayvijayvargiya.factseveryday.services.LetterTileProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FactViewActivity extends AppCompatActivity {

    Fact mFact;
    TextView titleText, usernameText, timestampText, factContentText;
    CircleImageView profilePic;
    AppCompatImageView bannerImage, favImage, shareImage;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_fact_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
            // close this activity and return to preview activity (if there is any)
        }

        if(item.getItemId() == R.id.action_share){
            shareText(mFact.getTitle(), mFact.getContent());
        }

        if(item.getItemId() == R.id.action_search){
            finish();
            startActivity(new Intent(this,SearchActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fact_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(2);

        titleText  = (TextView) findViewById(R.id.title_text);
        usernameText = (TextView) findViewById(R.id.username_text);
        timestampText = (TextView) findViewById(R.id.timestamp_text);
        profilePic = (CircleImageView) findViewById(R.id.profile_pic);
        factContentText = (TextView) findViewById(R.id.fact_content_text);
        bannerImage = (AppCompatImageView) findViewById(R.id.bannerImage);
        favImage = (AppCompatImageView) findViewById(R.id.favButton);
        shareImage = (AppCompatImageView) findViewById(R.id.shareButton);

        Intent i = getIntent();
        if(i == null){
            navigateToHome();

        }
        Bundle factBundle = i.getBundleExtra("fact");
        if(factBundle == null){
            navigateToHome();
        }

        mFact = new Fact();
        String factId = factBundle.getString("factId");
        if(factId != null){
            mFact.set_id(factId);
            mFact.setTitle(factBundle.getString("factTitle"));
            mFact.setContent(factBundle.getString("factContent"));
            bind(mFact);
            LearnEverydayService.getInstance().getApi()
                    .getFact(factId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Fact>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Fact fact) {
                            bind(fact);
                        }
                    });
        }else
            navigateToHome();
    }

    void navigateToHome(){
        finish();
        startActivity(new Intent(this, ActivityHome.class));
    }

    public void bind(final Fact fact){
        titleText.setText(fact.getTitle());
        if(fact.getBannerUrl() != null && fact.getBannerUrl() != ""){
            Transformation transformation = new Transformation() {

                @Override
                public Bitmap transform(Bitmap source) {

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

            Picasso.with(this).load(fact.getBannerUrl()).transform(transformation).into(bannerImage);
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
        final Resources res = this.getResources();
        final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);

        final LetterTileProvider tileProvider = new LetterTileProvider(this);
        final Bitmap letterTile = tileProvider.getLetterTile(authorName, "key", tileSize, tileSize);

        profilePic.setImageBitmap(letterTile);
        if(fact.getContent() != null)
            factContentText.setText(fact.getContent());

        if(fact.isFavorite){
            favImage.setImageResource(R.drawable.heartred);
        }else
            favImage.setImageResource(R.drawable.heartgrey);
        if(!favImage.hasOnClickListeners())
            favImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                    fact.isFavorite = !fact.isFavorite;
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
        if(!shareImage.hasOnClickListeners())
            shareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

    }
    private void shareText(String title, String content) {
        String shareContent = "Fact : " + title + "\n\n";
        shareContent = shareContent.concat(content.concat("\n\nShared via Facts - Learn Everyday"));

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TITLE, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareContent);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}


