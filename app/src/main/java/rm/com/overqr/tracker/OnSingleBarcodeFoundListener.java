package rm.com.overqr.tracker;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by alex
 */

public interface OnSingleBarcodeFoundListener {
  @UiThread
  void onSingleBarcodeFound(@NonNull Barcode barcode);
}
