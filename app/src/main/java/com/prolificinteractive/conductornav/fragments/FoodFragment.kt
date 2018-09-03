package com.prolificinteractive.conductornav.fragments

import androidx.navigation.NavDirections
import com.prolificinteractive.conductornav.R

class FoodFragment : BaseFragment() {
  override val nextDirections: NavDirections
    get() = FoodFragmentDirections.toNextFoodFragment()
        .setComNcapdeviSampleArgsInstance(index + 1)

  override val rootId: Int
    get() = R.id.foodFragment
}
