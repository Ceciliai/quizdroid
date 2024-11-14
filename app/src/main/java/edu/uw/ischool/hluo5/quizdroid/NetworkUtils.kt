package edu.uw.ischool.hluo5.quizdroid

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog

object NetworkUtils {

    // 检查网络是否可用
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            val isNetworkAvailable = capabilities != null
            Log.d("NetworkUtils", "Network available: $isNetworkAvailable")
            return isNetworkAvailable
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            val isNetworkAvailable = networkInfo != null && networkInfo.isConnected
            Log.d("NetworkUtils", "Network available (legacy): $isNetworkAvailable")
            return isNetworkAvailable
        }
    }

    // 检查飞行模式是否打开
    fun isAirplaneModeOn(context: Context): Boolean {
        val isAirplaneModeOn = Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
        Log.d("NetworkUtils", "Airplane mode is on: $isAirplaneModeOn")
        return isAirplaneModeOn
    }

    // 显示无网络连接的对话框
    fun showNoNetworkDialog(context: Context) {
        Log.d("NetworkUtils", "Showing no network dialog")
        AlertDialog.Builder(context)
            .setTitle("No Internet Connection")
            .setMessage("You are currently offline. Please check your network connection.")
            .setPositiveButton("OK", null)
            .show()
    }

    // 显示飞行模式的对话框，并引导用户前往设置页面关闭飞行模式
    fun handleAirplaneMode(context: Context) {
        Log.d("NetworkUtils", "Handling airplane mode")
        AlertDialog.Builder(context)
            .setTitle("Airplane Mode Enabled")
            .setMessage("Your device is currently in airplane mode. Would you like to go to settings to turn it off?")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
                context.startActivity(intent)
            }
            .setNegativeButton("Cancel") { _, _ ->
                Log.d("NetworkUtils", "User chose not to disable airplane mode")
            }
            .show()
    }
}
