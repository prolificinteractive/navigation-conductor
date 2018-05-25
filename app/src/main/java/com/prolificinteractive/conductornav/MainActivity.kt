package com.prolificinteractive.conductornav

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_navmain.toolbar
import kotlinx.android.synthetic.main.activity_navmain.controller_container

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_navmain)

    setSupportActionBar(toolbar)
  }

  override fun onBackPressed() {
    if (!controller_container.onBackPressed()) {
      super.onBackPressed()
    }
  }
}
