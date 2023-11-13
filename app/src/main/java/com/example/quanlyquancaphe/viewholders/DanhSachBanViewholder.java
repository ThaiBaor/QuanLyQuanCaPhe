package com.example.quanlyquancaphe.viewholders;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquancaphe.R;

public class DanhSachBanViewholder extends RecyclerView.ViewHolder{
public TextView tvtenBan, tvsoChoNgoi;
public LinearLayout layout_ban;
    public DanhSachBanViewholder(@NonNull View itemView) {
        super(itemView);
        tvtenBan = itemView.findViewById(R.id.tvTenBan);
        tvsoChoNgoi = itemView.findViewById(R.id.tvSoNguoiNgoi);
        layout_ban = itemView.findViewById(R.id.linerLayout_ban);
        itemView.setOnCreateContextMenuListener(contextMenuListener);
    }
    //Khởi tạo context menu
    private final View.OnCreateContextMenuListener contextMenuListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        }
    };
    private final MenuItem.OnMenuItemClickListener clickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
            return true;
        }
    };
}
