package com.example.vacationschedulerapp.database;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.vacationschedulerapp.dao.CarDAO;
import com.example.vacationschedulerapp.dao.ExcursionDAO;
import com.example.vacationschedulerapp.dao.VacationDAO;
import com.example.vacationschedulerapp.entities.Car;
import com.example.vacationschedulerapp.entities.Excursion;
import com.example.vacationschedulerapp.entities.Vacation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Repository {
    private VacationDAO mVacationDAO;
    private ExcursionDAO mExcursionDAO;
    private CarDAO mCarDAO;
    private static final String TAG = "CarDetailsActivity";



    private List<Excursion> mAllExcursions;
    private List<Vacation> mAllVacations;
    private List<Vacation> mAllVacationsById;
    private List<Car> mAllCars;



    // Multi-threading
    private static int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mVacationDAO = db.VacationDAO();
        mExcursionDAO = db.ExcursionDAO();
        mCarDAO = db.carDAO();

    }

    public List<Vacation> getAllVacations() {
        databaseExecutor.execute(() -> {
            mAllVacations = mVacationDAO.getAllVacations();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllVacations;
    }

    public List<Vacation> getAllVacationsById() {
        databaseExecutor.execute(() -> {
            mAllVacationsById = mVacationDAO.getAllVacationsById();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllVacationsById;
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.insert(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.update(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> {

            //Fetch associated Cars for Vacation Deletion
           List<Car> associatedCars = mCarDAO.getCarsByVacationId(vacation.getVacationId());

            //Delete associated Cars
            for (Car car: associatedCars){
                mCarDAO.delete(car);
            }

            mVacationDAO.delete(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Excursion> getAllExcursions() {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllExcursions;
    }

    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // CARS

    public LiveData<List<Car>> getAllCars() {
        return mCarDAO.getAllCars();
    }

    public LiveData<List<Car>> getAssociatedCars(int vacationID) {
        Log.d(TAG, "getAssociatedCars called for vacationID: " + vacationID);
        return mCarDAO.getAssociatedCars(vacationID);
    }




    public LiveData<List<Car>> getmAllCars() {
        return mCarDAO.getAllCars();
    }

    public LiveData<List<Car>> getAvailableCars(int vacationId) {
        MutableLiveData<List<Car>> availableCarsList = new MutableLiveData<>();

        databaseExecutor.execute(() -> {
            List<Car> cars = new ArrayList<>();
            Vacation vacation = getVacationById(vacationId);

            if (vacation != null) {
                String vacationStartDate = vacation.getVacationStartDate();
                String vacationEndDate = vacation.getVacationEndDate();

                cars.add(new Car(1, "Toyota Corolla", -1, vacationStartDate + " - " + vacationEndDate));
                cars.add(new Car(2, "Honda Civic", -1, vacationStartDate + " - " + vacationEndDate));
                cars.add(new Car(3, "Ford Mustang", -1, vacationStartDate + " - " + vacationEndDate));
                cars.add(new Car(4, "Tesla Model 3", -1, vacationStartDate + " - " + vacationEndDate));
                cars.add(new Car(5, "Chevrolet Silverado", -1, vacationStartDate + " - " + vacationEndDate));
                cars.add(new Car(6, "Kia Soul", -1, vacationStartDate + " - " + vacationEndDate));
                cars.add(new Car(7, "Ford F150", -1, vacationStartDate + " - " + vacationEndDate));
                cars.add(new Car(8, "Tesla Model Y", -1, vacationStartDate + " - " + vacationEndDate));
                cars.add(new Car(9, "Toyota RAV4", -1, vacationStartDate + " - " + vacationEndDate));
                cars.add(new Car(10, "Honda Accord", -1, vacationStartDate + " - " + vacationEndDate));
            }

            availableCarsList.postValue(cars);
        });

        return availableCarsList;
    }

    private Vacation getVacationById(int vacationId) {
        // Use mVacationDAO to fetch the vacation from database
        return mVacationDAO.getVacationById(vacationId);
    }

    public void insert(Car car)
    {
        databaseExecutor.execute(() -> mCarDAO.insert(car));
        Log.d(TAG, "Adding car: " + car.toString());
    }

    public void update(Car car) {
        databaseExecutor.execute(() -> mCarDAO.update(car));
    }

    public void delete(Car car) {
        databaseExecutor.execute(() -> mCarDAO.delete(car));
    }

    public Car getCarById(int carId) {
        return mCarDAO.getCarById(carId);
    }

    public void saveCar(Car car) {
        databaseExecutor.execute(() -> {
            Car existingCar = mCarDAO.getCarById(car.getCarID());
            if (existingCar == null) {
                mCarDAO.insert(car);
            } else {
                mCarDAO.update(car);
            }
        });
    }



}




