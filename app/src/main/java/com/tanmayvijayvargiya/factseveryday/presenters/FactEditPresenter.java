package com.tanmayvijayvargiya.factseveryday.presenters;

import com.tanmayvijayvargiya.factseveryday.models.Fact;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;
import com.tanmayvijayvargiya.factseveryday.services.SharedPreferencesManager;
import com.tanmayvijayvargiya.factseveryday.views.FactEditActivity;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by tanmayvijayvargiya on 04/07/16.
 */
public class FactEditPresenter {
    private FactEditActivity view;
    private LearnEverydayService apiService;

    public FactEditPresenter(FactEditActivity view){
        this.view = view;
        apiService = LearnEverydayService.getInstance();
    }

    public void init(){

    }

    public void publishFact(String factTitle, String factContent){
        String authorName = SharedPreferencesManager.getLoggedInUserName(view);
        String authorId = SharedPreferencesManager.getLoggedInUserId(view);
        Fact fact = new Fact();
        fact.setTitle(factTitle);
        fact.setContent(factContent);
        fact.initAuthor(authorName, authorId);
        apiService.getApi()
                .createFact(fact)
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
                        view.navigateToHome();
                    }
                });
    }

}
