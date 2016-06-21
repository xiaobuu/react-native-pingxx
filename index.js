/**
 * Created by tdzl2_000 on 2015-10-13.
 */

import {NativeModules, NativeAppEventEmitter} from 'react-native';

let nativeAPI = NativeModules.Pingxx;

let savedCallback = undefined;

NativeAppEventEmitter.addListener('Pingxx_Resp', resp => {
    const callback = savedCallback;
    savedCallback = undefined;
    callback && callback(resp);
});

function waitForResponse() {
    return new Promise((resolve, reject) => {
        if (savedCallback) {
            savedCallback({result: 'canceled'});
        }
        savedCallback = r => {
            savedCallback = undefined;
            const {result, errCode, errMsg} = r;

            if (result && result === 'success') {
                resolve(result);
            }
            else {
                const err = new Error(errMsg);
                err.result = result;
                err.errCode = errCode;
                reject(err);
            }
        };
    });
}

// return promise
export function pay(charge){
    if(typeof charge === 'string') {
        nativeAPI.pay(charge);
    }
    else {
        nativeAPI.pay(JSON.stringify(charge));
    }
    return waitForResponse();
}