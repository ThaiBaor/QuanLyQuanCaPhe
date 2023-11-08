package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.viewholders.DanhSachMonTrongOrderViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanhSachMonTrongOrderPhaCheAdapter extends RecyclerView.Adapter<DanhSachMonTrongOrderViewHolder> {
    private Context context;
    private List<ChiTietMon> list_CT;
    String id_Mon;
    Integer id_TrangThai_Mon;

    public DanhSachMonTrongOrderPhaCheAdapter(Context context, List<ChiTietMon> list_CT) {
        this.context = context;
        this.list_CT = list_CT;
    }

    @NonNull
    @Override
    public DanhSachMonTrongOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danhsachmontrongorderphache_layout, parent, false);
        return new DanhSachMonTrongOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachMonTrongOrderViewHolder holder, int position) {
        Glide.with(context).load(list_CT.get(position).getHinh()).into(holder.ivImages);
        holder.tvGhiChu.setText(list_CT.get(position).getGhiChu());
        holder.tvTenMonAn.setText(list_CT.get(position).getTenMon());
        holder.tvsoLuong.setText(String.valueOf(list_CT.get(position).getSl()));
        // Kiểm tra trạng thái món
        if(list_CT.get(position).getId_TrangThai() == 1 ){
            holder.ibtnWaiting.setEnabled(false);
        }
        if(list_CT.get(position).getId_TrangThai() == 2 ){
            holder.ibtnWaiting.setEnabled(false);
            holder.ibtnComplete.setEnabled(false);
        }
        if(list_CT.get(position).getId_TrangThai() == 3 ){
            holder.ibtnWaiting.setEnabled(false);
            holder.ibtnComplete.setEnabled(false);
            holder.ibtnConfig.setEnabled(false);
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E7E7E7"));
        }
        // Trạng thái tắt bật
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mon");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    id_Mon = dataSnapshot.child("id_Mon").getValue(String.class);
                    if (id_Mon.equals(list_CT.get(holder.getAdapterPosition()).getId_Mon())) {
                        Boolean aBoolean = dataSnapshot.child("hetMon").getValue(Boolean.class);
                        holder.swTrangThai.setChecked(aBoolean);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        holder.ibtnWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.ibtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        holder.swTrangThai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mon");
                if (compoundButton.isChecked()) {
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#D2CBCB"));
                    holder.ibtnWaiting.setVisibility(View.INVISIBLE);
                    holder.ibtnConfig.setVisibility(View.INVISIBLE);
                    holder.ibtnComplete.setVisibility(View.INVISIBLE);

                    Map<String, Object> updateOn = new HashMap<>();
                    updateOn.put("hetMon", true);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String ib_Mon = dataSnapshot.child("id_Mon").getValue(String.class);
                                if (list_CT.get(holder.getAdapterPosition()).getId_Mon().equals(ib_Mon)) {
                                    reference.child(ib_Mon).updateChildren(updateOn);
                                    break;
                                }

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.ibtnWaiting.setVisibility(View.VISIBLE);
                    holder.ibtnConfig.setVisibility(View.VISIBLE);
                    holder.ibtnComplete.setVisibility(View.VISIBLE);
                    Map<String, Object> updateOff = new HashMap<>();
                    updateOff.put("hetMon", false);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String ib_Mon = dataSnapshot.child("id_Mon").getValue(String.class);
                                if (list_CT.get(holder.getAdapterPosition()).getId_Mon().equals(ib_Mon)) {
                                    reference.child(ib_Mon).updateChildren(updateOff);
                                    break;
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return list_CT.size();
    }
}
