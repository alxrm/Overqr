package rm.com.overqr.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.OnClick;
import rm.com.overqr.R;
import rm.com.overqr.utils.Permissions;

/**
 * Created by alex
 */

public final class IntroFragment extends BaseFragment {

  private static final int REQ_CAMERA_PERMISSION = 1;

  public static IntroFragment newInstance() {
    return new IntroFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_intro, container, false);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (Permissions.isCameraPermissionGranted(getActivity())) {
      onStartCapture();
    }
  }

  @OnClick(R.id.intro_start) final void onStartCapture() {
    if (!hasCameraHardware(getActivity())) {
      Toast.makeText(getActivity(), "The device does not have a camera", Toast.LENGTH_LONG).show();
      return;
    }

    if (Permissions.isCameraPermissionGranted(getActivity())) {
      navigateTo(CaptureFragment.newInstance());
      //startActivity(new Intent(getActivity(), CaptureActivity.class));
    } else {
      requestCameraPermission();
    }
  }

  private void requestCameraPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(new String[] { Manifest.permission.CAMERA }, REQ_CAMERA_PERMISSION);
    }
  }

  private boolean hasCameraHardware(Context context) {
    return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
  }
}
