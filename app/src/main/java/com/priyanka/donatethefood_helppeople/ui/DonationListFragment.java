package com.priyanka.donatethefood_helppeople.ui;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.priyanka.donatethefood_helppeople.R;
import com.priyanka.donatethefood_helppeople.adapter.DonationAdapter;
import com.priyanka.donatethefood_helppeople.donation_database.DonationDatabase;
import com.priyanka.donatethefood_helppeople.model_class.InformationModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonationListFragment extends Fragment {
    private RecyclerView recyclerView;
    private DonationAdapter adapter;
    List<InformationModel> modelList = new ArrayList<>();
    Context context;
    FirebaseFirestore fireStoreDb;

    public DonationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fireStoreDb = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_donation_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.donationRecycler);
        fireStoreDb.collection("Donation")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                InformationModel informationModel = new InformationModel(
                                        document.getData().get("name").toString(), //kry name
                                        document.getData().get("phone").toString(),
                                        document.getData().get("address").toString(),
                                        document.getData().get("food_name").toString(),
                                        document.getData().get("quantity").toString(),
                                        document.getData().get("date").toString(),
                                        document.getData().get("time").toString(),
                                        document.getData().get("is_picked_up").toString());
                                modelList.add(informationModel);


                            }
                            adapter = new DonationAdapter(context, modelList);
                            GridLayoutManager glm = new GridLayoutManager(context, 1);
                            recyclerView.setLayoutManager(glm);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}
