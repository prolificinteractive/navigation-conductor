package com.prolificinteractive.conductornav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bluelinelabs.conductor.Controller
import com.prolificinteractive.conductornav.util.BundleBuilder
import com.prolificinteractive.conductornav.util.ColorUtil
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.controller_navigation_demo.*

@ContainerOptions(cache = CacheImplementation.NO_CACHE)
class DemoController(args: Bundle) : Controller(args), LayoutContainer {

  override lateinit var containerView: View

  private val index: Int
  private var displayUpMode = DisplayUpMode.SHOW_FOR_CHILDREN_ONLY

  protected val title: String
    get() = "Navigation Demos"

  enum class DisplayUpMode {
    SHOW,
    SHOW_FOR_CHILDREN_ONLY,
    HIDE;

    val displayUpModeForChild: DisplayUpMode
      get() {
        when (this) {
          HIDE -> return HIDE
          else -> return SHOW
        }
      }
  }

  init {
    index = args.getInt(KEY_INDEX)
    displayUpMode = DisplayUpMode.values()[args.getInt(KEY_DISPLAY_UP_MODE)]
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    containerView = inflater.inflate(R.layout.controller_navigation_demo, container, false)
    onViewBound(containerView)
    return containerView
  }

  protected fun onViewBound(view: View) {
    if (displayUpMode != DisplayUpMode.SHOW) {
      view.findViewById<View>(R.id.btn_up).visibility = View.GONE
    }

    view.setBackgroundColor(ColorUtil.getMaterialColor(resources, index))
    tv_title.text = resources!!.getString(R.string.navigation_title, index)

    btn_next.setOnClickListener{ onNextClicked() }
    btn_up.setOnClickListener{ onUpClicked() }
    btn_pop_to_root.setOnClickListener{ onPopToRootClicked() }
  }

  private fun onNextClicked() {
    val args = DemoController.fromBundle(index + 1, displayUpMode.displayUpModeForChild)
    Navigation.findNavController(view!!).navigate(R.id.firstController, args)
  }

  private fun onUpClicked() {
    Navigation.findNavController(view!!).navigate(R.id.activity)
  }

  private fun onPopToRootClicked() {
    Navigation.findNavController(view!!).popBackStack(R.id.firstController, true)
  }

  companion object {
    private val KEY_INDEX = "index"
    private val KEY_DISPLAY_UP_MODE = "displayUpMode"

    internal fun fromBundle(index: Int, displayUpMode: DisplayUpMode): Bundle {
      return BundleBuilder(Bundle())
          .putInt(KEY_INDEX, index)
          .putInt(KEY_DISPLAY_UP_MODE, displayUpMode.ordinal)
          .build()
    }
  }
}
