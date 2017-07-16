package qq.max.progressbar;

import android.view.MotionEvent;

import java.lang.ref.WeakReference;

/**
 * Created by marseltzatzo on 11/07/2017.
 */

public class TouchEventHandler {

    private final static String TAG = TouchEventHandler.class.getSimpleName();

    private WeakReference<ProgressBarView> progressViewWeakReference;

    public TouchEventHandler(ProgressBarView progressBarView) {
        this.progressViewWeakReference = new WeakReference<ProgressBarView>(progressBarView);
    }

    public boolean onTouchEvent(MotionEvent event) {
        ProgressBarView progressBarView = this.progressViewWeakReference.get();
        if (progressBarView != null) {

        }
        return true;
    }

}
