import android.app.Activity;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class ActivityManager {

    /**
     * Activity列表
     */
    private List<Activity> activityList = new LinkedList<Activity>();

    /**
     * 全局唯一实例
     */
    private static ActivityManager instance;

    /**
     * 该类采用单例模式，不能实例化
     */
    private ActivityManager() {

    }

    /**
     * 获取类实例对象
     *
     * @return MyActivityManager
     */
    public static ActivityManager getInstance() {

        if (null == instance) {

            instance = new ActivityManager();

        }
        return instance;

    }

    /**
     * 保存Activity到现有列表中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {

        activityList.add(activity);
    }

    /**
     * 关闭保存的Activity
     */
    public void exit() {

        if (activityList != null) {
            Activity activity;
            Log.v("MyActivityManager", "activityList==" + activityList.size());
            for (int i = 0; i < activityList.size(); i++) {

                activity = activityList.get(activityList.size() - 1 - i);

                if (activity != null) {
                    activity.finish();
                    activity = null;
                }
            }

        }
        activityList = new LinkedList<Activity>();
    }
}