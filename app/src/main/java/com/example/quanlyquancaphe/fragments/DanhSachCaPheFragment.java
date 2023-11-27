package com.example.quanlyquancaphe.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.activities.GioHangActivity;
import com.example.quanlyquancaphe.adapters.DanhSachMonPhucVuAdapter;
import com.example.quanlyquancaphe.interfaces.DSMonPVInterface;
import com.example.quanlyquancaphe.models.ChiTietMon;
import com.example.quanlyquancaphe.models.MonTrongDS;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class DanhSachCaPheFragment extends Fragment implements DSMonPVInterface {
    ImageButton btnCart;
    EditText edtSearchBox;
    RecyclerView rvDSMon;
    ArrayList<MonTrongDS> data = new ArrayList<>();
    DanhSachMonPhucVuAdapter adapter;
    DatabaseReference databaseReference;

    public DanhSachCaPheFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DanhSachMonPhucVuAdapter(getActivity(), data, this);
        loadData("", adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_danhsachcaphe_layout, container, false);
        rvDSMon = rootView.findViewById(R.id.rvDSMon);
        btnCart = rootView.findViewById(R.id.btnCart);
        edtSearchBox = rootView.findViewById(R.id.edtSearchBox);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GioHangActivity.class);
                startActivity(intent);
            }
        });
        edtSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadData(edtSearchBox.getText().toString().trim().toLowerCase(), adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rvDSMon.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDSMon.setAdapter(adapter);
        return rootView;
    }

    private void loadData(String key, DanhSachMonPhucVuAdapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle("").setMessage("Đang tải dữ liệu...");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        data.clear();
        adapter.notifyDataSetChanged();
        databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MonTrongDS mon = snapshot.getValue(MonTrongDS.class);
                if (mon.getId_Loai() == 0 && mon.getTenMon().toLowerCase().contains(key.trim().toLowerCase())) {
                    data.add(mon);
                    adapter.notifyItemInserted(data.size());
                }
                dialog.dismiss();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MonTrongDS mon = snapshot.getValue(MonTrongDS.class);
                if (mon.getId_Loai() == 0 && mon.getTenMon().toLowerCase().contains(key.trim().toLowerCase())) {
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getId_Mon().equals(mon.getId_Mon())) {
                            data.set(i, mon);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int changedPosition = 0;
                MonTrongDS mon = snapshot.getValue(MonTrongDS.class);
                if (mon.getId_Loai() == 0 && mon.getTenMon().toLowerCase().contains(key.trim().toLowerCase())) {
                    for (int i = 0; i < data.size(); i++) {
                        if (mon.getId_Mon().equals(data.get(i).getId_Mon())) {
                            changedPosition = i;
                            break;
                        }
                    }
                    data.remove(changedPosition);
                    adapter.notifyItemRemoved(changedPosition);
                }
                dialog.dismiss();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onAddButtonClick(Integer position, Integer sl) {
        if (sl == 0) {
            return;
        }
        ChiTietMon chiTietMon = new ChiTietMon();
        MonTrongDS monTrongDS = data.get(position);
        chiTietMon.setId_Mon(monTrongDS.getId_Mon());
        chiTietMon.setSl(sl);
        chiTietMon.setHinh(monTrongDS.getHinh());
        chiTietMon.setTenMon(monTrongDS.getTenMon());
        if (monTrongDS.getGiamGia() == 0) {
            chiTietMon.setGia(monTrongDS.getDonGia());
        } else {
            chiTietMon.setGia(monTrongDS.getDonGia() * (100 - monTrongDS.getGiamGia()) / 100);
        }
        int tempSize = GioHangActivity.currentData.size();
        // Trường hợp đã có phần tử trong mảng
        if (tempSize > 0) {
            for (int i = 0; i < tempSize; ++i) {

                if (i == GioHangActivity.currentData.size() - 1) {
                    // Trường hợp trùng phần tử cuối cùng
                    if (GioHangActivity.currentData.get(i).getId_Mon().equals(chiTietMon.getId_Mon())) {
                        GioHangActivity.currentData.get(i).setSl(GioHangActivity.currentData.get(i).getSl() + sl);
                    }
                    // Trường hợp không trùng phần tử
                    else {
                        GioHangActivity.currentData.add(chiTietMon);
                    }
                    // Trường hợp trùng phần tử
                } else if (GioHangActivity.currentData.get(i).getId_Mon().equals(chiTietMon.getId_Mon())) {
                    GioHangActivity.currentData.get(i).setSl(GioHangActivity.currentData.get(i).getSl() + sl);
                    break;
                }
            }
        }
        // Trường hợp không có phần tử trong mảng
        else {
            GioHangActivity.currentData.add(chiTietMon);
        }
        Toast.makeText(getActivity(), "Đã thêm vào danh sách", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlusButtonClick(Integer position, EditText edtSL) {
        data.get(position).tang();
        edtSL.setText(String.valueOf(data.get(position).getSl()));
    }

    @Override
    public void onMinusButtonClick(Integer position, EditText edtSL) {
        data.get(position).giam();
        edtSL.setText(String.valueOf(data.get(position).getSl()));
    }
}