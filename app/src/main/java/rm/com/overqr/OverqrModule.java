package rm.com.overqr;

import android.os.Handler;
import android.os.Looper;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Singleton;

/**
 * Created by alex
 */

@Module final class OverqrModule {

  @Provides @Singleton static ExecutorService provideExecutorService() {
    return Executors.newSingleThreadScheduledExecutor();
  }

  @Provides @Singleton static Handler provideOperationHandler() {
    return new Handler(Looper.getMainLooper());
  }
}
