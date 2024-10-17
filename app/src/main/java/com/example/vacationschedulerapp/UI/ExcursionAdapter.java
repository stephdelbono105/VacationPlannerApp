package com.example.vacationschedulerapp.UI;

import static com.example.vacationschedulerapp.R.*;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationschedulerapp.dao.ExcursionDAO;
import com.example.vacationschedulerapp.entities.Excursion;
import com.example.vacationschedulerapp.entities.Vacation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> mExcursion;

    private AdapterView.OnItemClickListener listener;

    private ExcursionDAO excursionDAO;


    private  Context context;
    private  LayoutInflater mInflater;




    class ExcursionViewHolder extends RecyclerView.ViewHolder {

        public final TextView excursionItemView;
    //    private final TextView excursionItemView2;

        public ExcursionViewHolder(View itemView) {
            super(Objects.requireNonNull(itemView));
            excursionItemView = itemView.findViewById(id.textView3);
        //    excursionItemView2 = itemView.findViewById(id.textView4);


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    String myFormat = "MM/dd/yy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


                    int position = getAdapterPosition();
                    final Excursion current = mExcursion.get(position);

                    Intent intent = new Intent(context, ExcursionDetails.class);

                    intent.putExtra("vacationId", current.getVacationId());
                    intent.putExtra("excursionId", current.getExcursionId());
                    intent.putExtra("excursionTitle", current.getExcursionTitle());
                    intent.putExtra("excursionStartDate", current.getExcursionStartDate());
                    context.startActivity(intent);
                }
            });
        }
    }
    public ExcursionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public ExcursionAdapter.ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(layout.activity_excursion_list_item, parent,false);
        return new ExcursionAdapter.ExcursionViewHolder((itemView));
    }


    @Override
    public void onBindViewHolder(@NonNull ExcursionAdapter.ExcursionViewHolder holder,int position) {

        if(mExcursion != null) {
            Excursion current = mExcursion.get(position);
            String title =  current.getExcursionTitle();
            holder.excursionItemView.setText(title);
        //    holder.excursionItemView2.setText(Integer.toString(current.getVacationId()));
        }
        else{
            holder.excursionItemView.setText("No Title");
        //    holder.excursionItemView2.setText("No Vacation ID");
        }
    }
    public void setVacationTitle(List<Excursion> title) {
        mExcursion = title;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        if(mExcursion != null)
            return mExcursion.size();
        else return 0;
    }
    public void setExcursions (List<Excursion> excursions){
        mExcursion = excursions;
        notifyDataSetChanged();
    }



}
