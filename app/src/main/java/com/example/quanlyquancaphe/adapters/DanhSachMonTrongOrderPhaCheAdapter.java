package com.example.quanlyquancaphe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private List<Integer> id_TrangThaiMon;
    private List<String> node_Time = new ArrayList<>();
    String key_node;

    //ibtnComlete
    String sComplete_id_time;
    String sComplete_key_Time;
    // ibtnWaiting
    String sWaiting_id_Time;
    String sWaiting_key_Time;
    // ibtnConfig
    String sConfig_id_Time;
    String sConfig_key_Time;

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
        holder.tvsoLuong.setText("Số lượng: " + String.valueOf(list_CT.get(position).getSl()));
        System.out.println("Adapter"+ String.valueOf(list_CT.get(position).getSl()));

        /* kiểm tra trạng thái món : Đang làm ,hoàn thành ,xác nhận */
        if (list_CT.get(position).getId_TrangThai() == 0) {
            holder.ibtnComplete.setEnabled(false);
            holder.ibtnConfig.setEnabled(false);
            holder.ibtnWaiting.setEnabled(true);
        }
        if (list_CT.get(position).getId_TrangThai() == 1) {
            holder.ibtnWaiting.setBackground(null);
            holder.ibtnWaiting.setEnabled(false);

        }
        if (list_CT.get(position).getId_TrangThai() == 2) {
            holder.ibtnWaiting.setBackground(null);
            holder.ibtnWaiting.setEnabled(false);
            holder.ibtnComplete.setBackground(null);
            holder.ibtnComplete.setEnabled(false);
        }
        if (list_CT.get(position).getId_TrangThai() == 3) {
            holder.ibtnWaiting.setBackground(null);
            holder.ibtnWaiting.setEnabled(false);
            holder.ibtnComplete.setBackground(null);
            holder.ibtnComplete.setEnabled(false);
            holder.ibtnConfig.setBackground(null);
            holder.ibtnConfig.setEnabled(false);
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
                    if (id_Mon.equals(id_CT_Mon)) {
                        Boolean get_Id_TT = dataSnapshot.child("hetMon").getValue(Boolean.class);

                        if (get_Id_TT != null) {
                            if (get_Id_TT) {
                                holder.swTrangThai.setChecked(false);
                            } else {
                                holder.swTrangThai.setChecked(true);
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
                holder.ibtnComplete.setEnabled(true);
                String sWaiting_CT_Mon = list_CT.get(holder.getBindingAdapterPosition()).getId_Mon();
                Integer iWaiting_id_TrangThai = 1;
                DatabaseReference setWaiting = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(key_node).child("HT");
                setWaiting.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        node_Time.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            sWaiting_id_Time = list_CT.get(holder.getBindingAdapterPosition()).getGioGoiMon();
                            String id_node = dataSnapshot.getKey();
                            node_Time.add(id_node);
                        }
                        StringBuilder setTime = StringTime(sWaiting_id_Time);
                        for (int i = 0; i < node_Time.size(); i++) {
                            String id = node_Time.get(i);
                            if (id.equals(setTime.toString())) {
                                sWaiting_key_Time = id;
                            }
                        }
                        setWaiting.child(sWaiting_key_Time).child(sWaiting_CT_Mon).child("id_TrangThai").setValue(iWaiting_id_TrangThai);
                        holder.ibtnWaiting.setBackground(null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });
        holder.ibtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMon = list_CT.get(holder.getBindingAdapterPosition()).getTenMon();
                NotificationUtility.updateNotiOnFirebase(2, "Món hoàn thành "+tenMon);
                holder.ibtnConfig.setEnabled(true);
                String sComplete_CT_Mon = list_CT.get(holder.getBindingAdapterPosition()).getId_Mon();
                DatabaseReference setComplete = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(key_node).child("HT");
                setComplete.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        node_Time.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            sComplete_id_time = list_CT.get(holder.getBindingAdapterPosition()).getGioGoiMon();
                            String id_node = dataSnapshot.getKey();
                            node_Time.add(id_node);
                        }
                        StringBuilder setTime = StringTime(sComplete_id_time);
                        for (int i = 0; i < node_Time.size(); i++) {
                            String id = node_Time.get(i);
                            if (id.equals(setTime.toString())) {
                                sComplete_key_Time = id;
                            }
                        }
                        Integer iComplete_id_TrangThai = 2;
                        setComplete.child(sComplete_key_Time).child(sComplete_CT_Mon).child("id_TrangThai").setValue(iComplete_id_TrangThai);
                        holder.ibtnComplete.setBackground(null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.ibtnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sConfig_CT_Mon = list_CT.get(holder.getBindingAdapterPosition()).getId_Mon();
                DatabaseReference setConfig = FirebaseDatabase.getInstance().getReference("ChiTietMon").child(key_node).child("HT");
                setConfig.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        node_Time.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            sConfig_id_Time = list_CT.get(holder.getBindingAdapterPosition()).getGioGoiMon();
                            String id_node = dataSnapshot.getKey().toString();
                            node_Time.add(id_node);
                        }
                        StringBuilder setTime = StringTime(sConfig_id_Time);
                        for (int i = 0; i < node_Time.size(); i++) {
                            String id = node_Time.get(i);
                            if (id.equals(setTime.toString())) {
                                sConfig_key_Time = id;
                            }
                        }
                        Integer iComfig_id_TrangThai = 3;
                        setConfig.child(sConfig_key_Time).child(sConfig_CT_Mon).child("id_TrangThai").setValue(iComfig_id_TrangThai);
                        holder.ibtnConfig.setBackground(null);
                        holder.cardView.setCardBackgroundColor(Color.parseColor("#E7E7E7"));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        /*Tắt / bật nút switch hết món */
        holder.swTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference set_Switch_database = FirebaseDatabase.getInstance().getReference("Mon");
                if (holder.swTrangThai.isChecked()){
                    if (list_CT.get(holder.getBindingAdapterPosition()).getId_TrangThai() == 3) {
                        holder.cardView.setCardBackgroundColor(Color.parseColor("#E7E7E7"));
                    } else {
                        holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    holder.ibtnWaiting.setVisibility(View.VISIBLE);
                    holder.ibtnConfig.setVisibility(View.VISIBLE);
                    holder.ibtnComplete.setVisibility(View.VISIBLE);
                    Map<String, Object> updateOff = new HashMap<>();
                    updateOff.put("hetMon", false);
                    set_Switch_database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String ib_Mon = dataSnapshot.child("id_Mon").getValue(String.class);
                                if (list_CT.get(holder.getAdapterPosition()).getId_Mon().equals(ib_Mon)) {
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
                    // set Notification
                    String tenMon = list_CT.get(holder.getBindingAdapterPosition()).getTenMon();
                    NotificationUtility.updateNotiOnFirebase(1, "Hết Món "+tenMon);
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#E7E7E7"));
                    holder.ibtnWaiting.setVisibility(View.INVISIBLE);
                    holder.ibtnConfig.setVisibility(View.INVISIBLE);
                    holder.ibtnComplete.setVisibility(View.INVISIBLE);
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
    @Override
    public int getItemCount() {
        return list_CT.size();
    }
}
