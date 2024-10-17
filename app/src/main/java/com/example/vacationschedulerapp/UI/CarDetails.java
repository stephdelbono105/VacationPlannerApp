package com.example.vacationschedulerapp.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.vacationschedulerapp.R;
import com.example.vacationschedulerapp.database.Repository;
import com.example.vacationschedulerapp.entities.Car;
import com.example.vacationschedulerapp.viewmodel.CarViewModel;
import com.example.vacationschedulerapp.viewmodel.CarViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CarDetails extends AppCompatActivity {
    private static final String TAG = "CarDetailsActivity";
    private CarViewModel carViewModel;
    private Spinner spinnerCars;
    private int vacationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);


        spinnerCars = findViewById(R.id.carSpinner);

        Repository repository = new Repository(getApplication());


        // Create instance of the ViewModelFactory
        CarViewModelFactory factory = new CarViewModelFactory(repository);
        // Use the factory to instantiate the CarViewModel
        carViewModel = new ViewModelProvider(this, factory).get(CarViewModel.class);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        Log.d(TAG, "onCreate: Received vacationID: " + vacationID);

        carViewModel.getAvailableCars(vacationID).observe(this, cars -> {
            Log.d(TAG, "Observing available cars: " + cars.size() + " cars received for vacationID: " + vacationID);
            ArrayAdapter<Car> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCars.setAdapter(adapter);
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Log.d(TAG, "Home option selected, finishing activity.");
            finish();
            return true;
        }

        if (id == R.id.carshare) {
            Car selectedCar = (Car) spinnerCars.getSelectedItem();

            selectedCar.getCarTitle();

            Intent sendIntent = new Intent();

            sendIntent.setAction(Intent.ACTION_SEND);



            sendIntent.putExtra(Intent.EXTRA_TEXT, selectedCar.getCarTitle().toString());

            sendIntent.putExtra(Intent.EXTRA_TITLE, "Check out this Rental Car!");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;



        }


        else if (id == R.id.carsave) {
            Car selectedCar = (Car) spinnerCars.getSelectedItem();
            if (selectedCar != null) {
                Log.d(TAG, "Saving car: " + selectedCar.toString() + " for vacationID: " + vacationID);
                selectedCar.setVacationID(vacationID);
                carViewModel.saveCar(selectedCar);
                finish();
            } else {
                Log.d(TAG, "No car selected to save.");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
