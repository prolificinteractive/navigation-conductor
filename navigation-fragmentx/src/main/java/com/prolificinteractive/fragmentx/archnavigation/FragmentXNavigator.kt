package com.prolificinteractive.fragmentx.archnavigation

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.*
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavController.Companion.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.prolificinteractive.conductor.archnavigation.R
import com.prolificinteractive.fragmentx.archnavigation.FragmentXNavigator.Destination
import java.util.*
import kotlin.collections.set

@Navigator.Name("fragmentx")
class FragmentXNavigator(private val controller: FragNavController) : Navigator<Destination>() {

  companion object {
    const val NO_INDEX = -1
    const val ARG_DESTINATION_ID = "destinationId"

    private const val KEY_LAST_TAB_TRANSACTION_ID = "lastTabTransactionId"
    private const val KEY_LAST_TAB_TRANSACTION_INDEX = "lastTabTransactionIndex"
  }

  private val transactionListener = TransactionListener()

  private var isInitialized: Boolean = false
  private var savedState: Bundle = Bundle()

  init {
    controller.transactionListener = transactionListener
    controller.fragmentHideStrategy = DETACH_ON_NAVIGATE_HIDE_ON_SWITCH
  }

  fun getFragmentId(f: Fragment?): Int? {
    return f?.arguments?.getInt("destinationId")
  }

  override fun popBackStack(): Boolean {
    Log.d("Navigator#popBackStack", "size: ${controller.currentStack.orEmpty().size} currentFrag: ${controller.currentFrag}")
    return if (controller.currentStack.orEmpty().size > 1) {
      controller.popFragment()
    } else {
      false
    }
  }

  override fun createDestination(): Destination {
    return Destination(this)
  }

  override fun navigate(destination: FragmentXNavigator.Destination, args: Bundle?,
                        navOptions: NavOptions?) {
    Log.d("Navigator", "navigate(${destination.label})")


    val destinationIndex = getIndexFromFragment(controller.rootFragments, destination.id)

    if (!isInitialized) {
      controller.initialize(index = destinationIndex, savedInstanceState = savedState)
      isInitialized = true
      return
    }

    if (destination.isRootFragment) {
      controller.switchTab(destinationIndex)
    } else {
      val options = FragNavTransactionOptions
          .newBuilder()
//          .apply {
//            enterAnimation = navOptions?.enterAnim ?: -1
//            exitAnimation = navOptions?.exitAnim ?: -1
//            popEnterAnimation = navOptions?.popEnterAnim ?: -1
//            popExitAnimation = navOptions?.popExitAnim ?: -1
//          }
          .transition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
          .build()

      if (navOptions?.popUpTo != 0) {
        val destinationFragment = getFragmentWithId(controller.currentStack, destination.id)
        val popInclusive = if (navOptions?.isPopUpToInclusive == true) 0 else 1
        val popCount = controller.currentStack!!.search(destinationFragment) + popInclusive
        if (popCount > 0) {
          controller.popFragments(popCount, options)
        }
      } else {
        val fragment = destination.createFragment(args)
        controller.pushFragment(fragment, options)
      }

    }
  }

  fun getIndexFromFragment(collection: Collection<Fragment>?, id: Int): Int {
    return collection.orEmpty().indexOfFirst {
      it.arguments!!.getInt(ARG_DESTINATION_ID) == id
    }
  }

  fun getFragmentWithId(collection: Collection<Fragment>?, id: Int): Fragment? {
    return collection.orEmpty().firstOrNull {
      it.arguments!!.getInt(ARG_DESTINATION_ID) == id
    }
  }

  override fun onSaveState(): Bundle? {
    transactionListener.onSaveState(savedState)
    controller.onSaveInstanceState(savedState)
    return savedState
  }

  override fun onRestoreState(savedState: Bundle) {
    this.savedState = savedState
    transactionListener.onRestoreState(savedState)
    controller.initialize(transactionListener.lastTabTransactionIndex, savedState)
    isInitialized = true
  }

  private inner class TransactionListener : FragNavController.TransactionListener {
    var lastTabTransactionId: Int = 0
      private set
    var lastTabTransactionIndex: Int = NO_INDEX
      private set

    override fun onFragmentTransaction(fragment: Fragment?, transactionType: FragNavController.TransactionType) {
      val id = getFragmentId(fragment)

      id?.let {
        when (transactionType) {
          FragNavController.TransactionType.PUSH -> {
            dispatchOnNavigatorNavigated(it, BACK_STACK_DESTINATION_ADDED)
            Log.d("Navigator#onFragmentTransaction", "dispatchOnNavigatorNavigated(BACK_STACK_DESTINATION_ADDED) $fragment")
          }
          FragNavController.TransactionType.POP -> {
            dispatchOnNavigatorNavigated(it, BACK_STACK_DESTINATION_POPPED)
            Log.d("Navigator#onFragmentTransaction", "dispatchOnNavigatorNavigated(BACK_STACK_DESTINATION_POPPED) $fragment")
          }
          FragNavController.TransactionType.REPLACE -> {
            dispatchOnNavigatorNavigated(it, BACK_STACK_UNCHANGED)
            Log.d("Navigator#onFragmentTransaction", "dispatchOnNavigatorNavigated(BACK_STACK_UNCHANGED) $fragment")
          }
          else -> {
          }
        }
      }
    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {
      Log.d("Navigator", "onTabTransaction ${fragment?.javaClass?.simpleName}")

      val id = getFragmentId(fragment)

      if (lastTabTransactionIndex != NO_INDEX) {
        val lastStack = controller.getStack(lastTabTransactionIndex)
        lastStack.orEmpty().forEach { lastFragment ->
          getFragmentId(lastFragment)?.let { fragmentID ->
            dispatchOnNavigatorNavigated(fragmentID, BACK_STACK_DESTINATION_POPPED)
            Log.d("Navigator#onTabTransaction", "dispatchOnNavigatorNavigated(BACK_STACK_DESTINATION_POPPED) $lastFragment")
          }
        }
      }


      Log.d("Navigator#currentStack", "${controller.currentStack?.size}")

      controller.currentStack.orEmpty().forEach { currentFragment ->
        getFragmentId(currentFragment)?.let { fragmentID ->
          dispatchOnNavigatorNavigated(fragmentID, BACK_STACK_DESTINATION_ADDED)
          Log.d("Navigator#onTabTransaction", "dispatchOnNavigatorNavigated(BACK_STACK_DESTINATION_ADDED) $currentFragment")
        }
      }

      // There is a bug in FragNav where the currentStack size does not update until it has been
      // navigated away from for the first time.
      if (controller.currentStack.orEmpty().isEmpty()) {
        getFragmentId(fragment)?.let { fragmentID ->
          dispatchOnNavigatorNavigated(fragmentID, BACK_STACK_DESTINATION_ADDED)
          Log.d("Navigator#onTabTransaction", "dispatchOnNavigatorNavigated(BACK_STACK_DESTINATION_ADDED) $fragment")
        }
      }

      lastTabTransactionIndex = index
      id?.let {
        lastTabTransactionId = id
      }
    }

    fun onSaveState(outState: Bundle) {
      outState.putInt(KEY_LAST_TAB_TRANSACTION_ID, lastTabTransactionId)
      outState.putInt(KEY_LAST_TAB_TRANSACTION_INDEX, lastTabTransactionIndex)
    }

    fun onRestoreState(savedState: Bundle) {
      lastTabTransactionId = savedState.getInt(KEY_LAST_TAB_TRANSACTION_ID, lastTabTransactionId)
      lastTabTransactionIndex = savedState.getInt(KEY_LAST_TAB_TRANSACTION_INDEX, lastTabTransactionIndex)
    }
  }

  /**
   * NavDestination specific to [FragmentXNavigator]
   *
   * Construct a new controller destination. This destination is not valid until you set the
   * Controller via [.setControllerClass].
   *
   * @param controllerNavigator The [FragmentXNavigator] which this destination
   * will be associated with. Generally retrieved via a
   * [NavController]'s
   * [NavigatorProvider.getNavigator] method.
   */
  class Destination(controllerNavigator: Navigator<Destination>) : NavDestination(controllerNavigator) {

    private lateinit var fragmentClass: Class<out Fragment>

    var isRootFragment = false

    override fun onInflate(context: Context, attrs: AttributeSet) {
      super.onInflate(context, attrs)
      val a = context.resources.obtainAttributes(attrs,
          R.styleable.FragmentXNavigator)
      fragmentClass = getFragmentClassByName(context, a.getString(
          R.styleable.FragmentXNavigator_android_name))
      isRootFragment = a.getBoolean(R.styleable.FragmentXNavigator_isRoot, false)

      a.recycle()
    }

    private fun getFragmentClassByName(context: Context, name: String): Class<out Fragment> {
      var name = name
      if (name.isNotEmpty() && name[0] == '.') {
        name = context.packageName + name
      }

      var clazz: Class<out Fragment>? = controllerClasses[name]
      if (clazz == null) {
        try {
          clazz = Class.forName(name, true, context.classLoader) as Class<out Fragment>
          controllerClasses[name] = clazz
        } catch (e: ClassNotFoundException) {
          throw RuntimeException(e)
        }

      }
      return clazz
    }

    /**
     * Create a new instance of the [android.support.v4.app.Fragment] associated with this destination.
     *
     * @param args optional args to set on the new android.support.v4.app.Fragment
     * @return an instance of the [android.support.v4.app.Fragment class][.getandroid.support.v4.app.FragmentClass] associated
     * with this destination
     */
    fun createFragment(args: Bundle?): Fragment {
      val arguments = args ?: Bundle()
      arguments.putInt(ARG_DESTINATION_ID, id)
      val fragment: Fragment
      try {
        fragment = fragmentClass.newInstance() as Fragment
        fragment.arguments = arguments
      } catch (e: Exception) {
        throw RuntimeException(e)
      }

      return fragment
    }

    companion object {
      private val controllerClasses = HashMap<String, Class<out Fragment>>()
    }
  }
}
