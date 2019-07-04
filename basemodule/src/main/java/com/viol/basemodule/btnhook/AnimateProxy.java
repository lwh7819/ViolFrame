package com.viol.basemodule.btnhook;

import android.animation.ObjectAnimator;
import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by LvWeiHao
 * Date: 2019/4/17 0017 9:54
 * <p>
 * Describe:
 */
public abstract class AnimateProxy {
    protected void doAnimate(View view) {}

    abstract void doCustomEvent(Method method, Object... args);

    static class ScaleXAnimate extends AnimateProxy{

        @Override
        protected void doAnimate(View view) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 1f);
            objectAnimator.setDuration(500);
            objectAnimator.start();
        }

        @Override
        void doCustomEvent(Method method, Object... args) {

        }
    }

    final static class RotationAnimate extends AnimateProxy{

        @Override
        protected void doAnimate(View view) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
            objectAnimator.setDuration(500);
            objectAnimator.start();
        }

        @Override
        void doCustomEvent(Method method, Object... args) {

        }
    }
}
