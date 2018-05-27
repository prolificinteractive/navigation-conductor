package com.prolificinteractive.conductor.archnavigation

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.navigation.*
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import java.lang.reflect.Constructor
import java.util.*
import kotlin.collections.set

@Navigator.Name("controller")
class ControllerNavigator(private val router: Router) : Navigator<ControllerNavigator.Destination>() {

  private val lastTransaction: RouterTransaction?
    get() = if (router.backstackSize == 0) {
      null
    } else router.backstack[router.backstackSize - 1]

  private var lastPoppedTag: String = ""

  private val lastTag: String?
    get() {
      lastTransaction ?: return lastPoppedTag
      return lastTransaction?.tag()
    }

  init {
    router.addChangeListener(object : ControllerChangeHandler.ControllerChangeListener {
      override fun onChangeStarted(
          to: Controller?,
          from: Controller?,
          isPush: Boolean,
          container: ViewGroup,
          handler: ControllerChangeHandler) {
      }

      override fun onChangeCompleted(to: Controller?,
                                     from: Controller?,
                                     isPush: Boolean,
                                     container: ViewGroup,
                                     handler: ControllerChangeHandler) {
        val backStackEffect = if (isPush) {
          Navigator.BACK_STACK_DESTINATION_ADDED
        } else {
          Navigator.BACK_STACK_DESTINATION_POPPED
        }

        dispatchOnNavigatorNavigated(Integer.valueOf(lastTag), backStackEffect)
      }
    })
  }

  override fun popBackStack(): Boolean = if (router.backstackSize > 1) {
    lastPoppedTag = lastTransaction?.tag().orEmpty()
    router.popCurrentController()
  } else {
    false
  }

  override fun createDestination(): Destination {
    return Destination(this)
  }

  override fun navigate(destination: ControllerNavigator.Destination, args: Bundle?,
                        navOptions: NavOptions?) {

    val controller = destination.createController(args)

    val transaction = RouterTransaction
        .with(controller)
        .tag(destination.id.toString())

    if (!router.hasRootController()) {
      router.setRoot(transaction)
    } else {
      router.pushController(transaction)
    }
  }

  /**
   * NavDestination specific to [ControllerNavigator]
   */
  class Destination
  /**
   * Construct a new controller destination. This destination is not valid until you set the
   * Controller via [.setControllerClass].
   *
   * @param controllerNavigator The [ControllerNavigator] which this destination
   * will be associated with. Generally retrieved via a
   * [NavController]'s
   * [NavigatorProvider.getNavigator] method.
   */
  internal constructor(controllerNavigator: Navigator<Destination>) : NavDestination(controllerNavigator) {

    private var controllerClass: Class<out Controller>? = null

    override fun onInflate(context: Context, attrs: AttributeSet) {
      super.onInflate(context, attrs)
      val a = context.resources.obtainAttributes(attrs,
          com.prolificinteractive.conductor.archnavigation.R.styleable.ControllerNavigator)
      controllerClass = getControllerClassByName(context, a.getString(
          com.prolificinteractive.conductor.archnavigation.R.styleable.ControllerNavigator_android_name))
      a.recycle()
    }

    private fun getControllerClassByName(context: Context, name: String): Class<out Controller>? {
      var name = name
      if (name.isNotEmpty() && name[0] == '.') {
        name = context.packageName + name
      }

      var clazz: Class<out Controller>? = controllerClasses[name]
      if (clazz == null) {
        try {
          clazz = Class.forName(name, true, context.classLoader) as Class<out Controller>
          controllerClasses[name] = clazz
        } catch (e: ClassNotFoundException) {
          throw RuntimeException(e)
        }

      }
      return clazz
    }

    /**
     * Create a new instance of the [Controller] associated with this destination.
     *
     * @param args optional args to set on the new Controller
     * @return an instance of the [Controller class][.getControllerClass] associated
     * with this destination
     */
    fun createController(args: Bundle?): Controller {
      val clazz = controllerClass ?: throw IllegalStateException("controller class not set")

      val c: Controller
      try {
        val constructors = getBundleConstructor(clazz.constructors)
        c = constructors!!.newInstance(args) as Controller
      } catch (e: Exception) {
        throw RuntimeException(e)
      }

      return c
    }

    companion object {
      private val controllerClasses = HashMap<String, Class<out Controller>>()

      private fun getBundleConstructor(constructors: Array<Constructor<*>>): Constructor<*>? {
        for (constructor in constructors) {
          if (constructor.parameterTypes.size == 1 && constructor.parameterTypes[0] == Bundle::class.java) {
            return constructor
          }
        }
        return null
      }
    }
  }
}
