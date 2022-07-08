package com.example.a02travel.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a02travel.R;
import com.example.a02travel.database.DatabaseHelper;
import com.example.a02travel.session.SessionManager;

import java.util.HashMap;

public class BookHotelActivity extends AppCompatActivity {
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_hotel);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        setupToolbar();
    }
    public void hotelMargo(View v){
        Intent i = new Intent(this, CheckinHotelActivity.class);
        i.putExtra("nmHotel", "HOTEL OYO MARGONDA");
        i.putExtra("harga", "250000");
        startActivity(i);
    }
    public void hotelBor(View v){
        Intent i = new Intent(this, CheckinHotelActivity.class);
        i.putExtra("nmHotel", "HOTEL BOROBUDUR JAKARTA");
        i.putExtra("harga", "461000");
        startActivity(i);
    }
    public void hotelAston(View v){
        Intent i = new Intent(this, CheckinHotelActivity.class);
        i.putExtra("nmHotel", "HOTEL ASTON JAKARTA");
        i.putExtra("harga", "573000");
        startActivity(i);
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbHotel);
        toolbar.setTitle("Booking Hotel");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
