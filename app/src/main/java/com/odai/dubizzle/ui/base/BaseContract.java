package com.odai.dubizzle.ui.base;

import com.odai.dubizzle.data.network.HttpError;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@UiThread
public interface BaseContract {

    @UiThread
    interface Presenter {

        void onCreate();

        void onViewInflated();

        void onStart();

        void onResume();

        void onPause();

        void onStop();

        void onDestroy();
    }

    @SuppressWarnings("unused")
    @UiThread
    interface View {

        void toast(String msg);

        void toast(@StringRes int msgRes);

        void snack(String msg);

        void snack(@StringRes int msgRes);

        void snack(String msg, android.view.View.OnClickListener onClickListener);

        void snack(@StringRes int msgRes, android.view.View.OnClickListener onClickListener);

        void showLoginDialog();

        void disableViews();

        void enableViews();

        void showProgress();

        void hideProgress();

        void handleDataError(@NonNull HttpError error);
    }
}
