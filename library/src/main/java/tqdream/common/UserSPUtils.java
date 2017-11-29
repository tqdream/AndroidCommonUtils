package tqdream.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.boc.bocapplication.user.bean.LoginBean;
import com.boc.bocapplication.user.bean.UserInfo;

import java.io.IOException;

import tqdream.base.utils.AppUtil;

/**
 * @author taoqiang @create on 2016/10/20 9:12
 * @email tqdream@163.com
 */
public class UserSPUtils {
    private UserSPUtils() {

    }

    private static UserSPUtils userSPUtils;
    private static SharedPreferences sharedPreferences;

    public static UserSPUtils getInstance() {
        synchronized (UserSPUtils.class) {
            if (userSPUtils == null) {
                userSPUtils = new UserSPUtils();
            }
            return userSPUtils;
        }
    }

    public final static String CONFIG_USER_INFO = "config_user_info";

    /**
     * 保存登陆信息
     *
     * @param context
     * @param
     */
    public void setLoginInfo(Context context, String result) {
        sharedPreferences = context.getSharedPreferences(CONFIG_USER_INFO,
                Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        String serStr;
        try {
            serStr = AppUtil.serialize(result);
            editor.putString("loginStr", serStr);
            editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取登陆信息
     *
     * @param context
     * @return
     */
    public LoginBean getLoginInfo(Context context) {
        if (context == null) {
            return null;
        }
        sharedPreferences = context.getSharedPreferences(CONFIG_USER_INFO,
                Context.MODE_PRIVATE);
        Object serStr;
        try {
            serStr = AppUtil.deSerialization(sharedPreferences.getString("loginStr", ""));
            return GsonUtil.GsonToBean(serStr.toString(),LoginBean.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存用户信息
     *
     * @param context
     * @param
     */
    public void setUserInfo(Context context, String result) {
        sharedPreferences = context.getSharedPreferences(CONFIG_USER_INFO,
                Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        String serStr;
        try {
            serStr = AppUtil.serialize(result);
            editor.putString("userStr", serStr);
            editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @return
     */
    public UserInfo getUserInfo(Context context) {
        if (context == null) {
            return null;
        }
        sharedPreferences = context.getSharedPreferences(CONFIG_USER_INFO,
                Context.MODE_PRIVATE);
        Object serStr;
        try {
            serStr = AppUtil.deSerialization(sharedPreferences.getString("userStr", ""));
            return GsonUtil.GsonToBean(serStr.toString(),UserInfo.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置刷新时间
     *
     * @param context
     * @param
     */
    public void setRefreshTime(Context context, String type, String time) {
        sharedPreferences = context.getSharedPreferences(CONFIG_USER_INFO,
                Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(type, time);
        editor.commit();
    }

    /**
     * 获取刷新时间
     *
     * @param context
     * @return
     */
    public String getRefreshTime(Context context, String type) {
        if (context == null) {
            return null;
        }
        sharedPreferences = context.getSharedPreferences(CONFIG_USER_INFO,
                Context.MODE_PRIVATE);

        String userStr = sharedPreferences.getString(type, "");
        return userStr;
    }



    /**
     * 清空用户信息
     *
     * @param context
     */
    public void clearLoginInfo(Context context) {
        sharedPreferences = context.getSharedPreferences(CONFIG_USER_INFO,
                Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }
}
