package com.apicloud.A6989041790790.libraries;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.pingplusplus.android.PaymentActivity;
import com.tencent.mm.sdk.modelbase.BaseResp;

/**
 * Created by tdzl2_000 on 2015-10-13.
 */
public class PingxxModule extends ReactContextBaseJavaModule {

    static final int REQUEST_CODE_PAYMENT = 0x91001;

    public PingxxModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RCTPingxx";
    }

    private static Activity mainActivity;

    public static void setMainActivity(Activity activity){
        mainActivity = activity;
    }

    @ReactMethod
    public void pay(String charge, Callback resolve, Callback reject){
        _setCallback(new PromiseCallback(resolve, reject));
        Intent intent = new Intent();
        String packageName = this.getReactApplicationContext().getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        if (mainActivity != null) {
            mainActivity.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }
    }
    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK) {
            this.handleResultData(data);
        }
    }

    private void handleResultData(Intent data) {
        String result = data.getExtras().getString("pay_result");
        if (result != null) {
            if (result.equals("success")){
                _resolve(result);
            } else {
                WritableMap map = Arguments.createMap();
                map.putString("err", result);
                map.putString("errMsg", data.getExtras().getString("error_msg"));
                _reject(map);
            }
        }
    }

    private class PromiseCallback{
        private PromiseCallback(Callback resolve, Callback reject){
            this.resolve = resolve;
            this.reject = reject;
        }
        private Callback resolve;
        private Callback reject;
    }
    private PromiseCallback callback;

    private void _setCallback(PromiseCallback callback){
        if (this.callback != null){
            WritableMap event = Arguments.createMap();
            event.putInt("err", BaseResp.ErrCode.ERR_USER_CANCEL);
            _reject(event);
        }
        this.callback = callback;
    }
    private void _reject(WritableMap event){
        if (callback != null){
            callback.reject.invoke(event);
            callback = null;
        }
    }

    private void _resolve(){
        if (callback != null){
            callback.resolve.invoke();
            callback = null;
        }
    }

    private void _resolve(Object event){
        if (callback != null){
            callback.resolve.invoke(event);
            callback = null;
        }
    }
}
