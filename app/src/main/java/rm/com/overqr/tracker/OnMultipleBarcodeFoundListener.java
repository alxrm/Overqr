package rm.com.overqr.tracker;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import com.google.android.gms.vision.barcode.Barcode;
import java.util.List;

/**
 * Created by alex
 */

public interface OnMultipleBarcodeFoundListener {
  @UiThread
  void onMultipleBarcodeFound(@NonNull List<Barcode> barcode);
}
