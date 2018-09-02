package com.prolificinteractive.conductornav.fragments

import android.os.Bundle
import android.view.View

class RecentsFragment : BaseFragment() {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btn.setOnClickListener {
      navController.navigate(RecentsFragmentDirections.toNextRecentsFragment()
          .setComNcapdeviSampleArgsInstance(mInt + 1))
    }
    btn.text = javaClass.simpleName + " " + mInt
  }
}
