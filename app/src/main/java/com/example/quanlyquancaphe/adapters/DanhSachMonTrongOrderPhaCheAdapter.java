package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.ultilities.NotificationUtility;
import com.example.quanlyquancaphe.viewholders.DanhSachMonTrongOrderViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanhSachMonTrongOrderPhaCheAdapter extends RecyclerView.Adapter<DanhSachMonTrongOrderViewHolder> {
    private Context context;
    private List<ChiTietMon> list_CT;
    private List<String> node_Time = new ArrayList<>();
    String key_node;
    String _key_Time;
    String id_Time;

    public DanhSachMonTrongOrderPhaCheAdapter(Context context, List<ChiTietMon> list_CT, String key_node) {
        this.context = context;
        this.list_CT = list_CT;
        this.key_node = key_node;
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
        holder.tvGhiChu.setText("Ghi chú: " + list_CT.get(position).getGhiChu());
        holder.tvTenMonAn.setText(list_CT.get(position).getTenMon());
        holder.tvsoLuong.setText("Số lượng: " + list_CT.get(position).getSl());

        /* kiểm tra trạng thái món : Đang làm ,hoàn thành ,xác nhận */
        if (list_CT.get(position).getId_TrangThai() == 0) {
            holder.ibtnComplete.setVisibility(View.INVISIBLE);
            holder.ibtnConfig.setVisibility(View.INVISIBLE);
        }

        if (list_CT.get(position).getId_TrangThai() == 1) {
            holder.ibtnWaiting.setVisibility(View.INVISIBLE);

        }
        if (list_CT.get(position).getId_TrangThai() == 2) {
            holder.ibtnWaiting.setVisibility(View.INVISIBLE);
            holder.ibtnComplete.setVisibility(View.INVISIBLE);

        }
        if (list_CT.get(position).getId_TrangThai() == 3) {
            holder.ibtnWaiting.setVisibility(View.INVISIBLE);
            holder.ibtnComplete.setVisibility(View.INVISIBLE);
            holder.ibtnConfig.setVisibility(View.INVISIBLE);
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E7E7E7"));
        }
        /* kiểm tra trạng thái bật / tắt nút switch*/
        String id_CT_Mon = list_CT.get(holder.getBindingAdapterPosition()).getId_Mon();
        DatabaseReference check_switch = FirebaseDatabase.getInstance().getReference("Mon");
        check_switch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id_Mon = dataSnapshot.child("id_Mon").getValue(String.class);
                    if (id_Mon != null && id_CT_Mon != null) {
                        if (id_Mon.equals(id_CT_Mon)) {
                            Boolean get_Id_TT = dataSnapshot.child("hetMon").getValue(Boolean.class);
                            if (get_Id_TT != null) {
                                if (get_Id_TT) {
                                    holder.swTrangThai.setChecked(false);
                                    holder.ibtnWaiting.setVisibility(View.INVISIBLE);
                                    holder.ibtnComplete.setVisibility(View.INVISIBLE);
                                    holder.ibtnConfig.setVisibility(View.INVISIBLE);
                                } else {
                                    holder.swTrangThai.setChecked(true);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        /* Xét trạng thái trạng thái 3 nút 1 , 2 , 3 */

        holder.ibtnWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ibtnComplete.setVisibility(View.VISIBLE);
                String id_Mon = list_CT.get(holder.getBindingAdapterPosition()).getId_Mon();
                id_Time = list_CT.get(holder.getBindingAdapterPosition()).getGioGoiMon();
                Integer id_TT = 1;
                setButton(id_Mon, id_TT, id_Time);
                holder.ibtnWaiting.setVisibility(View.INVISIBLE);

            }
        });

        holder.ibtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Trạng thái  Thông báo
                holder.ibtnConfig.setVisibility(View.VISIBLE);
                String tenMon = list_CT.get(holder.getBindingAdapterPosition()).getTenMon();
                NotificationUtility.updateNotiOnFirebase(2, "Có món hoàn thành: " + tenMon);
                String id_Mon = list_CT.get(holder.getBindingAdapterPosition()).getId_Mon();
                id_Time = list_CT.get(holder.getBindingAdapterPosition()).getGioGoiMon();
                Integer id_TT = 2;
                setButton(id_Mon, id_TT, id_Time);
                holder.ibtnComplete.setVisibility(View.INVISIBLE);

            }
        });
        holder.ibtnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_Mon = list_CT.get(holder.getBindingAdapterPosition()).getId_Mon();
                id_Time = list_CT.get(holder.getBindingAdapterPosition()).getGioGoiMon();
                Integer id_TT = 3;
                setButton(id_Mon, id_TT, id_Time);
                holder.ibtnConfig.setBackground(null);
                holder.cardView.setCardBackgroundColor(Color.parseColor("#E7E7E7"));
            }
        });

        /*Tắt / bật nút switch hết món */
        holder.swTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference set_Switch_database = FirebaseDatabase.getInstance().getReference("Mon");
                Integer id_TT = list_CT.get(holder.getBindingAdapterPosition()).getId_TrangThai();
                if (holder.swTrangThai.isChecked()) {
                    switch (id_TT) {
                        case 0:
                            holder.ibtnWaiting.setVisibility(View.VISIBLE);
                            holder.ibtnComplete.setVisibility(View.INVISIBLE);
                            holder.ibtnConfig.setVisibility(View.INVISIBLE);
                            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                            break;
                        case 1:
                            holder.ibtnWaiting.setVisibility(View.INVISIBLE);
                            holder.ibtnComplete.setVisibility(View.VISIBLE);
                            holder.ibtnConfig.setVisibility(View.INVISIBLE);
                            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                            break;
                        case 2:
                            holder.ibtnWaiting.setVisibility(View.INVISIBLE);
                            holder.ibtnComplete.setVisibility(View.INVISIBLE);
                            holder.ibtnConfig.setVisibility(View.VISIBLE);
                            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                            break;
                        case 3:
                            holder.ibtnWaiting.setVisibility(View.INVISIBLE);
                            holder.ibtnComplete.setVisibility(View.INVISIBLE);
                            holder.ibtnConfig.setVisibility(View.INVISIBLE);
                            holder.cardView.setCardBackgroundColor(Color.parseColor("#E7E7E7"));
                            break;
                    }
                    Map<String, Object> updateOff = new HashMap<>();
                    updateOff.put("hetMon", false);
                    set_Switch_database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String ib_Mon = dataSnapshot.child("id_Mon").getValue(String.class);
                                if (list_CT.get(holder.getBindingAdapterPosition()).getId_Mon().equals(ib_Mon)) {
                                    set_Switch_database.child(ib_Mon).updateChildren(updateOff);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    String tenMon = list_CT.get(holder.getBindingAdapterPosition()).getTenMon();
                    NotificationUtility.updateNotiOnFirebase(1, "Hết Món: "+tenMon);
                    Map<String, Object> mSwitch_On = new HashMap<>();
                    mSwitch_On.put("hetMon", true);
                    set_Switch_database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String ib_Mon = dataSnapshot.child("id_Mon").getValue(String.class);
                                if (list_CT.get(holder.getBindingAdapterPosition()).getId_Mon().equals(ib_Mon)) {
                                    set_Switch_database.child(ib_Mon).updateChildren(mSwitch_On);
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

    // Đưa chuỗi thời gian 10:10:10 -> 10010010
    public StringBuilder StringTime(String sr) {
        StringBuilder builder = new StringBuilder(sr);
        char[] chars = builder.toString().toCharArray();
        chars[2] = '0';
        chars[5] = '0';
        builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            builder.append(chars[i]);
        }
        return builder;
    }

    private void setButton(String id_Mon, Integer id_TrangThaiMon, String id_Time) {
        DatabaseReference setComplete = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(key_node).child("HT");
        setComplete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                node_Time.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id_node = dataSnapshot.getKey();
                    node_Time.add(id_node);
                }
                StringBuilder setTime = StringTime(id_Time);
                for (int i = 0; i < node_Time.size(); i++) {
                    String id = node_Time.get(i);
                    if (id.equals(setTime.toString())) {
                        _key_Time = id;
                    }
                }
                setComplete.child(_key_Time).child(id_Mon).child("id_TrangThai").setValue(id_TrangThaiMon);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list_CT.size();
    }
}
