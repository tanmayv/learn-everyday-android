package com.tanmayvijayvargiya.factseveryday.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tanmayvijayvargiya.factseveryday.R;
import com.tanmayvijayvargiya.factseveryday.adapters.ViewPagerAdapter;
import com.tanmayvijayvargiya.factseveryday.app.Config;
import com.tanmayvijayvargiya.factseveryday.helper.Presenter;
import com.tanmayvijayvargiya.factseveryday.helper.PresenterFactory;
import com.tanmayvijayvargiya.factseveryday.helper.PresenterLoader;
import com.tanmayvijayvargiya.factseveryday.models.Fact;
import com.tanmayvijayvargiya.factseveryday.models.User;
import com.tanmayvijayvargiya.factseveryday.presenters.HomePresenter;
import com.tanmayvijayvargiya.factseveryday.services.SharedPreferencesManager;
import com.tanmayvijayvargiya.factseveryday.singletons.UserSingleton;

import java.util.List;

public class ActivityHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListOfFactsFragment.OnFragmentInteractionListener ,
        LoaderManager.LoaderCallbacks<Presenter>{

    HomePresenter mPresenter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListOfFactsFragment discoverFragment;
    private ListOfFactsFragment favFragment;
    TextView loggedUserName, loggedUserEmail;
    ImageView loggedProfilePic;
    private final int LOADER_ID = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");


                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL

                    Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();
                }
            }
        };
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);

        loggedUserEmail = (TextView) navHeaderView.findViewById(R.id.logged_user_email);
        loggedUserName = (TextView) navHeaderView.findViewById(R.id.logged_user_name);
        loggedProfilePic = (ImageView) navHeaderView.findViewById(R.id.logged_user_profile);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);




    }


    public void setLoginDetails(){
        UserSingleton.getInstance().getLoggedInUser(this, new UserSingleton.Callback() {
            @Override
            public void success(User user) {
                if(user != null) {
                    loggedUserEmail.setText(user.getEmailId());
                    if(user.getName() != null)
                        loggedUserName.setText(user.getName().fullName());
                    Picasso.with(getApplicationContext()).load(user.getProfilePicUrl()).into(loggedProfilePic);
                }
            }

            @Override
            public void error(Throwable e) {

            }
        });
    }

    public void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), new ViewPagerAdapter.FragmentReferenceListener() {
            @Override
            public void onAllFactFragmentInstance(ListOfFactsFragment fragment) {
                discoverFragment = fragment;
                mPresenter.allFactsFragmentReady();
            }

            @Override
            public void onFavFactFragmentInstance(ListOfFactsFragment fragment) {
                favFragment = fragment;
                mPresenter.favFactsFragmentReady();
            }
        });
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    protected void onStart() {
        super.onStart();

        // Ready to use presenter
        mPresenter.onViewAttached(this);

    }

    @Override
    protected void onStop() {
        mPresenter.onViewDetached();
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferencesManager.setLoggedInUserid(this,null);
            SharedPreferencesManager.setLoggedInUserName(this,null);
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        if(id == R.id.action_search){
            startActivity(new Intent(this,SearchActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void navigateToLogin(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    @Override
    public void favButtonClicked(Fact fact, int mode) {
        if(mode == ListOfFactsFragment.ALL_FACTS_MODE){
            mPresenter.allFactsFavToggle(fact);
        }
        else{
            mPresenter.allFactsFavToggle(fact);
        }

    }

    @Override
    public void refreshFactsList(int mode) {
        if(mode == ListOfFactsFragment.ALL_FACTS_MODE){
            mPresenter.refreshAllFacts();
        }
        else{
            mPresenter.refreshAllFavFact();
        }

    }

    @Override
    public boolean endOfListReached(int mode) {
        if(mode == ListOfFactsFragment.ALL_FACTS_MODE)
            return mPresenter.appendFactsToHomeList();
        else
            return true;
    }

    @Override
    public void shareButtonClicked(Fact fact, int currentMode) {
        shareText(fact.getTitle(), fact.getContent());
    }

    private void shareText(String title, String content) {
        String shareContent = "**Fact : " + title + "\n\n**";
        shareContent = shareContent.concat(content.concat("\n\nShared via Facts - Learn Everyday"));

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TITLE, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareContent);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void showAllFacts(List<Fact> factList){
        discoverFragment.setFactList(factList);
    }

    public void showFavFacts(List<Fact> favFactList){
        favFragment.setFactList(favFactList);
    }

    public void favFactDataChanged(){
        favFragment.notifyDataSetChanged();
    }

    public void allFactsDataChanged() {
        discoverFragment.notifyDataSetChanged();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new PresenterLoader(this, new PresenterFactory() {
            @Override
            public Presenter create() {
                return new HomePresenter();
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<Presenter> loader, Presenter data) {
        this.mPresenter = (HomePresenter) data;
    }


    @Override
    public void onLoaderReset(Loader loader) {
        this.mPresenter = null;
    }

    public void noInternetConnection() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please Check Internet Connectivity.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
