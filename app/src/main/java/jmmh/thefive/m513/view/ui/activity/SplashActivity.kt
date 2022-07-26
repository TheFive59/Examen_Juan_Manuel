package jmmh.thefive.m513.view.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import jmmh.thefive.m513.R
import jmmh.thefive.m513.data.utils.NetworkHelper
import jmmh.thefive.m513.databinding.ActivitySplashBinding
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        var like = false
        likeAnimation(like)
        lifecycleScope.launch {
            delay(3000)
            val intent = Intent(applicationContext, Movies513Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun likeAnimation(
        like: Boolean
    ): Boolean {
        if (!like) {
            splash_Image_View.setAnimation(R.raw.movie)
            splash_Image_View.playAnimation()
        } else {
            splash_Image_View.animate().alpha(0f).setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animator: Animator) {
                        splash_Image_View.alpha = 1f
                    }
                })
        }
        return !like
    }
}