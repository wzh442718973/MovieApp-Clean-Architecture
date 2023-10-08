package com.mahmoud.mohammed.movieapp.presentation.ui.popmovies.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.mahmoud.mohammed.movieapp.R
import com.mahmoud.mohammed.movieapp.common.Endpoint.PRIVACY_URL
import com.mahmoud.mohammed.movieapp.databinding.ActivityMainBinding
import com.mahmoud.mohammed.movieapp.presentation.ui.popmovies.fragments.MOVIE_LIST_FRAGMENT_TAG
import com.mahmoud.mohammed.movieapp.presentation.ui.popmovies.fragments.newMovieListFragment
import com.mahmoud.mohammed.movieapp.presentation.ui.web.WebActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, newMovieListFragment(), MOVIE_LIST_FRAGMENT_TAG)
                .commitNow()
            title = getString(R.string.app_name)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val version = menu?.findItem(R.id.menu_version)
        try {
            version?.title = "v" + packageManager.getPackageInfo(packageName, 0).versionName
        } catch (tt: Throwable) {

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(R.id.menu_privacy == item.itemId){
            WebActivity.openWeb(this, null, PRIVACY_URL)
        }
        return super.onOptionsItemSelected(item)
    }
}


