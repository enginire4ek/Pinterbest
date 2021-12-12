package com.example.pinterbest.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pinterbest.domain.common.Result
import com.example.pinterbest.domain.repositories.AuthRepository
import com.example.pinterbest.presentation.common.appComponent
import com.example.pinterbest.presentation.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var authRepository: AuthRepository

    private var _navHostFragment: NavHostFragment? = null
    private val navHostFragment get() = _navHostFragment!!

    private var _navController: NavController? = null
    private val navController get() = _navController!!

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Pinterbest)
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setupBottomNavigationBar()

        binding.root.alpha = 1.0F
        setContentView(binding.root)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in NO_BOTTOM_NAVIGATION_SCREENS) {
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

    private fun setupBottomNavigationBar() {
        _navHostFragment = supportFragmentManager
            .findFragmentById(R.id.NavHostFragment) as NavHostFragment
        _navController = navHostFragment.navController
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

//    override fun onBackPressed() {
//        if (navHostFragment.childFragmentManager.backStackEntryCount > 0) {
//            navController.popBackStack()
//            setMenuChecked(navController.currentDestination?.id)
//        } else {
//            super.onBackPressed()
//        }
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle("current_navcontroller", navController.saveState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        navController.restoreState(savedInstanceState.getBundle("current_navcontroller"))
    }

    companion object {
        val NO_BOTTOM_NAVIGATION_SCREENS = listOf(
            R.id.loginFragment,
            R.id.registrationFragment,
            R.id.errorFragment,
            R.id.actualPinFragment,
            R.id.pinCreationFragment,
            R.id.boardCreationFragment
        )
        const val HOME_POSITION_BNV = 0
        const val SEARCH_POSITION_BNV = 1
        const val MESSAGE_POSITION_BNV = 2
        const val PROFILE_POSITION_BNV = 3
    }
}
