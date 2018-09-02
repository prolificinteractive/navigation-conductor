package com.prolificinteractive.conductornav

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_fragmentx.*

class FragmentXActivity : AppCompatActivity() {

  private val navController: NavController by lazy { findNavController(R.id.navHost) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_fragmentx)

    bottom_navigation.setOnNavigationItemSelectedListener { item ->
      onNavDestinationSelected(item, navController, false)
    }
    navController.addOnNavigatedListener { _, destination ->
      val destinationId = destination.id
      val menu = bottom_navigation.menu
      var h = 0
      val size = menu.size()
      while (h < size) {
        val item = menu.getItem(h)
        if (item.itemId == destinationId) {
          item.isChecked = true
        }
        h++
      }
    }
  }

  override fun onBackPressed() {
    navHost.onBackPressed()
  }

  companion object {
    internal fun onNavDestinationSelected(item: MenuItem,
                                          navController: NavController, popUp: Boolean): Boolean {
      val builder = NavOptions.Builder()
          .setLaunchSingleTop(true)
          .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
          .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
          .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
          .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
      if (popUp) {
        builder.setPopUpTo(navController.graph.startDestination, false)
      }
      val options = builder.build()
      return try {
        //TODO provide proper API instead of using Exceptions as Control-Flow.
        navController.navigate(item.itemId, null, options)
        true
      } catch (e: IllegalArgumentException) {
        false
      }

    }
  }
}
