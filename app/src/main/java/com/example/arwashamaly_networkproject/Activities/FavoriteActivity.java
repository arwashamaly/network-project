package com.example.arwashamaly_networkproject.Activities;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.arwashamaly_networkproject.Adapters.PerfumeAdapter;
import com.example.arwashamaly_networkproject.Listeneres.PerfumeListener;
import com.example.arwashamaly_networkproject.Utility.BaseActivity;
import com.example.arwashamaly_networkproject.Utility.Perfume;
import com.example.arwashamaly_networkproject.databinding.ActivityFavoriteBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends BaseActivity {
    ActivityFavoriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getFavoriteProducts();
    }

    private void getFavoriteProducts() {
        DocumentReference document = firebaseFirestore.collection("Users").document(user.getUid());
        document.collection("FavoritePerfumes").get().addOnSuccessListener(FavoriteActivity.this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                binding.progressBar2.setVisibility(View.GONE);
                if (!queryDocumentSnapshots.isEmpty()) {

                    List<Perfume> perfumeList = new ArrayList<>();
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Perfume perfume = d.toObject(Perfume.class);
                        perfume.setId(d.getId());
                        perfumeList.add(perfume);
                    }

                    binding.rcFavorite.setLayoutManager(new GridLayoutManager(FavoriteActivity.this,
                            2, RecyclerView.VERTICAL, false));

                    PerfumeAdapter adapter = new PerfumeAdapter(perfumeList, FavoriteActivity.this, new PerfumeListener() {
                        @Override
                        public void itemClick(int position) {

                        }
                        @Override
                        public void favoriteClick(int position) {
                            if (perfumeList.get(position).isFavorite()) {
                                document.collection("FavoritePerfumes")
                                        .document(perfumeList.get(position).getId())
                                        .set(perfumeList.get(position));
                            } else {
                                document.collection("FavoritePerfumes")
                                        .document(perfumeList.get(position).getId()).delete();
                            }

                        }
                    });
                    binding.rcFavorite.setAdapter(adapter);
                }
            }
        });
    }


}