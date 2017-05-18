package rm.com.overqr.ui.preview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public final class AutoFitSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

  private final SurfaceHolder surfaceHolder;

  private OnSurfaceReadyListener onSurfaceHolderReady;

  public AutoFitSurfaceView(@NonNull Context context) {
    this(context, null);
  }

  public AutoFitSurfaceView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AutoFitSurfaceView(@NonNull Context context, @Nullable AttributeSet attrs, int theme) {
    super(context, attrs, theme);
    this.surfaceHolder = getHolder();
    this.surfaceHolder.addCallback(this);
    this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    fitInBounds(w, h);
  }

  public void fitInBounds(int width, int height) {
    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Size cannot be negative.");
    }

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

    layout(-1 * childXOffset, -1 * childYOffset, childWidth - childXOffset,
        childHeight - childYOffset);
  }

  public void setOnSurfaceReady(@Nullable OnSurfaceReadyListener onSurfaceHolderReady) {
    this.onSurfaceHolderReady = onSurfaceHolderReady;
  }

  @Override public void surfaceCreated(SurfaceHolder holder) {
    if (onSurfaceHolderReady != null) {
      onSurfaceHolderReady.onReady(holder);
    }
  }

  @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
  }

  @Override public void surfaceDestroyed(SurfaceHolder holder) {
  }
}