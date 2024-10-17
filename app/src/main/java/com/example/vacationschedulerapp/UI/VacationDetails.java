package com.example.vacationschedulerapp.UI;

import static java.nio.file.Paths.get;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Query;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vacationschedulerapp.R;
import com.example.vacationschedulerapp.dao.CarDAO;
import com.example.vacationschedulerapp.database.Repository;
import com.example.vacationschedulerapp.entities.Car;
import com.example.vacationschedulerapp.entities.Excursion;
import com.example.vacationschedulerapp.entities.Vacation;
import com.example.vacationschedulerapp.viewmodel.CarViewModel;
import com.example.vacationschedulerapp.viewmodel.CarViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.vacationschedulerapp.UI.CarAdapter;
import com.example.vacationschedulerapp.UI.CarDetails;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class VacationDetails extends AppCompatActivity {

    private static final String TAG = "VacationDetailsActivity";


    //    EditText editId;
    EditText editTitle;
    EditText editLodging;
    EditText editStartDate;
    EditText editEndDate;


    int vacationId;
    String title;
    String lodging;
    String start_date;
    String end_date;


    Vacation currentVacation;
    int numExcursions = 0;
    int excursionId;
    private Repository repository;

    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;

    List<Car> selectedCars = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details_excursion_list);
        //    FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        repository = new Repository(getApplication());
        //editId = findViewById(R.id.)
        editTitle = findViewById(R.id.vacation_title);
        editLodging = findViewById(R.id.vacation_lodging);
        editStartDate = findViewById(R.id.vacation_startDate);
        editEndDate = findViewById(R.id.vacation_endDate);





        vacationId = getIntent().getIntExtra("vacationId", -1);

        String vacStringStart = getIntent().getStringExtra("vacStartDate");
        String vacStringEnd = getIntent().getStringExtra("vacEndDate");

        title = getIntent().getStringExtra("vacationTitle");
        lodging = getIntent().getStringExtra("vacationLodging");
        start_date = getIntent().getStringExtra("vacationStartDate");
        end_date = getIntent().getStringExtra("vacationEndDate");


        editTitle.setText(title);
        editLodging.setText(lodging);
        editStartDate.setText(start_date);
        editEndDate.setText(end_date);

        editStartDate = findViewById(R.id.vacation_startDate);




        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }






        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                updateLabelStart();
            }

        };

        editStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editEndDate = findViewById(R.id.vacation_endDate);

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                updateLabelEnd();
            }

        };

        editEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationId", vacationId);
                startActivity(intent);
            }
        });


        FloatingActionButton fab6 = findViewById(R.id.floatingActionButton6);
        fab6.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d("VacationDetails", "fab to open vehicle rental clicked");
                openCarDetails(vacationId);
                //Intent intent = new Intent(VacationDetails.this, CarDetails.class);
                //intent.putExtra("vacationId", vacationId);
               // startActivity(intent);
            }
        });


        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {


                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelStart();
            }

        };


        RecyclerView recyclerView = findViewById(R.id.excursion_recyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationId() == vacationId) filteredExcursions.add(e);
        }


        excursionAdapter.setExcursions(filteredExcursions);
        //excursionAdapter.setExcursions(repository.getAllExcursions());

        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
        final CarAdapter carAdapter = new CarAdapter(this);
        recyclerView2.setAdapter(carAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        // Observe LiveData<List<Car>>
        repository.getmAllCars().observe(this, newCars -> {
            List<Car> selectedCars = new ArrayList<>();
            for (Car c : newCars) {
                if (c.getVacationID() == vacationId) {
                    selectedCars.add(c);
                }
            }

            // Log the number of associated cars found
            Log.d("CarDetails", "Number of associated cars found: " + selectedCars.size());
            if (selectedCars.isEmpty()) {
                Log.w("CarDetails", "No associated cars found for vacationID: " + vacationId);
            } else {
                for (Car car : selectedCars) {
                    Log.d("CarDetails", "Associated car: " + car.getCarTitle());
                }
            }

            // Update the adapter with the filtered list of cars
            carAdapter.setmCars(selectedCars);
            Log.d("CarDetails", "Cars set to the adapter.");
        });


        }



    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    private boolean dateCheck() {

        Date startDateVac = new Date();
        try {
            startDateVac = new SimpleDateFormat("MM/dd/yy").parse(start_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date endDateVac = new Date();
        try {
            endDateVac = new SimpleDateFormat("MM/dd/yy").parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (startDateVac.before(endDateVac)) {
            return true;
        } else {
            return false;
        }
    }


    public boolean dateValidationStart(String start_date) {

        if (start_date.trim().equals("")) {
            return true;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            sdf.setLenient(false);
            try {
                Date validDate = sdf.parse(start_date);

            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(VacationDetails.this, "Enter Valid Date Format: MM/dd/yy", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;

        }

    }


    public boolean dateValidationEnd(String end_date) {

        if (end_date.trim().equals("")) {
            return true;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            sdf.setLenient(false);
            try {
                Date validDate = sdf.parse(end_date);

            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(VacationDetails.this, "Enter Valid Date Format: MM/dd/yy", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == R.id.vacationSave) {

            start_date = editStartDate.getText().toString();
            end_date = editEndDate.getText().toString();

// NOT SURE IF THIS NEEDS TO BE HERE
            Vacation vacation;


            if (dateCheck() && dateValidationStart(start_date) && dateValidationEnd(end_date)) {

                if (vacationId == -1) {

                    String title = editStartDate.getText().toString();
                    String lodging = editLodging.getText().toString();


                    if (repository.getAllVacations().size() == 0) vacationId = 1;

                    else
                        vacationId = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationId() + 1;

                    vacation = new Vacation(vacationId, editTitle.getText().toString(), editLodging.getText().toString(),
                            editStartDate.getText().toString(), editEndDate.getText().toString());

                    repository.insert(vacation);

                    Toast.makeText(VacationDetails.this, "Vacation Added Successfully", Toast.LENGTH_LONG).show();

                    this.finish();

                } else {

                    vacation = new Vacation(vacationId, editTitle.getText().toString(), editLodging.getText().toString(),
                            editStartDate.getText().toString(), editEndDate.getText().toString());

                    repository.update(vacation);

                    Toast.makeText(VacationDetails.this, "Vacation Updated Successfully", Toast.LENGTH_LONG).show();

                    this.finish();
                }

                return true;
            } else {
                Toast.makeText(VacationDetails.this, "End Date must be after Start Date", Toast.LENGTH_LONG).show();
                return false;
            }
        }

// TODO delete cars first to delete vacation // not letting me delete vacation with car associated
        if (item.getItemId() == R.id.vacationDelete) {
            for (Vacation vac : repository.getAllVacations()) {
                if (vac.getVacationId() == vacationId)
                    currentVacation = vac;
            }

            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getVacationId() == vacationId) ++numExcursions;
            }


            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
            }

            else {
                Toast.makeText(VacationDetails.this, "Cannot Delete Vacation with Associated Excursions", Toast.LENGTH_LONG).show();
            }
            return true;
        }




        if (item.getItemId() == R.id.vacationShare) {

            List<Excursion> filteredExcursions = new ArrayList<>();



            for (Excursion e : repository.getAllExcursions()) {

                if (e.getVacationId() == vacationId) filteredExcursions.add(e);

            }




                Intent sendIntent = new Intent();

                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent.putExtra(Intent.EXTRA_TEXT, editTitle.getText().toString() + " " + editLodging.getText().toString() + " " +
                        editStartDate.getText().toString() + " " + editEndDate.getText().toString() + " " + filteredExcursions + " ");

                sendIntent.putExtra(Intent.EXTRA_TITLE, "Check out this Vacation!");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;

            }



        if (item.getItemId() == R.id.vacStartAlert) {
            String dateFromScreen = editStartDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Long trigger = myDate.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("key", title + " is starting");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            } catch (Exception e) {

            }
            return true;
        }


        if (item.getItemId() == R.id.vacEndAlert) {
            String dateFromScreen = editStartDate.getText().toString();
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Long trigger = myDate.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("key", title + " is ending");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            } catch (Exception e) {

            }
            return true;

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onResume() {

        super.onResume();
        RecyclerView recyclerView=findViewById(R.id.excursion_recyclerview);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();

        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationId() == vacationId) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

        RecyclerView carRecyclerView = findViewById(R.id.recyclerView2);
        final CarAdapter carAdapter = new CarAdapter(this);
        carRecyclerView.setAdapter(carAdapter);
        carRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observe LiveData<List<Car>>
        repository.getmAllCars().observe(this, newCars -> {

            List<Car> selectedCars = new ArrayList<>();
            for (Car c : newCars) {
                if (c.getVacationID() == vacationId) selectedCars.add(c);
            }

            Log.d("VacationDetails", "Number of associated cars found: " + selectedCars.size());
            if (selectedCars.isEmpty()) {
                Log.w("VacationDetails", "No associated cars found for vacationID: " + vacationId);
            } else {
                for (Car car : selectedCars) {
                    Log.d("VacationDetails", "Associated car: " + car.getCarTitle());
                }
            }
            carAdapter.setmCars(selectedCars);
        });

    }


    private void openCarDetails(int vacationID) {
        Intent intent = new Intent(VacationDetails.this, CarDetails.class);intent.putExtra("vacationID", vacationID);

        Log.d("VacationDetails", "Starting CarDetails with vacationID: " + vacationID);

        // Log the intent content for debugging
        Log.d("VacationDetails", "Intent content: " + intent.getExtras());try {
            startActivity(intent);
            Log.d("VacationDetails", "CarDetails activity started successfully.");
        } catch (Exception e) {
            Log.e("VacationDetails", "Error starting CarDetails activity: " + e.getMessage());
        }
    }





}






