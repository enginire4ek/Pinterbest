package com.example.pinterbest

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pinterbest.data.repository.Repository
import com.example.pinterbest.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding

    private var _navHostFragment: NavHostFragment? = null
    private val navHostFragment get() = _navHostFragment!!

    private var _navController: NavController? = null
    private val navController get() = _navController!!

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Pinterbest)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setupBottomNavigationBar()

        setContentView(binding.root)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment ||
                destination.id == R.id.registrationFragment ||
                destination.id == R.id.errorFragment
            ) {
                binding.cardBottomNavigation.visibility = View.GONE
            } else {
                binding.cardBottomNavigation.visibility = View.VISIBLE
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.stats -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.search -> {
                    navController.navigate(R.id.searchFragment)
                }
                R.id.avatar -> {
                    navController.navigate(R.id.profileFragment)
                }
                R.id.comment -> {
                    navController.navigate(R.id.messagesFragment)
                }
            }
            true
        }
    }

    private fun checkAuthUser(navGraph: NavGraph, repository: Repository) {
        lifecycleScope.launch {
            val code = async { repository.getauthCheck() }.await()
            if (code != null) {
                if (code == SUCCESS) {
                    navGraph.startDestination = R.id.homeFragment
                    navController.graph = navGraph
                    binding.root.alpha = 1.0F
                    setContentView(binding.root)
                } else {
                    navGraph.startDestination = R.id.loginFragment
                    navController.graph = navGraph
                    binding.root.alpha = 1.0F
                    setContentView(binding.root)
                }
            } else {
                navGraph.startDestination = R.id.errorFragment
                navController.graph = navGraph
                binding.root.alpha = 1.0F
                setContentView(binding.root)
            }
        }
    }

    private fun setupBottomNavigationBar() {
        _navHostFragment = supportFragmentManager
            .findFragmentById(R.id.NavHostFragment) as NavHostFragment
        _navController = navHostFragment.navController
        checkAuthUser(
            navController.navInflater.inflate(R.navigation.navigation),
            Repository(
                preferences = this.getSharedPreferences(
                    getString(R.string.login_info),
                    Context.MODE_PRIVATE
                )
            )
        )
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun setMenuChecked(itemId: Int?) {
        when (itemId) {
            R.id.homeFragment -> {
                binding.bottomNavigation.menu.getItem(HOME_POSITION_BNV).isChecked = true
            }
            R.id.searchFragment -> {
                binding.bottomNavigation.menu.getItem(SEARCH_POSITION_BNV).isChecked = true
            }
            R.id.messagesFragment -> {
                binding.bottomNavigation.menu.getItem(MESSAGE_POSITION_BNV).isChecked = true
            }
            R.id.profileFragment -> {
                binding.bottomNavigation.menu.getItem(PROFILE_POSITION_BNV).isChecked = true
            }
            else -> {
                binding.bottomNavigation.menu.getItem(HOME_POSITION_BNV).isChecked = true
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onBackPressed() {
        if (navHostFragment.childFragmentManager.backStackEntryCount > 0) {
            navController.navigateUp()
            setMenuChecked(navController.currentDestination?.id)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val HOME_POSITION_BNV = 0
        const val SEARCH_POSITION_BNV = 1
        const val MESSAGE_POSITION_BNV = 2
        const val PROFILE_POSITION_BNV = 3

        const val SUCCESS = 204
    }
}
