package artground.otterbear.com.artground.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import artground.otterbear.com.artground.R
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageView = findViewById<ImageView>(R.id.logo)
        (imageView.getBackground() as AnimationDrawable).start()

        val intent = Intent(this, PreperationActivity::class.java)
        intent.putExtra("state", "launch")


        // delay
        Timer().schedule(object : TimerTask() {
            override fun run() {
                startActivity(intent)
                finish()
            }
        }, 1500)

    }
}
