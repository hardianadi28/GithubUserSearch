package id.hardianadi.githubusersearch.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.ui.mainlist.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent = Intent(this, MainActivity::class.java)


        Handler().postDelayed({
            startActivity(intent)
            this@SplashActivity.finish()
        }, 2000)
    }
}