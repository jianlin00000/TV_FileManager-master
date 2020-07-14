package com.filemanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.filemanager.AppHelper;
import com.filemanager.widget.FragmentHandler;
import com.filemanager.widget.IUIHandler;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;

/**
 * 功能描述：抽象基础界面activity
 * 开发状况：正在开发中
 */

public abstract class AbsBaseActivity extends FragmentActivity
        implements IUIHandler {

    protected static final int REQUEST_CODE = 111;
    //保存碎片处理器，用于加载fragment碎片
    private FragmentHandler mFragmentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        onCreateView();
        initData();
        initListener();
        onInitData(savedInstanceState);
    }

    /**
     * 初始化必要的内容
     */
    private void initView() {
        AppHelper.instance().putActivity(this);
        mFragmentHandler = new FragmentHandler(getSupportFragmentManager());
    }

    @Override
    public FragmentHandler getFragmentHandler() {
        return mFragmentHandler;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppHelper.instance().removeActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    protected String[] getPermissions() {
        final String[] permissions = new String[2];
        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
        return permissions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            PackageManager pm=getPackageManager();
            String[] rights=null;
            final List<String> deniedPermissions=new ArrayList<>();
            try{
                PackageInfo pi=pm.getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
                rights=pi.requestedPermissions;
            }catch (Exception e){
                e.printStackTrace();
            }
            if(rights!=null){
                for(int i=0;i<permissions.length;i++){
                    if(grantResults[i]==PackageManager.PERMISSION_DENIED){
                        for(int j=0;j<rights.length;j++){
                            if(rights[j].equals(permissions[i])){
                                String permission=permissions[i];
                                permission=permission.substring(permission.lastIndexOf('.')+1);
                                deniedPermissions.add(permission);
                                break;
                            }
                        }
                    }
                }
            }
            if(deniedPermissions.size()>0){
                StringBuffer temp=new StringBuffer();
                for(String s:deniedPermissions){
                    temp.append(s+"/");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
