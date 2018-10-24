package com.example.addressapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.addressapp.api.APIUtils;
import com.example.addressapp.api.AddressService;
import com.example.addressapp.helper.AddressAdapter;
import com.example.addressapp.models.Address;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AddressService addressService;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Addresses");
        setSupportActionBar(toolbar);
        addresses = new ArrayList<>();
        addressService = APIUtils.getUserService();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), VERTICAL));
        mAdapter = new AddressAdapter(MainActivity.this, addresses);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList();
    }

    @Override
    protected void onNewIntent(Intent intent){
        mAdapter.notifyDataSetChanged();
    }


    public void addAddress(View view) {
        Intent intent = new Intent(this, AddAddress.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void getAddressList(){
        Call<List<Address>> call = addressService.getAddresses(Address.token);
        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if(response.isSuccessful()){
                    addresses.clear();
                    addresses.addAll(response.body());
                    TextView empty1 = findViewById(R.id.textView3);
                    TextView empty2 = findViewById(R.id.textView4);
                    FloatingActionButton fab1 = findViewById(R.id.fab);
                    FloatingActionButton fab2 = findViewById(R.id.floatingActionButton2);
                    if(addresses.isEmpty()){
                        mRecyclerView.setVisibility(View.GONE);
                        empty1.setText("Your address book is blank");
                        empty2.setText("Kindly add shipping/billing address and enjoy faster checkout");
                        empty1.setVisibility(View.VISIBLE);
                        empty2.setVisibility(View.VISIBLE);
                        fab1.hide();
                        fab2.show();
                    }
                    else{
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setVisibility(View.VISIBLE);
                        empty1.setVisibility(View.GONE);
                        empty2.setVisibility(View.GONE);
                        fab1.show();
                        fab2.hide();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

}
