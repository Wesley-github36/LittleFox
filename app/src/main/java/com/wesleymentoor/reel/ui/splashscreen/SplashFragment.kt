package com.wesleymentoor.reel.ui.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.wesleymentoor.reel.MainActivity
import com.wesleymentoor.reel.R
import com.wesleymentoor.reel.connectivitymonitor.ConnectivityCallback
import com.wesleymentoor.reel.databinding.FragmentSplashBinding
import com.wesleymentoor.reel.utils.showDialog

class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        //bind the layout
        val binding: FragmentSplashBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash,
            container, false)
        val mainActivity = activity as MainActivity

        val dialog = showDialog(mainActivity, mainActivity)


        //Observe if WiFi and mobile connection is available
        mainActivity.getConnectionLiveData().observe(viewLifecycleOwner, {
            if (it) {
                dialog.dismiss()
                //Check if internet is available
                mainActivity.getNetDetect().checkInternetConnection(object: ConnectivityCallback {
                    override fun onDetected(isConnected: Boolean) {
                        if (isConnected) {
                            val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                            NavHostFragment.findNavController(this@SplashFragment).navigate(action)
                        }else
                            dialog.show()
                    }
                })
            }else {
                dialog.show()
            }
        })

        //Return the view
        return binding.root
    }
}