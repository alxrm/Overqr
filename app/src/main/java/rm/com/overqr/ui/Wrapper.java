package rm.com.overqr.ui;

import android.app.Fragment;
import android.support.annotation.NonNull;

/**
 * Created by alex
 */

interface Wrapper {
  void navigateTo(@NonNull Fragment dest);

  void navigateBack();
}
