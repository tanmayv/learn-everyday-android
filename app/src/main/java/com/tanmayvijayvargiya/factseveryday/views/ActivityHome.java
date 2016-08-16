package com.tanmayvijayvargiya.factseveryday.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;
import com.squareup.picasso.Picasso;
import com.tanmayvijayvargiya.factseveryday.R;
import com.tanmayvijayvargiya.factseveryday.adapters.ViewPagerAdapter;
import com.tanmayvijayvargiya.factseveryday.app.Config;
import com.tanmayvijayvargiya.factseveryday.event.FactSyncedEvent;
import com.tanmayvijayvargiya.factseveryday.event.FactUpdatedEvent;
import com.tanmayvijayvargiya.factseveryday.event.FetchFactsEvent;
import com.tanmayvijayvargiya.factseveryday.event.UserSyncedEvent;
import com.tanmayvijayvargiya.factseveryday.gcm.GcmIntentService;
import com.tanmayvijayvargiya.factseveryday.job.FetchFactsJob;
import com.tanmayvijayvargiya.factseveryday.job.RegisterWithServerGCMJob;
import com.tanmayvijayvargiya.factseveryday.job.SyncUserJob;
import com.tanmayvijayvargiya.factseveryday.job.UpdateFactJob;
import com.tanmayvijayvargiya.factseveryday.model.FactModel;
import com.tanmayvijayvargiya.factseveryday.model.UserModel;
import com.tanmayvijayvargiya.factseveryday.services.SharedPreferencesManager;
import com.tanmayvijayvargiya.factseveryday.utils.TimeUtil;
import com.tanmayvijayvargiya.factseveryday.vo.Fact;
import com.tanmayvijayvargiya.factseveryday.vo.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityHome extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListOfFactsFragment.OnFragmentInteractionListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListOfFactsFragment discoverFragment;
    private ListOfFactsFragment favFragment;
    TextView loggedUserName, loggedUserEmail;
    CircleImageView loggedProfilePic;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private final int LOADER_ID = 9000;
    User mUser;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Inject
    UserModel userModel;

    @Inject
    JobManager jobManager;

    @Inject
    EventBus mEventBus;

    @Inject
    FactModel mFactModel;

    List<Fact> allFacts = new ArrayList<Fact>();
    List<Fact> favFacts = new ArrayList<Fact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userId = SharedPreferencesManager.getLoggedInUserId(this);
        if(userId ==null ){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        setContentView(R.layout.activity_activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getComponent().inject(this);
        mEventBus.register(this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");
                    jobManager.addJobInBackground(new RegisterWithServerGCMJob(new Params(1).requireNetwork(),token));

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL

                    Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();
                }
            }
        };
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
        loggedProfilePic = (CircleImageView) navHeaderView.findViewById(R.id.logged_user_profile);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setLoginDetails();
        setupViewPager();
        jobManager.addJob(new SyncUserJob(new Params(1).requireNetwork().persist()));

        jobManager.addJob(new FetchFactsJob(new Params(1).requireNetwork().persist()));

        if(checkPlayServices()){
            registerGCM();
        }
    }

    private void refreshFromDiskFacts() {

        allFacts = mFactModel.loadAll();

        for(Fact tmp: allFacts){
            boolean contains = favFacts.contains(tmp);
            if(contains){
                if(tmp.isSynced && tmp.isFavorite){
                    favFacts.remove(tmp);
                    favFacts.add(0,tmp);
                }
                if(!tmp.isFavorite){
                    favFacts.remove(tmp);
                }
            }else{
                if(tmp.isFavorite){
                    favFacts.add(0,tmp);
                }
            }

        }
        if(discoverFragment!= null){
            Log.d("Frag","Discover fragment is updated " + allFacts.size());
            discoverFragment.setFactList(allFacts);
            discoverFragment.notifyDataSetChanged();
        }
        if(favFragment != null){
            Log.d("Frag","Fav fragment is updated " + favFacts.size());
            favFragment.setFactList(favFacts);
            favFragment.notifyDataSetChanged();
        }


    }

    public void onEventMainThread(FetchFactsEvent event){
        if(event.isSuccess()){
           refreshFromDiskFacts();
        }else{
            Log.d("Event", "Event is not success");
        }
    }

    public void onEventMainThread(FactUpdatedEvent e){
        if(TimeUtil.shouldReload())
        refreshFromDiskFacts();
    }

    public void onEventMainThread(FactSyncedEvent e){
        //refreshFromDiskFacts();
    }

    public void onEventMainThread(UserSyncedEvent e){

        if(TimeUtil.shouldReload())
            refreshFromDiskFacts();
    }



    @Override
    void cancelPendingJobsOnStop() {
       // jobManager.cancelJobs(TagConstraint.ALL);
    }


    public void setLoginDetails(){


        User user = userModel.getLoggedInUser();
        try{

        }catch (Exception e){

        }
        String userName = SharedPreferencesManager.getLoggedInUserName(this);
        String userEmail = SharedPreferencesManager.getLoggedInUserEmail(this);
        String userProfile = SharedPreferencesManager.getLoggedInUserprofile(this);
        loggedUserEmail.setText(userEmail);
        loggedUserName.setText(userName);

        Picasso.with(getApplicationContext()).load(userProfile).into(loggedProfilePic);
    }

    public void setupViewPager() {
        if(viewPager.getAdapter() == null) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), new ViewPagerAdapter.FragmentReferenceListener() {
                @Override
                public void onAllFactFragmentInstance(ListOfFactsFragment fragment) {
                    discoverFragment = fragment;
                }

                @Override
                public void onFavFactFragmentInstance(ListOfFactsFragment fragment) {
                    favFragment = fragment;
                }
            });
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        Log.d("lifecycle", "onResume");
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
        if(id == R.id.nav_logout){
            SharedPreferencesManager.setLoggedInUserid(this, null);
            SharedPreferencesManager.setLoggedInUserName(this, null);
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            return true;
        }
        if(id == R.id.nav_rate_us){
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void favButtonClicked(Fact fact, int mode) {
        Log.d("Fav","Add to fav " + fact.getTitle() + " " + fact.isFavorite);
        jobManager.addJob(new UpdateFactJob(new Params(1).requireNetwork().persist(), fact.get_id(), fact.isFavorite));
    }

    @Override
    public void refreshFactsList(int mode) {
        jobManager.addJob(new SyncUserJob(new Params(1).requireNetwork().persist()));
        jobManager.addJob(new FetchFactsJob(new Params(1).requireNetwork().persist()));
        if(TimeUtil.shouldReload())
            refreshFromDiskFacts();
    }

    @Override
    public boolean endOfListReached(int mode) {
        return false;
    }

    @Override
    public void shareButtonClicked(Fact fact, int currentMode) {
        shareText(fact.getTitle(),fact.getContent());
    }

    @Override
    public void navigateToFactViewActivity(Fact fact) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("SHIT", "This device is not supported. Google Play Services not installed!");
                Toast.makeText(this, "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        this.startService(intent);
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
