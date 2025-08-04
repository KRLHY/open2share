package top.linesoft.open2share

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class ReceiveOpenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shareFile =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //Log.d("a","Activity 收到数据，执行Finish");
                finish()
            }
        //        Log.d("分享","Data："+ getIntent().getData().toString()+ uri.getScheme());
        //        Log.d("分享","Type："+ getIntent().getType());
        if (intent.data?.scheme == "file") {
            if (PreferenceManager.getDefaultSharedPreferences(this)
                    .getBoolean("use_file_uri", false)
            ) {
                //API24以上系统分享支持file:///开头
                StrictMode.setVmPolicy(VmPolicy.Builder().build())
                VmPolicy.Builder().detectFileUriExposure()
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.no_use_file_uri_msg,
                    Toast.LENGTH_LONG
                ).show()
                finishAffinity()
            }
        }
        shareFile.launch(Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addCategory("android.intent.category.DEFAULT")
            putExtra(Intent.EXTRA_STREAM, intent.data)
            type = intent.type
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }, getString(R.string.share_title)))
    }
}