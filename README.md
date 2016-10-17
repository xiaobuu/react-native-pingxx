# ping++ 支付react native模块
## 说明
该模块只保留了支付宝功能，如果需要其他功能，请从官方库中获得依赖的文件，并加入对应android或ios目录

[Android](https://github.com/PingPlusPlus/pingpp-android)

[iOS](https://github.com/PingPlusPlus/pingpp-ios)

[官方SDK配置文档](https://www.pingxx.com/docs/client/sdk/android)

添加其他支付功能后，请注意参考上述官方库中的README，进行必要的配置


## 使用
```
import {pay} from 'react-native-pingxx';

async function pingxx() {
  try {
    const result = await pay(data /* object 或 JSON 字符串，一般由后端给出*/);
  } catch (ex) {
    console.log(ex);
  }
}

```

## React Native 0.33+
由于RN安卓版本的更新， 需要修改Android代码中的一个文件，以保证正常运行

修改  android/src/main/java/cn/reactnative/modules/pingxx/PingxxModule.java 最下方
```
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK) {
        this.handleResultData(data);
    }
}
```

增加一个参数
```
@Override
public void onActivityResult(Activity _activity, int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK) {
        this.handleResultData(data);
    }
}
```
