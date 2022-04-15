package com.jorgemartinez.duckhunt.ui

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.jorgemartinez.duckhunt.R
import com.jorgemartinez.duckhunt.databinding.ActivityRankingBinding


class RankingActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRankingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        //hace un include para jalar el id.nav.. desde otro layout que no le pertenece
        val navController = findNavController(R.id.nav_host_fragment_content_ranking)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        supportFragmentManager. beginTransaction()
           .add(R.id.nav_host_fragment_content_ranking, UserRankingFragment())
            .commit()



    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_ranking)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}