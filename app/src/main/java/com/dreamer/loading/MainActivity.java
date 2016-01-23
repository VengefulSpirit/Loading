package com.dreamer.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.dreamer.library.Load;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button pull, preLoad, loading, stop;
    private Load load;
    private AnimatorSet animatorSet;

    private AnimatorSet animatorSet1;
    private AnimatorSet animatorSet2;
    private ObjectAnimator loadingRightMove1;
    private ObjectAnimator loadingLeftMove1;
    private ObjectAnimator loadingLeftMove2;
    private ObjectAnimator loadingRightMove2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pull = (Button) findViewById(R.id.pull);
        pull.setOnClickListener(this);
        preLoad = (Button) findViewById(R.id.pre_load);
        preLoad.setOnClickListener(this);
        loading = (Button) findViewById(R.id.loading);
        loading.setOnClickListener(this);
        load = (Load) findViewById(R.id.load);
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pull:
                load.setStatus(load.PULL);
                animatorSet = new AnimatorSet();
                Animator animator = ObjectAnimator.ofFloat(load, "bigRadius", 15, 19, 15);
                animator.setInterpolator(new BounceInterpolator());
                animator.setDuration(500);

                Animator leftMove = ObjectAnimator.ofFloat(load, "leftMoveDistance", 0, Load.MAX_DISTANCE);
                leftMove.setInterpolator(new DecelerateInterpolator());
                leftMove.setDuration(1000);

                Animator rightMove = ObjectAnimator.ofFloat(load, "rightMoveDistance", 0, Load.MAX_DISTANCE);
                rightMove.setInterpolator(new DecelerateInterpolator());
                rightMove.setDuration(1000);

                animatorSet.playTogether(animator, leftMove, rightMove);
                animatorSet.start();
                break;

            case R.id.pre_load:
                load.setStatus(load.PRE_LOAD);
                animatorSet = new AnimatorSet();
                Animator preRightMove = ObjectAnimator.ofFloat(load, "rightMoveDistance", 0, 2 * Load.MAX_DISTANCE);
                preRightMove.setDuration(1000);
                preRightMove.setInterpolator(new DecelerateInterpolator());

                Animator preLeftMove = ObjectAnimator.ofFloat(load, "leftMoveDistance", 0, Load.MAX_DISTANCE);
                preLeftMove.setDuration(1000);
                preLeftMove.setInterpolator(new DecelerateInterpolator());

                animatorSet.playTogether(preRightMove, preLeftMove);
                animatorSet.start();
                break;

            case R.id.loading:
                load.setStatus(load.LOADING);
                animatorSet1 = new AnimatorSet();
                animatorSet2 = new AnimatorSet();

                loadingRightMove1 = ObjectAnimator.ofFloat(load, "rightMoveDistance", 0, 3 * Load.MAX_DISTANCE);
                loadingRightMove1.setDuration(1500);

                loadingLeftMove1 = ObjectAnimator.ofFloat(load, "leftMoveDistance", 0, 3 * Load.MAX_DISTANCE);
                loadingLeftMove1.setDuration(1500);
                loadingLeftMove1.setStartDelay(200);

                animatorSet1.playTogether(loadingRightMove1, loadingLeftMove1);
                animatorSet1.start();
                animatorSet1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animatorSet2.start();
                    }
                });


                loadingLeftMove2 = ObjectAnimator.ofFloat(load, "leftMoveDistance", 3 * Load.MAX_DISTANCE, 0);
                loadingLeftMove2.setDuration(1500);

                loadingRightMove2 = ObjectAnimator.ofFloat(load, "rightMoveDistance", 3 * Load.MAX_DISTANCE, 0);
                loadingRightMove2.setDuration(1500);
                loadingRightMove2.setStartDelay(200);

                animatorSet2.playTogether(loadingLeftMove2, loadingRightMove2);
                //animatorSet2.start();
                animatorSet2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animatorSet1.start();
                    }
                });
                break;

            case R.id.stop:
                animatorSet1.removeAllListeners();
                animatorSet2.removeAllListeners();
                break;
        }
    }
}
