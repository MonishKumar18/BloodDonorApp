package com.example.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bloodbank.R;
import com.example.bloodbank.Utils.Endpoints;
import com.example.bloodbank.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginAcivity extends AppCompatActivity {
    EditText numberEt,passwordEt;
    Button submit_button;
    TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acivity);
        numberEt=findViewById(R.id.number);
        passwordEt=findViewById(R.id.password);
        submit_button=findViewById(R.id.submit_button);
        signupText=findViewById(R.id.sign_up_text);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAcivity.this,RegisterActivity.class));
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberEt.setError(null);
                passwordEt.setError(null);
                String number =numberEt.getText().toString();
                String password=passwordEt.getText().toString();
                if(isValid(number,password)){
                    login(number,password);
                }
            }
        });
    }
    private void login(final String number,final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!(response.equals("Invalid Credentials"))){

                    SharedPreferences prefs=getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putString("login", response);
                    editor.commit();

                    Toast.makeText(LoginAcivity.this,response,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginAcivity.this,MainActivity.class));
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("number",number).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city",response).apply();
                    LoginAcivity.this.finish();
                }else{
                    Toast.makeText(LoginAcivity.this,response,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginAcivity.this,"Something went Wrong:(",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", Objects.requireNonNull(error.getMessage()));
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("number",number);
                params.put("password",password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
    private boolean isValid(String number, String password){
        if(number.isEmpty()){
            showMessage("Empty Mobile Number");
            numberEt.setError("Empty Mobile Number");
            return false;
        }else if(password.isEmpty()){
            showMessage("Empty Password");
            passwordEt.setError("Empty Password");
            return false;
        }
        return true;
    }
    private void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
