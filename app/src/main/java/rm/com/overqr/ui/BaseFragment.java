package rm.com.overqr.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.Unbinder;

abstract class BaseFragment extends Fragment {

  protected Unbinder unbinder;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    final Bundle args = getArguments();

    if (args != null) {
      unwrapArguments(args);
    }
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      navigateBack();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  protected void unwrapArguments(@NonNull Bundle args) {
  }

  protected void navigateBack() {
    final Wrapper owner = getWrapper();

    if (owner != null) {
      owner.navigateBack();
    }
  }

  @Nullable final protected Wrapper getWrapper() {
    return (Wrapper) getActivity();
  }

  final protected void navigateTo(@NonNull Fragment fragment) {
    final Wrapper owner = getWrapper();

    if (owner != null) {
      owner.navigateTo(fragment);
    }
  }
}