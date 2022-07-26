package jmmh.thefive.m513.view.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import jmmh.thefive.m513.R
import jmmh.thefive.m513.view.ui.fragment.FileManagerFragment
import jmmh.thefive.m513.view.ui.fragment.MoviesFragment
import kotlinx.android.synthetic.main.activity_movies513.*

class Movies513Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies513)
        supportActionBar?.hide()
        /*if (savedInstanceState == null) {
            val f = MoviesFragment()
            val t: FragmentTransaction = supportFragmentManager.beginTransaction()
            t.replace(layout_Details.id, f).commit()
        }*/
        replaceFragment(MoviesFragment())
        //requestLocationPermission()
        bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> replaceFragment(MoviesFragment())
                R.id.location -> openActivity()
                R.id.upload -> replaceFragment(FileManagerFragment())
                else -> {
                }
            }
            true
        }
    }

    /*private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    /*private fun equestLocationPermission(){
        if (isLocationPermissionGranted()) {
            }
    }*/
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                this,
                "Ve a permisos y acepta los permisos",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.REQUEST_CODE_LOCATION -> if
                                                       (grantResults.isNotEmpty() && grantResults[0]
                == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Permisos definidos", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Para activar la localizacionVe a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
        }
    }*/
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_Details, fragment)
        fragmentTransaction.commit()
    }

    private fun openActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        this.startActivity(intent)
        finish()
    }
}
