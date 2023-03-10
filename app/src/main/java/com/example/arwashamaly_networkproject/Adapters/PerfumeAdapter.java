package com.example.arwashamaly_networkproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arwashamaly_networkproject.Listeneres.PerfumeListener;
import com.example.arwashamaly_networkproject.R;
import com.example.arwashamaly_networkproject.Utility.Perfume;
import com.example.arwashamaly_networkproject.databinding.ItemPerfumeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class PerfumeAdapter extends RecyclerView.Adapter<PerfumeAdapter.perfumeHolder> {
    List<Perfume> perfumeList;
    Context context;
    PerfumeListener listener;
    FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

    public PerfumeAdapter(List<Perfume> perfumeList, Context context, PerfumeListener listener) {
        this.perfumeList = perfumeList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public perfumeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPerfumeBinding binding = ItemPerfumeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new perfumeHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull perfumeHolder holder, int position) {
        int pos = position;
        Perfume perfume = perfumeList.get(pos);

        holder.tvName.setText(perfume.getName());
        holder.tvPrice.setText(perfume.getPrice()+" $");

        if (perfume.isFavorite()){
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite);
        }else {
            holder.imgFavorite.setImageResource(R.drawable.ic_un_favorite);
        }

        Glide.with(context).load(perfume.getPhoto()).into(holder.imgPerfume);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClick(pos);
            }
        });

        holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null){
                    if (perfume.isFavorite()) {
                        perfume.setFavorite(false);
                        holder.imgFavorite.setImageResource(R.drawable.ic_un_favorite);
                    } else {
                        perfume.setFavorite(true);
                        holder.imgFavorite.setImageResource(R.drawable.ic_favorite);
                    }
                }
                listener.favoriteClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return perfumeList.size();
    }

    class perfumeHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgPerfume, imgFavorite;

        public perfumeHolder(@NonNull ItemPerfumeBinding binding) {
            super(binding.getRoot());
            imgPerfume = binding.imgPerfume;
            imgFavorite = binding.imgFavorite;
            tvName = binding.tvName;
            tvPrice = binding.tvPrice;
        }
    }
}
