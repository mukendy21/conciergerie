package ci.deminacconciergerie.com.conciergerie;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class CompteFragment extends Fragment {

    Realm realm;

    User user;

    String token = "PtUxgwmz4j";
    String finalResult ;
    String HttpURL = "http://deminacconciergerie.com/api/client/update.php";
    Boolean CheckEditText ;



    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();


    String id = new String();
    String passwords = new String();
    String nom , prenoms , email , tel , password , password_current , password_old;


    private TextView txt_mon_compte;
    private Button btn_pass, btn_update_user;
    final CompteFragment c = this;


    private EditText et_nom , et_prenom , et_email , et_tel , et_old_password, et_new_password;



    public CompteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_compte, container, false);

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();


        txt_mon_compte = (TextView)v.findViewById(R.id.txt_mon_compte);
        btn_pass = (Button)v.findViewById(R.id.btn_pass);
        btn_update_user = (Button)v.findViewById(R.id.btn_update_user);

        et_nom = (EditText)v.findViewById(R.id.et_nom_mod);
        et_prenom = (EditText)v.findViewById(R.id.et_prenom_mod);
        et_email = (EditText)v.findViewById(R.id.et_email_mod);
        et_tel = (EditText)v.findViewById(R.id.et_tel_mod);
        et_old_password = ( EditText)v.findViewById(R.id.et_old_password_mod);
        et_new_password = (EditText)v.findViewById(R.id.et_new_password_mod);


        et_nom.setText(user.getNom().toString());
        et_prenom.setText(user.getPrenom().toString());
        et_tel.setText(user.getTel().toString());
        et_email.setText(user.getEmail().toString());
        id = user.getId().toString();
        passwords = user.getPassword();


      //  Toast.makeText(getContext(), ""+passwords, Toast.LENGTH_SHORT).show();






        btn_update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.


                    ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = manager.getActiveNetworkInfo();

                    if(networkInfo != null && networkInfo.isConnected()){
                        // UserRegisterFunction(token ,nom,prenoms, email, tel , password);
                        //

                        //Toast.makeText(getContext(), "ok"+et_old_password.getText().toString(), Toast.LENGTH_SHORT).show();


                        String edit_password = et_old_password.getText().toString();
                        String password_realm = passwords.toString();

                        if(edit_password.equals(password_realm)){



                            new MaterialDialog.Builder(getContext())
                                    .title(R.string.mon_compte)
                                    .content(R.string.modify)
                                    .positiveText(getActivity().getResources().getString(R.string.yes))
                                    .negativeText(getActivity().getResources().getString(R.string.no))
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            UserUpdateFunction(token, id , nom,prenoms, email, tel , password);


                                        }
                                    })


                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        }
                                    })
                                    .show();
                        }else{
                            Snackbar snackbar = Snackbar
                                    .make(btn_update_user, R.string.current_pass,  Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(Color.RED);
                            snackbar.show();



                        }
                       // Toast.makeText(getContext(), ""+token+" "+id+" "+nom+" "+prenoms+" "+" "+email+" "+tel+" "+password, Toast.LENGTH_SHORT).show();
                    }else {
                        Snackbar snackbar = Snackbar
                                .make(btn_update_user, R.string.internet,  Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }



                }
                else {

                    // If EditText is empty then this block will execute .
                    Snackbar snackbar = Snackbar
                            .make(btn_update_user, R.string.formulaire, Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();

                }

            }
        });






       /* Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets() , "coneria.ttf");
        txt_mon_compte.setTypeface(myTypeface);*/

        return v;
    }


    public void CheckEditTextIsEmptyOrNot(){

        nom = et_nom.getText().toString();
        prenoms = et_prenom.getText().toString();
        email = et_email.getText().toString();
        tel = et_tel.getText().toString();
        password = et_new_password.getText().toString();
        token = token.toString();
        password_old = et_old_password.getText().toString();


        if(TextUtils.isEmpty(nom) || TextUtils.isEmpty(prenoms)|| TextUtils.isEmpty(email) || TextUtils.isEmpty(tel) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password_old))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }


    public void UserUpdateFunction(final String id, final String token ,final String nom , final String prenoms , final String email , String tel , final String password){

        class UserUpdateFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(getContext(),getString(R.string.data),null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                String id = null;
                String nom = null;
                String prenoms = null;
                String email = null;
                String password = null;
                String tel = null;


                try {
                    JSONObject reader = new JSONObject(httpResponseMsg);
                    id = reader.getString("id");
                    nom = reader.getString("nom");
                    prenoms = reader.getString("prenoms");
                    email = reader.getString("email");
                    password = reader.getString("password");
                    tel = reader.getString("tel");






                    realm.beginTransaction();
                    user = realm.where(User.class).equalTo("id", id).findFirst();
                    user.setNom(nom);
                    user.setPrenom(prenoms);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setTel(tel);
                    realm.commitTransaction();

                    et_new_password.setText("");
                    et_old_password.setText("");



                } catch (JSONException e) {
                    e.printStackTrace();
                }






            }

            @Override
            protected String doInBackground(String... params) {

                /*hashMap.put("token", params[0]);

                hashMap.put("nom",params[1]);

                hashMap.put("prenoms",params[2]);

                hashMap.put("email",params[3]);

                hashMap.put("tel",params[4]);

                hashMap.put("password",params[5]);*/


                hashMap.put("token",params[0]);

                hashMap.put("id",params[1]);

                hashMap.put("nom",params[2]);

                hashMap.put("prenoms",params[3]);

                hashMap.put("email",params[4]);

                hashMap.put("tel",params[5]);

                hashMap.put("password",params[6]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserUpdateFunctionClass userUpdateFunctionClass = new UserUpdateFunctionClass();

        //userRegisterFunctionClass.execute(token ,nom ,prenoms, email , tel ,password);
        userUpdateFunctionClass.execute(id , token , nom ,prenoms, email , tel ,password);
    }





}
