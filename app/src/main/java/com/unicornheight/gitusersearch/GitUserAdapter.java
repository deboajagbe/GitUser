package com.unicornheight.gitusersearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by deboajagbe on 3/5/17.
 */

public class GitUserAdapter extends RecyclerView.Adapter<GitUserAdapter.GitUserAdapterViewHolder>  {

    Context mContext;
    private String[] mUserData;


    private final GitUserAdapterOnClickHandler mClickHandler;


    public interface GitUserAdapterOnClickHandler {
        void onClick(String wearDay);
    }


    public GitUserAdapter(@NonNull Context context, GitUserAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mContext = context;
    }


    public class GitUserAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mUser_name;
        ImageView mUser_image;



        public GitUserAdapterViewHolder(View itemView) {
            super(itemView);
            mUser_name = (TextView) itemView.findViewById(R.id.user_name);
            mUser_image = (ImageView) itemView.findViewById(R.id.user_image);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String clickData = mUserData[adapterPosition];
            mClickHandler.onClick(clickData);
        }
    }

    @Override
    public GitUserAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.user_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new GitUserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GitUserAdapterViewHolder holder, int position) {
        String userData = mUserData[position];
        String[] parts = userData.split("-");

        String username = parts[0];
        String userurl = parts[1];
        String userimage = parts[2];

        holder.mUser_name.setText(username);
            Glide.with(mContext).load(userimage)
                .placeholder(R.drawable.imageholder)
                .error(R.drawable.imageholder)
                .into(holder.mUser_image);
    }
    @Override
    public int getItemCount() {
        if (null == mUserData) return 0;
        return mUserData.length;


    }

    public void setUserData(String[] userData) {
        mUserData = userData;
        notifyDataSetChanged();
    }


}
