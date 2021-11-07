package com.wesleymentoor.reel

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.wesleymentoor.reel.connectivitymonitor.ConnectionLiveData
import com.wesleymentoor.reel.connectivitymonitor.NetDetect
import com.wesleymentoor.reel.databinding.ActivityMainBinding
import com.wesleymentoor.reel.ui.compose.ComposeFragmentDirections
import com.google.android.material.transition.MaterialElevationScale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var netDetect: NetDetect

    private val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        connectionLiveData = ConnectionLiveData.getInstance(this)
        netDetect = NetDetect()
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        setUpBottomNavigationAndFab()

    }

    private fun setUpBottomNavigationAndFab() {

        //Setup bottom navigation with navController
        binding.bottomNavigation.background = null
        binding.bottomNavigation.menu.getItem(1).isEnabled = false
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.splashFragment -> {
                    setBottomAppBarForSplash()
                }
                R.id.homeFragment -> {
                    setBottomAppBarForHomeAndMore()
                }
                R.id.composeFragment -> {
                    setBottomAppBarForCompose()
                }
                R.id.moreFragment -> {
                    setBottomAppBarForHomeAndMore()
                }
            }
        }

        // Set a custom animation for showing and hiding the FAB
        binding.fab.apply {
            setShowMotionSpecResource(R.animator.fab_show)
            setHideMotionSpecResource(R.animator.fab_hide)
            setOnClickListener {
                navigateToCompose()
            }
        }
    }

    private fun setBottomAppBarForSplash() {
        binding.run {
            //bottomNavigation.visibility = View.GONE
            bottomAppBar.visibility = View.GONE
            fab.hide()
        }
    }

    private fun setBottomAppBarForHomeAndMore() {
        binding.run {
            fab.setImageState(intArrayOf(-android.R.attr.state_activated), true)
            bottomAppBar.visibility = View.VISIBLE
            fab.contentDescription = "Compose new date"
            bottomAppBar.performShow()
            fab.show()
        }
    }

    private fun setBottomAppBarForCompose() {
        hideBottomAppBar()
    }

    private fun hideBottomAppBar() {
        binding.run {
            bottomAppBar.performHide()
            // Get a handle on the animator that hides the bottom app bar so we can wait to hide
            // the fab and bottom app bar until after it's exit animation finishes.
            bottomAppBar.animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) return

                    // Hide the BottomAppBar to avoid it showing above the keyboard
                    // when composing a new date.
                    bottomAppBar.visibility = View.GONE
                    fab.visibility = View.INVISIBLE
                }
                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
    }

    private fun navigateToCompose() {
        currentNavigationFragment?.apply {
            exitTransition = MaterialElevationScale(false).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }
            val action = ComposeFragmentDirections.actionGlobalComposeFragment()
            navController.navigate(action)
        }
    }

    fun getConnectionLiveData(): ConnectionLiveData = connectionLiveData
    fun getNetDetect(): NetDetect = netDetect

}