package com.example.addressapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddress extends AppCompatActivity {
    EditText name;
    EditText add1;
    EditText add2;
    EditText landmark;
    EditText city;
    EditText state;
    EditText pincode;
    CheckBox isDefault;
    UserService userService;
    Boolean def;
    User user;
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
        userService = APIUtils.getUserService();
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            user = (User) extras.getSerializable("User");
            name.setText(user.getName());
            add1.setText(user.getAdd1());
            city.setText(user.getCity());
            name.setText(user.getName());
            pincode.setText(""+user.getZip());
            SharedPreferences sharedPref = AddAddress.this.getSharedPreferences("default", Context.MODE_PRIVATE);
            if(user.getId()==sharedPref.getInt("default",0)){
                isDefault.setChecked(true);
                def = true;
            }
            else def = false;
        }
        else{
            user = new User();
        }
    }

    public void sendMessage(View view) {
        view.setClickable(false);
        String name = this.name.getText().toString();
        if(TextUtils.isEmpty(name.trim())){
            Toast.makeText(AddAddress.this, "Name can't be empty",Toast.LENGTH_SHORT).show();
        }
        else {
            user.setName(name);
            if (user.getId() == null)
                addUser(user);
            else
                updateUser(user);
        }
    }

    public void addUser(User u){
        Call<User> call = userService.addUser(User.token, u);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(isDefault.isChecked()){
                        SharedPreferences sharedPref = AddAddress.this.getSharedPreferences("default", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("default", response.body().getId());
                        editor.apply();
                    }
                    Toast.makeText(AddAddress.this, "User added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddAddress.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    public void updateUser(User u){
        Call<User> call = userService.updateUser(u.getId(), User.token, u);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(isDefault.isChecked()){
                        SharedPreferences sharedPref = AddAddress.this.getSharedPreferences("default", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("default", response.body().getId());
                        editor.apply();
                    }
                    else {
                        if(def==true){
                            SharedPreferences sharedPref = AddAddress.this.getSharedPreferences("default", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("default", -1);
                            editor.apply();

                        }
                    }
                    Toast.makeText(AddAddress.this, "User updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddAddress.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

}
