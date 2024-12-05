package com.prescription.app.ui.splash

object SplashViewContract {
    sealed interface Effect {
        data object Completed : Effect
    }
}
