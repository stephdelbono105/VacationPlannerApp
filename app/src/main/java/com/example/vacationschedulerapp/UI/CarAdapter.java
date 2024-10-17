package com.example.vacationschedulerapp.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationschedulerapp.R;
import com.example.vacationschedulerapp.entities.Car;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    // ViewHolder for individual car rental items
    class CarViewHolder extends RecyclerView.ViewHolder {

        private final TextView carItemView;


        private CarViewHolder(View itemView) {
            super(itemView);
            carItemView = itemView.findViewById(R.id.textView4);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && mCars != null) {
                        final Car current = mCars.get(position);
                        Intent intent = new Intent(context, CarDetails.class);
                        intent.putExtra("id", current.getCarID());
                        intent.putExtra("title", current.getCarTitle());
                        intent.putExtra("vacationID", current.getVacationID());
                        intent.putExtra("carDate", current.getCarDate());
                        context.startActivity(intent);
                    }
                }
            });
        }

    }

    private List<Car> mCars;
    private final Context context;
    private final LayoutInflater mInflater;


    public CarAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public CarAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.car_list_item, parent, false);
        return new CarAdapter.CarViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CarAdapter.CarViewHolder holder, int position) {
        if (mCars != null) {
            Car current = mCars.get(position);
            holder.carItemView.setText(current.getCarTitle());
        } else {
            holder.carItemView.setText("No car title");
        }
    }

    //Sets the list of car rentals and notifies the adapter that the data has changed
    public void setmCars(List<Car> cars) {
        mCars = cars;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mCars != null) ? mCars.size() : 0;
    }
}