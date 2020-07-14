package com.filemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.common.data.BaseObservableData;
import com.common.utils.DataObservable;
import com.common.utils.LogUtil;
import com.filemanager.utils.Constant;
import com.filemanager.utils.PreferencesUtil;
import com.filemanager.utils.SettingUtil;

/**
 *@author: cjl
 *@date: 2020/7/8 14:01
 *@desc: usb拔插状态监听
 */
public class UsbReceiver
        extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        LogUtil.d("usb action:"+action);
        if (Intent.ACTION_MEDIA_MOUNTED.equals(action)){
            LogUtil.d("usb 插入");
            SettingUtil.setUsbAttached(context, true);
            BaseObservableData<Boolean> data = new BaseObservableData<>(Constant.Msg.USB_STATUS_CHANGE, null);
            DataObservable.getInstance().setData(data);
        }else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)){
            LogUtil.d("usb 拔出");
            SettingUtil.setUsbAttached(context, false);
            BaseObservableData<Boolean> data = new BaseObservableData<>(Constant.Msg.USB_STATUS_CHANGE, null);
            DataObservable.getInstance().setData(data);
        }
    }


    /**
     * 注册Usb插入广播
     */
    public void registerUsbReceiver(Context context) {
        final IntentFilter filter = new IntentFilter();
//        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
//        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);//表明sd对象是存在并具有读/写权限
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);//SDCard已卸掉,如果SDCard是存在但没有被安装
//        filter.addAction(Intent.ACTION_MEDIA_EJECT); //物理的拔出
//        filter.addAction(Intent.ACTION_MEDIA_REMOVED);//完全拔出
        filter.addDataScheme("file");
        context.registerReceiver(this, filter);
    }



    /**
     * 判断usb是否插入
     * @param context 运行环境
     * @return true表示插入，否则表示没有
     */
    private  boolean isUsbAttached(Context context) {
        PreferencesUtil.initContext(context);
        return PreferencesUtil.getBoolean("isUsbAttached", false);
    }
}
