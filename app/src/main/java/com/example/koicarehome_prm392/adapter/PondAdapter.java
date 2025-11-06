// File: com/example/koicarehome_prm392/adapter/PondAdapter.java
package com.example.koicarehome_prm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koicarehome_prm392.R;
import com.example.koicarehome_prm392.data.entities.Pond;
import java.util.ArrayList;
import java.util.List;

public class PondAdapter extends RecyclerView.Adapter<PondAdapter.PondHolder> {
    private List<Pond> ponds = new ArrayList<>();

    @NonNull
    @Override
    public PondHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pond_item, parent, false); // Bạn cần tạo layout pond_item.xml
        return new PondHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PondHolder holder, int position) {
        Pond currentPond = ponds.get(position);
        holder.textViewName.setText(currentPond.name);
        holder.textViewVolume.setText(String.format("Thể tích: %.0f L", currentPond.volumeLiters));
        holder.textViewSalt.setText(String.format("Lượng muối cần: %.2f kg", currentPond.requiredSaltAmount));
    }

    @Override
    public int getItemCount() {
        return ponds.size();
    }

    public void setPonds(List<Pond> ponds) {
        this.ponds = ponds;
        notifyDataSetChanged();
    }

    // *** PHƯƠNG THỨC GETPONDAT CẦN THÊM ***
    public Pond getPondAt(int position) {
        return ponds.get(position);
    }

    class PondHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewVolume;
        private TextView textViewSalt;

        public PondHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewPondName);
            textViewVolume = itemView.findViewById(R.id.textViewPondVolume);
            textViewSalt = itemView.findViewById(R.id.textViewPondSalt);
        }
    }
}