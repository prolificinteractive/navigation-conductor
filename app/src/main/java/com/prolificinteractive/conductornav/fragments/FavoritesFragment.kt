package com.prolificinteractive.conductornav.fragments

import androidx.navigation.NavDirections
import com.prolificinteractive.conductornav.R

class FavoritesFragment : BaseFragment() {
  override val nextDirections: NavDirections
    get() = FavoritesFragmentDirections.toNextFavoritesFragment()
        .setComNcapdeviSampleArgsInstance(index + 1)

  override val rootId: Int
    get() = R.id.favoritesFragment
}
