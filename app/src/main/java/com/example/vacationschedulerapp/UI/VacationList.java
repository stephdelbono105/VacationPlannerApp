package com.example.vacationschedulerapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.vacationschedulerapp.R;
import com.example.vacationschedulerapp.database.Repository;
import com.example.vacationschedulerapp.entities.Excursion;
import com.example.vacationschedulerapp.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {

    private static final String TAG = "VacationListActivity";
    private Repository mRepository;
    int vacationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }




        setContentView(R.layout.activity_vacation_list);
        FloatingActionButton fab=findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        Log.d("Vacations","onCreate called");

        RecyclerView recyclerView = findViewById(R.id.vacation_recyclerview);
        mRepository = new Repository(getApplication());
        List<Vacation> mAllVacations = mRepository.getAllVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);

        // Instance of adapter
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        vacationAdapter.setVacations(mAllVacations);

        Log.d("Vacations","Vacation List set to adapter, count: " +mAllVacations.size());


       // System.out.print(getIntent().getStringExtra("test"));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        Log.d("Vacations", "onCreateOptionsMenu called");
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Vacations", "onResume called");
        List<Vacation> allVacations=mRepository.getAllVacations();
        RecyclerView recyclerView=findViewById(R.id.vacation_recyclerview);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(mRepository.getAllVacations());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

      if (item.getItemId()==R.id.sample){
      mRepository=new Repository(getApplication());
        //  Toast.makeText(VacationList.this,"put in sample data",Toast.LENGTH_LONG).show();
          Vacation vacation = new Vacation(0, "Vacation", "Lodging", "Start Date", "End Date");
          mRepository.insert(vacation);
          vacation = new Vacation(0, "Vacation2", "Lodging2", "Start Date2", "End Date2");
          mRepository.insert(vacation);

          Excursion excursion=new Excursion(0,1,"Excursion", "Start Date");
          mRepository.insert(excursion);
          excursion=new Excursion(0,2, "Excursion2", "Start Date2");
          mRepository.insert(excursion);

          return true;
      }
      if (item.getItemId()==android.R.id.home){
          this.finish();
          return true;
      }

      // navigation to Logging activity

        if (id == R.id.viewLogs) {
            Log.d("Vacations", "Navigating to Logging Activity");
            Intent intent = new Intent(VacationList.this, Logging.class);
            startActivity(intent);
            return true;
        }
      return super.onOptionsItemSelected(item);

    }
}