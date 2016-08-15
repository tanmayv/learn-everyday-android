package com.tanmayvijayvargiya.factseveryday.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanmayvijayvargiya.factseveryday.R;
import com.tanmayvijayvargiya.factseveryday.adapters.FactsListAdapter;
import com.tanmayvijayvargiya.factseveryday.models.Fact;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfFactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListOfFactsFragment extends Fragment implements FactsListAdapter.FactItemListener {

    public static final int ALL_FACTS_MODE = 0;
    public static final int FAV_FACTS_MODE = 1;
    private TextView internetErrorText;
    private List<Fact> factList;
    private int currentMode;
    private int previousTotal = 0;
    public boolean loading = true;
    private int visibleThreshold = 0;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private OnFragmentInteractionListener mListener;
    RecyclerView factListRecyclerView;
    FactsListAdapter factsListAdapter;
    SwipeRefreshLayout factSwipeRefreshView;


    public List<Fact> getFactList() {
        return factList;
    }

    public void setFactList(List<Fact> factList) {
        this.factList = factList;
        if(factsListAdapter != null) {
            factsListAdapter.setFactList(factList);
            internetErrorText.setVisibility(View.INVISIBLE);
        }

        notifyDataSetChanged();
    }



    public ListOfFactsFragment() {
        // Required empty public constructor
    }

    public static ListOfFactsFragment newInstance( int mode){
        ListOfFactsFragment frag = new ListOfFactsFragment();
        Bundle args = new Bundle();
        args.putInt("MODE", mode);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        factsListAdapter = new FactsListAdapter(getContext(), this);



        Log.d("Shit", "Fragment is created " + currentMode);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_facts, container, false);
        factSwipeRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.fact_swipe_refresh);
        factSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               mListener.refreshFactsList(currentMode);
            }
        });

        internetErrorText = (TextView) view.findViewById(R.id.no_internet_text);
        factListRecyclerView = (RecyclerView) view.findViewById(R.id.facts_list_recycler_view);

        initRecyclerView();
        Log.d("Shit", "Everythins is updated");
        currentMode = getArguments().getInt("MODE");
        return view;
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        factListRecyclerView.setLayoutManager(mLayoutManager);
        factListRecyclerView.setAdapter(factsListAdapter);
        factListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    visibleItemCount = factListRecyclerView.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (factSwipeRefreshView.isRefreshing()) {
                        if (totalItemCount > previousTotal) {
                            factSwipeRefreshView.setRefreshing(false);
                            previousTotal = totalItemCount;
                        }
                    }
                    Log.d("Equation", "VisibleItemCount " + visibleItemCount + " totalItemCount " + totalItemCount + " First VIsible item " + firstVisibleItem);
                    if (!factSwipeRefreshView.isRefreshing() && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached

                        Log.i("SHIT", "end called");

                        factSwipeRefreshView.setRefreshing(true);
                        mListener.endOfListReached(currentMode);
                    }
                }
            }
        });

    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void updateFactListView(){
        factSwipeRefreshView.setRefreshing(false);
        factsListAdapter.setFactList(factList);
        factsListAdapter.notifyDataSetChanged();
    }




    public void notifyDataSetChanged() {
        if(factsListAdapter != null)
            factsListAdapter.notifyDataSetChanged();
        factSwipeRefreshView.setRefreshing(false);
    }

    @Override
    public void favButtonClick(Fact fact) {
        Log.d("Shit","Fav clicked");
        mListener.favButtonClicked(fact, currentMode);
    }


    @Override
    public void shareButtonClick(Fact fact) {
        mListener.shareButtonClicked(fact, currentMode);
    }

    @Override
    public void navigateToFactViewActivity(Fact fact) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void favButtonClicked(Fact fact, int mode);
        void refreshFactsList(int mode);
        boolean endOfListReached(int mode);
        void shareButtonClicked(Fact fact, int currentMode);
    }
}
