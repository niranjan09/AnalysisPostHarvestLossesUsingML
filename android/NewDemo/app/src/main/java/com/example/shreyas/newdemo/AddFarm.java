package com.example.shreyas.newdemo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddFarm extends AppCompatActivity {

    private Spinner spinner1;
    private EditText f_name,f_area,hd_id;
    public static  Spinner spinner2;

    private ImageButton ib,ib1;
    private Calendar cal,cal1;
    private int day,day1;
    private int month,month1;
    private int year,year1;
    private EditText et,et1;

    public static String farm_name;
    public static String crop="Wheat";
    public static String type_of_crop;
    public static List<String> wheatlist;
    public static ArrayAdapter<String> dataAdapter1;
    public static int dateflag=0;
    public static String farm_area;
    public static String start_date,end_date;
    public static String hwid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farm);

        f_name = (EditText) findViewById(R.id.farm_name);
        f_area= (EditText) findViewById(R.id.area_farm);
        hd_id= (EditText) findViewById(R.id.hd_id);


        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);



        List<String> list = new ArrayList<String>();
        list.add("Wheat");
        list.add("Rice");
        list.add("Onion");

        wheatlist = new ArrayList<String>();
        wheatlist.add("Wheat1");
        wheatlist.add("Wheat2");
        wheatlist.add("Wheat3");
        wheatlist.add("Wheat4");
        ;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_dropdown_item_1line);

        spinner1.setAdapter(dataAdapter);

        // Spinner item selection Listener




        dataAdapter1 = new ArrayAdapter<String>
                (this, android.R.layout.simple_expandable_list_item_1,wheatlist);

        dataAdapter1.setDropDownViewResource
                (android.R.layout.simple_expandable_list_item_1);

        spinner2.setAdapter(dataAdapter1);

        addListenerOnSpinnerItemSelection();


        ib = (ImageButton) findViewById(R.id.imageButton1);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        et = (EditText)findViewById(R.id.startdate);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
                dateflag=0;
            }
        });


        ib1 = (ImageButton) findViewById(R.id.imageButton2);
        cal1 = Calendar.getInstance();
        day1 = cal1.get(Calendar.DAY_OF_MONTH);
        month1 = cal1.get(Calendar.MONTH);
        year1 = cal1.get(Calendar.YEAR);
        et1 = (EditText)findViewById(R.id.enddate);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
                dateflag=1;
            }
        });



        Button addfarm = (Button) findViewById(R.id.add_farm);
        addfarm.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                Log.d("Clicked", "btn clicked");
                attemptAddFarm();
            }
        });

    }
    public void addListenerOnSpinnerItemSelection(){

        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void attemptAddFarm()
    {

        Log.d("Inside adfarm","inside");
        farm_name = f_name.getText().toString();
        farm_area = f_area.getText().toString();
        crop=spinner1.getSelectedItem().toString();
        type_of_crop=spinner2.getSelectedItem().toString();
        start_date=et.getText().toString();
        end_date=et1.getText().toString();
        hwid=hd_id.getText().toString();

        if(MainActivity.ConnectedToNetwork)
        {

            JSONObject j = new JSONObject();
            try {
                j.put("AddFarmUID",MainActivity.Global_Email_Id);
                j.put("AddFarmName", farm_name);
                j.put("AddFarmArea", farm_area);
                j.put("AddFarmCrop", crop);
                j.put("AddFarmCropType", type_of_crop);
                j.put("AddFarmStartDate", start_date);
                j.put("AddFarmEndDate", end_date);
                j.put("AddFarmHWID", hwid);

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            String url = MainActivity.ServerIP + "/addfarm/";
            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.POST, url, j, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                response = response.getJSONObject("Android");
                                String signinresult = response.getString("Result");
                                if (signinresult.equals("Valid")) {
                                    Intent i = new Intent(AddFarm.this, MainActivity.class);


                                    startActivity(i);
                                    finish();
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

        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id)
    {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay)
        {
            if(dateflag==0)
            {
                et.setText(selectedDay + "/" + (selectedMonth + 1) + "/"
                        + selectedYear);

            }
            else
            {
                et1.setText(selectedDay + "/" + (selectedMonth + 1) + "/"
                        + selectedYear);
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddFarm.this,MainActivity.class );
        startActivity(i);
        finish();
    }
}

