package com.tenduke.client.android.sample.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tenduke.client.android.sample.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/** Abstract base activity, which provides toolbar, progress bar and content-container.
 *
 */
public class AbstractActivity extends AppCompatActivity implements ProgressIndicator {

    public LinearLayout.LayoutParams LAYOUT_MATCH_MATCH = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
    public LinearLayout.LayoutParams LAYOUT_MATCH_FILL = new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1);

    private LinearLayout _root;
    private FrameLayout _progressContainer;
    private ProgressBar _progressBar;
    private FrameLayout _contentContainer;
    private Toolbar _toolbar;

    private boolean _progressIndicatorVisible;


    /** {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isProgressIndicatorVisible() {
        return _progressIndicatorVisible;
    }


    /** Creates the base layout:
     *
     *  <ul>
     *      <li>Toolbar</li>
     *      <li>Progress indicator</li>
     *      <li>Content view</li>
     *  </ul>
     *
     *  @param savedInstanceState {@inheritDoc}
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //
        super.onCreate (savedInstanceState);
        //
        _root = new LinearLayout(this);
        _root.setLayoutParams(LAYOUT_MATCH_MATCH);
        _root.setOrientation(LinearLayout.VERTICAL);

        // Toolbar
        _toolbar = new Toolbar(this);
        // TODO: Proper height... resolve this from theme obtainStyledAttributes().
        _toolbar.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        if (Build.VERSION.SDK_INT < 23) {
          _toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
          _toolbar.setTitleTextColor(getResources().getColor (android.R.color.white));
        }

        setSupportActionBar(_toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.identity_48dp);
        _root.addView(_toolbar);

        FrameLayout container = new FrameLayout(this);
        container.setLayoutParams(LAYOUT_MATCH_FILL);
        _root.addView(container);

        _contentContainer = new FrameLayout(this);
        _contentContainer.setLayoutParams(LAYOUT_MATCH_MATCH);
        container.addView(_contentContainer);

        _progressContainer = new FrameLayout(this);
        _progressContainer.setLayoutParams(LAYOUT_MATCH_MATCH);
        _progressContainer.setVisibility(View.VISIBLE);
        _progressContainer.setBackgroundColor(getResources().getColor(android.R.color.white));
        _progressContainer.setAlpha(0.8f);
        container.addView(_progressContainer);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

        layoutParams.gravity = Gravity.CENTER;

        _progressBar = new ProgressBar(this);
        _progressBar.setLayoutParams(layoutParams);
        _progressBar.setIndeterminate(true);
        _progressContainer.addView(_progressBar);

        _progressIndicatorVisible = true;

        super.setContentView(_root);
    }


    /** Inflates the given layout, and plugs the inflated layout into the base-layouts content container.
     *
     * @param layoutResID id of the LayoutResource to inflate and plug in.
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        //
        setContentView (getLayoutInflater().inflate(layoutResID, _contentContainer, false));
    }


    /** Sets the given view into the content container.
     *
     *  @param view view to set.
     */
    @Override
    public void setContentView(View view) {
        //
        _contentContainer.removeAllViews();
        _contentContainer.addView(view);
    }


    /** {@inheritDoc}
     *
     * @param visible {@inheritDoc}
     */
    @Override
    public void setProgressIndicatorVisible(boolean visible) {
        //
        if (_progressContainer != null) {
            if (visible) {
                final AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                animation.setDuration(200);
                _progressContainer.setAnimation(animation);
                _progressContainer.setVisibility(View.VISIBLE);
            }
            else {
                final AlphaAnimation animation = new AlphaAnimation(1f, 0f);
                animation.setDuration(200);
                _progressContainer.setAnimation(animation);
                _progressContainer.setVisibility(View.GONE);
            }
        }
        _progressIndicatorVisible = visible;
    }


    /** {@inheritDoc}
     */
    @Override
    public void setProgressIndicatorVisible() {
        //
        setProgressIndicatorVisible(_progressIndicatorVisible);
    }


    /** Sets toolbar title.
     *
     *  @param resourceId resource id
     */
    protected void setToolbarTitle (@StringRes int resourceId) {
        //
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resourceId);
        }
    }

}
