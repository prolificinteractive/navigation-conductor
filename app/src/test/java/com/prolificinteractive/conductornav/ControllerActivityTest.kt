package com.prolificinteractive.conductornav

import android.app.Activity
import android.widget.TextView
import junit.framework.Assert.fail
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.Robolectric
import java.lang.IllegalArgumentException

@RunWith(ParameterizedRobolectricTestRunner::class)
class ControllerTest(private val clazz: Class<out Activity>) {

  companion object {
    @JvmStatic
    @ParameterizedRobolectricTestRunner.Parameters(name = "Activity = {0}")
    fun data(): Collection<Array<Any>> {
      return listOf<Array<Any>>(
          arrayOf(ControllerActivity::class.java),
          arrayOf(FragmentActivity::class.java)
      )
    }
  }

  @Test
  fun `clicking next navigates to next controller, popping to root pops`() {
    val activity = Robolectric.setupActivity(clazz)

    fun navigateNext(iteration: Int) {
      activity.findViewById<TextView>(R.id.nextBtn).performClick()
      val midText = activity.findViewById<TextView>(R.id.midText)
      assertEquals(midText.text.toString(), "View #$iteration")
    }

    (1..10).forEach {
      navigateNext(it)
    }

    activity.findViewById<TextView>(R.id.popToRootBtn).performClick()
    val midText = activity.findViewById<TextView>(R.id.midText)
    assertEquals(midText.text.toString(), "View #0")
  }

  @Test
  fun `navigating up twice crashes`() {
    val activity = Robolectric.setupActivity(clazz)
    activity.findViewById<TextView>(R.id.upBtn).performClick()

    try {
      activity.findViewById<TextView>(R.id.upBtn).performClick()
      fail("there should have been a crash")
    } catch (e: IllegalArgumentException) {
    }
  }

  @Test
  fun `multiple pops to root does not crash`() {
    val activity = Robolectric.setupActivity(clazz)
    activity.findViewById<TextView>(R.id.popToRootBtn).apply {
      performClick()
      performClick()
      performClick()
      performClick()
    }
  }
}