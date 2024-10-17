package com.example.vacationschedulerapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.vacationschedulerapp.entities.Excursion;
import com.example.vacationschedulerapp.entities.Vacation;
//import com.example.vacationschedulerapp.entities.VacationWithExcursions;

import java.util.List;

@Dao
public interface VacationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM VACATIONS ORDER BY VACATIONID ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM VACATIONS WHERE VACATIONID = VACATIONID ORDER BY VACATIONID ASC")
    List<Vacation> getAllVacationsById();


    @Query("SELECT * FROM vacations WHERE vacationId = :vacationId")
    Vacation getVacationById(int vacationId);


}



