package rm.com.overqr.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

/**
 * Created by alex
 */

public final class Permissions {

  private Permissions() {
  }

  public static boolean isCameraPermissionGranted(@NonNull Context context) {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED;
  }
}