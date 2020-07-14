package com.filemanager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.common.data.BaseObservableData;
import com.common.utils.DataObservable;
import com.filemanager.AppHelper;
import com.filemanager.R;
import com.filemanager.bean.CFile;
import com.filemanager.blls.MainMenu;
import com.filemanager.fragment.AbsCommonFragment;
import com.filemanager.fragment.DiskFragment;
import com.filemanager.receiver.UsbReceiver;
import com.filemanager.utils.Constant;
import com.filemanager.utils.DialogUtil;
import com.filemanager.utils.ToastUtil;
import com.filemanager.widget.FolderWindowOp;
import com.filemanager.widget.MenuWindowOp;
import com.filemanager.widget.SortWindowOp;

import java.util.Observable;
import java.util.Observer;

import androidx.core.app.ActivityCompat;


/**
 * 功能描述：主界面
 * 开发状况：正在开发中
 */
public class MainActivity extends AbsBaseActivity
        implements View.OnFocusChangeListener, Observer {

    //响应按键菜单
    private MenuWindowOp mMenuWindowOp;
    //保存退出间隔时间
    private long mExitTime = 0;
    //排序入口
    private Button mSortBtn;
    //保存一级菜单
    private MainMenu mMainMenu;
    //路径和排序视图
    //在进入多存储盘时隐藏该视图
    private View mPathBarRl;
    //暂时当前进入的文件路径
    private TextView mPathTv;
    //排序选择窗口
    private SortWindowOp mWindowOp;
    //磁盘碎片
    private DiskFragment mDiskFragment;
    private AbsCommonFragment mFrament;
    private UsbReceiver mUsbReceiver;

    @Override
    public void onCreateView() {
        setContentView(R.layout.activity_main_tab);
        mPathTv = findViewById(R.id.tv_path);
        mSortBtn = findViewById(R.id.btn_sort);
        mPathBarRl = findViewById(R.id.rl_path_bar);
    }

    @Override
    public void initData() {
        registerReceiver();
        DataObservable.getInstance().addObserver(this);
        mMenuWindowOp = new MenuWindowOp(this);
        mWindowOp = new SortWindowOp(this);
        mMainMenu = new MainMenu();
        mDiskFragment = new DiskFragment();
    }

    private void registerReceiver() {
        mUsbReceiver = new UsbReceiver();
        mUsbReceiver.registerUsbReceiver(this);
    }

    @Override
    public void initListener() {
        mSortBtn.setOnClickListener(v -> {
            if(mMainMenu.clearMuselectModel()) {
                mMenuWindowOp.resetMuSelectBtnText();
            } else {
                mWindowOp.windowOption(v);
            }
        });
        mMenuWindowOp.setMenuSelectedCallback(mMenuSelectCallback);
    }


    @Override
    public void onInitData(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,getPermissions(),REQUEST_CODE);
        //获取菜单名
        final String menuName = getIntent().getStringExtra("menuName");
        Log.d("wlf", "onInitData: " + menuName);
        if(!TextUtils.isEmpty(menuName)) {
            mMainMenu.setMenuChange(menuName, new MainMenu.IChangeCallback() {
                @Override
                public void onChange(MainMenu.Menu menu) {
                    menu.getMenuView().requestFocus();
                    menu.getMenuView().requestFocusFromTouch();
                    replaceFragment(menu.getFragment());
                }
            });
        } else {
            replaceFragment(mDiskFragment);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof BaseObservableData){
            BaseObservableData data= (BaseObservableData) arg;
            int type = data.getType();
            switch (type) {
                case Constant.Msg.USB_STATUS_CHANGE:
                    final AbsCommonFragment fragment = getFragment();
                    if (!(fragment instanceof DiskFragment)) {
                        replaceFragment(mDiskFragment);
                    }
                    mDiskFragment.loadDisks();
                    break;
                default:
                     break;
            }
        }
    }


    //用于监听
    private MenuWindowOp.IMenuSelectCallback mMenuSelectCallback = new MenuWindowOp.IMenuSelectCallback() {
        @Override
        public void onSelected(View view, CFile selectedFilePath) {
            final AbsCommonFragment fragment = getFragment();
            switch (view.getId()) {
                case R.id.tv_menu_create:
                    DialogUtil.createNewFolderDialog(fragment);
                    break;
                case R.id.tv_menu_delete:
                    if(selectedFilePath != null) {
                        if(!fragment.isMuselectModel()) {
                            DialogUtil.deleteFileDialog(fragment,selectedFilePath);
                        } else {
                            DialogUtil.deleteFileDialog(fragment,fragment.getMuselectedData());
                        }
                    } else {
                        ToastUtil.toast(view.getContext(),getString(R.string.not_select_file));
                    }
                    break;
                case R.id.tv_menu_copy:
                    final FolderWindowOp windowOp = new FolderWindowOp(view.getContext());
                    if(!fragment.isMuselectModel()) {
                        windowOp.setCopyFile(selectedFilePath);
                    } else {
                        windowOp.setCopyFiles(fragment.getMuselectedData());
                    }
                    windowOp.windowOption(mPathBarRl);
                    break;
                case R.id.tv_menu_select_mode:
                    final TextView muselected = (TextView)view;
                    if(TextUtils.equals(getStr(R.string.multiple_choice), muselected.getText().toString().trim())) {
                        fragment.setAdapterMuselectModel(true);
                        mMenuWindowOp.muselectModel();
                    } else {
                        fragment.setAdapterMuselectModel(false);
                        mMenuWindowOp.resetMuSelectBtnText();
                        if(fragment instanceof DiskFragment) {
                            mMenuWindowOp.hideMenuViewById(R.id.tv_menu_select_mode, View.VISIBLE);
                        }
                    }
                    break;
                case R.id.tv_menu_rename:
                    if(selectedFilePath != null) {
                        DialogUtil.renameFileDialog(fragment,selectedFilePath.getPath());
                    } else {
                        ToastUtil.toast(view.getContext(),getString(R.string.not_select_file));
                    }
                    break;
                case R.id.tv_menu_file_info:
                    if(selectedFilePath != null) {
                        DialogUtil.showFileInfoDialog(view.getContext(),selectedFilePath.getPath());
                    } else {
                        ToastUtil.toast(view.getContext(),getString(R.string.not_select_file));
                    }
                    break;
                case R.id.tv_menu_send :
//                    FileUtil.sendFile(this,);
                    break;
                default:
                    break;
            }
            mMainMenu.currentMenuRequestFocus();
        }
    };


    private AbsCommonFragment getFragment() {
        return mFrament;
    }
    /**
     * 替换碎片
     * @param fragment 目标碎片
     */
    private void replaceFragment(AbsCommonFragment fragment) {
        mFrament = fragment;
        if(mMainMenu.clearMuselectModel()) {
            mMenuWindowOp.resetMuSelectBtnText();
            return;
        }
        getFragmentHandler().replaceFragment(R.id.fl_container, fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
            case 3:
            case 4:
                mMainMenu.currentMenuRequestFocus();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final AbsCommonFragment fragment = getFragment();
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(fragment.isMuselectModel()) {
                fragment.setAdapterMuselectModel(false);
                mMenuWindowOp.resetMuSelectBtnText();
                return true;
            }
            if(fragment.onKeyDown()) {
                return true;
            }
            return isExitApp();
        } else if(keyCode == KeyEvent.KEYCODE_MENU) {
            mMenuWindowOp.setSelectedFile(mPathTv,null);
            if(fragment instanceof DiskFragment) {
                if(!((DiskFragment) fragment).isDiskData()) {
                    if(!fragment.isMuselectModel()) {
                        mMenuWindowOp.hideMenuViewById(R.id.tv_menu_create,View.VISIBLE);
                    } else {
                        mMenuWindowOp.hideMenuViewById(R.id.tv_menu_create,View.GONE);
                    }
                    mMenuWindowOp.windowOption();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    /**
     * 退出应用程序
     * @return 是否退出
     */
    private boolean isExitApp() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mExitTime < 2000) {
            AppHelper.instance().exitApp();
        } else {
            mExitTime = currentTime;
            ToastUtil.toast(this,"再按一次退出");
        }
        return true;
    }

    /**
     * 设置当前被选中的文件路径
     * @param view 视图
     * @param path 路径
     */
    public void setCurrSelectedFilePath(View view,CFile path) {
        if(path != null) {
            mPathTv.setText(path.getPath());
            mMenuWindowOp.setSelectedFile(view,path);
        } else {
            mMenuWindowOp.setSelectedFile(view,null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUsbReceiver!=null){
            unregisterReceiver(mUsbReceiver);
            mUsbReceiver=null;
        }
    }

    public TextView getPathTv() {
        return mPathTv;
    }

    public View getPathBarRl() {
        return mPathBarRl;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            ((TextView)v).setTextColor(Color.YELLOW);
            mMainMenu.setMenuTextColor(v.getId(), Color.WHITE);
        } else {
            if(mMainMenu.isCurrentMenuId(v.getId())) {
                mMainMenu.getCurrentMenu().getMenuView().setTextColor(getResources().getColor(R.color.color5));
            } else {
                ((TextView)v).setTextColor(Color.WHITE);
            }
        }
    }

    /**
     * 获取主菜单
     * @return 菜单
     */
    public MainMenu getMainMenu() {
        return mMainMenu;
    }

    /**
     * 获取排序按钮
     * @return 排序按钮实例
     */
    public Button getSortBtn() {
        return mSortBtn;
    }

    /**
     * 获取字符串
     * @param resId 资源Id
     * @return 字符串
     */
    private String getStr(int resId) {
        return getResources().getString(resId);
    }



}
