package com.prolificinteractive.conductornav.fragments

import androidx.navigation.NavDirections
import com.prolificinteractive.conductornav.R

class FriendsFragment : BaseFragment() {

  override val nextDirections: NavDirections
    get() = FriendsFragmentDirections.toNextFriendsFragment()
        .setComNcapdeviSampleArgsInstance(index + 1)

  override val rootId: Int
    get() = R.id.friendsFragment

}
