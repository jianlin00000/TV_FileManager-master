package com.common.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.collection.SimpleArrayMap;

/**
 *@author: cjl
 *@date: 2018/4/28 15:50
 *@desc: SharePreference工具类
 *        key统一在ConstantConfig中配置，方便管理
 */
public final class SPUtils {

    private static              SimpleArrayMap<String, SPUtils> SP_UTILS_MAP = new SimpleArrayMap<>();
    private static String                                       mDefaultName ="data";
    private              static Context                         mContext;
    private                     SharedPreferences               sp;

    private SPUtils() {
    }

    public static void init(Application context,String defaultName){
        mContext=context;
        if (!TextUtils.isEmpty(defaultName)) mDefaultName=defaultName;
    }
    /**
     * 获取默认名称的sp实例
     * @return
     */
    public static SPUtils getInstance() {
        return getInstance(mDefaultName, Context.MODE_PRIVATE);
    }


    /**
     *获取指定模式默认名称的sp实例
     * @param mode 模式
     * @return
     */
    public static SPUtils getInstance(final int mode) {
        return getInstance(mDefaultName, mode);
    }


    /**
     * 根据名称获取sp实例
     * @param spName
     * @return
     */
    public static SPUtils getInstance(String spName) {
        return getInstance(spName, Context.MODE_PRIVATE);
    }


    /**
     * 根据名称和模式获取sp实例
     * @param spName
     * @param mode
     * @return
     */
    public static SPUtils getInstance(String spName, final int mode) {
        if (isSpace(spName)) spName = "spUtils";
        SPUtils spUtils = SP_UTILS_MAP.get(spName);
        if (spUtils == null) {
            spUtils = new SPUtils(spName, mode);
            SP_UTILS_MAP.put(spName, spUtils);
        }
        return spUtils;
    }

    private SPUtils(final String spName) {
        sp = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    private SPUtils(final String spName, final int mode) {
        sp = mContext.getSharedPreferences(spName, mode);
    }


    public void put(@NonNull final String key, final String value) {
        put(key, value, false);
    }


    /**
     *
     * @param key
     * @param value
     * @param isCommit 是否通过commit()的方式提交
     */
    public void put(@NonNull final String key, final String value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putString(key, value).commit();
        } else {
            sp.edit().putString(key, value).apply();
        }
    }

    public String getString(@NonNull final String key) {
        return getString(key, "");
    }


    public String getString(@NonNull final String key, final String defaultValue) {
        return sp.getString(key, defaultValue);
    }


    public void put(@NonNull final String key, final int value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key, final int value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putInt(key, value).commit();
        } else {
            sp.edit().putInt(key, value).apply();
        }
    }


    public int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }


    public int getInt(@NonNull final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }


    public void put(@NonNull final String key, final long value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key, final long value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putLong(key, value).commit();
        } else {
            sp.edit().putLong(key, value).apply();
        }
    }


    public long getLong(@NonNull final String key) {
        return getLong(key, -1L);
    }


    public long getLong(@NonNull final String key, final long defaultValue) {
        return sp.getLong(key, defaultValue);
    }


    public void put(@NonNull final String key, final float value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key, final float value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putFloat(key, value).commit();
        } else {
            sp.edit().putFloat(key, value).apply();
        }
    }


    public float getFloat(@NonNull final String key) {
        return getFloat(key, -1f);
    }


    public float getFloat(@NonNull final String key, final float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }


    public void put(@NonNull final String key, final boolean value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key, final boolean value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putBoolean(key, value).commit();
        } else {
            sp.edit().putBoolean(key, value).apply();
        }
    }


    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }


    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }


    public void put(@NonNull final String key, final Set<String> value) {
        put(key, value, false);
    }


    public void put(@NonNull final String key,
            final Set<String> value,
            final boolean isCommit) {
        if (isCommit) {
            sp.edit().putStringSet(key, value).commit();
        } else {
            sp.edit().putStringSet(key, value).apply();
        }
    }


    public Set<String> getStringSet(@NonNull final String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    /**
     * 返回一个set数据
     * @param key
     * @param defaultValue .
     * @return
     */
    public Set<String> getStringSet(@NonNull final String key,
            final Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    /**
     *返回sp的所有值
     * @return
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }


    /**
     * 是否包含某一个键
     * @param key
     * @return
     */
    public boolean contains(@NonNull final String key) {
        return sp.contains(key);
    }


    /**
     * 通过key移除
     * @param key
     */
    public void remove(@NonNull final String key) {
        remove(key, false);
    }


    public void remove(@NonNull final String key, final boolean isCommit) {
        if (isCommit) {
            sp.edit().remove(key).commit();
        } else {
            sp.edit().remove(key).apply();
        }
    }

    /**
     * 清除所有
     */
    public void clear() {
        clear(false);
    }

    /**
     * 清除所有
     */
    public void clear(final boolean isCommit) {
        if (isCommit) {
            sp.edit().clear().commit();
        } else {
            sp.edit().clear().apply();
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
