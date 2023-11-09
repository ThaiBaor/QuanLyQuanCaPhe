package com.example.quanlyquancaphe.viewholders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.interfaces.GioHangInterface;

public class GioHangViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivHinh;
    public TextView tvTenMon, tvGia, tvTrangThai;
    public EditText edtGhiChu, edtSL;
    public ImageButton btnTang, btnGiam, btnXoa;

    public GioHangViewHolder(@NonNull View itemView, GioHangInterface buttonClickListener) {
        super(itemView);
        setControl();
       btnTang.setOnClickListener(view -> {
           if (buttonClickListener != null) {
               int position = getAdapterPosition();
               if (position != RecyclerView.NO_POSITION) {
                   buttonClickListener.onPlusButtonClick(position, edtSL, tvGia);
               }
           }
       });
       btnGiam.setOnClickListener(view -> {
           if (buttonClickListener != null) {
               int position = getAdapterPosition();
               if (position != RecyclerView.NO_POSITION) {
                   buttonClickListener.onMinusButtonClick(position, edtSL, tvGia);
               }
           }
       });
       btnXoa.setOnClickListener(view -> {
           if (buttonClickListener != null) {
               int position = getAdapterPosition();
               if (position != RecyclerView.NO_POSITION) {
                   buttonClickListener.onDeleteButtonClick(position);
               }
           }
       });
       edtGhiChu.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if (buttonClickListener != null) {
                   int position = getAdapterPosition();
                   if (position != RecyclerView.NO_POSITION) {
                       buttonClickListener.onNoteChange(position, edtGhiChu.getText().toString());
                   }
               }
           }
           @Override
           public void afterTextChanged(Editable editable) {

           }
       });
       edtSL.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if (buttonClickListener != null) {
                   int position = getAdapterPosition();
                   if (position != RecyclerView.NO_POSITION) {
                       buttonClickListener.onQtyChange(position, Integer.parseInt(edtSL.getText().toString()));
                   }
               }
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });
    }

    private void setControl() {
        ivHinh = itemView.findViewById(R.id.ivHinh);
        tvTenMon = itemView.findViewById(R.id.tvTenMon);
        tvGia = itemView.findViewById(R.id.tvGia);
        tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        edtGhiChu = itemView.findViewById(R.id.edtGhiChu);
        edtSL = itemView.findViewById(R.id.edtSL);
        btnTang = itemView.findViewById(R.id.btnTang);
        btnGiam = itemView.findViewById(R.id.btnGiam);
        btnXoa = itemView.findViewById(R.id.btnXoa);
    }
}
