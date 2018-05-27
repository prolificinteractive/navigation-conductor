package com.prolificinteractive.conductornav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
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
      view.findViewById<View>(R.id.btn_up).visibility = View.GONE
    }

    view.setBackgroundColor(ColorUtil.getMaterialColor(resources!!, index))
    tv_title.text = resources!!.getString(R.string.navigation_title, index)

    btn_next.setOnClickListener {
      navController.navigate(DemoControllerDirections
          .toNextController()
          .setIndex(index + 1))
    }

    btn_up.setOnClickListener {
      navController.navigateUp()
    }

    btn_pop_to_root.setOnClickListener {
      navController.popBackStack(R.id.firstController, true)
    }

    toolbar.title = resources!!.getString(R.string.controller_title)
    toolbar.inflateMenu(R.menu.menu_controller)
    toolbar.setOnMenuItemClickListener {
      navController.navigate(MainDirections.toOtherActivity())
      true
    }
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
