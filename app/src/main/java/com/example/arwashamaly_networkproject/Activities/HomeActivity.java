package com.example.arwashamaly_networkproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.arwashamaly_networkproject.Adapters.PerfumeAdapter;
import com.example.arwashamaly_networkproject.Listeneres.PerfumeListener;
import com.example.arwashamaly_networkproject.R;
import com.example.arwashamaly_networkproject.Utility.BaseActivity;
import com.example.arwashamaly_networkproject.Utility.Perfume;
import com.example.arwashamaly_networkproject.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar2);
        setTitle("Marilyn perfumes");
        getAllProducts();

    }

    private void getAllProducts() {
        firebaseFirestore.collection("Perfumes").get()
                .addOnSuccessListener(HomeActivity.this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<Perfume> perfumeList = new ArrayList<>();
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Perfume perfume = d.toObject(Perfume.class);
                                perfume.setId(d.getId());
                                perfumeList.add(perfume);
                            }

                            binding.rc.setLayoutManager(new GridLayoutManager(HomeActivity.this,
                                    2, RecyclerView.VERTICAL, false));
                            binding.progressBar.setVisibility(View.GONE);

                            DocumentReference document = firebaseFirestore.collection("Users").document(user.getUid());
                            document.collection("FavoritePerfumes").get().addOnSuccessListener(HomeActivity.this, new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<Perfume> favorite = new ArrayList<>();
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        for (DocumentSnapshot d : list) {
                                            Perfume perfume = d.toObject(Perfume.class);
                                            perfume.setId(d.getId());
                                            favorite.add(perfume);
                                        }
                                    }
                                    PerfumeAdapter adapter = new PerfumeAdapter(perfumeList,HomeActivity.this, new PerfumeListener() {
                                        @Override
                                        public void itemClick(int position) {
                                            Perfume perfume = perfumeList.get(position);
                                            Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
                                            intent.putExtra("photo", perfume.getPhoto());
                                            intent.putExtra("name", perfume.getName());
                                            intent.putExtra("price", perfume.getPrice());
                                            intent.putExtra("details", perfume.getDetails());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void favoriteClick(int position) {
                                            if (user == null) {
                                                new AlertDialog.Builder(HomeActivity.this)
                                                        .setTitle("Sign in")
                                                        .setMessage("You must log in first, in order to place the product in the preferences !")
                                                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                            }
                                                        })
                                                        .setPositiveButton("sign in", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                                                finish();
                                                            }
                                                        })
                                                        .create().show();
                                            } else {
                                                DocumentReference document = firebaseFirestore.collection("Users").document(user.getUid());
                                                if (perfumeList.get(position).isFavorite()) {
                                                    document.collection("FavoritePerfumes")
                                                            .document(perfumeList.get(position).getId())
                                                            .set(perfumeList.get(position));
                                                } else {
                                                    document.collection("FavoritePerfumes")
                                                            .document(perfumeList.get(position).getId()).delete();
                                                }
                                            }
                                        }
                                    },favorite );
                                    binding.rc.setAdapter(adapter);
                                }
                            });

                        }
                    }
                });
    }

    private void getAllProductsByPrice() {
        firebaseFirestore.collection("Perfumes")
                .orderBy("price", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(HomeActivity.this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<Perfume> perfumeList = new ArrayList<>();
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Perfume perfume = d.toObject(Perfume.class);
                                perfume.setId(d.getId());
                                perfumeList.add(perfume);
                            }

                            binding.rc.setLayoutManager(new GridLayoutManager(HomeActivity.this,
                                    2, RecyclerView.VERTICAL, false));
                            binding.progressBar.setVisibility(View.GONE);

                            PerfumeAdapter adapter = new PerfumeAdapter(perfumeList, HomeActivity.this, new PerfumeListener() {
                                @Override
                                public void itemClick(int position) {
                                    Perfume perfume = perfumeList.get(position);
                                    Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
                                    intent.putExtra("photo", perfume.getPhoto());
                                    intent.putExtra("name", perfume.getName());
                                    intent.putExtra("price", perfume.getPrice());
                                    intent.putExtra("details", perfume.getDetails());
                                    startActivity(intent);
                                }

                                @Override
                                public void favoriteClick(int position) {
                                    if (user == null) {
                                        new AlertDialog.Builder(HomeActivity.this)
                                                .setTitle("Sign in")
                                                .setMessage("You must log in first, in order to place the product in the preferences !")
                                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                })
                                                .setPositiveButton("sign in", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                                                        finish();
                                                    }
                                                })
                                                .create().show();
                                    } else {
                                        DocumentReference document = firebaseFirestore.collection("Users").document(user.getUid());
                                        if (perfumeList.get(position).isFavorite()) {
                                            document.collection("FavoritePerfumes")
                                                    .document(perfumeList.get(position).getId())
                                                    .set(perfumeList.get(position));
                                        } else {
                                            document.collection("FavoritePerfumes")
                                                    .document(perfumeList.get(position).getId()).delete();
                                        }
                                    }
                                }
                            });
                            binding.rc.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileIcon:
                if (user != null) {
                    startActivity(new Intent(getBaseContext(), ProfileActivity.class));
                } else {
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Sign in")
                            .setMessage("You must log in first!")
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("sign in", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    finish();
                                }
                            })
                            .create().show();
                }
                return true;
            case R.id.favoriteIcon:
                if (user != null) {
                    startActivity(new Intent(getBaseContext(), FavoriteActivity.class));
                } else {
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Sign in")
                            .setMessage("You must log in first !")
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("sign in", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    finish();
                                }
                            })
                            .create().show();
                }
                return true;
            case R.id.filterIcon:
                getAllProductsByPrice();
                return true;
        }
        return false;
    }


}