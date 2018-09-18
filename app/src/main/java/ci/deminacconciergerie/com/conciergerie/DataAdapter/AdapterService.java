package ci.deminacconciergerie.com.conciergerie.DataAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ci.deminacconciergerie.com.conciergerie.R;
import ci.deminacconciergerie.com.conciergerie.Services;
import ci.deminacconciergerie.com.conciergerie.Validate;


public class AdapterService extends RecyclerView.Adapter<AdapterService.ViewHolder>  {


    private Context context;

    //List to store all services
    List<service> services;

    public AdapterService(List<service> services, Context context){

        super();
        //Getting all service
        this.services = services;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_modele, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        service service = services.get(position);
        holder.textViewLibelle.setText(service.getLibelle().toUpperCase());

         holder.textViewIdService.setText(service.getId());




        int i = position +1 ;

     //   Toast.makeText(context, ""+i, Toast.LENGTH_SHORT).show();

        holder.textViewCountService.setText(""+i);

        if((i % 2) == 0){

            //holder.linearLayout_count.setBackgroundColor(Color.parseColor("#BDBDBD"));
            holder.textViewCountService.setTextColor(context.getResources().getColor(R.color.color_or));
        }else {

            //holder.linearLayout_count.setBackgroundColor(Color.parseColor("#FF918C8C"));
            holder.textViewCountService.setTextColor(context.getResources().getColor(R.color.color_white));
        }



        holder.textViewLibelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Validate.class);
                intent.putExtra("id_service", ""+holder.textViewIdService.getText().toString());
                intent.putExtra("libelle_service", ""+holder.textViewLibelle.getText().toString());
                //Toast.makeText(context, ""+holder.textViewIdService.getText().toString(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return services.size();
    }



    public class ViewHolder  extends RecyclerView.ViewHolder{

        public TextView textViewLibelle;
        public TextView textViewIdService;
        public TextView textViewCountService;
        public LinearLayout linearLayout_count;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewLibelle = (TextView) itemView.findViewById(R.id.et_service_name);
            textViewIdService= (TextView)itemView.findViewById(R.id.txt_service_id);
            textViewCountService= (TextView)itemView.findViewById(R.id.text_count_service);
            linearLayout_count = (LinearLayout)itemView.findViewById(R.id.linear_count);
        }
    }
}
