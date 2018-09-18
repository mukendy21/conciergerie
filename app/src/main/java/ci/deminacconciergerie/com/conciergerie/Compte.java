package ci.deminacconciergerie.com.conciergerie;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Compte extends AppCompatActivity {

    private TextView txt_mon_compte;
    private Button btn_pass;
    final Context c = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);


        txt_mon_compte = (TextView)findViewById(R.id.txt_mon_compte);
        btn_pass = (Button)findViewById(R.id.btn_pass);

        Typeface myTypeface = Typeface.createFromAsset(getAssets() , "coneria.ttf");
        txt_mon_compte.setTypeface(myTypeface);


        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.password_input_dialog_box, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogPassword = (EditText) mView.findViewById(R.id.et_password);
                final EditText userInputDialogPassword_confirm = (EditText) mView.findViewById(R.id.et_confirmPassword);
                final Button btn_annuler = (Button)mView.findViewById(R.id.btn_annuler);
                final Button btn_modifier = (Button)mView.findViewById(R.id.btn_modifier_bow);

                btn_annuler.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Compte.this, "Action annulée", Toast.LENGTH_SHORT).show();
                    }
                });


                btn_modifier.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Compte.this, "Mot de passse modifié", Toast.LENGTH_SHORT).show();
                    }
                });

               /* alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                            }
                        })


                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });*/

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });

    }
}
