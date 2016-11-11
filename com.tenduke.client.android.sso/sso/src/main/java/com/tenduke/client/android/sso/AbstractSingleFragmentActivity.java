package com.tenduke.client.android.sso;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tenduke.client.android.android_utils.ViewIdGenerator;


/** Very simple base Activity, which hosts single fragment.
 *
 */
public abstract class AbstractSingleFragmentActivity extends AppCompatActivity {

    /** Key of in-parameter for the toolbar-title. */
    public static final String EXTRA_IN_TITLE = "extra.in.title";

    /** Key of return value for Error, to be used with {@link android.content.Intent#getSerializableExtra(java.lang.String)}. */
    public static final String EXTRA_OUT_ERROR = "extra.out.error";

    /** Result code indicating error. */
    public static final int RESULT_ERROR = RESULT_FIRST_USER;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        final View fragmentContainer = setupLayout ();
        setFragment (createFragment(), fragmentContainer.getId());
    }


    /** Sets up the base layout.
     *
     *  NOTE: This method currently calls {@link #setContentView(View)}. Subclasses, which
     *  override this method may need to do that manually somehow.
     *
     * @return the fragment container.
     */
    protected View setupLayout () {
        //
        // Create root container:
        final LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        // Create toolbar:
        final Toolbar toolbar = createToolbar(getIntent().getStringExtra(EXTRA_IN_TITLE));
        if (toolbar != null) {
            root.addView(toolbar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        // Create fragment container
        final View fragmentContainer = createFragmentContainer();
        root.addView(fragmentContainer, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));

        setContentView(root);

        return fragmentContainer;
    }


    /** Creates the toolbar.
     *
     *  @return the toolbar, or {@code null} if no toolbar is wanted.
     */
    protected @Nullable Toolbar createToolbar(@Nullable final String defaultTitle) {
        //
        final Toolbar toolbar = new Toolbar(this);
        //
        setSupportActionBar(toolbar);
        toolbar.setTitle(defaultTitle);

        return toolbar;
    }


    /** Creates container element for the fragment.
     *
     *  NOTE: The container must have id.
     *
     *  @return the container for the fragment.
     */
    protected @NonNull ViewGroup createFragmentContainer () {
        //
        final FrameLayout fragmentContainer = new FrameLayout(this);
        fragmentContainer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        fragmentContainer.setId(ViewIdGenerator.generateViewId());
        return fragmentContainer;
    }


    /** Creates the fragment, implementation point for the subclasses.
     *
     * @return the fragment
     */
    protected abstract @NonNull Fragment createFragment ();


    /** Sets the fragment into given container.
     *
     * @param fragment The fragment
     * @param containerId Id of the container.
     */
    protected void setFragment (
            @NonNull final Fragment fragment,
            int containerId) {
        //
        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(containerId, fragment);
        transaction.commit();
    }

}
