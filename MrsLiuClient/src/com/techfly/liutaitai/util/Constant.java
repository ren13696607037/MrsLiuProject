package com.techfly.liutaitai.util;

import com.baidu.location.BDLocation;

import android.R.integer;

public class Constant {

	public static final String YIHUIMALL_BASE_URL = "http://121.43.158.189/liu/";
	// public static final String YIHUIMALL_BASE_URL =
	// "http://192.168.0.212:8080/yhmall-api/";
	public static final String IMG_HEADER_URL = "http://121.43.158.189/liu";
	public static final String IMG_URL = "http://121.43.158.189/liuTai";

	public static final String PRODUCT_CATEGORY = "goods/category/root";
	public static final String PRODUCT_GROUP = "yiqituan";
	public static final String SMSCODE_URL = "user/vercode";
	public static final String LOGIN_URL = "user/login";
	public static final String LOGIN_EMAIL_URL = "auth-web/login";
	public static final String REGISTER_URL = "user/signup";
	public static final String REGISTER_EMAIL_URL = "auth-web/regist";
	public static final String SUGGEST_URL = "user/feedback";
	public static final String ADDRESS_URL = "user/addresses";
	public static final String MYORDER_LIST_URL = "member/order";
	public static final String EMAIL_CODE_URL = "auth-web/smscode";
	public static final String ORDER_DELETE_URL = "member/order/remove";
	public static final String ORDER_CANCEL_URL = "member/order/cancel";
	public static final String COMMENT_URL = "goods/comment";
	public static final String ZTO_URL = "http://api.zto.cn/json.aspx?Userid=wuyicheng&Pwd=WUYICHENG007&SrtjobNo=";
	public static final String YTO_URL = "http://58.32.246.70:8088/MarketingInterface/";
	public static final String YTO_JAVA_URL = "api-transffer/yto";
	public static final String LOGOUT_URL = "auth/logout";
	public static final String LocationUrl = "common/location";

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
	public static final String CITY_URL = "common/citys";
	public static final String PROVINCE_URL = "member/address/province";
	public static final String ADD_ADDRESS_URL = "member/address/input";
	public static final String PRODUCT_COLLECT = "user/collections";
	public static final String PRODUCT_BROWSER = "member/history";
	public static final String PRODUCT_CANCEL_COLLECT = "goods/collect/deleteAll";
	public static final String PRODUCT_TUANGOU = "tuangou";
	public static final String ADDRESS_DEFAULT_URL = "user/setaddress";
	public static final String ADDRESS_DELETE_URL = "user/deladdress";
	public static final String CHANGE_ADDRESS_URL = "user/doaddress";
	public static final String GOODS_DETAIL = "product/detail";// 商品详情
	public static final String SERVICE_DETAIL = "service/detail";// 服务详情
	public static final String SECKILL_DETAIL = "seckill/detail";// 限时秒杀详情
	public static final String TUANGOU_DETAIL = "tuangou/detail";// 团购商品详情
	public static final String GOODS_COLLECT = "goods/collect";// 收藏
	public static final String GOODS_UNCOLLECT = "goods/collect/delete";// 取消收藏
	public static final String GOODS_PIC_TEXT = "user/getImgDetail?id=";// 商品图文详情
	public static final String PASS_URL = "user/modifypwd";// 修改密码
	public static final String ORDER_SERVICE_URL = "commodity/bespeak/";// 美甲等预约订单
	public static final String ORDER_APPOINTMENT_URL = "";// 洗衣预约订单
	public static final String ORDER_SERVICE_DETAIL_URL = "service/reservation";
	public static final String USER_HEADER_URL = "uploadfile/pic/";
	public static final String SERVICE_URL = "master/index";
	public static final String TECH_INFO_URL = "master/mine";
	public static final String TECH_INFO_HEADER_URL = "master/modify";
	public static final String TECH_INFO_CHANGE_URL = "master/modifyinfo";
	public static final String SERVICE_LIST_URL = "service/reservations";
	public static final String SERVICE_CANCEL_URL = "service/cancelreservation";
	public static final String SERVICE_DELETE_URL = "service/delreservation";
	public static final String SERVICE_RATE_URL = "common/review";
	public static final String TECH_ORDER_DETAIL_URL = "master/order";
	public static final String TECH_ORDER_LIST_URL = "master/orders";
	public static final String TECH_ORDER_TAKE_URL = "master/take";
	public static final String TECH_ORDER_REFRSE_URL = "master/refuse";
	public static final String TECH_ORDER_DONE_URL = "master/done";
	public static final String TECH_ORDER_START_URL = "master/start";
	public static final String TECH_ORDER_REMOVE_URL = "master/removeorder";
	public static final String TECH_ACCOUNT_URL = "master/binding";
	public static final String TECH_CASH_URL = "master/cash";
	public static final String TECH_CASH_INFO_URL = "master/bindinginfo";
	public static final String TECH_RATE_LIST_URL = "common/reviews";
	public static final String FORGET_URL = "user/forgotpwd";
	public static final String USER_INFO_URL = "user/info";
	public static final String COLLECT_SERVICE_URL = "service/collect";

	public static final String PRODUCT_LIST = "product/productList";
	public static final String ORDER_COMMIT_REQUEST_URL = "product/addOrder";
	public static final String SERVICE_ORDER_COMMIT_REQUEST_URL = "service/addreservation";
	public static final String ALIPAY_CALLBACK_URL = "common/payNotify";
	public static final String ALIPAY_ORDER_CALLBACK_URL = "common/payNotify";

	public static final String ORDER_BASKET_LIST = "order/list";// 订单篮订单列表

	public static final String ORDER_INFO = "order/detail";// 订单篮订单详情
	public static final String ORDER_INFO_ID = "orderid";
	public static final String CANCEL_ORDER = "order/cancelOrder";// 取消订单
	public static final String DELETE_ORDER = "order/deleteOrder";// 删除订单

	public static final String AFTER_SALE_SERVICE = "order/orderReply";// 申请售后
	public static final String AFTER_SALE_SERVICE_CONTENT = "message";// 申请售后
	public static final String ORDER_EVA = "common/reviewgoods";// 订单评价
	public static final String ORDER_EVA_CONTENT = "msg";// 订单评价
	public static final String ORDER_EVA_ID = "orderid";

	public static final String SHOP_CARD_REQUEST_URL = "product/cartList";
	public static String UPDATE_TO_CART_REQUEST_URL = "product/editCartCount";
	public static String ADD_TO_CART_REQUEST_URL = "product/addCart";
	public static String DELETE_TO_CART_REQUEST_URL = "product/deleteUserCart";
	public static String CITY_REQUEST_LIST_URL = "common/citys";
	public static String CONFIRM_ORDER_REQUEST = "product/firmOrder";

	public static final String CHANGE_INFO_URL = "user/modifyprofile";
	public static final String BALANCE_HISTORY_URL = "user/moneyrecords";
	public static final String BALANCE_URL = "user/vouchercards";
	public static final String VOUCHER_URL = "user/myvouchers";
	public static final String CHANGE_NICK_URL = "user/modifynickname";
	public static final String APPLY_URL = "master/apply";

	public static final int PAY_TYPE_ORDER = 110; // 对已有订单的支付
	public static final int PAY_TYPE_CREATE = 111; // 创建订单的支付

	public static final int PRODUCT_TYPE_ENTITY = 210; // 对已有订单的支付
	public static final int PRODUCT_TYPE_SERVICE = 111; // 创建订单的支付

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

	public static final int NOTIFY_LIST = 0x126;
	public static final String PAGE = "no";
	public static final String SIZE = "size";

	public static final int PAY_BALANCE = 0x1561;
	public static final int PAY_ALIPAY = 0x1562;
	public static final int PAY_WENXIN = 0x1563;
	public static final int PAY_OFFLINE = 0x1564;

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
	public static final int BALANCE_INTENT = 825;
	public static final int BALANCE_SUCCESS = BALANCE_INTENT + 1;
	public static final int RATE_INTENT = 827;
	public static final int RATE_SUCCESS = RATE_INTENT + 1;

	public static final int PRODUCT_SALE_REQ = 1;// 按销量请求
	public static final int PRODUCT_PRICE_REQ = 2;// 按价格请求
	public static final int PRODUCT_SHELVES_REQ = 3;// 按上架时间请求

	// 自动填充邮箱控件
	public static final String[] EMAILS = { "@qq.com", "@163.com", "@126.com",
			"@yeah.net", "@sina.cn", "@sina.com", "@vip.qq.com",
			"@vip.163.com", "@vip.sina.com", "@gmail.com", };
	public static final String REFRESH_UPLOAD_GRIDVIEW_IMAGE = "refresh_upload_gridview_image";

	public static final String KEFU_PHONE = "4008275899";
	public static boolean isShouldRefresh = false;
	
	public static BDLocation mLocation=null;
}
