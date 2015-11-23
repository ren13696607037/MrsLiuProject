package com.techfly.liutaitai.model.pcenter.fragment;



import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.LoginParser;
import com.techfly.liutaitai.model.pcenter.activities.AddressManageActivity;
import com.techfly.liutaitai.model.pcenter.activities.LoginActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyApplyActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyBalanceActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyCollectActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyServiceActivity;
import com.techfly.liutaitai.model.pcenter.activities.MyVoucherActivity;
import com.techfly.liutaitai.model.pcenter.activities.PCenterHomeActivity;
import com.techfly.liutaitai.model.pcenter.activities.PcenterInfoActivity;
import com.techfly.liutaitai.model.pcenter.activities.SettingActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.CircleImageView;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class PCenterHomeFragment extends CommonFragment implements OnClickListener{
	private PCenterHomeActivity mActivity;
	private RelativeLayout mWelcome;//登录注册头部
	private RelativeLayout mTop;//个人中心头部
	private RelativeLayout mBanlance;//我的余额
	private RelativeLayout mCollect;//我的收藏
	private RelativeLayout mVoucher;//我的现金券
	private RelativeLayout mAddress;//我的收货地址
	private RelativeLayout mApply;//申请成为技师
	private RelativeLayout mService;//我的服务
	private RelativeLayout mShare;
	private CircleImageView mHeader;
	private TextView mTvNick;
	private User mUser;
	private Button mButton;
	
	private static final String shareTitle = "刘太太.生活，您生活中的好太太";
	private static final String shareContent = "下载APP，随时随地一键下单，手艺人和太太管家马上到您身边，让您足不出户，享受惬意生活。";
	private static final String shareUrl = "http://121.43.158.189/d";
	
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private Map<String, SHARE_MEDIA> mPlatformsMap = new HashMap<String, SHARE_MEDIA>();


    @Override
    public void requestData() {
    	RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.USER_INFO_URL);
		param.setmIsLogin(true);
		param.setmId(mUser.getmId());
		param.setmToken(mUser.getmToken());
		param.setmHttpURL(url);
		param.setPostRequestMethod();
		param.setmParserClassName(LoginParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);

	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				mUser = (User) object;
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
					SharePreferenceUtils.getInstance(mActivity).saveUser(mUser);
					setView();
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.Loge(" data failed to load" + error.getMessage());
				if (!isDetached()) {
					mLoadHandler.removeMessages(Constant.NET_SUCCESS);
					mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				}
			}
		};
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(PCenterHomeActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
        if(mUser != null){
        	startReqTask(PCenterHomeFragment.this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleView();
        onInitView(view);
    }
    private void initTitleView(){
        setTitleText(R.string.home_pcenter_tab);
        setRightMoreIcon(R.drawable.pcenter_setting, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(mActivity,SettingActivity.class), Constant.EXIT_INTENT);
				
			}
		});
    }
    private void onInitView(View view){
    	mCollect = (RelativeLayout) view.findViewById(R.id.pcenter_collect);
    	mBanlance = (RelativeLayout) view.findViewById(R.id.pcenter_balance);
    	mTop = (RelativeLayout) view.findViewById(R.id.pcenter_top);
    	mWelcome = (RelativeLayout) view.findViewById(R.id.pcenter_welcome);
    	mTvNick=(TextView) view.findViewById(R.id.mine_name);
    	mHeader = (CircleImageView) view.findViewById(R.id.mine_img);
    	mAddress = (RelativeLayout) view.findViewById(R.id.pcenter_address);
    	mApply = (RelativeLayout) view.findViewById(R.id.pcenter_apply);
    	mService = (RelativeLayout) view.findViewById(R.id.pcenter_service);
    	mVoucher = (RelativeLayout) view.findViewById(R.id.pcenter_voucher);
    	mButton = (Button) view.findViewById(R.id.mine_btn);
    	mShare = (RelativeLayout) view.findViewById(R.id.pcenter_share);
    	
    	mButton.setOnClickListener(this);
    	mCollect.setOnClickListener(this);
    	mVoucher.setOnClickListener(this);
    	mWelcome.setOnClickListener(this);
    	mTop.setOnClickListener(this);
    	mAddress.setOnClickListener(this);
    	mApply.setOnClickListener(this);
    	mService.setOnClickListener(this);
    	mBanlance.setOnClickListener(this);
    	mShare.setOnClickListener(this);
    	
    	mService.setVisibility(View.GONE);
    }
    private void setView(){
    	if(mUser==null){
    		mTop.setVisibility(View.GONE);
        	mWelcome.setVisibility(View.VISIBLE);
        	mService.setVisibility(View.GONE);
    	}else{
    		mTop.setVisibility(View.VISIBLE);
    		mWelcome.setVisibility(View.GONE);
    		mTvNick.setText(mUser.getmNick());
    		ImageLoader.getInstance().displayImage(mUser.getmImage(), mHeader);
    		if("2".equals(mUser.getmType())){
    			mService.setVisibility(View.VISIBLE);
    		}else if("0".equals(mUser.getmType())){
    			mService.setVisibility(View.GONE);
    		}
    		ManagerListener.newManagerListener().notifyStartServiceListener(0);
    	}
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pcenter_home,
                container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.pcenter_collect:
			if(mUser!=null){
				intent=new Intent(mActivity,MyCollectActivity.class);
			}else{
				showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.pcenter_address:
			if(mUser!=null){
				intent=new Intent(mActivity,AddressManageActivity.class);
			}else{
				showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.pcenter_welcome: case R.id.mine_btn:
			 Intent loginIntent=new Intent(mActivity,LoginActivity.class);
			 startActivityForResult(loginIntent, Constant.LOGIN_INTENT);
			break;
		case R.id.pcenter_top:
			intent=new Intent(mActivity,PcenterInfoActivity.class);
			break;
		case R.id.pcenter_apply:
		    if(mUser!=null){
                intent=new Intent(mActivity,MyApplyActivity.class);
            }else{
                showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
            }
		    break;
		case R.id.pcenter_voucher:
		    if(mUser!=null){
                intent=new Intent(mActivity,MyVoucherActivity.class);
            }else{
                showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
            }
		    break;
		case R.id.pcenter_balance:
		    if(mUser!=null){
                intent=new Intent(mActivity,MyBalanceActivity.class);
            }else{
                showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
            }
		    break;
		case R.id.pcenter_service:
		    if(mUser!=null){
                intent=new Intent(mActivity,MyServiceActivity.class);
            }else{
                showSmartToast(R.string.login_notice, Toast.LENGTH_SHORT);
            }
		    break;
		case R.id.pcenter_share:
//			intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
//            intent.setType("text/plain"); // 分享发送的数据类型
//            intent.putExtra(Intent.EXTRA_TEXT, mActivity.getString(R.string.share_text)); // 分享的内容
//            Intent.createChooser(intent, "分享");
			initSocialSDK();
			
			setShareContent();
			
			addCustomPlatforms();
			break;
		default:
			break;
		}
		if(intent!=null){
			startActivity(intent);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				resultCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		
		if (resultCode == Constant.EXIT_SUCCESS
				|| resultCode == Constant.LOGIN_SUCCESS
				|| resultCode == Constant.REGISTER_SUCCESS) {
			mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
			setView();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
		if(mTvNick!=null){
			setView();
		}
	}
	
	/**
	 * 初始化SDK，添加一些平台
	 */
	private void initSocialSDK() {
		// 添加QQ平台
		UMQQSsoHandler qqHandler = new UMQQSsoHandler(mActivity, "1104897558",
				"TlZCzJkZxMXvMmAv");
		qqHandler.addToSocialSDK();

		// 添加QQ空间平台
		QZoneSsoHandler qzoneHandler = new QZoneSsoHandler(mActivity,
				"1104897558", "TlZCzJkZxMXvMmAv");
		qzoneHandler.addToSocialSDK();

	}
	
	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent() {

		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		mController.getConfig().registerListener(mShareListener);

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
				"1104897558", "TlZCzJkZxMXvMmAv");
		qZoneSsoHandler.addToSocialSDK();
		mController
				.setShareContent(shareContent);

		// APP ID：201874, API
		// * KEY：28401c0964f04a72a14c812d6132fcef, Secret
		// * Key：3bf66e42db1e4fa9829b955cc300b737.
//		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(getActivity(),
//				"201874", "28401c0964f04a72a14c812d6132fcef",
//				"3bf66e42db1e4fa9829b955cc300b737");
//		mController.getConfig().setSsoHandler(renrenSsoHandler);

		UMImage localImage = new UMImage(getActivity(), R.drawable.ic_launcher);
		// UMImage urlImage = new UMImage(getActivity(),
		// "http://www.umeng.com/images/pic/social/integrated_3.png");
		// UMImage resImage = new UMImage(getActivity(), R.drawable.icon);

		// // 视频分享
		// UMVideo video = new UMVideo(
		// "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
		// //
		// vedio.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
		// video.setTitle("友盟社会化组件视频");
		// video.setThumb(urlImage);

		// UMusic uMusic = new UMusic(
		// "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
		// uMusic.setAuthor("umeng");
		// uMusic.setTitle("天籁之音");
		// // uMusic.setThumb(urlImage);
		// uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");

		// UMEmoji emoji = new UMEmoji(getActivity(),
		// "http://www.pc6.com/uploadimages/2010214917283624.gif");
		// UMEmoji emoji = new UMEmoji(getActivity(),
		// "/storage/sdcard0/emoji.gif");

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent
				.setShareContent(shareContent);
		weixinContent.setTitle(shareTitle);
		weixinContent.setTargetUrl(shareUrl);
		weixinContent.setShareMedia(localImage);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia
				.setShareContent(shareContent);
		circleMedia.setTitle(shareTitle);
		circleMedia.setShareMedia(localImage);
		// circleMedia.setShareMedia(uMusic);
		// circleMedia.setShareMedia(video);
		circleMedia.setTargetUrl(shareUrl);
		mController.setShareMedia(circleMedia);

		// 设置renren分享内容
		RenrenShareContent renrenShareContent = new RenrenShareContent();
		renrenShareContent
				.setShareContent(shareContent);
		UMImage image = new UMImage(getActivity(),
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		image.setTitle(shareTitle);
		// image.setThumb("http://www.umeng.com/images/pic/social/integrated_3.png");
		renrenShareContent.setShareImage(image);
		renrenShareContent.setAppWebSite(shareUrl);
		mController.setShareMedia(renrenShareContent);

		UMImage qzoneImage = new UMImage(getActivity(),
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		qzoneImage.setTargetUrl(shareUrl);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(shareContent);
		qzone.setTargetUrl(shareUrl);
		qzone.setTitle(shareTitle);
		qzone.setShareMedia(localImage);
		// qzone.setShareMedia(uMusic);
		mController.setShareMedia(qzone);

		// video.setThumb(new UMImage(getActivity(),
		// BitmapFactory.decodeResource(
		// getResources(), R.drawable.device)));

		QQShareContent qqShareContent = new QQShareContent();
		// qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
		qqShareContent.setTitle(shareTitle);
		// qqShareContent.setShareMedia(uMusic);
		qqShareContent
				.setShareContent(shareContent);
		UMImage qqImage = new UMImage(getActivity(),
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		qqShareContent.setShareImage(qqImage);
		qqShareContent.setTargetUrl(shareUrl);
		mController.setShareMedia(qqShareContent);

		// 视频分享
		// UMVideo umVideo = new UMVideo(
		// "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
		// umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
		// umVideo.setTitle("友盟社会化组件视频");

//		TencentWbShareContent tencent = new TencentWbShareContent();
//		tencent.setShareContent(shareContent);
//		// 设置tencent分享内容
//		mController.setShareMedia(tencent);

//		// 设置邮件分享内容， 如果需要分享图片则只支持本地图片
//		MailShareContent mail = new MailShareContent(localImage);
//		mail.setTitle("生活很忙-最优质本地生活服务平台");
//		UMImage mailImage = new UMImage(getActivity(),
//				BitmapFactory.decodeResource(getResources(), R.drawable.icon));
//		mail.setShareImage(mailImage);
//		mail.setShareContent("真服务只为你，生活很忙，一指开启新生活！下载APP：http://wap.busylife.cn/");
//		// 设置tencent分享内容
//		mController.setShareMedia(mail);

		// 设置短信分享内容
		SmsShareContent sms = new SmsShareContent();
		sms.setShareContent(shareContent);
		sms.setShareImage(localImage);
		mController.setShareMedia(sms);

		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setTargetUrl(shareUrl);
		sinaContent.setTitle(shareTitle);
		sinaContent.setShareImage(localImage);
		sinaContent
				.setShareContent(shareContent);

	}
	
	/**
	 * 分享监听器
	 */
	SnsPostListener mShareListener = new SnsPostListener() {

		@Override
		public void onStart() {

		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int stCode,
				SocializeEntity entity) {
			if (stCode == 200) {
				Toast.makeText(mActivity, "分享成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mActivity, "分享失败 : error code : " + stCode,
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "1104897558";
		String appKey = "TlZCzJkZxMXvMmAv";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId,
				appKey);
		qqSsoHandler.setTargetUrl(shareUrl);
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
				appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		// String appId = "wx9aff4ab482a228ad";
		// String appSecret = "93e6ed78c6787c95bbfae619fe83e2ab";
		// String appId = "wxa8f71fa9e58da868";
		// String appSecret = "a723c8d76ce67703d7eadf18bbc16bfe";
		String appId = "wx51956473b6a4801d";
		String appSecret = "d3c4e38a1ab02fbcf1e06c30a3cfc1fd";
		UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * 添加短信平台</br>
	 */
	private void addSMS() {
		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
	}

	/**
	 * 添加Email平台</br>
//	 */
//	private void addEmail() {
//		// 添加email
//		EmailHandler emailHandler = new EmailHandler();
//		emailHandler.addToSocialSDK();
//	}

	/**
	 * 添加所有的平台</br>
	 */
	private void addCustomPlatforms() {
		// 添加微信平台
		addWXPlatform();
		// 添加QQ平台
		addQQQZonePlatform();
		// 添加短信平台
		addSMS();
		// 添加email平台
//		addEmail();

		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,SHARE_MEDIA.SINA,
				SHARE_MEDIA.SMS);
		mController.openShare(getActivity(), false);
		// SHARE_MEDIA.TENCENT SHARE_MEDIA.SINA,
	}
	
}
