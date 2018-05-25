package com.prolificinteractive.conductor.archnavigation;

import android.app.Activity;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;

import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;

/**
 * NavHostController provides an area within your layout for self-contained navigation to occur.
 *
 * <p>NavHostController is intended to be used as the content area within a layout resource
 * defining your app's chrome around it, e.g.:</p>
 *
 * <pre class="prettyprint">
 *     <android.support.v4.widget.DrawerLayout
 *             xmlns:android="http://schemas.android.com/apk/res/android"
 *             xmlns:app="http://schemas.android.com/apk/res-auto"
 *             android:layout_width="match_parent"
 *             android:layout_height="match_parent">
 *         <com.prolificinteractive.conductor.archnavigation.archnavigation.NavHostLayout
 *             android:layout_width="match_parent"
 *             android:layout_height="match_parent"
 *             app:defaultNavHost="true"
 *             app:navGraph="@navigation/nav_graph"
 *             />
 *         <android.support.design.widget.NavigationView
 *                 android:layout_width="wrap_content"
 *                 android:layout_height="match_parent"
 *                 android:layout_gravity="start"/>
 *     </android.support.v4.widget.DrawerLayout>
 * </pre>
 *
 * <p>Each NavHostController has a {@link NavController} that defines valid navigation within
 * the navigation host. This includes the {@link NavGraph navigation graph} as well as navigation
 * state such as current location and back stack that will be saved and restored along with the
 * NavHostController itself.</p>
 *
 * <p>NavHostControllers register their navigation controller at the root of their view subtree
 * such that any descendant can obtain the controller instance through the {@link Navigation}
 * helper class's methods such as {@link Navigation#findNavController(View)}. View event listener
 * implementations such as {@link View.OnClickListener} within navigation destination
 * controllers can use these helpers to navigate based on user interaction without creating a tight
 * coupling to the navigation host.</p>
 */
public class NavHostLayout extends FrameLayout implements NavHost, LifecycleObserver {
  private static final String KEY_NAV_CONTROLLER_STATE =
      "android-support-nav:controller:navControllerState";
  private static final String KEY_DEFAULT_NAV_HOST = "android-support-nav:fragment:defaultHost";

  private int graphId;
  private boolean defaultHost;
  private StateViewModel viewModel;
  private NavController navController;
  private Router router;

  public NavHostLayout(@NonNull Context context) {
    super(context);
  }

  public NavHostLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    final TypedArray a = context.obtainStyledAttributes(attrs, com.prolificinteractive.conductor.archnavigation.R.styleable.NavHostLayout);
    graphId = a.getResourceId(com.prolificinteractive.conductor.archnavigation.R.styleable.NavHostLayout_navGraph, 0);
    defaultHost = a.getBoolean(com.prolificinteractive.conductor.archnavigation.R.styleable.NavHostLayout_defaultNavHost, false);
    a.recycle();

    init(context);
  }

  public void init(Context context) {
    viewModel = ViewModelProviders.of((AppCompatActivity) context).get(StateViewModel.class);
    Bundle savedInstanceState = viewModel.getState();

    router = Conductor.attachRouter((Activity) this.getContext(), this, savedInstanceState);
    navController = new NavController(context);
    Navigation.setViewNavController(this, navController);
    navController.getNavigatorProvider().addNavigator(createControllerNavigator());

    Bundle navState = null;
    if (savedInstanceState != null) {
      navState = savedInstanceState.getBundle(KEY_NAV_CONTROLLER_STATE);

      if (savedInstanceState.getBoolean(KEY_DEFAULT_NAV_HOST, false)) {
        defaultHost = true;
      }
    }

    if (navState != null) {
      navController.restoreState(navState);
    } else {
      if (defaultHost) {
        if (graphId != 0) {
          navController.setGraph(graphId);
        } else {
          navController.setMetadataGraph();
        }
      }
    }
  }

  @NonNull
  @Override
  public NavController getNavController() {
    if (navController == null) {
      throw new IllegalStateException("NavController is not available");
    }
    return navController;
  }

  @Nullable
  @Override
  protected Parcelable onSaveInstanceState() {
    Bundle navState = navController.saveState();
    if (navState != null) {
      viewModel.getState().putBundle(KEY_NAV_CONTROLLER_STATE, navState);
    }
    if (defaultHost) {
      viewModel.getState().putBoolean(KEY_DEFAULT_NAV_HOST, true);
    }
    return super.onSaveInstanceState();
  }

  @NonNull
  protected Navigator<ControllerNavigator.Destination> createControllerNavigator() {
    return new ControllerNavigator(router);
  }

  public boolean onBackPressed() {
    return router.handleBack();
  }

  public static class StateViewModel extends ViewModel {
    private final Bundle state;

    public StateViewModel() {
      this.state = new Bundle();
    }

    Bundle getState() {
      return state;
    }
  }
}