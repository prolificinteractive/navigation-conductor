package com.prolificinteractive.conductornav.fragments

import androidx.navigation.NavDirections
import com.prolificinteractive.conductornav.R

class RecentsFragment : BaseFragment() {

  override val nextDirections: NavDirections
    get() = RecentsFragmentDirections.toNextRecentsFragment()
        .setComNcapdeviSampleArgsInstance(index + 1)

  override val rootId: Int
    get() = R.id.recentsFragment

}
