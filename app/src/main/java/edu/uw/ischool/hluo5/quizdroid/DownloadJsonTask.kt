package edu.uw.ischool.hluo5.quizdroid

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

class DownloadJsonTask(
    private val context: Context,
    private val url: String,
    private val callback: (Boolean) -> Unit
) {
    private val client = OkHttpClient()

    // 启动下载任务
    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = downloadJson()
            withContext(Dispatchers.Main) {
                if (result) {
                    callback(true)
                } else {
                    // 在主线程中显示重试对话框
                    showRetryDialog()
                }
            }
        }
    }

    // 下载 JSON 文件并保存到本地
    private suspend fun downloadJson(): Boolean {
        Log.d("DownloadJsonTask", "Starting download from URL: $url")
        return try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                Log.d("DownloadJsonTask", "Download successful, saving JSON to file")
                val jsonString = response.body?.string() ?: return false
                val file = File(context.filesDir, "questions.json")
                FileOutputStream(file).use { fos ->
                    fos.write(jsonString.toByteArray())
                }
                Log.d("DownloadJsonTask", "File saved successfully at ${file.path}")
                true
            } else {
                Log.e("DownloadJsonTask", "Download failed with response code: ${response.code}")
                false
            }
        } catch (e: Exception) {
            Log.e("DownloadJsonTask", "Exception during download", e)
            false
        }
    }

    // 显示重试对话框
    private fun showRetryDialog() {
        // 显示重试对话框必须在主线程中执行
        CoroutineScope(Dispatchers.Main).launch {
            AlertDialog.Builder(context)
                .setTitle("Download Failed")
                .setMessage("Unable to download questions. Would you like to retry?")
                .setPositiveButton("Retry") { _, _ ->
                    Log.d("DownloadJsonTask", "User selected to retry download")
                    start() // 重试下载
                }
                .setNegativeButton("Quit") { _, _ ->
                    Log.d("DownloadJsonTask", "User selected to quit after download failure")
                    callback(false)
                }
                .show()
        }
    }
}
