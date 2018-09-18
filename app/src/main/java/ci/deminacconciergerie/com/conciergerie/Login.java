package ci.deminacconciergerie.com.conciergerie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ci.deminacconciergerie.com.conciergerie.DataAdapter.User;
import ci.deminacconciergerie.com.conciergerie.manager.HttpParse;
import io.realm.Realm;

public class Login extends AppCompatActivity {

    Realm realm;

    User user;




    private EditText inputEmail, inputPassword;





    private Button btnSignIn , btnSignUp;   private Button btn_start;

    private TextView text_forget_password;

    private ImageView close_activity;

    private TextView deminac_text ,text_connexion;

    String email , password;

    String finalResult ;
    //String HttpURL = "http://192.168.1.21/deminac/login.php";
    String HttpURL = "http://deminacconciergerie.com/api/client/connect.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserEmail = "";
    public static final String UserId = "";

    String token = "PtUxgwmz4j";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Deconnexion
        //realm.delete(User.class);

        realm = Realm.getDefaultInstance();


        user = realm.where(User.class).findFirst();
        if(user != null){
            startActivity(new Intent(Login.this , Principale.class));
            return;
        }


        inputEmail = (EditText) findViewById(R.id.login_emailid);
        inputPassword = (EditText) findViewById(R.id.login_password);

        btnSignUp = (Button)findViewById(R.id.btn_sign_up);
        //btn_start = (Button)findViewById(R.id.btn_start);
        deminac_text = (TextView)findViewById(R.id.deminac_text);
        text_connexion = (TextView)findViewById(R.id.text_connexion);

        Typeface myTypeface = Typeface.createFromAsset(getAssets() , "coneria.ttf");
        deminac_text.setTypeface(myTypeface);
        text_connexion.setTypeface(myTypeface);

        close_activity = (ImageView)findViewById(R.id.close_activity);


        text_forget_password = (TextView)findViewById(R.id.text_forget_password);

        text_forget_password.setPaintFlags(text_forget_password.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        text_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Intent = new Intent(Login.this, password.class);
                startActivity(Intent);
                finish();

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(Login.this, Inscription.class);
                startActivity(Intent);
            }
        });

       /* btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(Login.this , Principale.class));
            }
        });*/


        close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignIn = (Button) findViewById(R.id.btn_sign_in);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = manager.getActiveNetworkInfo();

                    if(networkInfo != null && networkInfo.isConnected()){
                        UserLoginFunction(token , email, password);

                    }else {
                        Snackbar snackbar = Snackbar
                                .make(btnSignUp, R.string.internet,  Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }



                }
                else {

                    Snackbar snackbar = Snackbar
                            .make(btnSignUp, R.string.formulaire,  Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();


                }

            }
        });
    }




    public void CheckEditTextIsEmptyOrNot(){

        email = inputEmail.getText().toString();

        token = token.toString();

        password = inputPassword.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }



    public void UserLoginFunction(final String token , final String email, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Login.this,getString(R.string.patienter),null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();


                String id = null;
                String email = null;
                String tel = null;
                String nom = null;
                String prenoms = null;
                String password = null;

                try {
                    JSONObject reader = new JSONObject(httpResponseMsg);
                    id = reader.getString("id");
                    email = reader.getString("email");
                    nom = reader.getString("nom");
                    prenoms = reader.getString("prenoms");
                    tel = reader.getString("tel");
                    password = reader.getString("password");





                } catch (JSONException e) {
                    e.printStackTrace();
                }





               // if(httpResponseMsg.equalsIgnoreCase("Data Matched")){

                if(id != null){

                    finish();

                    Intent intent = new Intent(Login.this, Principale.class);
                    intent.putExtra("userEmail" , email);
                    intent.putExtra("userId" , id);
                    intent.putExtra("userTel" , tel);
                    intent.putExtra("userPassword", password);
                    intent.putExtra("userNom", nom);
                    intent.putExtra("userNom", prenoms);
                    //intent.putExtra(UserEmail,email);


                    user = new User();
                    user.setEmail(email);
                    user.setId(id);
                    user.setNom(nom);
                    user.setPrenom(prenoms);
                    user.setPassword(password);
                    user.setTel(tel);


                    realm.beginTransaction();
                    realm.copyToRealm(user);
                    realm.commitTransaction();

                   // Toast.makeText(Login.this, ""+httpResponseMsg, Toast.LENGTH_SHORT).show();
                    startActivity(intent);


                }
                else{


                    Snackbar snackbar = Snackbar
                            .make(btnSignUp, R.string.email_pass_incorrect,  Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();




                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("token",params[0]);

                hashMap.put("email",params[1]);

                hashMap.put("password",params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(token , email ,password);



    }


    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(Login.this)
                .title("DEMINAC CONCIERGERIE")
                .content(R.string.leave_app)
                .positiveText(R.string.leave)
                .negativeText(R.string.stay)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        //Are you sure you want to log out?

                        finish();

                    }
                })


                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();

    }


}
