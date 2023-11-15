package com.example.quanlyquancaphe.ultilities;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.example.quanlyquancaphe.R;
import com.example.quanlyquancaphe.services.NotificationService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class NotificationUtility {
    public static void pushNotification(Context context, String textContent) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_icon);
        android.app.Notification notification = new NotificationCompat.Builder(context, NotificationService.CHANNEL_ID)
                .setContentTitle("Thông báo")
                .setContentText(textContent)
                .setSmallIcon(R.drawable.logo_icon)
                .setLargeIcon(bitmap)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) new Date().getTime(), notification);
        }
    }

    public static void updateNotiOnFirebase(Integer id, String contentText) {
        class ThongBao {
            private Integer id;
            private String contentText;

            public Integer getId() {
                return id;
            }

            public String getContentText() {
                return contentText;
            }

            public ThongBao(Integer id, String contentText) {
                this.id = id;
                this.contentText = contentText;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public void setContentText(String contextText) {
                this.contentText = contextText;
            }
        }
        ThongBao thongBao = new ThongBao(id, contentText);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ThongBao");
        databaseReference.setValue(thongBao);
    }
}
