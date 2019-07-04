package com.viol.basemodule.btnhook;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by LvWeiHao
 * Date: 2019/4/17 0017 9:54
 * <p>
 * Describe:
 */
public abstract class AnimateProxy {
    abstract void doAnimate(View view);

    static class ScaleXAnimate extends AnimateProxy{

        @Override
        void doAnimate(View view) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 1f);
            objectAnimator.setDuration(500);
            objectAnimator.start();
        }
    }

    final static class RotationAnimate extends AnimateProxy{

        @Override
        void doAnimate(View view) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
            objectAnimator.setDuration(500);
            objectAnimator.start();
        }
    }
}
