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
                .inflate(R.layout.pond_item, parent, false);
        return new PondHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PondHolder holder, int position) {
        Pond currentPond = ponds.get(position);
        // Hiển thị ID của hồ để phân biệt
        holder.textViewTitle.setText("Hồ số: " + currentPond.pondId);
        // Hiển thị thể tích
        holder.textViewVolume.setText(String.format("Thể tích: %.0f L", currentPond.volumeLiters));
        // Hiển thị lượng khoáng, sử dụng trường 'mineralAmount'
        holder.textViewMineral.setText(String.format("Lượng khoáng cần: %.2f kg", currentPond.mineralAmount));
    }

    @Override
    public int getItemCount() {
        return ponds.size();
    }

    public void setPonds(List<Pond> ponds) {
        this.ponds = ponds;
        notifyDataSetChanged();
    }

    // Phương thức này vẫn giữ nguyên, rất hữu ích cho chức năng xóa
    public Pond getPondAt(int position) {
        return ponds.get(position);
    }

    class PondHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewVolume;
        private TextView textViewMineral;

        public PondHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ tới các ID mới trong pond_item.xml
            textViewTitle = itemView.findViewById(R.id.textViewPondTitle);
            textViewVolume = itemView.findViewById(R.id.textViewPondVolume);
            textViewMineral = itemView.findViewById(R.id.textViewPondMineral);
        }
    }
}