package com.prolificinteractive.conductornav.fragments

import androidx.navigation.NavDirections
import com.prolificinteractive.conductornav.R

class NearbyFragment : BaseFragment() {

  override val nextDirections: NavDirections
    get() = NearbyFragmentDirections.toNextNearbyFragment()
        .setComNcapdeviSampleArgsInstance(index + 1)

  override val rootId: Int
    get() = R.id.nearbyFragment
}
