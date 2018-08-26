package com.prolificinteractive.conductornav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.ui.onNavDestinationSelected
import com.bluelinelabs.conductor.Controller
import com.prolificinteractive.conductornav.util.ColorUtil
import com.prolificinteractive.conductornav.util.findNavController
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.controller_navigation_demo.*

@ContainerOptions(cache = CacheImplementation.NO_CACHE)
class DemoController(args: Bundle) : Controller(args), LayoutContainer {

  override lateinit var containerView: View

  private val index: Int by lazy { args.getInt(KEY_INDEX)}
  private val displayUpMode: DisplayUpMode by lazy {
    DisplayUpMode.values()[args.getInt(KEY_DISPLAY_UP_MODE)]
  }
  private val navController: NavController by lazy { findNavController() }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    containerView = inflater.inflate(R.layout.controller_navigation_demo, container, false)
    onViewBound(containerView)
    return containerView
  }

  private fun onViewBound(view: View) {
    if (displayUpMode == DisplayUpMode.HIDE) {
      view.findViewById<View>(R.id.upBtn).visibility = View.GONE
    }

    view.setBackgroundColor(ColorUtil.getMaterialColor(resources!!, index))
    midText.text = resources!!.getString(R.string.navigation_title, index)

    nextBtn.setOnClickListener {
      navController.navigate(DemoControllerDirections
          .toNextController()
          .setIndex(index + 1))
    }

    upBtn.setOnClickListener {
      navController.navigateUp()
    }

    popToRootBtn.setOnClickListener {
      navController.popBackStack(R.id.firstController, false)
    }

    toolbar.title = resources!!.getString(R.string.controller_title)
    toolbar.inflateMenu(R.menu.controller)
    toolbar.setOnMenuItemClickListener { it.onNavDestinationSelected(navController) }
  }

  enum class DisplayUpMode {
    SHOW,
    HIDE;
  }

  companion object {
    private const val KEY_INDEX = "index"
    private const val KEY_DISPLAY_UP_MODE = "displayUpMode"
  }
}
