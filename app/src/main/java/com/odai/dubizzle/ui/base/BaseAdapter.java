package com.odai.dubizzle.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.paginate.Paginate;
import com.odai.dubizzle.R;
import com.odai.dubizzle.data.network.HttpError;
import com.odai.dubizzle.ui.custom.BaseRecyclerView;
import com.odai.dubizzle.ui.custom.BaseRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public abstract class BaseAdapter<M, V extends BaseViewHolder<M>> extends RecyclerView.Adapter<V>
        implements Paginate.Callbacks {

    public static final int LOADING_STATE = 0;
    public static final int FAILED_STATE = 1;
    public static final int NORMAL_STATE = 2;

    private static final int NO_DATA_TYPE = 0;
    private static final int LOADING_TYPE = 1;
    private static final int PROGRESS_TYPE = 2;
    private static final int FAILED_TYPE = 3;
    protected static final int ITEM_TYPE = 4;

    private String emptyMsg;
    private int emptyIcon;
    private String emptyButtonText;

    private String failedButtonText;

    @Nullable
    private List<M> items;
    private Context mContext;
    private HttpError error;
    private LayoutInflater mInflater;
    private boolean progressShowing;
    private final boolean enableAnimations;
    private boolean failedPagination = false;
    private OnItemClickListener<M> onItemClickListener;
    private OnLoadMoreListener onLoadMoreListener;
    private View.OnClickListener noDataButtonClickListener;
    private View.OnClickListener failedButtonClickListener;
    protected RecyclerView recyclerView;
    private int pageNumber = 1;
    private boolean isLoading = false;
    private boolean isFinished;
    private BaseRefreshLayout refreshLayout;

    @AdapterState
    private int adapterState;

    protected BaseAdapter() {
        this(new ArrayList<>());
    }

    protected BaseAdapter(@Nullable List<M> items) {
        this(items, true);
    }

    protected BaseAdapter(@Nullable List<M> items, boolean enableAnimations) {
        this(items, LOADING_STATE, enableAnimations);
    }

    protected BaseAdapter(@Nullable List<M> items, @AdapterState int adapterState) {
        this(items, adapterState, true);
    }

    protected BaseAdapter(
            @Nullable List<M> items,
            @AdapterState int adapterState,
            boolean enableAnimations
    ) {
        this.items = items;
        this.enableAnimations = enableAnimations;
        this.adapterState = adapterState;
    }

    protected abstract V getViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        mContext = recyclerView.getContext();
        mInflater = LayoutInflater.from(mContext);

        if (recyclerView.getParent() instanceof SwipeRefreshLayout) {
            refreshLayout = (BaseRefreshLayout) recyclerView.getParent();
            refreshLayout.addOnRefreshListener(() -> {
                resetPagination();
                return Unit.INSTANCE;
            });
        }

        if (recyclerView instanceof BaseRecyclerView) {
            BaseRecyclerView baseRecyclerView = (BaseRecyclerView) recyclerView;
            emptyMsg = baseRecyclerView.getEmptyMsg();
            emptyIcon = baseRecyclerView.getEmptyIcon();
            emptyButtonText = baseRecyclerView.getEmptyButtonText();

            failedButtonText = baseRecyclerView.getFailedButtonText();
        }

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            if (enableAnimations) {
                int resId = R.anim.grid_layout_animation_from_bottom;
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mContext, resId);
                recyclerView.setLayoutAnimation(animation);
            }
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    if (items == null || items.size() == 0) {
                        return ((GridLayoutManager) manager).getSpanCount();
                    }
                    return 1;
                }
            });
        } else if (manager instanceof LinearLayoutManager) {
            if (enableAnimations) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                int resId = layoutManager.getOrientation() == RecyclerView.VERTICAL ?
                        R.anim.linear_layout_animation_from_bottom :
                        R.anim.linear_layout_animation_from_end;
                final Context context = recyclerView.getContext();
                final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, resId);
                recyclerView.setLayoutAnimation(controller);
            }
        }
        if (enableAnimations && items != null && items.size() > 0) {
            recyclerView.post(recyclerView::scheduleLayoutAnimation);
        }

        Paginate.with(recyclerView, this)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(false)
                .build();
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case LOADING_TYPE:
                view = mInflater.inflate(R.layout.row_loading, parent, false);
                return (V) new LoadingHolder(view);
            case PROGRESS_TYPE:
                view = mInflater.inflate(R.layout.row_progress, parent, false);
                return (V) new ProgressHolder(view);
            case NO_DATA_TYPE:
                view = mInflater.inflate(R.layout.row_no_data, parent, false);
                return (V) new NoDataHolder(view);
            case FAILED_TYPE:
                view = mInflater.inflate(R.layout.row_failed, parent, false);
                return (V) new FailedHolder(view);
        }
        return getViewHolder(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE) {
            holder.bind(position, getItem(position));
            holder.itemView.setOnClickListener(v -> {
                int aPosition = holder.getAdapterPosition();
                if (aPosition != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClicked(v, getItem(aPosition), aPosition);
                }
            });
        } else {
            holder.bind(position, null);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (adapterState == LOADING_STATE) {
            return LOADING_TYPE;
        } else if (adapterState == FAILED_STATE && (items == null || items.size() == 0)) {
            return FAILED_TYPE;
        } else if (items == null || items.size() == 0) {
            return NO_DATA_TYPE;
        } else if (progressShowing && position == items.size()) {
            return PROGRESS_TYPE;
        }
        return ITEM_TYPE;
    }

    @Override
    public int getItemCount() {
        if (adapterState == LOADING_STATE || (items == null || items.size() == 0)) {
            return 1;
        } else {
            return progressShowing ? items.size() + 1 : items.size();
        }
    }

    @Override
    public void onLoadMore() {
        if (!isLoading && items != null && items.size() > 0 && onLoadMoreListener != null) {
            pageNumber++;
            isLoading = true;
            onLoadMoreListener.onLoadMore(pageNumber);
        }
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return isFinished;
    }

    public void setPagingFinish(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void resetPagination() {
        pageNumber = 1;
        isLoading = false;
        isFinished = false;
    }

    public void showProgress() {
        progressShowing = true;
    }

    public void hideProgress() {
        recyclerView.postDelayed(() -> {
            notifyItemRemoved(getItemCount() - 1);
            progressShowing = false;
        }, 500);
    }

    public boolean isProgressShowing() {
        return progressShowing;
    }

    public Context getContext() {
        return mContext;
    }

    protected LayoutInflater getInflater() {
        return mInflater;
    }

    @Nullable
    public List<M> getItems() {
        return items;
    }

    protected M getItem(int position) {
        if (items != null) {
            return items.get(position);
        }
        return null;
    }

    public void submitList(List<M> items) {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        isLoading = false;
        setAdapterState(NORMAL_STATE);
        this.items = items;
        notifyDataSetChanged();
        if (recyclerView != null) {
            recyclerView.scheduleLayoutAnimation();
        }
    }

    public void add(M item) {
        if (items != null) {
            items.add(item);
            notifyItemInserted(items.size());
        }
    }

    public void add(int position, M item) {
        if (items != null) {
            items.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void add(List<M> items) {
        if (this.items != null) {
            isLoading = false;
            int oldSize = this.items.size();
            this.items.addAll(items);
            notifyItemRangeInserted(oldSize, items.size());
        }
    }

    public void add(int position, List<M> items) {
        if (this.items != null) {
            isLoading = false;
            int oldSize = this.items.size();
            this.items.addAll(position, items);
            notifyItemRangeInserted(oldSize, items.size());
        }
    }

    public void remove(M item) {
        if (items != null) {
            int position = items.indexOf(item);
            if (position > -1) {
                this.items.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public void remove(int position) {
        if (items != null) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void remove(List<M> items) {
        if (this.items != null) {
            int oldSize = this.items.size();
            this.items.removeAll(items);
            notifyItemRangeRemoved(oldSize, items.size());
        }
    }

    public void reset() {
        if (items != null) {
            this.items.clear();
            notifyDataSetChanged();
        }
    }

    public BaseAdapter<M, V> setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public BaseAdapter<M, V> setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        onLoadMoreListener.onLoadMore(pageNumber);
        this.onLoadMoreListener = onLoadMoreListener;
        return this;
    }

    public OnItemClickListener<M> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void handleRetryPagination() {
        if (items != null && isProgressShowing()) {
            failedPagination = true;
            notifyItemChanged(items.size());
        }
    }

    public void setAdapterState(@AdapterState int adapterState) {
        this.adapterState = adapterState;
        if (adapterState != FAILED_STATE) {
            error = null;
        }
        notifyDataSetChanged();
    }

    public void setNoDataButtonClickListener(View.OnClickListener listener) {
        this.noDataButtonClickListener = listener;
    }

    public void setFailedButtonClickListener(View.OnClickListener listener) {
        this.failedButtonClickListener = listener;
    }

    @AdapterState
    public int getAdapterState() {
        return adapterState;
    }

    public void handleDataError(HttpError error, Function1<HttpError, Object> cannotHandleCallback) {
        this.error = error;
        if (items == null || items.size() == 0) {
            setAdapterState(FAILED_STATE);
        } else if (progressShowing) {
            handleRetryPagination();
        } else if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), error.getLocalizedMsg(), Toast.LENGTH_LONG).show();
        } else if (cannotHandleCallback != null) {
            cannotHandleCallback.invoke(error);
        }
    }

    public void handleDataError(HttpError error) {
        handleDataError(error, null);
    }

    class ProgressHolder extends BaseViewHolder<M> {

        @BindView(R.id.pb_progress)
        ProgressBar pbProgress;
        @BindView(R.id.v_progress)
        View vProgress;
        @BindView(R.id.btn_row_progress_retry)
        AppCompatButton btnRetry;
        @BindView(R.id.tv_row_progress_retry_msg)
        AppCompatTextView tvRetryMsg;

        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(int position, M item) {
            if (failedPagination) {
                btnRetry.setVisibility(View.VISIBLE);
                tvRetryMsg.setVisibility(View.VISIBLE);
                vProgress.setVisibility(View.GONE);
                pbProgress.setVisibility(View.GONE);
                if (error != null) {
                    tvRetryMsg.setText(error.getErrorTitle());
                }
            } else {
                btnRetry.setVisibility(View.GONE);
                tvRetryMsg.setVisibility(View.GONE);
                vProgress.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.VISIBLE);
            }
        }

        @OnClick(R.id.btn_row_progress_retry)
        void onRetryClicked(View view) {
            failedPagination = false;
            bind(getAdapterPosition(), null);
            error = null;
            int position = getAdapterPosition();
            if (getOnItemClickListener() != null && position != RecyclerView.NO_POSITION) {
                getOnItemClickListener().onItemClicked(view, null, position);
            }
        }
    }

    class NoDataHolder extends BaseViewHolder<M> {

        @BindView(R.id.tv_row_no_data_title)
        AppCompatTextView tvNoData;
        @BindView(R.id.iv_row_no_data)
        AppCompatImageView ivNoData;
        @BindView(R.id.btn_row_no_data)
        AppCompatButton btnNoData;

        NoDataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(int position, M item) {
            tvNoData.setText(emptyMsg);
            btnNoData.setText(emptyButtonText);
            ivNoData.setImageResource(emptyIcon);
            btnNoData.setOnClickListener(v -> {
                if (noDataButtonClickListener != null) {
                    setAdapterState(LOADING_STATE);
                    noDataButtonClickListener.onClick(v);
                }
            });
        }
    }

    class LoadingHolder extends BaseViewHolder<M> {

        LoadingHolder(View itemView) {
            super(itemView);
        }

        @SuppressWarnings("EmptyMethod")
        @Override
        public void bind(int position, M item) {
        }
    }

    class FailedHolder extends BaseViewHolder<M> {

        @BindView(R.id.tv_row_failed_title)
        AppCompatTextView tvFailed;
        @BindView(R.id.iv_row_failed)
        AppCompatImageView ivFailed;
        @BindView(R.id.btn_row_failed)
        AppCompatButton btnFailed;

        FailedHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(int position, M item) {
            if (error != null) {
                tvFailed.setText(error.getLocalizedMsg());
                ivFailed.setImageResource(error.getErrorIcon());
            }
            btnFailed.setText(failedButtonText);
            btnFailed.setOnClickListener(v -> {
                if (failedButtonClickListener != null) {
                    setAdapterState(LOADING_STATE);
                    failedButtonClickListener.onClick(v);
                }
            });
        }
    }

    @FunctionalInterface
    public interface OnItemClickListener<M> {

        void onItemClicked(View view, M item, int position);
    }

    @FunctionalInterface
    public interface OnLoadMoreListener {

        void onLoadMore(int pageNumber);
    }

    @IntDef({NORMAL_STATE, FAILED_STATE, LOADING_STATE})
    public @interface AdapterState {
    }
}
