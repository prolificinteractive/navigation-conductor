package com.prolificinteractive.conductor.archnavigation

import android.app.Activity
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.navigation.plusAssign
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router


/**
 * NavHostController provides an area within your layout for self-contained navigation to occur.
 *
 * NavHostController is intended to be used as the content area within a layout resource
 * defining your app's chrome around it, e.g.:
 *
 * Each NavHostController has a [NavController] that defines valid navigation within
 * the navigation host. This includes the [navigation graph][NavGraph] as well as navigation
 * state such as current location and back stack that will be saved and restored along with the
 * NavHostController itself.
 *
 * NavHostControllers register their navigation controller at the root of their view subtree
 * such that any descendant can obtain the controller instance through the [Navigation]
 * helper class's methods such as [Navigation.findNavController]. View event listener
 * implementations such as [View.OnClickListener] within navigation destination
 * controllers can use these helpers to navigate based on user interaction without creating a tight
 * coupling to the navigation host.
 */
class NavHostLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), NavHost, LifecycleObserver {

  private var graphId: Int = 0
  private var defaultHost: Boolean = false
  private var viewModel: StateViewModel
  private var navigationController: NavController
  private var router: Router

  init {
    val a = context.obtainStyledAttributes(attrs, com.prolificinteractive.conductor.archnavigation.R.styleable.NavHostLayout)
    graphId = a.getResourceId(com.prolificinteractive.conductor.archnavigation.R.styleable.NavHostLayout_navGraph, 0)
    defaultHost = a.getBoolean(com.prolificinteractive.conductor.archnavigation.R.styleable.NavHostLayout_defaultNavHost, false)
    a.recycle()

    viewModel = ViewModelProviders.of(context as AppCompatActivity).get(StateViewModel::class.java)
    val savedInstanceState = viewModel.state

    router = Conductor.attachRouter(this.context as Activity, this, savedInstanceState)
    navigationController = NavController(context)
    Navigation.setViewNavController(this, navigationController)
    navigationController.navigatorProvider += createControllerNavigator()

    var navState: Bundle? = null
    if (savedInstanceState != null) {
      navState = savedInstanceState.getBundle(KEY_NAV_CONTROLLER_STATE)

      if (savedInstanceState.getBoolean(KEY_DEFAULT_NAV_HOST, false)) {
        defaultHost = true
      }
    }

    if (navState != null) {
      navigationController.restoreState(navState)
    } else {
      if (defaultHost) {
        if (graphId != 0) {
          navigationController.setGraph(graphId)
        } else {
          navigationController.setMetadataGraph()
        }
      }
    }
  }

  override fun getNavController(): NavController = navigationController

  override fun onSaveInstanceState(): Parcelable? {
    val navState = navigationController.saveState()
    if (navState != null) {
      viewModel.state.putBundle(KEY_NAV_CONTROLLER_STATE, navState)
    }
    if (defaultHost) {
      viewModel.state.putBoolean(KEY_DEFAULT_NAV_HOST, true)
    }
    return super.onSaveInstanceState()
  }

  fun onBackPressed(): Boolean {
    return router.handleBack()
  }

  private fun createControllerNavigator(): Navigator<ControllerNavigator.Destination> {
    return ControllerNavigator(router)
  }

  class StateViewModel : ViewModel() {
    internal val state: Bundle = Bundle()
  }

  companion object {
    private const val KEY_NAV_CONTROLLER_STATE = "navControllerState"
    private const val KEY_DEFAULT_NAV_HOST = "defaultHost"
  }
}