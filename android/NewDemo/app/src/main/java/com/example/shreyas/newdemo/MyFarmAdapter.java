package com.example.shreyas.newdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
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
        personViewHolder.temp.setText(farms.get(i).temp);
        personViewHolder.humi.setText(farms.get(i).Humi);
        personViewHolder.sm.setText(farms.get(i).sm);
        Log.d("Locat", farms.get(i).location_url);

        new DownloadImageTask(personViewHolder.imv1).execute(farms.get(i).location_url.replaceAll("\\\\",""));
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
        ImageButton edit_btn;

        ImageView imv1;

        TextView temp,humi,sm,n1,n2,n3;

        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.myfarmcv);
            Farmname = (TextView)itemView.findViewById(R.id.name_of_farm);
            edit_btn=(ImageButton)itemView.findViewById(R.id.farm_card_edit_btn);

            temp = (TextView)itemView.findViewById(R.id.temp);
            humi = (TextView)itemView.findViewById(R.id.humidity);
            sm = (TextView)itemView.findViewById(R.id.sm);

            n1 = (TextView)itemView.findViewById(R.id.name_temp);
            n2 = (TextView)itemView.findViewById(R.id.name_humidity);
            n3 = (TextView)itemView.findViewById(R.id.name_sm);

            imv1 = (ImageView)itemView.findViewById(R.id.location_display_on_card);

            n1.setText("T(°C)");
            n2.setText("RH(%)");
            n3.setText("SM(%)");

            edit_btn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent i=new Intent(v.getContext(),UpdateFarm.class).putExtra("FarmName",Farmname.getText());
                    v.getContext().startActivity(i);

                }
            });
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent i = new Intent(view.getContext(), MyFarm.class).putExtra("FarmName", Farmname.getText());
                    view.getContext().startActivity(i);
                }
            });
        }
    }
    List<Farm_info> farms;
    MyFarmAdapter(List<Farm_info> persons){
        this.farms = persons;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
