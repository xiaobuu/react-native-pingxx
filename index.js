/**
 * Created by tdzl2_000 on 2015-10-13.
 */

import {NativeModules} from 'react-native';

let nativeAPI = NativeModules.Pingxx;

export class PingxxError extends Error {
    constructor(err, msg){
        super(msg || "Error occured with QQ API.");
        this.err = err;
    }
}

function translateError(e){
    if (typeof(e) == 'object' && !(e instanceof  Error)){
        throw new PingxxError(e.err, e.errMsg);
    }
}

export function pay(charge){
    return new Promise((resolve, reject)=>{
        nativeAPI.pay(JSON.stringify(charge), resolve, reject)
    }).catch(translateError)
}