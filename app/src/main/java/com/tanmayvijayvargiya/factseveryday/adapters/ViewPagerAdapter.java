package com.tanmayvijayvargiya.factseveryday.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.tanmayvijayvargiya.factseveryday.views.ListOfFactsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanmayvijayvargiya on 10/07/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public interface FragmentReferenceListener{
        void onAllFactFragmentInstance(ListOfFactsFragment fragment);
        void onFavFactFragmentInstance(ListOfFactsFragment fragment);
    }
    private FragmentReferenceListener mListener;

    private ListOfFactsFragment allFactFragment;

    public ListOfFactsFragment getFavFactFragment() {
        return favFactFragment;
    }

    public ListOfFactsFragment getAllFactFragment() {
        return allFactFragment;
    }

    private ListOfFactsFragment favFactFragment;
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager, FragmentReferenceListener listener) {
        super(manager);
        this.mListener = listener;
    }


    @Override
    public Fragment getItem(int position) {
        Log.d("Frag", "Fetch me a fragment at "  + position);
        switch (position){
            case 0 :
                return ListOfFactsFragment.newInstance(ListOfFactsFragment.ALL_FACTS_MODE);
            case 1 : return ListOfFactsFragment.newInstance(ListOfFactsFragment.FAV_FACTS_MODE);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createFragemnt = (Fragment) super.instantiateItem(container, position);
        if(createFragemnt == null){
            Log.d("Frag", "Created Fragment is null "  + position);
        }else{
            Log.d("Frag", "Created Fragment is not null. " + position);
        }
        switch (position){
            case 0 : allFactFragment = (ListOfFactsFragment) createFragemnt;
                mListener.onAllFactFragmentInstance(allFactFragment);
                break;
            case 1 : favFactFragment = (ListOfFactsFragment) createFragemnt;
                mListener.onFavFactFragmentInstance(favFactFragment);
                break;
        }
        return createFragemnt;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 : return "Discover";
            case 1 : return "Favourite";
        }
        return null;
    }
}
