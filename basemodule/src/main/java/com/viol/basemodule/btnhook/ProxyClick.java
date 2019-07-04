package com.viol.basemodule.btnhook;

import android.graphics.Color;
import android.view.View;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by LvWeiHao
 * Date: 2019/4/17 0017 9:54
 * <p>
 * Describe: 动态代理动画
 */
public class ProxyClick implements InvocationHandler {
    protected CustomClick customClick;

    public ProxyClick(CustomClick customClick) {
        this.customClick = customClick;
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        View view = (View) args[0];
        view.setBackgroundColor(Color.parseColor("#ff00ff"));
        AnimateProxy animateProxy = new AnimateProxy.RotationAnimate();
        animateProxy.doAnimate(view);
        return method.invoke(customClick, args);
    }
}
