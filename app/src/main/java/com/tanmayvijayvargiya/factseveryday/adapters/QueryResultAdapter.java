package com.tanmayvijayvargiya.factseveryday.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanmayvijayvargiya.factseveryday.R;
import com.tanmayvijayvargiya.factseveryday.vo.Fact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanmayvijayvargiya on 13/08/16.
 */
public class QueryResultAdapter extends RecyclerView.Adapter<QueryResultAdapter.ViewHolder> {

    List<Fact> queryResult;
    QueryItemListener mListener;
    Context context;

    public interface QueryItemListener{
        void onClickQueryItem(Fact fact);
    }
    public QueryResultAdapter(Context context, QueryItemListener listener) {
        this.context = context;
        this.mListener = listener;
    }

    public void setQueryResult(List<Fact> queryResult){
        this.queryResult = queryResult;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.query_result_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(queryResult.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        if(queryResult == null){
            queryResult = new ArrayList<Fact>();
        }

        return queryResult.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView contentTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.query_result_title);
            contentTextView = (TextView) itemView.findViewById(R.id.query_result_content);
        }

        public void bind(final Fact fact,final QueryItemListener mListener) {
            if(fact.getTitle() != null){
                titleTextView.setText(fact.getTitle());
            }
            if(fact.getContent() != null){
                contentTextView.setText(fact.getContent());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickQueryItem(fact);
                }
            });
        }
    }
}
