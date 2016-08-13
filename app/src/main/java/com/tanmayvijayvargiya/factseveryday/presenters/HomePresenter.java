package com.tanmayvijayvargiya.factseveryday.presenters;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tanmayvijayvargiya.factseveryday.LearnEverydayApplication;
import com.tanmayvijayvargiya.factseveryday.gcm.GcmIntentService;
import com.tanmayvijayvargiya.factseveryday.helper.Presenter;
import com.tanmayvijayvargiya.factseveryday.models.Fact;
import com.tanmayvijayvargiya.factseveryday.models.User;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;
import com.tanmayvijayvargiya.factseveryday.services.SharedPreferencesManager;
import com.tanmayvijayvargiya.factseveryday.singletons.UserSingleton;
import com.tanmayvijayvargiya.factseveryday.views.ActivityHome;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tanmayvijayvargiya on 04/07/16.
 */
public class HomePresenter implements Presenter<ActivityHome>{
    private ActivityHome view;
    private String userId;
    private LearnEverydayService apiService;
    private final int factsCacheLimit = 40;
    private List<Fact> factsCache;
    private List<Fact> favFactList;
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int LOADER_ID = 9000;

    public HomePresenter(){

    }

    public void init(){

        if(!((LearnEverydayApplication)view.getApplication()).isNetworkAvailable()){
            view.noInternetConnection();
        }
        String userId = SharedPreferencesManager.getLoggedInUserId(view);
        if(userId == null){
            view.navigateToLogin();
        }else {
            view.setLoginDetails();
            this.userId = userId;
        }
        if (checkPlayServices()) {
            registerGCM();
        }

    }

    public void refreshAllFacts(){
        fetchAllFacts();
    }

    public void refreshAllFavFact(){
        fetchAllFavFacts();
    }

    private void fetchAllFavFacts() {
       UserSingleton.getInstance().getLoggedInUser(view, new UserSingleton.Callback() {
           @Override
           public void success(User user) {
               if (user != null) {
                   final List<String> favFactIds = user.getFavFacts();
                   if (favFactIds != null) {
                       if (favFactList == null)
                           favFactList = new ArrayList<Fact>();

                       for (String factId : favFactIds) {


                           Subscription sub = LearnEverydayService.getInstance().getApi()
                                   .getFact(factId)
                                   .subscribeOn(Schedulers.newThread())
                                   .observeOn(AndroidSchedulers.mainThread())
                                   .doOnError(new Action1<Throwable>() {
                                       @Override
                                       public void call(Throwable throwable) {

                                       }
                                   })
                                   .subscribe(new Observer<Fact>() {
                                       @Override
                                       public void onCompleted() {

                                           view.showFavFacts(favFactList);
                                           view.favFactDataChanged();
                                       }

                                       @Override
                                       public void onError(Throwable e) {

                                       }

                                       @Override
                                       public void onNext(Fact fact) {
                                           fact.isFavorite = true;
                                           if (!favFactList.contains(fact))
                                               favFactList.add(fact);

                                       }
                                   });

                       }
                   }
               }
           }

           @Override
           public void error(Throwable e) {
               Log.e("Shit", "Cant fetch user", e);
           }
       });


    }

    private void fetchAllFacts() {
        LearnEverydayService.getInstance().getApi()
                .getFacts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                })

                .subscribe(new Observer<List<Fact>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Shit", "Cant fetch facts", e);
                    }

                    @Override
                    public void onNext(List<Fact> factList) {

                        processAndDisplayFacts(factList);
                    }
                });
    }

    private void processAndDisplayFacts(List<Fact> factList){
        if(factsCache == null){
            factsCache = factList;
        }else{
            for (Fact fact: factList
                 ) {
                if(!factsCache.contains(fact)){
                    factsCache.add(0,fact);
                }
            }
            if(factsCache.size() > factsCacheLimit)
                factsCache = factsCache.subList(0,factsCacheLimit);
        }
        UserSingleton.getInstance().getLoggedInUser(view, new UserSingleton.Callback() {
            @Override
            public void success(User user) {
                if (user != null) {
                    List<String> favFactIds = user.getFavFacts();
                    if (favFactIds != null) {
                        for (Fact fact : factsCache
                                ) {
                            if (favFactIds.contains(fact.get_id())) {
                                fact.isFavorite = true;
                            }
                        }
                    }
                    view.showAllFacts(factsCache);
                }
            }

            @Override
            public void error(Throwable e) {
                Log.e("Shit", "Cant fetch user", e);
            }
        });

    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(view);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(view, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("SHIT", "This device is not supported. Google Play Services not installed!");
                Toast.makeText(view, "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                view.finish();
            }
            return false;
        }
        return true;
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(view, GcmIntentService.class);
        intent.putExtra("key", "register");
        view.startService(intent);
    }

    public void allFactsFavToggle(final Fact fact) {
        Log.d("Shit", "Function is calling");
        UserSingleton.getInstance().getLoggedInUser(view, new UserSingleton.Callback() {
            @Override
            public void success(User user) {
                if (user != null) {

                    List<String> userFavs = user.getFavFacts();
                    if (userFavs == null) {
                        userFavs = new ArrayList<String>();
                    }
                    if (userFavs.contains(fact.get_id())) {
                        Log.d("Shit", "removed from fact");
                        userFavs.remove(fact.get_id());
                        factsCache.get(factsCache.indexOf(fact)).isFavorite = false;
                        favFactList.remove(fact);
                        fact.isFavorite = false;
                    } else {
                        Log.d("Shit", "add to fav");
                        userFavs.add(fact.get_id());
                        favFactList.add(fact);
                        fact.isFavorite = true;
                    }
                    user.setFavFacts(userFavs);
                    LearnEverydayService.getInstance().getApi()
                            .updateUser(userId, user)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<User>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    fact.isFavorite = !fact.isFavorite;
                                }

                                @Override
                                public void onNext(User user) {
                                    UserSingleton.getInstance().setLoggedInUser(user);
                                    fetchAllFavFacts();
                                    view.favFactDataChanged();
                                    view.allFactsDataChanged();
                                }
                            });
                } else {
                    Log.d("Shit", "User data not exist");
                }
            }

            @Override
            public void error(Throwable e) {

            }
        });
    }

    @Override
    public void onViewAttached(ActivityHome view) {
        this.view = view;
        if(userId == null){
            init();
        }
        view.setupViewPager();


    }

    @Override
    public void onViewDetached() {
        this.view = null;
        compositeSubscription.unsubscribe();
    }

    @Override
    public void onDestroyed() {

    }

    public void allFactsFragmentReady() {
        if(factsCache != null){
            view.showAllFacts(factsCache);
        }else{
            fetchAllFacts();
        }
    }

    public void favFactsFragmentReady() {
        if(favFactList != null){
            view.showFavFacts(favFactList);
        }else{
            fetchAllFavFacts();
        }
    }


    public boolean appendFactsToHomeList() {
        Fact lastFact = factsCache.get(factsCache.size() - 1);
        LearnEverydayService.getInstance().getApi()
                .getFactsBetweenTimestamp(lastFact.getCreatedAt())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Fact>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Shit", "Done loading older facts");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Shit", e.getMessage());
                    }

                    @Override
                    public void onNext(List<Fact> factList) {

                        Log.d("Shit", "Number of facts fetched " + factList.size());
                        processAndDisplayFacts(factList);
                    }
                });

        return true;
    }
}
