package rm.com.overqr;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Singleton;
import rm.com.overqr.tracker.BarcodeTracker;

/**
 * Created by alex
 */

@Module final class OverqrModule {

  @NonNull private final OverqrApplication application;

  OverqrModule(@NonNull OverqrApplication application) {
    this.application = application;
  }

  @Provides @Singleton static ExecutorService provideExecutorService() {
    return Executors.newSingleThreadScheduledExecutor();
  }

  @Provides @Singleton static Handler provideReplyHandler() {
    return new Handler(Looper.getMainLooper());
  }

  @Provides @Singleton BarcodeTracker provideBarcodeTracker(@NonNull ExecutorService executor,
      @NonNull Handler replyHandler) {
    return new BarcodeTracker(application.getApplicationContext(), executor, replyHandler);
  }
}
