package com.prolificinteractive.conductornav.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.prolificinteractive.conductornav.R
import com.prolificinteractive.conductornav.util.ColorUtil
import kotlinx.android.synthetic.main.fragment_main.*

abstract class BaseFragment : Fragment() {

  internal var index = 0

  abstract val nextDirections: NavDirections

  abstract val rootId: Int

  val navController: NavController by lazy { NavHostFragment.findNavController(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val args = arguments
    if (args != null) {
      index = args.getInt(ARGS_INSTANCE)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val color = ColorUtil.getMaterialColor(resources, index)

    view.setBackgroundColor(color)
    toolbar.setBackgroundColor(color)

    midText.text = resources.getString(R.string.navigation_title, this::class.java.simpleName, index)

    upBtn.setOnClickListener {
      navController.navigateUp()
    }

    nextBtn.setOnClickListener {
      navController.navigate(nextDirections)
    }

    popToRootBtn.setOnClickListener {
      navController.popBackStack(rootId, false)
    }
  }

  companion object {
    val ARGS_INSTANCE = "com.ncapdevi.sample.argsInstance"
  }
}
