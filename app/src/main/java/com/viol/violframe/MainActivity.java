package com.viol.violframe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.viol.basemodule.base.BaseActivity;
import com.viol.basemodule.base.BaseApplication;
import com.viol.basemodule.btnhook.HookClick;
import com.viol.basemodule.btnhook.HookViewClickUtil;
import com.viol.basemodule.btnhook.RealClick;
import com.viol.basemodule.btnhook.RealClick2;
import com.viol.basemodule.utils.StatusBarUtils;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @HookClick(value = R.id.tv_hello_world, clazz = RealClick.class)
    @BindView(R.id.tv_hello_world)
    TextView tvHelloWorld;
    @BindView(R.id.m_view)
    View mView;
    @HookClick(value = R.id.m_view_red, clazz = RealClick2.class)
    @BindView(R.id.m_view_red)
    TextView mViewRed;
    @BindView(R.id.recycler_veiw)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFullScreen();
        StatusBarUtils.setDarkMode(this);

        createLoadingManager(mView, new ReloadFunction() {
            @Override
            public void reload(View retryView) {
                MainActivity.this.setRetryEvent(retryView);
            }
        });

        loadingAndRetryManager.showLoading();

        tvHelloWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
        loadData();

        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                HookViewClickUtil.hookViews(MainActivity.this);
            }
        });

        mViewRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BaseApplication.appContext, "main", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setRetryEvent(View retryView) {
        View view = retryView.findViewById(R.id.id_btn_retry);
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, "retry event invoked", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingAndRetryManager.showContent();
            }
        }, 2000);
    }

    @Override
    protected void initViewAndData() {
        setTitle("首页");
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {

        }
    }
}
