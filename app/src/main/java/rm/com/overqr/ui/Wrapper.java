package rm.com.overqr.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by alex
 */

interface Wrapper {
  void navigateTo(@NonNull Fragment dest);

  void navigateBack();
}
