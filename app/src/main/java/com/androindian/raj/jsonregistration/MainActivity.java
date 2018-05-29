package com.androindian.raj.jsonregistration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androindian.raj.jsonregistration.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    JSONObject jsonObject1=null;
    ConnectionDetector cd;
    String url="http://androindian.com/apps/example_app/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(
                MainActivity.this,R.layout.activity_main);

        cd=new ConnectionDetector();

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cd.isConnectingToInternet(MainActivity.this)) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name", binding.editText.getText().toString().trim());
                        jsonObject.put("mobile", binding.editText2.getText().toString().trim());
                        jsonObject.put("email", binding.editText3.getText().toString().trim());
                        jsonObject.put("pswrd", binding.editText4.getText().toString().trim());
                        jsonObject.put("baction", "register_user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Registrion registrion = new Registrion();
                    registrion.execute(jsonObject.toString());
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("No internet");
                    alertDialog.show();
                }
            }

        });
    }

    private class Registrion extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog=
                new ProgressDialog(MainActivity.this);
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
                //JSONObject j3=new JSONObject(s.toString());
                JSONObject j2=new
                        JSONObject(jsonObject1.toString());

                String res1=j2.getString("response");
                if(res1.equalsIgnoreCase("failed")){
                    String res2=j2.getString("user");
                    Toast.makeText(MainActivity.this,""+res2,
                            Toast.LENGTH_LONG).show();

                }else if(res1.equalsIgnoreCase("success")){
                    String res3=j2.getString("user");
                    Toast.makeText(MainActivity.this,""+res3,
                            Toast.LENGTH_LONG).show();
                    //binding.editText.clearComposingText();
                    Intent intent=
                            new Intent(MainActivity.this,Login.class);
                    startActivity(intent);
                    finish();


                }else {
                    String res4=j2.getString("user");
                    Toast.makeText(MainActivity.this,""+res4,
                            Toast.LENGTH_LONG).show();


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /*@Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }*/
    }
}
