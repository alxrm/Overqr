package rm.com.overqr.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;
import javax.inject.Inject;
import rm.com.overqr.OverqrApplication;
import rm.com.overqr.R;
import rm.com.overqr.utils.Logger;

/**
 * Created by alex
 */

public final class CaptureFragment extends BaseFragment {

  @Inject Handler operationHandler;

  @BindView(R.id.capture_preview) SurfaceView preview;

  private CameraSource cameraSource;

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
    ((OverqrApplication) getActivity().getApplication()).injector().inject(this);

    BarcodeDetector detector =
        new BarcodeDetector.Builder(getActivity()).setBarcodeFormats(Barcode.QR_CODE).build();

    detector.setProcessor(new Detector.Processor<Barcode>() {
      @Override public void release() {

      }

      @Override public void receiveDetections(Detector.Detections<Barcode> detections) {
        if (detections.getDetectedItems().size() != 0) {
          Logger.d("New detection!");
        }
      }
    });

    preview.getHolder().addCallback(new SurfaceHolder.Callback() {
      @Override public void surfaceCreated(SurfaceHolder holder) {
        startPreview(holder);
      }

      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      }

      @Override public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
      }
    });

    cameraSource = new CameraSource.Builder(getActivity(), detector).setRequestedFps(25F)
        .setAutoFocusEnabled(true)
        .setFacing(CameraSource.CAMERA_FACING_BACK)
        .build();
  }

  @SuppressWarnings("MissingPermission") private void startPreview(@NonNull SurfaceHolder holder) {
    try {
      cameraSource.start(holder);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void stopPreview() {
    if (cameraSource != null) {
      cameraSource.stop();
      cameraSource.release();
      cameraSource = null;
    }
  }
}
