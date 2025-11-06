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
    private OnItemClickListener listener; // *** THÊM MỚI ***

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
        holder.textViewTitle.setText("Hồ số: " + currentPond.pondId);
        holder.textViewVolume.setText(String.format("Thể tích: %.0f L", currentPond.volumeLiters));
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

    public Pond getPondAt(int position) {
        return ponds.get(position);
    }

    class PondHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewVolume;
        private TextView textViewMineral;

        public PondHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewPondTitle);
            textViewVolume = itemView.findViewById(R.id.textViewPondVolume);
            textViewMineral = itemView.findViewById(R.id.textViewPondMineral);

            // *** THÊM MỚI: Bắt sự kiện click vào item ***
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(ponds.get(position));
                }
            });
        }
    }

    // *** THÊM MỚI: Interface để lắng nghe sự kiện click ***
    public interface OnItemClickListener {
        void onItemClick(Pond pond);
    }

    // *** THÊM MỚI: Phương thức để Activity đăng ký lắng nghe ***
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}