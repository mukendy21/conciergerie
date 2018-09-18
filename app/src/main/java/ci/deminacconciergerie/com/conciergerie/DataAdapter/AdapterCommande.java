package ci.deminacconciergerie.com.conciergerie.DataAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import ci.deminacconciergerie.com.conciergerie.Inscription;
import ci.deminacconciergerie.com.conciergerie.Login;
import ci.deminacconciergerie.com.conciergerie.Principale;
import ci.deminacconciergerie.com.conciergerie.R;
import ci.deminacconciergerie.com.conciergerie.Services;
import ci.deminacconciergerie.com.conciergerie.manager.HttpParse;


public class AdapterCommande extends RecyclerView.Adapter<AdapterCommande.ViewHolder> {


    private Context context;

    //token
    String token = "PtUxgwmz4j";

    String finalResult ;
    String HttpURLs = "http://deminacconciergerie.com/api/commande/update.php";



    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    //List to store all services
    List<commande> commandes;



    public AdapterCommande(List<commande> commandes , Context context){

        super();
        //Getting all service
        this.commandes = commandes;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commandes_module, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final AdapterCommande.ViewHolder holder, int position) {

        commande commande = commandes.get(position);


        holder.textViewDateCommande.setText(context.getResources().getString(R.string.date_prestation)+" : "+commande.getDate_prestation());
        holder.textViewPrixCommande.setText(context.getResources().getString(R.string.prix)+" : "+commande.getBudget()+ " FCFA");
        holder.textViewEtatCommande.setText(commande.getEtat());
        holder.textViewLibelleCommande.setText(commande.getLibelle());
        holder.textViewIdCommande.setText(commande.getId());

        if(holder.textViewEtatCommande.getText().toString().equals("0")){
            holder.textViewEtatCommande.setText(context.getResources().getString(R.string.etat)+" : "+context.getResources().getString(R.string.en_cours));
            holder.textViewEtatCommande.setTextColor(Color.parseColor("#ff6d00"));
        }else if(holder.textViewEtatCommande.getText().toString().equals("-1")){
            holder.textViewEtatCommande.setText(context.getResources().getString(R.string.etat)+" : "+context.getString(R.string.c_annulee));
            holder.textViewEtatCommande.setTextColor(Color.parseColor("#82b1ff"));
            holder.btn_annuler_commande.setVisibility(View.GONE);
        }else if(holder.textViewEtatCommande.getText().toString().equals("1")){
            holder.textViewEtatCommande.setText(context.getResources().getString(R.string.etat)+" : "+context.getString(R.string.c_terminee));
            
            holder.textViewEtatCommande.setTextColor(Color.parseColor("#00c853"));
            holder.btn_annuler_commande.setVisibility(View.GONE);
        }




        //Toast.makeText(context, ""+holder.textViewIdCommande.getText().toString(), Toast.LENGTH_SHORT).show();



        
        holder.btn_annuler_commande.setOnClickListener(new View.OnClickListener() {
            String etat = "-1";
            String id  = (String) holder.textViewIdCommande.getText();
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(context)
                        .title(holder.textViewLibelleCommande.getText())
                        .content(R.string.commande_cancel)
                        .positiveText(context.getResources().getString(R.string.yes))
                        .negativeText(context.getResources().getString(R.string.no))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                                if(networkInfo != null && networkInfo.isConnected()) {
                                    CommandeUpdateFunction(token, id, etat);
                                    //Toast.makeText(context, ""+token+" "+id+" "+etat, Toast.LENGTH_SHORT).show();
                                }else{
                                    Snackbar snackbar = Snackbar
                                            .make(holder.btn_annuler_commande, R.string.internet,  Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                                //Toast.makeText(context, ""+token+" "+id_service+" "+etat, Toast.LENGTH_SHORT).show();
                            }
                        })


                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override// ... but they are not the same object
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return commandes.size();
    }


    public class ViewHolder  extends RecyclerView.ViewHolder{

        public TextView textViewIdCommande;
        public TextView textViewDateCommande;
        public TextView textViewPrixCommande;
        public TextView textViewEtatCommande;
        public TextView textViewLibelleCommande;
        public Button btn_annuler_commande;
        public CardView card_view_commande;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewIdCommande = (TextView) itemView.findViewById(R.id.id_commande);
            textViewDateCommande= (TextView)itemView.findViewById(R.id.date_commande);
            textViewPrixCommande= (TextView)itemView.findViewById(R.id.prix_commande);
            textViewEtatCommande= (TextView)itemView.findViewById(R.id.etat_commande);
            textViewLibelleCommande= (TextView)itemView.findViewById(R.id.libelle_commande);
            btn_annuler_commande = (Button)itemView.findViewById(R.id.btn_annuler_commande);
            card_view_commande = (CardView)itemView.findViewById(R.id.card_view1);

        }
    }







    public void CommandeUpdateFunction(final String token, final String  id ,final String etat){

        class CommandeUpdateFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(context,context.getString(R.string.wait),null,true,true);

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();


                final AdapterCommande.ViewHolder holder =null;
                String message = null;


                try {
                    JSONObject reader = new JSONObject(httpResponseMsg);
                    message = reader.getString("message");


                } catch (JSONException e) {
                    e.printStackTrace();
                }




                if(message.equals("commande was updated.")){
                    Toast.makeText(context, R.string.cancel_commade, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, Principale.class);
                    context.startActivity(intent);
                }



            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("token", params[0]);

                hashMap.put("id", params[1]);

                hashMap.put("etat",params[2]);


                finalResult = httpParse.postRequest(hashMap, HttpURLs);


                return finalResult;
            }
        }

        CommandeUpdateFunctionClass commandeUpdateFunctionClass= new CommandeUpdateFunctionClass();

        commandeUpdateFunctionClass.execute(token,  id ,etat);
    }
}
