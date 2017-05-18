package rm.com.overqr.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by alex
 */

public interface Navigator {
  void to(@NonNull Fragment dest);

  void back();
}
