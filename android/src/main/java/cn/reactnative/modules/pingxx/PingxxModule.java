package cn.reactnative.modules.pingxx;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;
import com.pingplusplus.android.PaymentActivity;

/**
 * Created by tdzl2_000 on 2015-10-13.
 */
public class PingxxModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    static final int REQUEST_CODE_PAYMENT = 0x91001;

    public PingxxModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RCTPingxx";
    }

    @Override
    public void initialize() {
        super.initialize();
        getReactApplicationContext().addActivityEventListener(this);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        getReactApplicationContext().removeActivityEventListener(this);
    }

    @ReactMethod
    public void pay(String charge){
        Intent intent = new Intent(getCurrentActivity(), PaymentActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        getCurrentActivity().startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private void handleResultData(Intent data) {
        String result = data.getExtras().getString("pay_result");
        if (result != null) {
            RCTNativeAppEventEmitter emitter = getReactApplicationContext().getJSModule(RCTNativeAppEventEmitter.class);
            WritableMap map = Arguments.createMap();
            map.putString("result", result);
            if (!result.equals("success")) {
                map.putInt("errCode", data.getExtras().getInt("code"));
                map.putString("errMsg", data.getExtras().getString("error_msg"));
                map.putString("extraMsg", data.getExtras().getString("extra_msg"));
            }
            emitter.emit("Pingxx_Resp", map);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK) {
            this.handleResultData(data);
        }
    }
}
