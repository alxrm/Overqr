package rm.com.overqr.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.google.android.gms.vision.barcode.Barcode;
import javax.inject.Inject;
import rm.com.overqr.R;
import rm.com.overqr.tracker.BarcodeTracker;
import rm.com.overqr.tracker.OnSingleBarcodeFoundListener;
import rm.com.overqr.ui.preview.AutoFitSurfaceView;
import rm.com.overqr.utils.Logger;

/**
 * Created by alex
 */

public final class CaptureFragment extends BaseFragment implements OnSingleBarcodeFoundListener {

  @Inject BarcodeTracker tracker;

  @BindView(R.id.capture_preview) AutoFitSurfaceView preview;

  public static CaptureFragment newInstance() {
    return new CaptureFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_capture, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    injector().inject(this);

    tracker.setSingleBarcodeListener(this);

    preview.setKeepScreenOn(true);
    preview.setOnSurfaceReady(holder -> {
      tracker.attachHolder(holder);
      tracker.start();
    });
  }

  @Override public void onResume() {
    super.onResume();
    tracker.start();
  }

  @Override public void onPause() {
    super.onPause();
    tracker.stop();
  }

  @Override public void onSingleBarcodeFound(@NonNull Barcode barcode) {
    Logger.d(barcode.displayValue);
  }
}
