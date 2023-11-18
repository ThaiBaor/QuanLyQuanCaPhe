package com.example.quanlyquancaphe.viewholders;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.interfaces.DSMonPVInterface;

public class DanhSachMonPhucVuViewHolder extends RecyclerView.ViewHolder {
    public TextView tenMon, donGia, moTa, giamGia, slDaBan, tvHetMon;
    public ImageView hinh;
    public ImageButton btnTang, btnGiam, btnAdd;
    public EditText edtSL;

    public DanhSachMonPhucVuViewHolder(@NonNull View itemView, DSMonPVInterface buttonClickListener) {
        super(itemView);
        setControl();
        btnTang.setOnClickListener(view -> {
            if (buttonClickListener != null) {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    buttonClickListener.onPlusButtonClick(position, edtSL);
                }
            }
        });
        btnGiam.setOnClickListener(view -> {
            if (buttonClickListener != null) {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    buttonClickListener.onMinusButtonClick(position, edtSL);
                }
            }
        });
        btnAdd.setOnClickListener(view -> {
            if (buttonClickListener != null) {
                int position = getBindingAdapterPosition();
                Integer sl = Integer.parseInt(edtSL.getText().toString());
                if (position != RecyclerView.NO_POSITION) {
                    buttonClickListener.onAddButtonClick(position, sl);
                }
            }
        });
    }

    private void setControl() {
        tenMon = itemView.findViewById(R.id.tvTenMon);
        donGia = itemView.findViewById(R.id.tvDonGia);
        tvHetMon = itemView.findViewById(R.id.tvhetMon);
        moTa = itemView.findViewById(R.id.tvMoTa);
        giamGia = itemView.findViewById(R.id.tvGiamGia);
        hinh = itemView.findViewById(R.id.ivHinh);
        slDaBan = itemView.findViewById(R.id.tvSLBan);
        btnTang = itemView.findViewById(R.id.btnTang);
        btnGiam = itemView.findViewById(R.id.btnGiam);
        btnAdd = itemView.findViewById(R.id.btnAdd);
        edtSL = itemView.findViewById(R.id.edtSL);
    }


}
