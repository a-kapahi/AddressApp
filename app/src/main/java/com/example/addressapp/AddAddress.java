package com.example.addressapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.addressapp.api.APIUtils;
import com.example.addressapp.models.Address;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddress extends AppCompatActivity {
    private EditText name;
    private EditText add1;
    private EditText add2;
    private EditText landmark;
    private EditText city;
    private EditText state;
    private EditText pincode;
    private CheckBox isDefault;
    private Boolean def;
    private Address address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddAddress.this.setTitle("Add Address");
        setContentView(R.layout.activity_add_address);
        name = findViewById(R.id.editText1);
        add1 = findViewById(R.id.editText2);
        add2 = findViewById(R.id.editText3);
        landmark = findViewById(R.id.editText4);
        city = findViewById(R.id.editText5);
        state = findViewById(R.id.editText6);
        pincode = findViewById(R.id.editText7);
        isDefault = findViewById(R.id.checkBox);
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            address = (Address) extras.getSerializable("Address");
            name.setText(address.getName());
            add1.setText(address.getAdd1());
            add2.setText(address.getAdd2());
            city.setText(address.getCity());
            name.setText(address.getName());
            pincode.setText(""+ address.getZip());
            SharedPreferences sharedPref = AddAddress.this.getSharedPreferences("default", Context.MODE_PRIVATE);
            if(address.getId()==sharedPref.getInt("default",0)){
                isDefault.setChecked(true);
                def = true;
            }
            else def = false;
        }
        else{
            address = new Address();
        }
    }

    public void sendMessage(View view) {
        view.setClickable(false);
        String name = this.name.getText().toString().trim();
        String add1 = this.add1.getText().toString().trim();
        String city = this.city.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(AddAddress.this, "Name can't be empty",Toast.LENGTH_SHORT).show();
            view.setClickable(true);
        }
        else if(TextUtils.isEmpty(add1)){
            Toast.makeText(AddAddress.this, "Address can't be empty",Toast.LENGTH_SHORT).show();
            view.setClickable(true);
        }
        else if(TextUtils.isEmpty(city)){
            Toast.makeText(AddAddress.this, "City can't be empty",Toast.LENGTH_SHORT).show();
            view.setClickable(true);
        }
        else {
            address.setName(name);
            address.setAdd1(add1);
            address.setCity(city);
            address.setAdd2(add2.getText().toString().trim());
            if (address.getId() == null)
                addAddress(address);
            else
                updateAddress(address);
        }
    }

    public void addAddress(Address address){
        Call<Address> call = APIUtils.getAddressService().addAddress(Address.token, address);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if(response.isSuccessful()){
                    if(isDefault.isChecked()){
                        SharedPreferences sharedPref = AddAddress.this.getSharedPreferences("default", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("default", response.body().getId());
                        editor.apply();
                    }
                    Toast.makeText(AddAddress.this, "Address added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("Address",response.body());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    public void updateAddress(final Address address){
        Call<Address> call = APIUtils.getAddressService().updateAddress(address.getId(), Address.token, address);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if(response.isSuccessful()){
                    if(isDefault.isChecked()){
                        SharedPreferences sharedPref = AddAddress.this.getSharedPreferences("default", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("default", response.body().getId());
                        editor.apply();
                    }
                    else {
                        if(def){
                            SharedPreferences sharedPref = AddAddress.this.getSharedPreferences("default", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("default", -1);
                            editor.apply();

                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("Address",response.body());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Address> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

}
