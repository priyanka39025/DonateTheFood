package com.priyanka.donatethefood_helppeople.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.priyanka.donatethefood_helppeople.R;
import com.priyanka.donatethefood_helppeople.model_class.InformationModel;

import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationListViewHolder> {
    Context context;
    List<InformationModel> modelList;
    FirebaseFirestore fireStoreDb;

    public DonationAdapter(Context context, List<InformationModel> modelList) {
        this.context = context;
        this.modelList = modelList;
        fireStoreDb = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public DonationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donation_list_row, parent, false);
        return new DonationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DonationListViewHolder holder, final int position) {
        final InformationModel model = modelList.get(position);
        holder.foodName.setText(modelList.get(position).getFoodName());
        holder.foodQtn.setText("Qty : " + modelList.get(position).getQuantity());
        holder.cDate.setText("Posted On : "+modelList.get(position).getDate());
        holder.cTime.setText("Pick Up Time : "+modelList.get(position).getTime());
        holder.dAddress.setText(modelList.get(position).getAdress());
        if (modelList.get(position).getIsPickedUp().equals("no")){
            holder.pickedUp.setText("Not Picked Up");
        }else {
            holder.pickedUp.setText("Picked Up");
            holder.pickedUp.setBackgroundColor(Color.GREEN);
            holder.pickedUp.setTextColor(Color.WHITE);
        }

        holder.pickedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!modelList.get(position).getIsPickedUp().equals("yes")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Warning");
                    alert.setMessage("Is Food Picked Up ?");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fireStoreDb.collection("Donation").document(modelList.get(position).getDonatorName()).update("is_picked_up", "yes").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    holder.pickedUp.setText("Picked Up");
                                    holder.pickedUp.setBackgroundColor(Color.GREEN);
                                    holder.pickedUp.setTextColor(Color.WHITE);
                                    Toast.makeText(context, "Food is picked up", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    alert.show();
                }
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.other_info, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Other Info");
                alert.setView(view);

                TextView dName = view.findViewById(R.id.donator_name);
                final TextView dPhone = view.findViewById(R.id.donator_phone);
                TextView dAddress = view.findViewById(R.id.donator_addr);

                dName.setText("Donator Name : " + modelList.get(position).getDonatorName());
                dPhone.setText("Phone No. : "+modelList.get(position).getPhone());
                dAddress.setText("Address : " +modelList.get(position).getAdress());

                dPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + modelList.get(position).getPhone()));
                        context.startActivity(intent);

                    }
                });

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public  class DonationListViewHolder extends RecyclerView.ViewHolder {
        TextView foodName,foodQtn, cDate, cTime, dAddress, pickedUp;
        LinearLayout linearLayout;

        public DonationListViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodNameTV);
            foodQtn = itemView.findViewById(R.id.foodQunatityTV);
            cDate = itemView.findViewById(R.id.collectionDateTv);
            cTime = itemView.findViewById(R.id.collectionTimeTv);
            dAddress = itemView.findViewById(R.id.addressTV);
            pickedUp = itemView.findViewById(R.id.picked_up);
            linearLayout = itemView.findViewById(R.id.root_lay);
        }
    }
}
