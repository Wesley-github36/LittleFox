package com.wesleymentoor.reel.ui.splashscreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(): ViewModel() {

    val list = listOf("Hl", "hhg")

} 