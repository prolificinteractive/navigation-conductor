package com.prolificinteractive.conductor.archnavigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.NavigatorProvider;

@Navigator.Name("controller")
public class ControllerNavigator extends Navigator<ControllerNavigator.Destination> {
  private Router router;

  public ControllerNavigator(@NonNull final Router router) {
    this.router = router;

    router.addChangeListener(new ControllerChangeHandler.ControllerChangeListener() {
      @Override
      public void onChangeStarted(
          @Nullable Controller to,
          @Nullable Controller from,
          boolean isPush,
          @NonNull ViewGroup container,
          @NonNull ControllerChangeHandler handler) {
      }

      @Override
      public void onChangeCompleted(@Nullable Controller to,
                                    @Nullable Controller from,
                                    boolean isPush,
                                    @NonNull ViewGroup container,
                                    @NonNull ControllerChangeHandler handler) {
        int backStackEffect;
        if (!isPush) {
          backStackEffect = BACK_STACK_DESTINATION_POPPED;
        } else {
          backStackEffect = BACK_STACK_DESTINATION_ADDED;
        }

        if (from != null && to != null) {
         // dispatchOnNavigatorNavigated(Integer.valueOf(getLastTag()), backStackEffect);
        }
      }
    });
  }

  @Nullable
  private RouterTransaction getLastTransaction() {
    if (router.getBackstackSize() == 0) {
      return null;
    }
    return router.getBackstack().get(router.getBackstackSize() - 1);
  }

  @Nullable
  private String getLastTag() {
    RouterTransaction transaction = getLastTransaction();
    if (transaction == null) {
      return null;
    } else {
      return transaction.tag();
    }
  }

  @Override
  public boolean popBackStack() {
    return router.popCurrentController();
  }

  @NonNull
  @Override
  public Destination createDestination() {
    return new Destination(this);
  }

  @Override
  public void navigate(@NonNull final ControllerNavigator.Destination destination, @Nullable Bundle args,
                       @Nullable NavOptions navOptions) {

    final Controller controller = destination.createController(args);

    router.pushController(RouterTransaction.with(controller));
  }

  /**
   * NavDestination specific to {@link ControllerNavigator}
   */
  public static class Destination extends NavDestination {
    private static final HashMap<String, Class<? extends Controller>> sControllerClasses =
        new HashMap<>();

    private Class<? extends Controller> mControllerClass;

    /**
     * Construct a new controller destination. This destination is not valid until you set the
     * Controller via {@link #setControllerClass(Class)}.
     *
     * @param controllerNavigator The {@link ControllerNavigator} which this destination
     *                            will be associated with. Generally retrieved via a
     *                            {@link NavController}'s
     *                            {@link NavigatorProvider#getNavigator(Class)} method.
     */
    Destination(@NonNull Navigator<Destination> controllerNavigator) {
      super(controllerNavigator);
    }

    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs) {
      super.onInflate(context, attrs);
      TypedArray a = context.getResources().obtainAttributes(attrs,
          com.prolificinteractive.conductor.archnavigation.R.styleable.ControllerNavigator);
      setControllerClass(getControllerClassByName(context, a.getString(
          com.prolificinteractive.conductor.archnavigation.R.styleable.ControllerNavigator_android_name)));
      a.recycle();
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Controller> getControllerClassByName(Context context, String name) {
      if (name != null && name.charAt(0) == '.') {
        name = context.getPackageName() + name;
      }
      Class<? extends Controller> clazz = sControllerClasses.get(name);
      if (clazz == null) {
        try {
          clazz = (Class<? extends Controller>) Class.forName(name, true,
              context.getClassLoader());
          sControllerClasses.put(name, clazz);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      }
      return clazz;
    }

    /**
     * Set the Controller associated with this destination
     *
     * @param clazz The class name of the Controller to show when you navigate to this
     *              destination
     * @return this {@link Destination}
     */
    public Destination setControllerClass(Class<? extends Controller> clazz) {
      mControllerClass = clazz;
      return this;
    }

    /**
     * Gets the Controller associated with this destination
     *
     * @return
     */
    public Class<? extends Controller> getControllerClass() {
      return mControllerClass;
    }

    /**
     * Create a new instance of the {@link Controller} associated with this destination.
     *
     * @param args optional args to set on the new Controller
     * @return an instance of the {@link #getControllerClass() Controller class} associated
     * with this destination
     */
    @SuppressWarnings("ClassNewInstance")
    public Controller createController(@Nullable Bundle args) {
      Class<? extends Controller> clazz = getControllerClass();
      if (clazz == null) {
        throw new IllegalStateException("controller class not set");
      }

      Controller f;
      try {
        Constructor constructors = getBundleConstructor(clazz.getConstructors());
        f = (Controller) constructors.newInstance(args);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return f;
    }

    @Nullable
    private static Constructor getBundleConstructor(@NonNull Constructor[] constructors) {
      for (Constructor constructor : constructors) {
        if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0] == Bundle.class) {
          return constructor;
        }
      }
      return null;
    }
  }
}
