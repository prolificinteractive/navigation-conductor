package com.prolificinteractive.fragmentx.archnavigation

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.util.Log
import androidx.navigation.*
import com.ncapdevi.fragnav.FragNavController
import com.prolificinteractive.conductor.archnavigation.R
import com.prolificinteractive.fragmentx.archnavigation.FragmentXNavigator.Destination
import java.lang.reflect.Constructor
import java.util.*
import kotlin.collections.set

@Navigator.Name("fragmentx")
class FragmentXNavigator(private val fragNavBuilder: FragNavController.Builder, provider: () -> List<Fragment>) : Navigator<Destination>() {


  private lateinit var rootFragments: List<Fragment>

  val controller: FragNavController by lazy {
    this.rootFragments = provider()
    val c = fragNavBuilder.rootFragments(rootFragments)
        .transactionListener(TabChange())
        .build()

    c
  }

  inner class TabChange : FragNavController.TransactionListener {

    var lastTabTransactionId: Int = 0
    var lastTabTransactionIndex: Int = 0

    override fun onFragmentTransaction(f: Fragment?, p1: FragNavController.TransactionType?) {
      val id = getFragmentId(f)

      id?.let {
        when (p1) {
          FragNavController.TransactionType.PUSH -> dispatchOnNavigatorNavigated(it, BACK_STACK_DESTINATION_ADDED)
          FragNavController.TransactionType.POP -> dispatchOnNavigatorNavigated(it, BACK_STACK_DESTINATION_POPPED)
          FragNavController.TransactionType.REPLACE -> dispatchOnNavigatorNavigated(it, BACK_STACK_UNCHANGED)
        }
      }
    }

    override fun onTabTransaction(f: Fragment?, p1: Int) {
//      Log.d("Navigator", "onTabTransaction ${f?.javaClass?.simpleName}")
//
//      val id = getFragmentId(f)
//
//      if (lastTabTransactionIndex != 0) {
//        val lastStack = controller.getStack(lastTabTransactionIndex)
//        lastStack.orEmpty().forEach { fragment ->
//          Log.d("Navigator", "dispatchOnNavigatorNavigated(BACK_STACK_DESTINATION_POPPED) ${fragment.javaClass.simpleName}")
//          getFragmentId(fragment)?.let { fragmentID ->
//            dispatchOnNavigatorNavigated(fragmentID, BACK_STACK_DESTINATION_POPPED)
//          }
//        }
//      }
//
//      controller.currentStack.orEmpty().forEach { fragment ->
//        getFragmentId(fragment)?.let { fragmentID ->
//          Log.d("Navigator", "dispatchOnNavigatorNavigated(BACK_STACK_DESTINATION_ADDED) ${fragment.javaClass.simpleName}")
//          dispatchOnNavigatorNavigated(fragmentID, BACK_STACK_DESTINATION_ADDED)
//        }
//      }
//
//      lastTabTransactionIndex = p1
//      id?.let {
//        lastTabTransactionId = id
//      }
    }
  }

  fun getFragmentId(f: Fragment?): Int? {
    val id = f?.arguments?.getInt("destinationId")
    return id
  }

  override fun popBackStack(): Boolean = true

  override fun createDestination(): Destination {
    return Destination(this)
  }

  var isInitial: Boolean = false

  override fun navigate(destination: FragmentXNavigator.Destination, args: Bundle?,
                        navOptions: NavOptions?) {
    Log.d("Navigator", "navigate(${destination.label})")

    if (destination.isRootFragment) {
//      if (controller.currentStack.orEmpty().size == 1) {
//        dispatchOnNavigatorNavigated(destination.id, BACK_STACK_DESTINATION_ADDED)
//      }
      controller.switchTab(rootFragments.indexOfFirst {
        it.arguments!!.getInt("destinationId") == destination.id
      })
    } else {
      val fragment = destination.createFragment(args)

      controller.pushFragment(fragment)
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
      arguments?.putInt("destinationId", id)
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

      private fun getBundleConstructor(constructors: Array<Constructor<*>>): Constructor<*> {
        for (constructor in constructors) {
          if (constructor.parameterTypes.size == 1 && constructor.parameterTypes[0] == Bundle::class.java) {
            return constructor
          }
        }

        throw IllegalStateException("The controller does not have a bundle constructor.")
      }
    }
  }
}
