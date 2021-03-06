package fr.guddy.androidstarter.mvp.repo_detail;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.guddy.androidstarter.ApplicationAndroidStarter;
import fr.guddy.androidstarter.ApplicationAndroidStarterComponent;
import fr.guddy.androidstarter.R;
import fr.guddy.androidstarter.persistence.entities.RepoEntity;
import pl.aprilapps.switcher.Switcher;

@FragmentWithArgs
public class FragmentRepoDetail
        extends MvpFragment<MvpRepoDetail.View, MvpRepoDetail.Presenter>
        implements MvpRepoDetail.View {

    //region FragmentArgs
    @Arg
    Long mItemId;
    //endregion

    //region Fields
    private Switcher mSwitcher;
    private Unbinder mUnbinder;
    //endregion

    //region Injected views
    @BindView(R.id.FragmentRepoDetail_TextView_Empty)
    TextView mTextViewEmpty;
    @BindView(R.id.FragmentRepoDetail_TextView_Error)
    TextView mTextViewError;
    @BindView(R.id.FragmentRepoDetail_ProgressBar_Loading)
    ProgressBar mProgressBarLoading;
    @BindView(R.id.FragmentRepoDetail_ContentView)
    LinearLayout mContentView;
    @BindView(R.id.FragmentRepoDetail_TextView_Description)
    TextView mTextViewDescription;
    @BindView(R.id.FragmentRepoDetail_TextView_Url)
    TextView mTextViewUrl;
    //endregion

    //region Default constructor
    public FragmentRepoDetail() {
    }
    //endregion

    //region Lifecycle
    @Override
    public void onCreate(final Bundle poSavedInstanceState) {
        super.onCreate(poSavedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override
    public View onCreateView(final LayoutInflater poInflater, final ViewGroup poContainer,
                             final Bundle savedInstanceState) {
        return poInflater.inflate(R.layout.fragment_repo_detail, poContainer, false);
    }

    @Override
    public void onViewCreated(final View poView, final Bundle poSavedInstanceState) {
        super.onViewCreated(poView, poSavedInstanceState);

        mUnbinder = ButterKnife.bind(this, poView);

        mSwitcher = new Switcher.Builder()
                .withEmptyView(mTextViewEmpty)
                .withProgressView(mProgressBarLoading)
                .withErrorView(mTextViewError)
                .withContentView(mContentView)
                .build();

        loadData(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
    //endregion

    //region MvpFragment
    @NonNull
    @Override
    public PresenterRepoDetail createPresenter() {
        final ApplicationAndroidStarterComponent loComponent = ApplicationAndroidStarter.sharedApplication().componentApplication();
        final MvpRepoDetailComponent loRepoDetailComponent = loComponent.plusMvpRepoDetailComponent(new MvpRepoDetail.Module());
        return loRepoDetailComponent.presenterRepoDetail();
    }
    //endregion

    //region ViewRepoDetail
    @Override
    public void showEmpty() {
        mSwitcher.showEmptyView();
    }

    @Override
    public void showContent() {
        mSwitcher.showContentView();
    }

    @Override
    public void showLoading(final boolean pbPullToRefresh) {
        mSwitcher.showProgressView();
    }

    @Override
    public void showError(final Throwable poThrowable, final boolean pbPullToRefresh) {
        mSwitcher.showErrorView();
    }

    @Override
    public void setData(final MvpRepoDetail.Model poData) {
        configureViewWithRepo(poData.repo);

        final Activity loActivity = this.getActivity();
        final CollapsingToolbarLayout loAppBarLayout = (CollapsingToolbarLayout) loActivity.findViewById(R.id.ActivityRepoDetail_ToolbarLayout);
        if (loAppBarLayout != null) {
            loAppBarLayout.setTitle(poData.repo.name);
        }
    }

    @Override
    public void loadData(final boolean pbPullToRefresh) {
        if (mItemId == null) {
            mSwitcher.showErrorView();
        } else {
            getPresenter().loadRepo(mItemId, pbPullToRefresh);
        }
    }
    //endregion

    //region Specific method
    private void configureViewWithRepo(@NonNull final RepoEntity poRepo) {
        mTextViewDescription.setText(poRepo.description);
        mTextViewUrl.setText(poRepo.url);
    }
    //endregion
}
