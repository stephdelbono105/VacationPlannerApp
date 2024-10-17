package com.example.vacationschedulerapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vacationschedulerapp.R;
import com.example.vacationschedulerapp.database.Repository;
import com.example.vacationschedulerapp.entities.Excursion;
import com.example.vacationschedulerapp.entities.Vacation;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    private static final String TAG = "ExcursionDetailsActivity";

            EditText editExcursionTitle;
    EditText editExcursionDate;

    int excursionId;
    int vacationId;
    String title;
    String date;

    String vacStartDate;
    String vacEndDate;

    DatePickerDialog.OnDateSetListener excursionDateCalendar;
    DatePickerDialog.OnDateSetListener alertDateCalendar;

    final Calendar myCalendar = Calendar.getInstance();

    Repository eRepository;

    int numExc;
    Excursion currentExc;
    Vacation currentVac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);



        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                Log.d("ExcursionDetails", String.format("Intent extra: %s = %s", key, value));
            }
        }


        eRepository = new Repository(getApplication());
        editExcursionTitle = findViewById(R.id.excursion_title);
        editExcursionDate = findViewById(R.id.excursion_date);


        vacationId = getIntent().getIntExtra("vacationId", -1);
        excursionId = getIntent().getIntExtra("excursionId", -1);
        title = getIntent().getStringExtra("excursionTitle");
        date = getIntent().getStringExtra("excursionStartDate");


        List<Vacation> myVacations = eRepository.getAllVacations();
        for (Vacation v : myVacations) {
            if (v.getVacationId() == vacationId) {
                vacStartDate = v.getVacationStartDate();
                vacEndDate = v.getVacationEndDate();
                break;
            }

        }

        if (title != null) {
            editExcursionTitle.setText(title);
            editExcursionDate.setText(date);
        }



        editExcursionDate = findViewById(R.id.excursion_date);
        excursionDateCalendar = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                updateLabelDate();
            }

        };

        editExcursionDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(ExcursionDetails.this, excursionDateCalendar, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        } // overrides back button and goes to onResume

        if (item.getItemId() == R.id.excursionSave) {

            date = editExcursionDate.getText().toString();

            if (dateCheckExcursion() && dateValidation(date)) {

                if (excursionId == -1) {

                    String title = editExcursionTitle.getText().toString();

                    Excursion newExcursion = new Excursion(0, vacationId, title, date);

                    eRepository.insert(newExcursion);

                    Toast.makeText(ExcursionDetails.this, "Excursion Added Successfully", Toast.LENGTH_LONG).show();

                    this.finish();

                } else if (excursionId >= 0) {

                    String title = editExcursionTitle.getText().toString();
                    String date = editExcursionDate.getText().toString();

                    Excursion newExcursion = new Excursion(excursionId, vacationId, title, date);

                    eRepository.update(newExcursion);

                    Toast.makeText(ExcursionDetails.this, "Excursion Updated Successfully", Toast.LENGTH_LONG).show();

                    this.finish();
                }

            } else {
                Toast.makeText(ExcursionDetails.this, "Must Enter Date within Vacation Date Range", Toast.LENGTH_LONG).show();
            }
            return true;
        }


        if (item.getItemId() == R.id.excursionDelete) {

            for (Excursion exc : eRepository.getAllExcursions()) {
                if (exc.getExcursionId() == excursionId) currentExc = exc;
            }

            eRepository.delete(currentExc);
            Toast.makeText(ExcursionDetails.this, currentExc.getExcursionTitle() + " was deleted", Toast.LENGTH_LONG).show();
            this.finish();
        }


        if (item.getItemId() == R.id.excursionAlert) {
            String title = editExcursionTitle.getText().toString();
            String dateFromScreen = editExcursionDate.getText().toString();
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
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                intent.putExtra("key", title);
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            } catch (Exception e) {

            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    public boolean dateCheckExcursion() {

        Date excursionStartDate = new Date();
        try {
            excursionStartDate = new SimpleDateFormat("MM/dd/yy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date startDateVac = new Date();
        try {
            startDateVac = new SimpleDateFormat("MM/dd/yy").parse(vacStartDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date endDateVac = new Date();
        try {
            endDateVac = new SimpleDateFormat("MM/dd/yy").parse(vacEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (startDateVac.after(excursionStartDate) || endDateVac.before(excursionStartDate)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean dateValidation(String date) {

        if (date.trim().equals("")) {
            return true;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            sdf.setLenient(false);
            try {
                Date validDate = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(ExcursionDetails.this, "Please enter a valid date.", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;

        }

    }


    private void updateLabelDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editExcursionDate.setText(sdf.format(myCalendar.getTime()));
    }


}









