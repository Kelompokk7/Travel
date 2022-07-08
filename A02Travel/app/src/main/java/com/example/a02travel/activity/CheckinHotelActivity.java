package com.example.a02travel.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a02travel.R;
import com.example.a02travel.database.DatabaseHelper;
import com.example.a02travel.session.SessionManager;

import java.util.Calendar;
import java.util.HashMap;

public class CheckinHotelActivity extends AppCompatActivity {

    protected Cursor cursor;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Spinner spinHotel, spinKasur, spinKamar;
    SessionManager session;
    String email;
    int id_hotel;
    public String sHotel, sTipekasur, sTanggal, eTanggal, sKamar;
    int hargaTotal;
    private EditText etTanggal, endTgl;
    private DatePickerDialog dpTanggal, cekoutTanggal;
    Calendar newCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        dbHelper = new DatabaseHelper(CheckinHotelActivity.this);
        db = dbHelper.getReadableDatabase();
        Intent getParam = getIntent();
        final String jgnHotel = getParam.getStringExtra("nmHotel");
        final String[] nmHotel = {getParam.getStringExtra("nmHotel")};

        final String[] tipeKasur = {"Double bed", "Twin bed"};
        final String[] jmlhKamar = {"1", "2", "3", "4", "5"};

        spinHotel = findViewById(R.id.jenengHotel);
        spinKasur = findViewById(R.id.tipeKasur);
        spinKamar = findViewById(R.id.totalKamar);

        ArrayAdapter<CharSequence> adapterAsal = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, nmHotel);
        adapterAsal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHotel.setAdapter(adapterAsal);

        ArrayAdapter<CharSequence> adapterTujuan = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, tipeKasur);
        adapterTujuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinKasur.setAdapter(adapterTujuan);

        ArrayAdapter<CharSequence> adapterDewasa = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, jmlhKamar);
        adapterDewasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinKamar.setAdapter(adapterDewasa);
        spinHotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sHotel = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//
        spinKasur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sTipekasur = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//
        spinKamar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sKamar = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnBook = findViewById(R.id.book);

        etTanggal = findViewById(R.id.tanggal_mulai);
        etTanggal.setInputType(InputType.TYPE_NULL);
        etTanggal.requestFocus();
        // tgl berakhir
        endTgl = findViewById(R.id.tanggal_end);
        endTgl.setInputType(InputType.TYPE_NULL);
        endTgl.requestFocus();
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        setDateTimeField();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perhitunganHarga();
                if (sHotel != null && sTipekasur != null && sTanggal != null && eTanggal !=null && sKamar != null) {
                        AlertDialog dialog = new AlertDialog.Builder(CheckinHotelActivity.this)
                                .setTitle("Ingin booking hotel sekarang?")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            db.execSQL("INSERT INTO TB_HOTEL (emailUsr_htl, nm_hotel, tgl_ci, tipe_kasur, tgl_co, total_kamar, total_harga) VALUES ('" +
                                                    email + "','" +
                                                    sHotel + "','" +
                                                    sTanggal + "','" +
                                                    sTipekasur + "','" +
                                                    eTanggal + "','" +
                                                    sKamar + "','" +
                                                    hargaTotal + "');");
                                            Toast.makeText(CheckinHotelActivity.this, "Booking berhasil", Toast.LENGTH_LONG).show();
                                            finish();
                                        } catch (Exception e) {
                                            Toast.makeText(CheckinHotelActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .create();
                        dialog.show();

                } else {
                    Toast.makeText(CheckinHotelActivity.this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setupToolbar();

    }

    private void setupToolbar() {
        Intent getParam = getIntent();
        final String nmHotel = getParam.getStringExtra("nmHotel");
        Toolbar toolbar = findViewById(R.id.tbKrl);
        toolbar.setTitle(nmHotel);
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

    public void perhitunganHarga() {
        Intent getParam = getIntent();
        final String hrgHotel = getParam.getStringExtra("harga");
        if (sTipekasur.equalsIgnoreCase("Double bed") && sKamar.equalsIgnoreCase("1")) {
            hargaTotal = Integer.parseInt(hrgHotel) * 1 + 50000;
        } else if (sTipekasur.equalsIgnoreCase("Double bed") && sKamar.equalsIgnoreCase("2")) {
            hargaTotal = Integer.parseInt(hrgHotel) * 2 + 50000;
        } else if (sTipekasur.equalsIgnoreCase("Double bed") && sKamar.equalsIgnoreCase("3")) {
            hargaTotal = Integer.parseInt(hrgHotel) * 3 + 50000;
        } else if (sTipekasur.equalsIgnoreCase("Double bed") && sKamar.equalsIgnoreCase("4")) {
            hargaTotal = Integer.parseInt(hrgHotel) * 4 + 50000;
        } else if (sTipekasur.equalsIgnoreCase("Double bed") && sKamar.equalsIgnoreCase("5")) {
            hargaTotal = Integer.parseInt(hrgHotel) * 5 + 50000;
        } else if (sTipekasur.equalsIgnoreCase("Twin bed") && sKamar.equalsIgnoreCase("1")) {
            hargaTotal = Integer.parseInt(hrgHotel) * 1 + 20000;
        } else if (sTipekasur.equalsIgnoreCase("Twin bed") && sKamar.equalsIgnoreCase("2")) {
            hargaTotal = Integer.parseInt(hrgHotel) * 2 + 20000;
        } else if (sTipekasur.equalsIgnoreCase("Twin bed") && sKamar.equalsIgnoreCase("3")) {
            hargaTotal = Integer.parseInt(hrgHotel) * 3 + 20000;
        } else if (sTipekasur.equalsIgnoreCase("Twin bed") && sKamar.equalsIgnoreCase("4")) {
            hargaTotal = Integer.parseInt(hrgHotel) * 4 + 20000;
        } else if (sTipekasur.equalsIgnoreCase("Twin bed") && sKamar.equalsIgnoreCase("5")) {
            hargaTotal = Integer.parseInt(hrgHotel) * 5 + 20000;
        }
    }

    private void setDateTimeField() {
        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpTanggal.show();
            }
        });

        endTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekoutTanggal.show();
            }
        });

        dpTanggal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                sTanggal = dayOfMonth + " " + bulan[monthOfYear] + " " + year;
                etTanggal.setText(sTanggal);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        cekoutTanggal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                eTanggal = dayOfMonth + " " + bulan[monthOfYear] + " " + year;
                endTgl.setText(eTanggal);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}