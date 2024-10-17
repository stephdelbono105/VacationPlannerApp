package com.example.vacationschedulerapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.vacationschedulerapp.UI.ExcursionDetails;
import com.example.vacationschedulerapp.UI.VacationDetails;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Entity(tableName = "Excursions" )
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionId;
    private int vacationId;
    private String excursionTitle;
    private String excursionStartDate;

    @Override
    public String toString() {
        return "Excursion: " + excursionTitle + " - " + excursionStartDate + "\n";

    }

    public Excursion(int excursionId, int vacationId, String excursionTitle, String excursionStartDate) {
        this.excursionId = excursionId;
        this.vacationId = vacationId;
        this.excursionTitle = excursionTitle;
        this.excursionStartDate = excursionStartDate;
    }




    public int getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(int excursionId) {
        this.excursionId = excursionId;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public String getExcursionTitle() {
        return excursionTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }

    public String getExcursionStartDate() {
        return excursionStartDate;
    }

    public void setExcursionStartDate(String excursionStartDate) {
        this.excursionStartDate = excursionStartDate;
    }


}
