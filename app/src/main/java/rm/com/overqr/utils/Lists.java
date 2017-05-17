package rm.com.overqr.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alex
 */

public final class Lists {
  private Lists() {
  }

  @NonNull public static <T> List<T> fromSparseArray(@Nullable SparseArray<T> from) {
    if (from == null || from.size() == 0) {
      return Collections.emptyList();
    }

    final ArrayList<T> result = new ArrayList<>(from.size());

    for (int i = 0; i < from.size(); i++) {
      T elem = from.get(i);

      if (elem != null) {
        result.add(elem);
      }
    }

    return result;
  }
}
