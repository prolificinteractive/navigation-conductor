package com.prolificinteractive.fragmentx.archnavigation

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

class NavControllerX(context: Context): NavController(context) {
  override fun navigate(directions: NavDirections, navOptions: NavOptions?) {
    super.navigate(directions, navOptions)
  }
}