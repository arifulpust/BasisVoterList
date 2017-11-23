package com.ariful.basisvotarlistapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ariful.basisvotarlistapp.R;
import com.ariful.basisvotarlistapp.interfaces.CheckInListener;
import com.ariful.basisvotarlistapp.model.Person;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dream71 on 13/11/2017.
 */

public class VoterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private ArrayList<String> posibilitirs = new ArrayList<>(
            Arrays.asList( new String[]{"Negative","Medium","Positive","Confirm","None"} ) );
    Context context;
    List<Person> persons;
    private static LayoutInflater inflater = null;

    CheckInListener checkInListener;
    public VoterAdapter(Context context,   List<Person> data,CheckInListener checkInListener) {
        this.context = context;
        this.persons = data;
        this.checkInListener = checkInListener;
        this.inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public VoterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_item, parent, false);


        return  new ViewHolder(view);
    }
    public void setPerson(List<Person> person) {
        this.persons = person;
        Log.e("setOffers",""+new Gson().toJson(person));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView VoterName;
        TextView company,isCalled;
        TextView address,isVisited,posibily;

        CardView data_holder;
        public ViewHolder(View itemView) {
            super(itemView);
            this.VoterName = (TextView)itemView.findViewById(R.id.name);
            this.isVisited = (TextView)itemView.findViewById(R.id.isVisited);
            this.isCalled = (TextView)itemView.findViewById(R.id.isCalled);
            this.company = (TextView)itemView.findViewById(R.id.company);
            this.address = (TextView)itemView.findViewById(R.id.address);
            this.posibily = (TextView)itemView.findViewById(R.id.posibily);


            this.data_holder = (CardView) itemView.findViewById(R.id.data_holder);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        if(persons!=null)
        {

            final ViewHolder holder=(ViewHolder)holder1;
            if (position % 2 == 0) {
                holder.data_holder.setBackgroundColor(Color.parseColor("#f5f5f5"));
            }else{
                holder.  data_holder.setBackgroundColor(Color.parseColor("#E7F1FC"));
            }

            Person person = persons.get(position);

            if(person.isvisited==1)
            {
                holder.isVisited.setVisibility(View.VISIBLE);
                holder.isVisited.setText("Visited");
            }
            else
            {
                //holder.isVisited.setText("Not Visited");
                holder.isVisited.setVisibility(View.GONE);
            }
            if(person.isCalled==1)
            {
                holder.isCalled.setVisibility(View.VISIBLE);
                holder.isCalled.setText("Called");
            }
            else
            {
                //holder.isCalled.setText("Not Visited");
               holder.isCalled.setVisibility(View.GONE);
            }
//if(person.possiblity)

            if(posibilitirs.contains(person.possiblity))
            {
                holder.posibily.setText(person.possiblity);
            }
            else
            {
                holder.posibily.setText("None");
            }

            Log.e("voter info",posibilitirs.get(getPosibility(person.possiblity))+"   "+person.possiblity+"   "+person+new Gson().toJson(person));
            holder.VoterName.setText(""+person.name);
            holder.company.setText(""+person.company_name);
            holder.address.setText(""+person.address);
            holder. data_holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkInListener.userInfo(persons.get(position),position);
                }
            });

            holder. data_holder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    checkInListener.openPopUp(persons.get(position));
                    return true;
                }
            });

        }
    }
private int getPosibility(String item)
{
    if(item.trim().equals("")||item.trim().length()<1){
        return 4;
    }
    else if(item.trim().toLowerCase().equals("Negative"))
    {
        return 0;
    }
    else if(item.trim().toLowerCase().equals("Medium"))
    {
        return 1;
    }
    else if(item.trim().toLowerCase().equals("Positive"))
    {
        return 2;
    }
    else if(item.trim().toLowerCase().equals("Confirm"))
    {
        return 3;
    }
    else
    {
        return 4;
    }
}
    @Override
    public int getItemCount() {
        return persons.size();
    }


}