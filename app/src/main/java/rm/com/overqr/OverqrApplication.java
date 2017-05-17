package rm.com.overqr;

import android.app.Application;

/**
 * Created by alex
 */

public final class OverqrApplication extends Application {

  private OverqrComponent component;

  @Override public void onCreate() {
    super.onCreate();
    component = DaggerOverqrComponent.builder().overqrModule(new OverqrModule()).build();
  }

  public final OverqrComponent injector() {
    return component;
  }
}
