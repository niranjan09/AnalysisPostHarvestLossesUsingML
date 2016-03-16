package com.example.shreyas.newdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import static com.google.android.gms.internal.zzip.runOnUiThread;

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
        personViewHolder.n1.setText(""+farms.get(i).temp + "°C");
        personViewHolder.n2.setText(""+farms.get(i).Humi+"%");
        personViewHolder.n3.setText(""+farms.get(i).sm+"%");
        personViewHolder.n4.setText(""+farms.get(i).crop);
        personViewHolder.n5.setText("Seed type : "+farms.get(i).croptype);
        personViewHolder.n6.setText("Sowing Period : "+farms.get(i).sowingperiod);
        personViewHolder.n7.setText("Field Area : "+farms.get(i).area);
        personViewHolder.n8.setText("Hardware ID : "+farms.get(i).hwid);
        personViewHolder.donutProgress.setProgress(Integer.parseInt(farms.get(i).growth));
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
        CardView cv,cv2;
        TextView Farmname;
        ImageButton edit_btn;
        //com.github.lzyzsd.circleprogress.CircleProgress circle_progress;
        DonutProgress donutProgress;

        ImageView imv1;

        TextView n1,n2,n3,n4,n5,n6,n7,n8,n9;
        static int set = 0;

//        private static final Random RANDOM = new Random();
//        private LineGraphSeries<DataPoint> series;
//        BarGraphSeries<DataPoint> series1;
//        private int lastX = 0;


        private static void FetchPredictionsData(final TextView v,String Farmname)
        {
            if (MainActivity.ConnectedToNetwork) {

                JSONObject j = new JSONObject();
                try {

                    j.put("UserID",MainActivity.Global_Email_Id);
                    j.put("FarmName",Farmname);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = MainActivity.ServerIP + "/getfarmpredictions/";

                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.POST, url, j, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // the response is already constructed as a JSONObject!
                                try {
                                    response = response.getJSONObject("Android");
                                    String signinresult = response.getString("Result");
                                    if (signinresult.equals("Valid")) {

                                        String a = response.getString("HTP") + "\n"+response.getString("Disease");
                                        v.setText(a);
                                        Log.d("HT", a);





                                    }
                                    else {



                                    }
                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();


                            }
                        });
                MainActivity.getInstance().addToRequestQueue(jsonRequest);
            } else {
                Log.d("Login activity check", MainActivity.ConnectedToNetwork + "");

            }


        }




        public void expandOrCollapse(final View v,String exp_or_colpse) {

            if(exp_or_colpse.equals("expand"))
            {
                Log.d("cardview height " ,v.getHeight()+"");
                //anim = new TranslateAnimation(0.0f, 0.0f, 0.0f ,20.0f);
                v.setVisibility(View.VISIBLE);
                ValueAnimator animator = ValueAnimator.ofInt(0, 350);


                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //Update Height
                        int value = (Integer) valueAnimator.getAnimatedValue();

                        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                        layoutParams.height = value;
                        v.setLayoutParams(layoutParams);
                    }
                });

                animator.start();
            }
            else
            {
                ValueAnimator animator = ValueAnimator.ofInt(350, 0);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //Update Height
                        int value = (Integer) valueAnimator.getAnimatedValue();

                        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                        layoutParams.height = value;
                        v.setLayoutParams(layoutParams);
                    }
                });
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //Height=0, but it set visibility to GONE
                        v.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
                animator.start();
            }
        }
        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.myfarmcv);
            cv2 = (CardView)itemView.findViewById(R.id.myfarmcv2);
            Farmname = (TextView)itemView.findViewById(R.id.name_of_farm);
            edit_btn=(ImageButton)itemView.findViewById(R.id.farm_card_edit_btn);
          //  circle_progress = (com.github.lzyzsd.circleprogress.CircleProgress)itemView.findViewById(R.id.circle_progress);
            //circle_progress.setProgress(45);


            n1 = (TextView)itemView.findViewById(R.id.name_temp);
            n2 = (TextView)itemView.findViewById(R.id.name_humidity);
            n3 = (TextView)itemView.findViewById(R.id.name_sm);
            n4 = (TextView)itemView.findViewById(R.id.name_crop);

            n5 = (TextView)itemView.findViewById(R.id.croptype);
            n6 = (TextView)itemView.findViewById(R.id.sowingperiod);
            n7 = (TextView)itemView.findViewById(R.id.area);
            n8 = (TextView)itemView.findViewById(R.id.hwid);

            n9 = (TextView)itemView.findViewById(R.id.predictions);

            donutProgress = (DonutProgress)itemView.findViewById(R.id.arc_progress);

            imv1 = (ImageView)itemView.findViewById(R.id.location_display_on_card);
          //  imv1.setScaleX(0.5f);


            /*

            // we get graph view instance
            GraphView sm_graph = (GraphView)itemView.findViewById(R.id.sm_graph);
            GraphView weather_graph = (GraphView)itemView.findViewById(R.id.weather_graph);
            // data
            series = new LineGraphSeries<DataPoint>();
            series1 = new BarGraphSeries<DataPoint>();



            series1.setSpacing(50);

// draw values on top
            series1.setDrawValuesOnTop(true);
            series1.setValuesOnTopColor(Color.RED);
//series.setValuesOnTopSize(50);







            sm_graph.addSeries(series);
           // sm_graph.setScaleX(0.5f);
            //sm_graph.setScaleY(0.5f);

            weather_graph.addSeries(series1);
            //weather_graph.setScaleX(0.5f);
            //weather_graph.setScaleY(0.5f);

            // customize a little bit viewport
            Viewport viewport = sm_graph.getViewport();
            viewport.setYAxisBoundsManual(true);
            viewport.setMinY(0);
            viewport.setMaxY(10);
            viewport.setScrollable(true);

            Viewport viewport1 = weather_graph.getViewport();
            //viewport1.setYAxisBoundsManual(true);
            //viewport1.setMinY(0);
            //viewport1.setMaxY(10);
            //viewport1.setScrollable(true);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // we add 100 new entries
                    for (int i = 0; i < 100; i++) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //addEntry();
                                series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 10d), true, 10);
                                series1.resetData(new DataPoint[]{new DataPoint(1,5),new DataPoint(3,7)});
                            }
                        });

                        // sleep to slow down the add of entries
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            // manage error ...
                        }
                    }
                }
            }).start();

            */
            edit_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), UpdateFarm.class).putExtra("FarmName", Farmname.getText());
                    v.getContext().startActivity(i);

                }
            });
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    //Intent i = new Intent(view.getContext(), MyFarm.class).putExtra("FarmName", Farmname.getText());
                    //view.getContext().startActivity(i);



                    if(set==0) {
                        FetchPredictionsData(n9,Farmname.getText().toString());
                        expandOrCollapse(cv2, "expand");
                        set=1;
                    }
                    else
                    {
                        set=0;
                        expandOrCollapse(cv2,"collapse");
                    }

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
