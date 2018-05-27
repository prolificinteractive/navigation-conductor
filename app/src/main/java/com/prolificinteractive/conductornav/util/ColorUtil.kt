package com.prolificinteractive.conductornav.util

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color

import com.prolificinteractive.conductornav.R

object ColorUtil {

  fun getMaterialColor(resources: Resources, index: Int): Int {
    val colors = resources.obtainTypedArray(R.array.mdcolor_300)

    val returnColor = colors.getColor(index % colors.length(), Color.BLACK)

    colors.recycle()
    return returnColor
  }

}
