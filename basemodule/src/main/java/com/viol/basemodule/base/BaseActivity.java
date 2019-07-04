package com.viol.basemodule.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viol.basemodule.R;
import com.viol.basemodule.loadview.LoadingAndRetryManager;
import com.viol.basemodule.loadview.OnLoadingAndRetryListener;
import com.viol.basemodule.utils.NoFastClickUtils;
import com.viol.basemodule.utils.ScreenUtils;
import com.viol.basemodule.utils.StatusBarUtils;

import butterknife.ButterKnife;

/**
 * Created by lvweihao on 2018/12/7.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public LayoutInflater mInflater;
    public FrameLayout mContentView;
    private FrameLayout baseFrameLayout;
    protected RelativeLayout mTopView;
    protected boolean canFastClick = false;

    private int statusColor = R.color.app_top_color;
    private boolean changeStatusTrans = false;

    public ImageView ivBackView;
    public TextView tvTopCenterTitle;
    protected View baseView;
    public LoadingAndRetryManager loadingAndRetryManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mInflater = LayoutInflater.from(this);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        initSystemBar();

        baseFrameLayout = findViewById(R.id.base_framelayout);
        mTopView = findViewById(R.id.rlyt_top);
        mContentView = findViewById(R.id.content_view);

        ivBackView = findViewById(R.id.topdefault_leftbutton);
        ivBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvTopCenterTitle = findViewById(R.id.topdefault_centertitle);
    }

    @Override
    public void setContentView(int layoutResID) {
        baseView = mInflater.inflate(layoutResID, null);
        ButterKnife.bind(this, baseView);
        setContentView(baseView);
    }

    @Override
    public void setContentView(View view) {
        mContentView.addView(view);
        initViewAndData();
        initEvent();
        tvTopCenterTitle.setText(getTitle());
    }

    public void createLoadingManager(Object viewOrActivityOrFragment, final ReloadFunction reloadFunction) {
        if (loadingAndRetryManager != null) loadingAndRetryManager = null;
        loadingAndRetryManager = LoadingAndRetryManager.generate(viewOrActivityOrFragment, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                if (reloadFunction != null) {
                    reloadFunction.reload(retryView);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void setChangeStatusTrans(boolean changeStatusTrans) {
        this.changeStatusTrans = changeStatusTrans;
    }

    protected void setStatusColor(int color) {
        this.statusColor = color;
    }

    protected void setFitsSystemWindows(boolean isFits) {
        baseFrameLayout.setFitsSystemWindows(isFits);
    }

    /**
     * 实现图片延伸沉浸式
     */
    public void setFullScreen() {
        setFitsSystemWindows(false);
        mTopView.setBackgroundColor(Color.TRANSPARENT);
        setContentViewMarin();
        initTitleBar();
    }

    /**
     * 设置contentView的上边距为0
     */
    public void setContentViewMarin() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        mContentView.setLayoutParams(params);
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
            int statusBarHeight = ScreenUtils.getStatusHeight();
            mTopView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    statusBarHeight + topHeight));
            mTopView.setPadding(mTopView.getPaddingLeft(), statusBarHeight,
                    mTopView.getPaddingRight(), 0);
        }
    }

    /**
     * 沉浸式状态栏
     */
    public void initSystemBar() {
        int result = StatusBarUtils.setLightMode(this);
        if (!changeStatusTrans) {
            if (result == 3) {
                // 6.0以上沉浸式
                StatusBarUtils.setColor(this, getResources().getColor(statusColor), 0);
            } else if (result == 4) {
                // 其它半透明效果
                StatusBarUtils.setColor(this, getResources().getColor(statusColor));
            } else {
                // miui、flyme沉浸式
                StatusBarUtils.setColor(this, getResources().getColor(statusColor), 0);
            }
        } else {
            if (result == 4) {
                // 其它半透明效果
                StatusBarUtils.setTranslucent(this, StatusBarUtils.DEFAULT_STATUS_BAR_ALPHA);
            } else {
                // 透明效果
                StatusBarUtils.setTransparent(this);
            }
        }
    }

    public <T extends View> T mFindViewById(int id) {
        return baseView.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        if (canFastClick) {
            doClick(v);
        } else {
            if (!NoFastClickUtils.isFastClick()) {
                doClick(v);
            }
        }
    }

    public interface ReloadFunction {
        void reload(View retryView);
    }

    //初始化view
    protected abstract void initViewAndData();

    //初始化事件
    public abstract void initEvent();

    //响应点击事件
    protected abstract void doClick(View v);

    //物理回退
    @Override
    public void onBackPressed() {
        finish();
    }
}
