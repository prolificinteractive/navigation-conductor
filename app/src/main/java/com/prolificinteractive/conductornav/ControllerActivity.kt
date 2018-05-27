package com.prolificinteractive.conductornav

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_controller.*

class ControllerActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_controller)
  }

  override fun onBackPressed() {
    if (!navHost.onBackPressed()) {
      super.onBackPressed()
    }
  }
}
