package com.example.addressapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    UserService userService;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Addresses");
        setSupportActionBar(toolbar);
        users = new ArrayList<>();
        userService = APIUtils.getUserService();
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AddressAdapter(MainActivity.this, users);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUsersList();
    }


    public void addAddress(View view) {
        Intent intent = new Intent(this, AddAddress.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void getUsersList(){
        Call<List<User>> call = userService.getUsers("52e04d83e87e509f07982e6ac851e2d2c67d1d0eabc4fe78");
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    users.clear();
                    users.addAll(response.body());
                    if(users.isEmpty()){
                        mRecyclerView.setVisibility(View.GONE);
                        TextView empty = findViewById(R.id.textView3);
                        empty.setText("Your address book is blank");
                        empty.setVisibility(View.VISIBLE);
                        FloatingActionButton fab = findViewById(R.id.fab);
                        fab.setTranslationX(-420);
                        fab.setTranslationY(-500);

                    }
                    else{
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setVisibility(View.VISIBLE);
                        TextView empty = findViewById(R.id.textView3);
                        empty.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

}
