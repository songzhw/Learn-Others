package com.andremion.floatingnavigationview.sample;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.andremion.floatingnavigationview.FloatingNavigationView;

public class MainActivity extends AppCompatActivity {

    private FloatingNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navView = findViewById(R.id.floating_navigation_view);
        navView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navView.open();
            }
        });
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Snackbar.make((View) navView.getParent(), item.getTitle() + " Selected!", Snackbar.LENGTH_SHORT).show();
                navView.close();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (navView.isOpened()) {
            navView.close();
        } else {
            super.onBackPressed();
        }
    }

}
