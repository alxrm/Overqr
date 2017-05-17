package rm.com.overqr;

import dagger.Component;
import javax.inject.Singleton;
import rm.com.overqr.ui.CaptureFragment;

/**
 * Created by alex
 */

@Singleton @Component(modules = OverqrModule.class)
public interface OverqrComponent {
  void inject(CaptureFragment fragment);
}
