package ci.deminacconciergerie.com.conciergerie.DataAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import ci.deminacconciergerie.com.conciergerie.R;
import ci.deminacconciergerie.com.conciergerie.Services;
import io.realm.Realm;

public class CardAdapter  extends RecyclerView.Adapter<CardAdapter.ViewHolder>  {

    //Imageloader to load image
    private ImageLoader imageLoader;
    private Context context;

    //List to store all categorie
    List<categorie> categories;








    //Constructor of this class
    public CardAdapter(List<categorie> categories, Context context){
        super();
        //Getting all catégorie
        this.categories = categories;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorie_modele, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //Getting the particular item from the list
        categorie categorie =  categories.get(position);

        //Loading image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(categorie.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.drawable.datted, android.R.drawable.ic_dialog_alert));
        //imageLoader.get(categorie.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.drawable.datted, android.R.drawable.ic_dialog_alert));


        //Showing data on the views
        holder.imageView.setImageUrl(categorie.getImageUrl(), imageLoader);
        Picasso.get().load(categorie.getImageUrl()).into(holder.imageView);
        holder.textViewNom.setText(categorie.getNom().toUpperCase());
        holder.textViewDescription.setText(categorie.getDescription());
        holder.textViewId.setText(categorie.getId());
        
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();


               if(networkInfo != null && networkInfo.isConnected()){*/
                    Intent intent = new Intent(context, Services.class);
                    intent.putExtra("id", ""+holder.textViewId.getText().toString());
                    intent.putExtra("categorie", ""+holder.textViewNom.getText().toString());


                    context.startActivity(intent);

             /*   }else {
                    Snackbar snackbar = Snackbar
                            .make(holder.imageView, R.string.internet,  Snackbar.LENGTH_LONG);
                    snackbar.show();
                }*/



            }
        });



    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public NetworkImageView imageView;
        public TextView textViewNom;
        public TextView textViewDescription;
        public TextView textViewId;


        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.image_tv);
            textViewNom = (TextView) itemView.findViewById(R.id.img_nom);
            textViewDescription = (TextView) itemView.findViewById(R.id.img_description);
            textViewId = (TextView) itemView.findViewById(R.id.img_id);

        }
    }
}
