package jmmh.bluelabs.m513

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import jmmh.bluelabs.m513.ui.popular_movie_details.PopularMovieActivity
import jmmh.bluelabs.m513.databinding.ActivitySplashBinding
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        var like = false


             likeAnimation( like)

            Handler(Looper.getMainLooper()).postDelayed(
                {
                    val intent = Intent(this, PopularMovieActivity::class.java)

                    this.startActivity(intent)
                    finish()
                },
                3000 // value in milliseconds
            )


             /* binding.btn2.setOnClickListener {
            val intent = Intent(this, PopularMovieActivity::class.java)
          //  intent.putExtra("id",697799 )
            this.startActivity(intent)
        }*/


    }
    private fun likeAnimation(
                              like: Boolean) : Boolean {
        if (!like) {
            splash_Image_View.setAnimation(R.raw.movie)
            splash_Image_View.playAnimation()
        } else {
            splash_Image_View.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animator: Animator) {

                        splash_Image_View.setImageResource(R.drawable.movie_icon)
                        splash_Image_View.alpha = 1f
                    }
                })
        }
        return !like
    }

}