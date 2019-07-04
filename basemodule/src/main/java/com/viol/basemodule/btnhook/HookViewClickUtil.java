package com.viol.basemodule.btnhook;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by LvWeiHao
 * Date: 2019/4/17 0017 9:54
 * <p>
 * Describe:
 */
public class HookViewClickUtil {

    /**
     * 通过注解HookClick获取view对象
     *
     * @param target activity fragment or view
     */
    public static void hookViews(Object target) {
        checkTarget(target);
        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();
        HookClick hookClick;
        int id;
        Class customClazz;
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof HookClick) {
                    hookClick = field.getAnnotation(HookClick.class);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    id = hookClick.value();
                    customClazz = hookClick.clazz();
                    if (target instanceof Activity) {
                        View view = ((Activity) target).findViewById(id);
                        hookView(view, customClazz);
                    } else if (target instanceof Fragment) {
                        View view = ((Fragment) target).getView().findViewById(id);
                        hookView(view, customClazz);
                    } else if (target instanceof View) {
                        View view = ((View) target).findViewById(id);
                        hookView(view, customClazz);
                    }
                }
            }
        }
    }

    /**
     * 拦截onclick事件
     *
     * @param view  注册click事件的view
     * @param clazz 代理对象的Class
     */
    public static void hookView(View view, Class clazz) {
        try {
            Class viewClazz = Class.forName("android.view.View");
            Method listenerInfoMethod = viewClazz.getDeclaredMethod("getListenerInfo");
            if (!listenerInfoMethod.isAccessible()) {
                listenerInfoMethod.setAccessible(true);
            }
            Object listenerInfo = listenerInfoMethod.invoke(view);

            Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
            Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");
            if (!onClickListenerField.isAccessible()) {
                onClickListenerField.setAccessible(true);
            }
            View.OnClickListener mOnClickListener = (View.OnClickListener) onClickListenerField.get(listenerInfo);
            View.OnClickListener mProxyListener = new OnClickListenerProxy(mOnClickListener, clazz);
            onClickListenerField.set(listenerInfo, mProxyListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //自定义的代理事件监听器
    private static class OnClickListenerProxy implements View.OnClickListener {

        private final CustomClick realClick;
        private final ProxyClick proxyClick;
        private final CustomClick customClick;
        private View.OnClickListener object;

        private OnClickListenerProxy(View.OnClickListener object, Class clazz) {
            this.object = object;
            realClick = RealClickFactory.getRealClick(clazz, this.object);
            proxyClick = new ProxyClick(realClick);
            customClick = (CustomClick) Proxy.newProxyInstance(CustomClick.class.getClassLoader(), realClick.getClass().getInterfaces(), proxyClick);
        }

        @Override
        public void onClick(View v) {
            customClick.click(v);
        }
    }

    private static void checkTarget(Object target) {
        if (!(target instanceof Activity || target instanceof Fragment || target instanceof View)) {
            throw new IllegalArgumentException("only support hook Activity and Fragment or View, please to check your code at @HookViewClickUtil.hookViews(target)");
        }
    }
}
