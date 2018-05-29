package com.androindian.raj.jsonregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Welcome extends AppCompatActivity {

    JSONObject jsonObject1=null;
    ArrayList Title_list=new ArrayList();
    ListView listView;
    ArrayAdapter arrayAdapter;
    String url="http://androindian.com/apps/blog_links/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        listView=findViewById(R.id.list);

         arrayAdapter=new ArrayAdapter(
                Welcome.this,
                android.R.layout.simple_list_item_1,
                Title_list);


        Intent intent=getIntent();
        String m1=intent.getStringExtra("Mobile");

        SharedPreferences preferences=
                getSharedPreferences("Login",MODE_PRIVATE);
        String s1=preferences.getString("Mobile",null);

        Toast.makeText(Welcome.this,""+m1+s1,Toast.LENGTH_LONG).show();


        JSONObject j1=new JSONObject();
        try {
            j1.put("action","get_all_links");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ProgamList progamList=new ProgamList();
        progamList.execute(j1.toString());

    }

    private class ProgamList extends
            AsyncTask<String,String,String> {

        ProgressDialog progressDialog=
                new ProgressDialog(Welcome.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Data Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            jsonObject1=JsonFunctions.getJsonFromUrlparam(url,params[0]);
            Log.i("result",jsonObject1.toString());
            return jsonObject1.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            try {
                JSONObject j2=new JSONObject(String.valueOf(jsonObject1));
                JSONArray jsonArray=j2.getJSONArray("data");

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject j3=jsonArray.getJSONObject(i);

                    String title=j3.getString("title");

                    Title_list.add(title);
                                    }
                listView.setAdapter(arrayAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
