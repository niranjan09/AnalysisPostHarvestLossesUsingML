package com.example.shreyas.newdemo;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shreyas on 12/17/15.
 */
public class MyFarmAdapter extends RecyclerView.Adapter<MyFarmAdapter.PersonViewHolder>{

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myfarm_cards, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        Log.d("oncrete", "c");

        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.Farmname.setText(farms.get(i).farm_name);

        Log.d("onbind", "c");
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d("onattach", "c");
    }
    @Override
    public int getItemCount() {
        Log.d("getitemc","c");
        return farms.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView Farmname;

        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.myfarmcv);
            Farmname = (TextView)itemView.findViewById(R.id.name_of_farm);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(view.getId()==R.id.farm_card_edit_btn)
                    {
                        Intent i=new Intent(view.getContext(),UpdateFarm.class).putExtra("FarmName",Farmname.getText());
                        view.getContext().startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(view.getContext(), MyFarm.class).putExtra("FarmName", Farmname.getText());
                        view.getContext().startActivity(i);

                    }
                }
            });
        }
    }
    List<Farm_info> farms;
    MyFarmAdapter(List<Farm_info> persons){
        this.farms = persons;
    }

}
