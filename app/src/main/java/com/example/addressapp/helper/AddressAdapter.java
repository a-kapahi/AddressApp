package com.example.addressapp.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.addressapp.AddAddress;
import com.example.addressapp.MainActivity;
import com.example.addressapp.R;
import com.example.addressapp.api.APIUtils;
import com.example.addressapp.api.AddressService;
import com.example.addressapp.models.Address;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private List<Address> mDataset;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AddressAdapter(Context context, List<Address> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Address address = mDataset.get(position);
        holder.name.setText(address.toString());
        SharedPreferences sharedPref = context.getSharedPreferences("default", Context.MODE_PRIVATE);
        if(address.getId()!=sharedPref.getInt("default",-1)){
            holder.check.setVisibility(View.INVISIBLE);
        }
        else{
            holder.check.setVisibility(View.VISIBLE);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update:
                                update(address);
                                return true;
                            case R.id.delete:
                                deleteUser(address);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.address_menu, popup.getMenu());
                popup.show();
            }

        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView id;
        public TextView name;
        public Button button;
        public ImageView check;
        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.textView1);
            button = v.findViewById(R.id.button2);
            check = v.findViewById(R.id.imageView);
        }
    }

    public void deleteUser(Address address){
        AddressService addressService = APIUtils.getUserService();
        Call<Address> call = addressService.deleteAddress(address.getId(), Address.token);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }


    public void update(Address address){
        Intent intent = new Intent(context, AddAddress.class);
        intent.putExtra("Address", address);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
