package com.example.vacationschedulerapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacationschedulerapp.entities.Car;

import java.util.List;

@Dao
public interface CarDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Car car);

    @Update
    void update(Car car);

    @Delete
    void delete(Car car);


    @Query("SELECT * FROM CARS ORDER BY carID ASC")
    LiveData<List<Car>> getAllCars();


    @Query("SELECT * FROM CARS WHERE vacationID=:vacation ORDER BY carID ASC")
    LiveData<List<Car>> getAssociatedCars(int vacation);

    @Query("SELECT * FROM CARS WHERE carID = :carID")
    Car getCarById(int carID);

    @Query("SELECT * FROM CARS WHERE vacationID = :vacationID")
    List<Car> getCarsByVacationId(int vacationID);
}
