package com.prolificinteractive.conductornav.fragments

import android.os.Bundle
import android.view.View

class FavoritesFragment : BaseFragment() {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btn.setOnClickListener {
      navController.navigate(FavoritesFragmentDirections.toNextFavoritesFragment()
          .setComNcapdeviSampleArgsInstance(mInt + 1))
    }
    btn.text = javaClass.simpleName + " " + mInt
  }
}
