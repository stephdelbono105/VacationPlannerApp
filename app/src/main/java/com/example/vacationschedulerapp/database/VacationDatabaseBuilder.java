package com.example.vacationschedulerapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vacationschedulerapp.dao.CarDAO;
import com.example.vacationschedulerapp.dao.ExcursionDAO;
import com.example.vacationschedulerapp.dao.VacationDAO;
import com.example.vacationschedulerapp.entities.Car;
import com.example.vacationschedulerapp.entities.Excursion;
import com.example.vacationschedulerapp.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class, Car.class}, version=10, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO VacationDAO();
    public abstract ExcursionDAO ExcursionDAO();
    public abstract CarDAO carDAO();


   // public CarDAO carDAO() {return null;}


    // Database Instance
    private static volatile VacationDatabaseBuilder INSTANCE;



    // Check if INSTANCE exists, if exists old INSTANCE is updated
    static  VacationDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (VacationDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VacationDatabaseBuilder.class, "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }

        }
        return INSTANCE;

    }



}
