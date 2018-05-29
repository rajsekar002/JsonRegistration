package com.androindian.raj.jsonregistration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androindian.raj.jsonregistration.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

ActivityLoginBinding binding;
JSONObject jsonObject1=null;
ConnectionDetector connectionDetector;
String url="http://androindian.com/apps/example_app/api.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(
                Login.this,R.layout.activity_login);

        connectionDetector=new ConnectionDetector();

        binding.newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.isConnectingToInternet(Login.this)) {
                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("mobile", binding.editText5.getText().toString().trim());
                        jsonObject.put("pswrd", binding.editText6.getText().toString().trim());
                        jsonObject.put("baction", "login_user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    LoginUser loginUser = new LoginUser();
                    loginUser.execute(jsonObject.toString());


                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
                    alertDialog.setTitle("No internet");
                    alertDialog.show();
                }
            }
        });


    }

    private class LoginUser extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog=
                new ProgressDialog(Login.this);
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
                JSONObject j2=new JSONObject(jsonObject1.toString());
                String r=j2.getString("response");

                if(r.equalsIgnoreCase("success")){

                    SharedPreferences preferences=
                            getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("Mobile",binding.editText5.getText().toString());
                    editor.commit();


                    Toast.makeText(Login.this,
                            "sucess",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(Login.this,Welcome.class);
                    intent.putExtra("Mobile",binding.editText5.getText().toString());
                    startActivity(intent);


                }else if(r.equalsIgnoreCase("failed")){
                    Toast.makeText(Login.this,
                            "Failed",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(Login.this,
                            "Error",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
