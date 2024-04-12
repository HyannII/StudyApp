package com.example.myapplication.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Kiểm tra kết nối mạng
        boolean isConnected = isNetworkAvailable(context);

        // Nếu không có kết nối mạng, hiển thị thông báo
        if (!isConnected) {
            Toast.makeText(context, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức kiểm tra kết nối mạng
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Đăng ký và hủy đăng ký BroadcastReceiver trong activity
    public static void registerReceiver(Context context, ConnectionReceiver receiver) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, filter);
    }

    public static void unregisterReceiver(Context context, ConnectionReceiver receiver) {
        context.unregisterReceiver(receiver);
    }
}
