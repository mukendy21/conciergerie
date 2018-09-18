package ci.deminacconciergerie.com.conciergerie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ci.deminacconciergerie.com.conciergerie.manager.HttpParse;

public class password extends AppCompatActivity {

    private TextView back_btn , send_email_btn;

    private ImageView close_activity_password;


    private EditText et_email;


    String email;

    Boolean CheckEditText ;




    String token = "PtUxgwmz4j";
    String finalResult ;
   // String HttpURL = "http://deminac-api.herokuapp.com/client/forget_password.php";
    String HttpURL = "http://deminacconciergerie.com/api/client/forget_password.php";



    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);


        setContentView(R.layout.activity_password);
        back_btn = (TextView) findViewById(R.id.backToLoginBtn);
        send_email_btn = (TextView)findViewById(R.id.forgot_button);
        et_email = (EditText)findViewById(R.id.registered_emailid) ;

        close_activity_password = (ImageView)findViewById(R.id.close_activity_password);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(password.this, Login.class);
                startActivity(Intent);
                finish();
            }
        });


        send_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = manager.getActiveNetworkInfo();

                    if(networkInfo != null && networkInfo.isConnected()){
                        EmailSendFunction(token, email);
                    }
                }else {
                    Snackbar snackbar = Snackbar
                            .make(send_email_btn, R.string.internet,  Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });


        close_activity_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    public void CheckEditTextIsEmptyOrNot(){

        email = et_email.getText().toString();
        token = token.toString();

        if(TextUtils.isEmpty(email))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }




    // validating email id
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }







    public void EmailSendFunction(final String token , final String email){




        class EmailSendFunctionClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(password.this, getString(R.string.data), null, true, true);
            }


            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();



                String message = null;


                try {
                    JSONObject reader = new JSONObject(httpResponseMsg);
                    message = reader.getString("message");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(message.equals("ok")){
                    Toast.makeText(password.this, R.string.email_send_mail, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(password.this, Login.class));
                    finish();
                }else{
                  //  Toast.makeText(password.this, "L'email n'est pas valide", Toast.LENGTH_SHORT).show();

                    Snackbar snackbar = Snackbar
                            .make(send_email_btn, R.string.validate_email, Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();
                }



                //Toast.makeText(password.this,message.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {


                hashMap.put("token", params[0]);

                hashMap.put("email", params[1]);


                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }

        }

        EmailSendFunctionClass emailSendFunctionClass = new EmailSendFunctionClass();

        emailSendFunctionClass.execute(token , email);
    }
}
