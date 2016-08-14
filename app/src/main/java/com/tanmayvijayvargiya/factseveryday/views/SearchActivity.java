package com.tanmayvijayvargiya.factseveryday.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.tanmayvijayvargiya.factseveryday.R;
import com.tanmayvijayvargiya.factseveryday.adapters.QueryResultAdapter;
import com.tanmayvijayvargiya.factseveryday.models.Fact;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity implements QueryResultAdapter.QueryItemListener{

    RecyclerView queryResultRecyclerView;
    EditText queryEditText;
    QueryResultAdapter mAdapter;
    Subscription querySub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        queryResultRecyclerView = (RecyclerView) findViewById(R.id.query_result_recycler_view);
        queryEditText = (EditText) findViewById(R.id.query_edit_text);

        initRecyclerView();
        queryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Query ", "Query String " + s.toString());
                if(s.length() > 0){
                    if(querySub != null){
//                        if(!querySub.isUnsubscribed())
//                            querySub.unsubscribe();
                    }
                    querySub = LearnEverydayService.getInstance().getApi()
                            .queryFacts(s.toString())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<List<Fact>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(List<Fact> factList) {
                                    if(factList.size() < 1){
                                        List<Fact> testFacts = new ArrayList<Fact>();
                                        Fact fact = new Fact();
                                        fact.setTitle("Nothing Found");
                                        testFacts.add(fact);

                                        mAdapter.setQueryResult(testFacts);
                                    }else {
                                        mAdapter.setQueryResult(factList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        queryResultRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new QueryResultAdapter(this,this);
        queryResultRecyclerView.setAdapter(mAdapter);
        List<Fact> testFacts = new ArrayList<Fact>();
        Fact fact = new Fact();
        fact.setTitle("Nothing Found");
        testFacts.add(fact);

        mAdapter.setQueryResult(testFacts);
    }


    @Override
    public void onClickQueryItem(Fact fact) {
        if(fact.get_id() != null) {
            Bundle factBundle = new Bundle();
            factBundle.putString("factId", fact.get_id());
            factBundle.putString("factTitle", fact.getTitle());
            factBundle.putString("factContent", fact.getContent());

            Intent i = new Intent(this,FactViewActivity.class);
            i.putExtra("fact", factBundle);
            startActivity(i);
            finish();
        }
    }
}
