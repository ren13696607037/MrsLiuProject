/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */
package com.techfly.liutaitai.bizz.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

  //合作身份者id，以2088开头的16位纯数字
    public static final String DEFAULT_PARTNER = "2088021177840134";

    //收款支付宝账号
    public static final String DEFAULT_SELLER = "yt@liutaitai.com";

    //商户私钥，自助生成
    public static final String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALpag6EvM2T3eKgJYJo7BlRhibyiOuXx0MZ4I4k24QH4pLx52el0KHSdHwphs8DlcdrvkqmbJt60KSiyz9vpOmUInxOK1XoNaiOyFGOzgS4qiJgqxFxsVnxN5ZFMV3+HwVQMnSbSe+6Sqgct0C7Sgviq1vZ5F8lHMDAIRH6t6u2hAgMBAAECgYEApPEupsEXdVuRIPJD3d82iANZHWxeQ17YkoKNuYqod80zSltz5C1bDXRwoAjASJENSGJaZuKb8ex2rb4vOo9hjb746jwhWNn0SCcnwkN/Kv9iyYlaH7R9oPHVekEok21hOHgsiVD7ybh50R6lYQ2VQR5GfGPmoE6IJSR0a+Mqw80CQQDdD85nGERCY9wyugyBKuwnc0qhdEfDGmgBrKWk9H7zfFBjRSzIVAVtR1+cPiox0NPQX/P1orm2nGKr7B5hXPEjAkEA185o4MPrGgn5A7hfQi7c5/28hTtngi0E8kYZHvFU7PSi4aFfGd3cp8Szi09n2Mhbco5w+Ka9bcpAAXlERbGMawJAaunKGXJTEQja5m20voPT0wCfh5aFXobW5nhpZG4bOAdXmatLDVgVh9SNGTO3lIA68Px3MMWSrA7Wae+4gb5BoQJALUA8wZSTQZyBuIP6hXgqHgMYep9gLu2dsed88P4NnroVRVZ/CH96+zrFoxp9Xfl1I2o2ygy8f77++NAivrJiDQJABG3NO1mALdcMvAlRrunHWRtdYSobnYmEJUpLE4tFSbWVKsd2PzLtJrrb+m+kVVkMvivRXUcd4HlS4suIHeVCwg==";

    //支付宝公钥
    public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6WoOhLzNk93ioCWCaOwZUYYm8ojrl8dDGeCOJNuEB+KS8ednpdCh0nR8KYbPA5XHa75KpmybetCkoss/b6TplCJ8TitV6DWojshRjs4EuKoiYKsRcbFZ8TeWRTFd/h8FUDJ0m0nvukqoHLdAu0oL4qtb2eRfJRzAwCER+rertoQIDAQAB";

}
