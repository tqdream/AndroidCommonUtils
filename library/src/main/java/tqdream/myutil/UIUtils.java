package tqdream.myutil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;

import com.boc.bocapplication.BOCApplication;

import java.util.Map;
import java.util.Set;

public class UIUtils {
	private static DisplayMetrics dm;

	/**
	 * 网络是否可用
	 */
	public static boolean checkNetworkState() {
		boolean flag = false;
		// 得到网络连接信息
		ConnectivityManager manager = (ConnectivityManager) getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 去进行判断网络是否连接
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}

	public static int getHeight() {
		dm = getContext().getResources().getDisplayMetrics();
		int screenHeight = dm.heightPixels;
		return screenHeight;
}

	public static int getWidth() {
		dm = getContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

	public static String simpleMapToJsonStr(Map<String, Object> values) {
		if (values == null || values.isEmpty()) {
			return "null";
		}
		String jsonStr = "{";
		Set<?> keySet = values.keySet();
		for (Object key : keySet) {
			jsonStr += "\"" + key + "\":\"" + values.get(key) + "\",";
		}
		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr += "}";
		return jsonStr;
	}
	public static Context getContext(){
		return BOCApplication.getContext();
	}
	public static int getMainThreadId(){
		return BOCApplication.getMainThreadId();
	}

	public static Thread getMainThread(){
		return BOCApplication.getMainThread();
	}

	public static Handler getHandler(){
		return BOCApplication.getHandler();
	}

	//string
	public static String getString(int id){
		return getContext().getResources().getString(id);
	}
	//drawable
	public static Drawable getDrawable(int id){
		return getContext().getResources().getDrawable(id);
	}
	//stringArray
	public static String[] getStringArray(int id){
		return getContext().getResources().getStringArray(id);
	}
	
	//dip--->px   1dp = 1px   1dp = 2px  
	public static int dip2px(int dip){
		//dp和px的转换关系比例值
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int)(dip*density+0.5);
	} 
	//px---->dp
	public static int px2dip(int px){
		//dp和px的转换关系比例值
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int)(px/density+0.5);
	}
	
	//判断是否是主线的方法
	public static boolean isRunInMainThread(){	
		return getMainThreadId() == android.os.Process.myTid(); 
	}
	
	//保证当前的UI操作在主线程里面运行
	public static void runInMainThread(Runnable runnable){
		if(isRunInMainThread()){
			//如果现在就是在主线程中，就直接运行run方法
			runnable.run();
		}else{
			//否则将其传递到主线程中运行
			getHandler().post(runnable);
		}
	}
	//java代码区设置颜色选择器的方法
/*	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		return getContext().getResources().getColorStateList(mTabTextColorResId);
	}*/
	
	public static View inflate(int id){
		return View.inflate(getContext(), id, null);
	}
	public static int getDimens(int id) {
		//根据dimens中提供的id，将其对应的dp值转换成相应的像素值大小
		return UIUtils.getContext().getResources().getDimensionPixelSize(id);
	}
	public static void postDelayed(Runnable runnable, long delayTime) {
		getHandler().postDelayed(runnable, delayTime);
	}
	public static void removeCallback(Runnable runnable) {
		//移除在当前handler中维护的任务(传递进来的任务)
		getHandler().removeCallbacks(runnable);
	}
	public static int getColorbyResourse(int id) {
		return getContext().getResources().getColor(id);
	}

	public static int getColorbyString(String color){return Color.parseColor(color);}
}
