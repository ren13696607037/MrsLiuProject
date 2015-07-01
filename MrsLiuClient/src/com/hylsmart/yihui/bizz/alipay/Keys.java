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
package com.hylsmart.yihui.bizz.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

  //合作身份者id，以2088开头的16位纯数字
    public static final String DEFAULT_PARTNER = "2088011276842720";

    //收款支付宝账号
    public static final String DEFAULT_SELLER = "anhuiyihui@163.com";

    //商户私钥，自助生成
    public static final String PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALjnuqoV18gdqzywlKrIJak3d+UdML8fKnlII8JPxOTgTAJyyP/fO00LPsry+z6PK/kSaGRj/eHOL2HqUV18ObTgizzBbdUzTFSspkEHv7PUN5kORlP2P/KstjVa7yyzLat4i3XIJz7z/2OcnBjjKIVAkDrI36lDDZQXyGEV42y/AgMBAAECgYEAj3hmcwC9zG2Q52dQA2+HIVSDuAe9Qh6Jk09xR50jdj+/Wor8AOWSGsal800zuyfWA83PqE2ZubKFO2FXvsTb9HYjN1zA0J4L7vjfMSwuIbSx6MWEEmGzAncqCnezIeP19NLbzoGiv4sTD80o0mX/TSckmk2A2IJvK2UXztrCdjkCQQDfSVpGsCgvMuNVlD8vDf7XswYNTTouJ/XYwJcnVp0PdAAnu4DtmhLNq0dOqZPIGRMMzDFjqdxiSezbJclz/NTjAkEA0/7VzFTItJg98NPdB1SeNkdMmg6sW1xOtxw6XgmIgBu2rdaZlE+3uNRCe86MBMuJluM8tahkLtVBDIqRnlwrdQJBAM989mrhnzXc7+vCisRLGRmPvnv1266wa4Az10Iy9lg5zzscvj4NApQkKWsroaw7wLwPs4WNfsHHOFac0or500kCQH1lFjd3ywQvrSu/VdUZM0At6lyR41djAViERO4Mu4XzXpny/wLgKG8WdoDfCqlnb2ol3WoiQagGEiUF7d567GUCQQDUbpRBKWn5HVsZEqFSRSPmXVNyqnxYQgNzMJvrfiMNZPf85TA9X96NW57SMY15Q0oNkCBLqclKunkalAPCosuN";

    //支付宝公钥
    public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC457qqFdfIHas8sJSqyCWpN3flHTC/Hyp5SCPCT8Tk4EwCcsj/3ztNCz7K8vs+jyv5EmhkY/3hzi9h6lFdfDm04Is8wW3VM0xUrKZBB7+z1DeZDkZT9j/yrLY1Wu8ssy2reIt1yCc+8/9jnJwY4yiFQJA6yN+pQw2UF8hhFeNsvwIDAQAB";

}
