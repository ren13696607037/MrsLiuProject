package com.techfly.liutaitai.util;

import android.R.integer;

public class Constant {

	public static final String YIHUIMALL_BASE_URL = "http://121.43.158.189/jerservice/";
//	public static final String YIHUIMALL_BASE_URL = "http://192.168.0.212:8080/yhmall-api/";



	public static final String PRODUCT_CATEGORY = "goods/category/root";
	public static final String PRODUCT_GROUP = "yiqituan";
	public static final String SMSCODE_URL = "user/vcode";
	public static final String LOGIN_URL = "user/applogin";
	public static final String LOGIN_EMAIL_URL = "auth-web/login";
	public static final String REGISTER_URL = "user/appregist";
	public static final String REGISTER_EMAIL_URL = "auth-web/regist";
	public static final String SUGGEST_URL = "member/suggestions";
	public static final String ADDRESS_URL = "member/address";
	public static final String MYORDER_LIST_URL = "member/order";
	public static final String EMAIL_CODE_URL = "auth-web/smscode";
	public static final String ORDER_DELETE_URL = "member/order/remove";
	public static final String ORDER_CANCEL_URL = "member/order/cancel";
	public static final String COMMENT_URL = "goods/comment";
	public static final String ZTO_URL = "http://api.zto.cn/json.aspx?Userid=wuyicheng&Pwd=WUYICHENG007&SrtjobNo=";
	public static final String YTO_URL = "http://58.32.246.70:8088/MarketingInterface/";
	public static final String YTO_JAVA_URL = "api-transffer/yto";
	public static final String LOGOUT_URL="auth/logout";

	// 启动整合接口
	public static final String SYSTEM_URL = "system";
	// 版本更新
	public static final String UPDATE_VERSION = "system/upgrade";
	// 关于我们
	public static final String ABOUT_US = "system/aboutus";
	// 帮助中心
	public static final String HELP_CENTER = "system/help";
	// 搜索商品列表接口
	public static final String SEARCH_PRODUCT_URL = "goods/category/list";
	public static final String CITY_URL = "member/address/city";
	public static final String PROVINCE_URL = "member/address/province";
	public static final String ADD_ADDRESS_URL = "member/address/input";
	public static final String PRODUCT_COLLECT = "member/collect";
	public static final String PRODUCT_BROWSER = "member/history";
	public static final String PRODUCT_CANCEL_COLLECT = "goods/collect/deleteAll";
	public static final String PRODUCT_TUANGOU = "tuangou";
	public static final String ADDRESS_DEFAULT_URL = "member/address/defaulted";
	public static final String ADDRESS_DELETE_URL = "member/addresses/remove";
	public static final String CHANGE_ADDRESS_URL = "member/address/edit";
	public static final String GOODS_DETAIL = "goods/detail";// 商品详情
	public static final String SECKILL_DETAIL = "seckill/detail";//  限时秒杀详情
	public static final String TUANGOU_DETAIL = "tuangou/detail";// 团购商品详情
	public static final String GOODS_COLLECT = "goods/collect";// 收藏
	public static final String GOODS_UNCOLLECT = "goods/collect/delete";// 取消收藏
	public static final String GOODS_PIC_TEXT = "goods/detail/";// 取消收藏
	public static final String PASS_URL = "user/updatepassword";//修改密码
	public static final String ORDER_SERVICE_URL = "commodity/bespeak/";//美甲等预约订单
	public static final String ORDER_APPOINTMENT_URL = "";//洗衣预约订单

	public static final String PRODUCT_LIST = "goods/category/list";
	public static final String ORDER_COMMIT_REQUEST_URL = "commodity/buy";
	public static final String ALIPAY_CALLBACK_URL = "alipayCallback";
	public static final String ALIPAY_ORDER_CALLBACK_URL = "alipayCallback";
	public static final int PAY_TYPE_ORDER = 110; // 对已有订单的支付
	public static final int PAY_TYPE_CREATE = 111; // 创建订单的支付

	public static final int PRODUCT_TYPE_ENTITY = 210; // 对已有订单的支付
	public static final int PRODUCT_TYPE_VIRTUAL = 111; // 创建订单的支付

	public static int DEFAULT_SIZE = 10;
	public static int RESULT_CODE = 0;
	public static final int NETWORK_NOT_AVALIABLE_MESSAG = 0x901;
	public static final int NET_STATUS_NODATA = 0x902;
	public static final int NET_SUCCESS = 0x903;
	public static final int NET_LOAD = 0x904;
	public static final int NET_REFRESH = 0x905;
	public static final int NET_FAILURE = 0x906;
	public static final int NET_REFRESHING = 0x907;

	public static int SCREEN_WIDTH = 480;
	public static int SCREEN_HEIGHT = 800;
	public static float SCREEN_DENSITY = (float) 1.0;

	public static int TUANGOU_PRO_ITEM_STYLE = 0X111;
	public static int COLLECT_PRO_ITEM_STYLE = 0X112;
	public static int COMMON_PRO_ITEM_STYLE = 0X113;

	public static int HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG = 0;// BACK iCON

	public static final int LOGIN_INTENT = 0x801;
	public static final int LOGIN_SUCCESS = 0x802;
	public static final int REGISTER_INTENT = 0x803;
	public static final int REGISTER_SUCCESS = 0x804;
	public static final int FORGET_INTENT = 0x805;
	public static final int FORGET_SUCCESS = 0x806;
	public static final int CITY_INTENT = 0x807;
	public static final int CITY_SUCCESS = 0x808;
	public static final int ADDRESS_INTENT = 0x809;
	public static final int ADDRESS_SUCCESS = 0x810;
	public static final int ORDER_ADDRESS_INTENT = 0x811;
	public static final int ORDER_ADDRESS_SUCCESS = 0x812;
	public static final int ORDER_CITY_INTENT = 0x813;
	public static final int ORDER_CITY_SUCCESS = 0x814;
	public static final int DETAIL_INTENT = 0x815;
	public static final int DETAIL_SUCCESS = 0x816;
	public static final int ADDRESS_REQUEST_CODE = 901;
	public static final int AD_MANAGE_INTENT = 819;
	public static final int AD_MANAGE_SUCCESS = 820;
	public static final int PRO_INTENT = 821;
	public static final int PRO_SUCCESS = 822;
	public static final int EXIT_INTENT = 823;
	public static final int EXIT_SUCCESS = 824;

	public static final int PRODUCT_SALE_REQ = 1;// 按销量请求
	public static final int PRODUCT_PRICE_REQ = 2;// 按价格请求
	public static final int PRODUCT_SHELVES_REQ = 3;// 按上架时间请求

	// 自动填充邮箱控件
	public static final String[] EMAILS = { "@qq.com", "@163.com", "@126.com",
			"@yeah.net", "@sina.cn", "@sina.com", "@vip.qq.com",
			"@vip.163.com", "@vip.sina.com", "@gmail.com", };

}
