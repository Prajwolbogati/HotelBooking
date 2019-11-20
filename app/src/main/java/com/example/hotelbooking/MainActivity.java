package com.example.hotelbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Spinner location, roomtype;
    private TextView txtindate, tvlocation, tvroomt, tvGuests, tvNumberofDays, tvRoomTotal, tvvat, tvservice, tvtotal;
    private TextView txtoutdate, txtroom, txtchildren, txtadults;
    private EditText ettext1, ettext2, ettext3;
    private Button btnresult;
    private Boolean da1, da2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = findViewById(R.id.location);
        roomtype = findViewById(R.id.roomtype);
        tvlocation = findViewById(R.id.tvlocation);
        tvroomt = findViewById(R.id.tvroomt);
        tvGuests = findViewById(R.id.tvGuests);
        tvNumberofDays = findViewById(R.id.tvNumberofDays);
        tvRoomTotal = findViewById(R.id.tvRoomTotal);
        tvvat = findViewById(R.id.tvvat);
        tvservice = findViewById(R.id.tvservice);
        tvtotal = findViewById(R.id.tvtotal);
        txtindate = findViewById(R.id.txtindate);
        txtoutdate = findViewById(R.id.txtoutdate);
        ettext1 = findViewById(R.id.ettext1);
        ettext2 = findViewById(R.id.ettext2);
        ettext3 = findViewById(R.id.ettext3);
        txtadults = findViewById(R.id.txtadults);
        txtchildren = findViewById(R.id.txtchildren);
        txtroom = findViewById(R.id.txtroom);
        btnresult = findViewById(R.id.btnresult);





        String locations[] = {"Select Location", "Chitwan", "Bhaktapur", "Pokhara", "Kathmandu"};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, locations);
        location.setAdapter(adapter);

        final String Room[] = {"Select Room Type", "Deluxe", "Platinum", "AC"};
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Room);
        roomtype.setAdapter(adapter1);


        txtindate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatePicker();
                da1 = true;


            }
        });

        txtoutdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatePicker();
                da2 = true;

            }
        });

        btnresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(ettext1.getText()) || (Integer.parseInt(ettext1.getText().toString())) < 1){
                    ettext1.setError("Enter Valid Number");
                    return;
                }
                else if (Integer.parseInt(ettext2.getText().toString()) < 0){
                    ettext2.setError("Enter Valid Number");
                    return;
                }
                else if (TextUtils.isEmpty(ettext3.getText()) || (Integer.parseInt(ettext3.getText().toString())) < 1){
                    ettext3.setError("Enter Valid Number");
                    return;
                }

                if (location.getSelectedItem().toString() == "Please select Location"){
                    Toast.makeText(MainActivity.this, "Please select Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (roomtype.getSelectedItem().toString() == "Please select Room Type"){
                    Toast.makeText(MainActivity.this, "Please select Room Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                float rate = 0, total = 0, VAT = 0, serviceCharge = 0;
                String roomType = roomtype.getSelectedItem().toString();
                int days = 0;
                String checkInDate = txtindate.getText().toString();
                String checkOutDate = txtoutdate.getText().toString();

                switch (roomtype.getSelectedItem().toString()){
                    case "Deluxe":
                        rate = 2000;
                        break;
                    case "AC":
                        rate = 3000;
                        break;
                    case "Platinum":
                        rate = 4000;
                        break;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
                try {
                    Date checkIn = sdf.parse(checkInDate);
                    Date checkOut = sdf.parse(checkOutDate);
                    long diff = checkOut.getTime() - checkIn.getTime();
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    days = (int) diffDays;

                    if (days < 1){
                        Toast.makeText(MainActivity.this, "Please enter valid dates", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    total = rate * days * Float.parseFloat(ettext3.getText().toString());
                    VAT = (total*13)/100;
                    serviceCharge = (total*10)/100;

                    tvlocation.setText("Location : " +location.getSelectedItem().toString());
                    tvroomt.setText("Room Type : " + roomtype.getSelectedItem().toString());
                    tvNumberofDays.setText("Number of days staying : " + days);
                    if (Integer.parseInt(ettext2.getText().toString()) == 0){
                        tvGuests.setText("Guests : " + ettext1.getText() + " Adults");
                    }
                    else {
                        tvGuests.setText("Guests : " + ettext1.getText() + " Adults, " + ettext2.getText() + " Children");
                    }
                    tvRoomTotal.setText("Total Cost of Room : Rs " + total);
                    tvvat.setText("VAT : Rs " + VAT);
                    tvservice.setText("Service Charge : Rs " + serviceCharge);
                    tvtotal.setText("Total : Rs " + (total + VAT + serviceCharge));
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadDatePicker(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                if (da1 == true) {
                    txtindate.setText(year + "/" + month + "/" + dayOfMonth);
                    da1 = false;
                }
                else if (da2 == true){
                    txtoutdate.setText(year + "/" + month + "/" + dayOfMonth);
                    da2 = false;
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}
