package com.viol.basemodule.btnhook;

import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by LvWeiHao
 * Date: 2019/4/17 0017 9:53
 * <p>
 * Describe:
 */
public class RealClickFactory {
    public static CustomClick getRealClick(Class clazz, View.OnClickListener onClickListener) {
        CustomClick realClick = null;
        try {
            Constructor constructor = clazz.getConstructor(View.OnClickListener.class);
            constructor.setAccessible(true);
            realClick = (CustomClick) constructor.newInstance(onClickListener);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return realClick;
    }
}
