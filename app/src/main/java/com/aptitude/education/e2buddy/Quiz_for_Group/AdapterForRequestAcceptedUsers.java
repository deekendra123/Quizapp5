package com.aptitude.education.e2buddy.Quiz_for_Group;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;
import com.aptitude.education.e2buddy.ViewData.AcceptedUsersData;

import java.util.List;

public class AdapterForRequestAcceptedUsers extends RecyclerView.Adapter<AdapterForRequestAcceptedUsers.RequestAcceptedUsersHolder> {

    Context mCtx;
    List<AcceptedUsersData> list;

    public AdapterForRequestAcceptedUsers(Context mCtx, List<AcceptedUsersData> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    @NonNull
    @Override
    public RequestAcceptedUsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.request_accepted_users_list, parent, false);
        return new RequestAcceptedUsersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAcceptedUsersHolder holder, int position) {

        AcceptedUsersData acceptedUsersData = list.get(position);

        holder.username.setText(""+ acceptedUsersData.getUsername());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RequestAcceptedUsersHolder extends RecyclerView.ViewHolder {

        TextView username;
        public RequestAcceptedUsersHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tvusername);
        }
    }
}
