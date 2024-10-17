package com.example.vacationschedulerapp.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.icu.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationschedulerapp.R;
import com.example.vacationschedulerapp.entities.Vacation;

import java.util.List;
import java.util.Locale;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> mVacation;
    private final Context context;
    private final LayoutInflater mInflater;




    public VacationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }




    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;


        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView=itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String myFormat = "MM/dd/yy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    int position = getAdapterPosition();
                    final Vacation current = mVacation.get(position);

                    // Intent used to switch screens
                    Intent intent = new Intent(context,VacationDetails.class);

                    intent.putExtra("vacationId", current.getVacationId());
                    intent.putExtra("vacationTitle", current.getVacationTitle());
                    intent.putExtra("vacationLodging", current.getVacationLodging());
                    intent.putExtra("vacationStartDate",current.getVacationStartDate());
                    intent.putExtra("vacationEndDate",current.getVacationEndDate());
                    intent.putExtra("position", position);

                    // Go to next screen
                    context.startActivity(intent);

                }
            });
        }
    }


    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.activity_vacation_list_item,parent,false);
        return new VacationViewHolder(itemView);
    }



    // onBindViewHolder where we put what we want to display in recyclerview
    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if(mVacation != null) {
            Vacation current = mVacation.get(position);
            String title = current.getVacationTitle();
            holder.vacationItemView.setText(title);
        }
        else {
            holder.vacationItemView.setText("No Title");
        }

    }

    public void setVacationTitle(List<Vacation> title) {
        mVacation = title;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        if (mVacation != null) {
            return mVacation.size();
        } else return 0;
    }



    public void setVacations(List<Vacation> vacations) {
        mVacation = vacations;
        notifyDataSetChanged();
    }


}
