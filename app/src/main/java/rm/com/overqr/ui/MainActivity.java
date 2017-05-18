package rm.com.overqr.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rm.com.overqr.R;
import rm.com.overqr.ui.fragment.IntroFragment;

public final class MainActivity extends AppCompatActivity implements Navigator {

  @BindView(R.id.toolbar) Toolbar toolbar;

  private Unbinder unbinder;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    unbinder = ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    navigateTo(IntroFragment.newInstance(), true);
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  @Override public void to(@NonNull Fragment dest) {
    navigateTo(dest, false);
  }

  @Override public void back() {
    onBackPressed();
  }

  private void navigateTo(@NonNull Fragment fragment, boolean root) {
    final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .replace(R.id.container, fragment);

    if (!root) {
      fragmentTransaction.addToBackStack(null);
    }

    fragmentTransaction.commit();
  }
}
