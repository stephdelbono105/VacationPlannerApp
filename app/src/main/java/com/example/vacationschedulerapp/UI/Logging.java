package com.example.vacationschedulerapp.UI;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vacationschedulerapp.R;
import com.example.vacationschedulerapp.entities.LogUtils;

public class Logging extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        TextView logTextView = findViewById(R.id.logTextView);
        EditText searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);

        logTextView.setText(LogUtils.getLogcatOutput());


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }


        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            String filteredLogs = LogUtils.searchLogcatOutput(query);
            logTextView.setText(filteredLogs);
        });
    }
}
