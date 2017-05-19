package rm.com.overqr.tracker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.view.SurfaceHolder;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import rm.com.overqr.utils.Lists;
import rm.com.overqr.utils.Permissions;

/**
 * Created by alex
 */

@SuppressWarnings("ALL") public final class BarcodeTracker {

  /**
   * best fps in order to provide non-darkened preview
   */
  private static final float TARGET_FPS = 15F;

  @NonNull private final Context context;
  @NonNull private final ExecutorService executor;
  @NonNull private final Handler replyHandler;

  @Nullable private CameraSource cameraSource;
  @Nullable private BarcodeDetector detector;
  @Nullable private SurfaceHolder cachedHolder;

  @Nullable private OnErrorListener errorListener;
  @Nullable private OnSingleBarcodeFoundListener singleBarcodeListener;
  @Nullable private OnMultipleBarcodeFoundListener multipleBarcodeListener;

  public BarcodeTracker(@NonNull Context context, @NonNull ExecutorService executor,
      @NonNull Handler replyHandler) {
    this.context = context;
    this.executor = executor;
    this.replyHandler = replyHandler;
  }

  public final void attachHolder(@Nullable SurfaceHolder holder) {
    if (holder == null) {
      return;
    }

    cachedHolder = holder;
  }

  public final void start() {
    if (cachedHolder == null) {
      return;
    }

    executor.submit(() -> {
      initDetector();
      initCameraSource(cachedHolder);
    });
  }

  public final void stop() {
    executor.submit(() -> {
      if (cameraSource != null) {
        cameraSource.stop();
        cameraSource.release();
        cameraSource = null;
      }
    });
  }

  public final void setErrorListener(@Nullable OnErrorListener errorListener) {
    this.errorListener = errorListener;
  }

  public final void setSingleBarcodeListener(
      @Nullable OnSingleBarcodeFoundListener singleBarcodeListener) {
    this.singleBarcodeListener = singleBarcodeListener;
  }

  public final void setMultipleBarcodeListener(
      @Nullable OnMultipleBarcodeFoundListener multipleBarcodeListener) {
    this.multipleBarcodeListener = multipleBarcodeListener;
  }

  @WorkerThread private void initDetector() {
    detector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build();

    if (!isTrackingAvailable(context)) {
      signalError("Could not start tracking");
      return;
    }

    detector.setProcessor(new Detector.Processor<Barcode>() {
      @Override public void release() {
        /*no-op*/
      }

      @Override public void receiveDetections(Detector.Detections<Barcode> detections) {
        final List<Barcode> detectionsList = Lists.fromSparseArray(detections.getDetectedItems());

        if (detectionsList.size() == 1 && singleBarcodeListener != null) {
          replyHandler.post(
              () -> singleBarcodeListener.onSingleBarcodeFound(detectionsList.get(0)));
        }

        if (detectionsList.size() != 0 && multipleBarcodeListener != null) {
          replyHandler.post(() -> multipleBarcodeListener.onMultipleBarcodeFound(detectionsList));
        }
      }
    });
  }

  @WorkerThread private void initCameraSource(@NonNull SurfaceHolder holder) {
    if (!isTrackingAvailable(context)) {
      signalError("Could not start tracking");
      return;
    }

    try {
      cameraSource = new CameraSource.Builder(context, detector).setRequestedFps(TARGET_FPS)
          .setAutoFocusEnabled(hasAutoFocus(context))
          .setFacing(CameraSource.CAMERA_FACING_BACK)
          .build();

      cameraSource.start(holder);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void signalError(@NonNull String msg) {
    if (errorListener != null) {
      errorListener.onError(msg);
    }
  }

  private boolean isTrackingAvailable(@NonNull Context context) {
    return detector != null && detector.isOperational() && Permissions.isCameraPermissionGranted(
        context);
  }

  private boolean hasAutoFocus(@NonNull Context context) {
    return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
  }
}
