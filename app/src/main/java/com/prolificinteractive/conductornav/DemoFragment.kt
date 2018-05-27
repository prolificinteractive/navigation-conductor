package com.prolificinteractive.conductornav

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.prolificinteractive.conductornav.util.ColorUtil
import kotlinx.android.synthetic.main.fragment_navigation_demo.*

class DemoFragment : Fragment() {

  private val index: Int by lazy { arguments?.getInt(KEY_INDEX) ?: 0 }
  private val displayUpMode: DisplayUpMode by lazy {
    DisplayUpMode.values()[arguments?.getInt(KEY_DISPLAY_UP_MODE) ?: 1]
  }
  private val navController: NavController by lazy { findNavController(this) }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_navigation_demo, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    if (displayUpMode == DisplayUpMode.HIDE) {
      view.findViewById<View>(R.id.btn_up).visibility = View.GONE
    }

    view.setBackgroundColor(ColorUtil.getMaterialColor(resources, index))
    tv_title.text = resources.getString(R.string.navigation_title, index)

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

    toolbar.title = resources.getString(R.string.fragment_title)
    toolbar.inflateMenu(R.menu.menu_fragment)
    toolbar.setOnMenuItemClickListener {
      navController.navigate(MainDirections.toOtherActivity())
      false
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
