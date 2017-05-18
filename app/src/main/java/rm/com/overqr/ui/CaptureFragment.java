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
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import rm.com.overqr.OverqrApplication;
import rm.com.overqr.R;
import rm.com.overqr.utils.Logger;

/**
 * Created by alex
 */

@SuppressWarnings("MissingPermission") public final class CaptureFragment extends BaseFragment {

  @Inject Handler operationHandler;
  @Inject ExecutorService executor;

  @BindView(R.id.capture_preview) SurfaceView preview;

  private CameraSource cameraSource;
  private BarcodeDetector detector;
  private SurfaceHolder cachedHolder;

  private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
    @Override public void surfaceCreated(SurfaceHolder holder) {
      if (cachedHolder != null) {
        return;
      }

      cachedHolder = holder;
      startPreview(holder);
    }

    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      findBestPreviewSize(preview.getWidth(), preview.getHeight());
    }

    @Override public void surfaceDestroyed(SurfaceHolder holder) {
    }
  };

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

    setupBarcodeDetector();

    preview.getHolder().addCallback(surfaceCallback);
    preview.setKeepScreenOn(true);
  }

  private void setupBarcodeDetector() {
    detector =
        new BarcodeDetector.Builder(getActivity()).setBarcodeFormats(Barcode.QR_CODE).build();

    detector.setProcessor(new Detector.Processor<Barcode>() {
      @Override public void release() {

      }

      @Override public void receiveDetections(Detector.Detections<Barcode> detections) {
        if (detections.getDetectedItems().size() == 1) {
          Logger.d(detections.getDetectedItems().get(0).displayValue);
        }
      }
    });
  }

  @Override public void onResume() {
    super.onResume();
    if (cachedHolder != null) {
      setupBarcodeDetector();
      startPreview(cachedHolder);
    }
  }

  @Override public void onPause() {
    super.onPause();
    releasePreview();
  }

  private void startPreview(@NonNull final SurfaceHolder holder) {
    executor.submit(new Runnable() {
      @Override public void run() {
        try {
          cameraSource = new CameraSource.Builder(getActivity(), detector).setRequestedFps(15F)
              .setAutoFocusEnabled(true)
              .setFacing(CameraSource.CAMERA_FACING_BACK)
              .build();

          cameraSource.start(holder);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void releasePreview() {
    executor.submit(new Runnable() {
      @Override public void run() {
        if (cameraSource != null) {
          cameraSource.stop();
          cameraSource.release();
          cameraSource = null;
        }
      }
    });
  }

  private void findBestPreviewSize(int width, int height) {
    int previewWidth = 240;
    int previewHeight = 320;

    int childWidth;
    int childHeight;
    int childXOffset = 0;
    int childYOffset = 0;
    float widthRatio = (float) width / (float) previewWidth;
    float heightRatio = (float) height / (float) previewHeight;

    // To fill the view with the camera preview, while also preserving the correct aspect ratio,
    // it is usually necessary to slightly oversize the child and to crop off portions along one
    // of the dimensions.  We scale up based on the dimension requiring the most correction, and
    // compute a crop offset for the other dimension.
    if (widthRatio > heightRatio) {
      childWidth = width;
      childHeight = (int) ((float) previewHeight * widthRatio);
      childYOffset = (childHeight - height) / 2;
    } else {
      childWidth = (int) ((float) previewWidth * heightRatio);
      childHeight = height;
      childXOffset = (childWidth - width) / 2;
    }

    preview.layout(-1 * childXOffset, -1 * childYOffset, childWidth - childXOffset,
        childHeight - childYOffset);
  }
}
