package com.davidaz.android.tutorial.popularmovies;
/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,h
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.davidaz.android.tutorial.popularmovies.utils.MoviesJsonUtils;
import com.davidaz.android.tutorial.popularmovies.utils.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * {@link PosterAdapter} exposes a list of weather forecasts to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {

    private static final String TAG = "Davidaz Adapter";
    private static int viewHolderCount;
    private ArrayList<Result> filmList;
    private final ListItemClickHandler mClickHandler;
    private Context mContext;


    public interface ListItemClickHandler {
        void onClick(Result weatherForDay);
    }
    public PosterAdapter(Context context, ListItemClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mContext = context;
    }

    @Override
    public PosterAdapter.PosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.poster_element;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + ++viewHolderCount);
        return new PosterAdapterViewHolder(view);
    }

    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView poster;
        private static final String TAG2 = "Davidaz ViewHolder";
        public PosterAdapterViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.ib_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Result result = filmList.get(adapterPosition);
            mClickHandler.onClick(result);
        }
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder posterAdapterViewHolder, int position) {
        Log.d(TAG, "imgPosterBindView");
        String path = filmList.get(position).getPoster_uri_path();
        Picasso.with(posterAdapterViewHolder.itemView.getContext())
                .load(path)
                .placeholder(R.drawable.img_poster)
                .error(R.drawable.img_poster)
                .into(posterAdapterViewHolder.poster);
    }

    @Override
    public int getItemCount() {
        if (null == filmList) return 0;
        return filmList.size();
    }
    public void setPosterData(ArrayList<Result> posterData) {
        Log.d(TAG, "Intentamos meter poster Data");
        if(null != filmList) {
            filmList.clear();
            filmList.addAll(posterData);
        }
        else{
            filmList = posterData;
        }
        Log.d(TAG, "Metemos Poster Data");
        notifyDataSetChanged();
    }
}