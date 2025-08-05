package top.linesoft.open2share

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import top.linesoft.open2share.databinding.ActivityGuideBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class GuideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuideBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.guideMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.textview.highlightColor = Color.TRANSPARENT
        val guideReader = BufferedReader(InputStreamReader(assets.open("guide.html")))
        binding.textview.text = HtmlCompat.fromHtml(
            guideReader.readText(),
            FROM_HTML_MODE_LEGACY
        )
        guideReader.close()
    }
}