package com.qiji.live.xiaozhibo.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.TIMManager;
import com.qiji.live.xiaozhibo.R;
import com.qiji.live.xiaozhibo.base.BaseFragment;
import com.qiji.live.xiaozhibo.inter.ITCUserInfoMgrListener;
import com.qiji.live.xiaozhibo.logic.TCUserInfoMgr;
import com.qiji.live.xiaozhibo.ui.EditUseInfoActivity;
import com.qiji.live.xiaozhibo.ui.SettingActivity;
import com.qiji.live.xiaozhibo.ui.customviews.TCLineControllerView;
import com.qiji.live.xiaozhibo.widget.AvatarImageView;
import com.tencent.rtmp.TXRtmpApi;

/**
 * 用户资料展示页面
 */
public class TCUserInfoFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "TCUserInfoFragment";
    private AvatarImageView mHeadPic;
    private TextView mNickName;
    private TextView mUserId;
    private TCLineControllerView mBtnLogout;
    private TCLineControllerView mBtnSet;


    public TCUserInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.fragment_user_info, container, false);

        initView(mRootView);
        initData();
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        //页面展示之前，更新一下用户信息
        TCUserInfoMgr.getInstance().queryUserInfo(new ITCUserInfoMgrListener() {
                @Override
                public void OnQueryUserInfo(int error, String errorMsg) {
                    if (0 == error) {
                        mNickName.setText(TCUserInfoMgr.getInstance().getNickname());
                        mUserId.setText("ID:" + TCUserInfoMgr.getInstance().getUserId());
                        mHeadPic.setLoadImageUrl(TCUserInfoMgr.getInstance().getHeadPic());
                        //TCUtils.showPicWithUrl(getActivity(), mHeadPic, TCUserInfoMgr.getInstance().getHeadPic(), R.drawable.face);
                    }
                }

                @Override
                public void OnSetUserInfo(int error, String errorMsg) {

                }
            });
        }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void enterEditUserInfo() {
        try {
            Intent intent = new Intent(getContext(), EditUseInfoActivity.class);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lcv_ui_set: //设置用户信息
                enterEditUserInfo();
                break;
            case R.id.lcv_ui_logout: //设置
                SettingActivity.startSettingActivity(getActivity());
                break;
        }
    }


    /**
     * 显示 APP SDK 的版本信息
     */
    private void showSDKVersion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        int[] sdkver = TXRtmpApi.getSDKVersion();
        builder.setMessage("APP : " + getAppVersion() + "\r\n"
                + "RTMP SDK: " + sdkver[0] + "." + sdkver[1] + "." + sdkver[2] + "\r\n"
                + "IM SDK: " + TIMManager.getInstance().getVersion()
                );
        builder.show();
    }

    /**
     * 获取APP版本
     *
     * @return APP版本
     */
    private String getAppVersion() {
        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packInfo;
        String version = "";
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public void initView(View view) {
        mHeadPic = (AvatarImageView) view.findViewById(R.id.iv_ui_head);
        mNickName = (TextView) view.findViewById(R.id.tv_ui_nickname);
        mUserId = (TextView) view.findViewById(R.id.tv_ui_user_id);
        mBtnSet = (TCLineControllerView) view.findViewById(R.id.lcv_ui_set);
        mBtnLogout = (TCLineControllerView) view.findViewById(R.id.lcv_ui_logout);
        mBtnSet.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
    }
}
