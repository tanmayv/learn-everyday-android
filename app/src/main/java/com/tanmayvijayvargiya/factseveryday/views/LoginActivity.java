package com.tanmayvijayvargiya.factseveryday.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;
import com.squareup.picasso.Picasso;
import com.tanmayvijayvargiya.factseveryday.R;
import com.tanmayvijayvargiya.factseveryday.event.UserSyncedEvent;
import com.tanmayvijayvargiya.factseveryday.job.FetchUserJob;
import com.tanmayvijayvargiya.factseveryday.services.SharedPreferencesManager;
import com.tanmayvijayvargiya.factseveryday.vo.User;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    EditText firstName, lastName, email, userId;
    AppCompatButton signup, login;
    private String mLoggedInUserId;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    @Inject
    EventBus mEventBus;

    @Inject
    JobManager jobManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getComponent().inject(this);
        mEventBus.register(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);

        ImageView bgImage = (ImageView) findViewById(R.id.background_image_login);
        Picasso.with(this).load(R.drawable.comfy_dog).into(bgImage);
    }

    @Override
    void cancelPendingJobsOnStop() {

    }

    public void navigateToHome(User user){
        Intent i = new Intent(this, ActivityHome.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Check Connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            User user = new User();
            user.setFullName(acct.getDisplayName(), "");
            user.setEmailId(acct.getEmail());
            try{
                if(acct.getPhotoUrl() != null)
                    user.setProfilePicUrl(acct.getPhotoUrl().toString());
            }catch (NullPointerException e){
                Log.d("Shit","No profile picture for the user");
            }

            SharedPreferencesManager.setLoggedInUserEmail(this, user.getEmailId());
            SharedPreferencesManager.setLoggedInUserName(this, user.getName().fullName());
            SharedPreferencesManager.setLoggedInUserprofile(this, user.getProfilePicUrl());


            jobManager.addJob(new FetchUserJob(new Params(10).requireNetwork().persist(),user.getName().fullName(), user.getEmailId()));
        } else {

        }
    }

    public void onEventMainThread(UserSyncedEvent e){
        Log.d("Event", "User Synced " + e.getUser().get_id());
        User user = e.getUser();
        SharedPreferencesManager.setLoggedInUserid(this, user.get_id());

        finish();
        startActivity(new Intent(getApplicationContext(), ActivityHome.class));
    }

    @Override
    protected void onStop() {
        Log.d("User","Un Registering");
        super.onStop();
        mEventBus.unregister(this);
    }
}
