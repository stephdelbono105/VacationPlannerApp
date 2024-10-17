package com.example.vacationschedulerapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vacationschedulerapp.database.Repository;
import com.example.vacationschedulerapp.entities.Car;

import java.util.ArrayList;
import java.util.List;

public class CarViewModel extends ViewModel {
    private Repository repository;
    private LiveData<List<Car>> associatedCars;

    public CarViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<List<Car>> getAssociatedCars(int vacationID) {
        if (associatedCars == null) {
            associatedCars = repository.getAssociatedCars(vacationID);
        }
        return associatedCars;
    }

    public LiveData<List<Car>> getAvailableCars(int vacationID) {
        return repository.getAvailableCars(vacationID);
    }

    public void saveCar(Car car) {
        repository.saveCar(car);
    }








}



