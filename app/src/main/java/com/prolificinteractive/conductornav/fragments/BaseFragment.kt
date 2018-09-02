package com.prolificinteractive.conductornav.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.prolificinteractive.conductornav.R

open class BaseFragment : Fragment() {

  lateinit var btn: Button
  internal var mInt = 0

  val navController: NavController by lazy { NavHostFragment.findNavController(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val args = arguments
    if (args != null) {
      mInt = args.getInt(ARGS_INSTANCE)
    }
  }


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_main, container, false)?.apply {
      btn = findViewById(R.id.button)
    }
  }

  companion object {
    val ARGS_INSTANCE = "com.ncapdevi.sample.argsInstance"
  }
}
