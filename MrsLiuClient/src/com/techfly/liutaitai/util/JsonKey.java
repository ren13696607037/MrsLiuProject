package com.techfly.liutaitai.util;

public interface JsonKey {
	static final String CODE = "code";
	static final String MESSAGE = "message";
	static final String DATA = "data";

	interface ProductCategory {
		static final String GOODS_CLASS = "goods_class";
		static final String ID = "id";
		static final String NAME = "name";
		static final String IMG = "image";
		static final String PARENT_ID = "parent_id";
		static final String SUB = "sub";
		static final String SUB_CATEGORY = "subCategory";
		static final String BANNER = "rootBanner";
	}

	interface CategorySubBanner {
		static final String TARGET_ID = "targetId";
		static final String ID = "id";
		static final String TARGET_TYPE = "targetType";
		static final String CONTENT = "content";
		static final String IMAGE_URL = "image";
	}

	interface UserKey {
		static final String ID = "id";
		static final String NAME = "name";
		static final String NICK = "nickname";
		static final String MONEY = "money";
		static final String MOBILE = "phone_number";
		static final String PASS = "pwd";
		static final String OPASS = "oldpwd";
		static final String TOKEN = "token";
		static final String SMS = "smscode";
		static final String PRINCIPAL = "principal";
		static final String USERNAME = "userName";
		static final String EMAIL = "email";
		static final String CODEURL = "codeUrl";
		static final String COMMENT = "commentjson";
		static final String LNAME = "login_name";
		static final String NPASS = "newpwd";
		static final String TYPE = "type";
		static final String ROLE = "role";
		static final String PUSH = "jpushcode";
		static final String AVATAR = "avatar";
	}

	interface SuggestKey {
		static final String CONTENT = "message";
		static final String CONTACT = "contact";
	}

	interface AddressKey {
		static final String ID = "id";
		static final String NAME = "name";
		static final String MOBILE = "mobile";
		static final String CITY = "city";
		static final String REGIONID = "regionId";
		static final String REGION = "region";
		static final String DETAIL = "location";
		static final String DEFAULT = "isdefault";
		static final String ADDRESS = "address";
		static final String AREAID = "areaId";
		static final String ZIPCODE = "postCode";
		static final String STATE = "state";
		static final String ADDRESSID = "addressId";
		static final String ADDRESSPKS = "addressPks";
		static final String ISDEFAULT = "isDefault";
		static final String PROVINCE="province";
		static final String AID = "aid";
	}

	interface HomeKey {
		static final String QQ = "qq";
		static final String WEIXIN = "weixin";
		static final String COMMODITYBANNER = "commodityBanner";
		static final String COMMODITYCARD = "commodityCard";
		static final String ADVERTISING = "advertising";
		static final String TUANGOUCARD = "tuangouCard";
		static final String COMMODITYHOT = "commodityHot";
		static final String SECKILLRESPONSE = "seckillResponse";
	    static final String SHARE_CONTENT = "shareContent";
	    static final String SHARE_URL = "shareUrl";
		
	}

	interface BannerKey {
		static final String ID = "id";
		static final String TARGETID = "targetId";
		static final String TARGETTYPE = "targetType";
		static final String CONTENT = "content";
		static final String IMAGE = "image";
	}

	interface ProductGroupBuyKey {
		static final String ID = "id";
		static final String COMMODITYID = "commodityId";
		static final String COMMODITYNAME = "commodityName";
		static final String PRICE = "price";
		static final String OLDPRICE = "oldPrice";
		static final String SALENUM = "salenum";
		static final String IMAGE = "image";
	}

	interface AdvertisementKey {
		static final String ID = "id";
		static final String IMAGE = "image";
		static final String CONTENT = "content";
		static final String GOODSID = "goodsId";
	}

	interface CategoryKey {
		static final String NAME = "name";
		static final String ITEMS = "items";
		static final String CATEGORYSHOW = "categoryShow";
		static final String COMMODITYSHOW = "commodityShow";
	}

	interface CategoryItemKey {
		static final String ID = "id";
		static final String Name = "name";
	}
	
	interface CategoryShowKey {
		static final String IMAGE = "image";
		static final String DESCRIPTION = "description";
		static final String TARGETID = "targetId";
		static final String TARGETNAME = "targetName";
	}
	
	interface HotGoodKey {
		static final String ID = "id";
		static final String COMMODITYID = "commodityId";
		static final String COMMODITYNAME = "commodityName";
		static final String DESCRIPTION = "description";
		static final String IMAGE = "image";
		static final String MARKETPRICE = "marketPrice";
		static final String PRICE = "price";
	}
	
	interface SecKillKey {
		static final String STARTTIME = "startTime";
		static final String STATUS = "status";
		static final String ITEMS = "items";
	}
	
	interface SecKillItemKey {
		static final String ID = "id";
		static final String COMMODITYID = "commodityId";
		static final String NAME = "name";
		static final String PRICE = "price";
		static final String OLDPRICE = "oldPrice";
		static final String IMAGE = "image";
		static final String ENABLED = "enabled";
	}

	interface ProductKey {
		static final String ID = "id";
		static final String NAME = "name";
		static final String PRICE = "price";
		static final String MARKET_PRICE = "marketPrice";
		static final String ICON = "icon";
		static final String SALENUM = "salenum";
	}

	interface TuanGouKey {
		static final String ID = "id";
		// static final String ID = "commodityId";
		static final String NAME = "commodityName";
		static final String PRICE = "price";
		static final String MARKET_PRICE = "oldPrice";
		static final String ICON = "image";
		static final String SALENUM = "salenum";
	}

	interface CommentsKey {
		static final String ID = "id";
		static final String NAME = "mbname";
		static final String PHONE = "phone";
		static final String TITLE = "title";
		static final String CONTENT = "content";
		static final String STAR_SCORE = "starScore";
		static final String VERIFY = "verify";
		static final String TIME = "createTime";
		static final String REPLAY = "replay";
	}

	interface ProductInfo {
		static final String ID = "id";
		static final String SN = "sn";
		static final String NAME = "name";
		static final String PRICE = "price";
		static final String ICON = "icon";
		static final String MARKET_PRICE = "marketPrice";
		static final String IMAGES = "images";
		static final String IMAGES_PATH = "path";
		static final String NORM = "norm";
		static final String COMMENT = "comment";
		static final String COMMENT_PERCENT = "reputably";
		static final String COMMENT_COUNT = "count";
		static final String COMMENT_STAR = "countStarScore";
		static final String COMMENT_COMMENTS = "comments";
		static final String COLLECT = "collected";
		static final String STORE_COUNT = "stock";
		static final String REBATE = "discount";
		static final String TYPE = "type";

		static final String STANDARD = "norm";
		static final String STANDARD_CLASS = "name";
		static final String STANDARD_ITEM = "items";
		static final String STANDARD_VALUE = "value";
		static final String STANDARD_NUM = "products";
		static final String STANDARD_SUB_ID = "productId";
		static final String STANDARD_SUB_STOCK = "productStock";
	}

	interface MyOrderKey {
		static final String ID = "oId";
		static final String PRICE = "allPrice";
		static final String PAY = "pay";
		static final String SIZE = "size";
		static final String PAGE = "page";
		static final String NAME = "goodsName";
		static final String IMG = "image";
		static final String STATE = "status";
		static final String ISEVALUATE = "isEvaluate";
		static final String TIME = "createTime";
		static final String ADDRNAME = "receiverName";
		static final String ADDRPHONE = "receiverMobile";
		static final String ADDRESS = "receiverAddress";
		static final String PAYSTATE = "payStatus";
		static final String FREE = "costFreight";
		static final String GOOD = "goods";
		static final String GOODID = "id";
		static final String GOODNUM = "nums";
		static final String GOODSN = "sn";
		static final String GOODNAME = "name";
		static final String GOODPRICE = "price";
		static final String GOODIMG = "picture";
		static final String NOTE = "buyerComments";
		static final String LOGISTICS = "logistics";
		static final String SHORTNAME = "shorName";
		static final String LOGNO = "logiNo";
		static final String RATENAME = "mbname";
		static final String RATECONTENT = "content";
	}

	interface OrderSearchKey {
		static final String CODE = "resultcode";// 标识码
		static final String REASON = "reason";
		static final String RESULT = "result";
		static final String COMPANY = "company";
		static final String COM = "com";
		static final String NO = "no";
		static final String LIST = "traces";
		static final String TIME = "acceptTime";
		static final String REMARK = "remark";
		static final String ZONE = "acceptAddress";
		static final String ERROR = "error";

		// 圆通
		static final String TIMESTAMP = "timestamp";
		static final String SIGN = "sign";
		static final String APPKEY = "app_key";
		static final String FORMAT = "format";
		static final String METHOD = "method";
		static final String USERID = "user_id";
		static final String V = "v";
		static final String PARAM = "param";
		static final String WAYBILL_NO = "Waybill_No";
		static final String UTIME = "Upload_Time";
		static final String INRO = "ProcessInfo";

	}
	interface ServiceKey{
		static final String ID = "id";
		static final String TIME = "time";
		static final String TECH = "technician";
		static final String PRICE = "price";
		static final String NAME = "name";
		static final String STATE = "state";
		static final String IMAGE = "images";
		static final String DATE = "date";
		static final String TYPE = "type";
	}
	interface ServiceDetailKey{
		static final String ID = "id";
		static final String TIME = "time";
		static final String TECH = "technician";
		static final String PRICE = "price";
		static final String NAME = "name";
		static final String STATE = "state";
		static final String IMAGE = "images";
		static final String DATE = "date";
		static final String TYPE = "type";
		static final String UNAME = "uname";
		static final String NUMBER = "number";
		static final String VOUCHER = "voucher";
		static final String AMOUNT = "amount";
		static final String ADDRESS = "address";
		static final String ADDTIME = "addtime";
	}
	interface BalanceKey{
		static final String PAGE = "no";
		static final String SIZE = "size";
		static final String DATAS = "datas";
		static final String ID = "id";
		static final String MONEY = "money";
		static final String TIME = "create_time";
		static final String TYPE = "type";
	}
	interface VoucherKey{
		static final String PAGE = "no";
		static final String SIZE = "size";
		static final String DATAS = "datas";
		static final String ID = "id";
		static final String MONEY = "money";
		static final String NEED = "need";
	}
	interface MyServiceKey{
		static final String ID = "id";
		static final String MONEY = "money";
		static final String ING_ORDERS = "ing_orders";
		static final String SERVICE_ORDERS = "service_orders";
		static final String TYPE = "type";
		static final String NEW_ORDERS = "new_orders";
	}
	interface TechnicianKey{
		static final String ID = "id";
		static final String TIMES = "times";
		static final String STARS = "stars";
		static final String NAME = "name";
		static final String SEX = "gender";
		static final String IMAGE = "image";
		static final String TYPE = "type";
		static final String CITY = "city";
	}

}
