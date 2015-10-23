package com.techfly.liutaitai.util;

import java.util.ArrayList;
import java.util.List;

import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.pcenter.bean.Area;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;

public class ManagerListener {
	public interface StartServiceListener{
		void onStartService();
	}
	public interface CityUpdateListener {
		void onUpdateListener(Area area);
	}

	public interface AddressListener {
		void onDeleteListener(boolean b);

		void onSelectListener(int type, String id);

		void onDefaultListener(boolean b, String id);
	}

	public interface OrderPayListener {// 技师联系客服
		void onOrderPayListener(TechOrder order);
	}

	public interface OrderDeleteListener {// 技师删除订单
		void onOrderDeleteListener(TechOrder order);
	}

	public interface OrderCancelListener {// 技师拒单
		void onOrderCancelListener(TechOrder order);
	}

	public interface OrderRateListener {// 技师完成服务
		void onOrderRateListener(TechOrder order);
	}

	public interface OrderLogiticsListener {// 技师开始服务
		void onOrderLogiticsListener(TechOrder order);
	}

	public interface OrderTakeListener {// 技师接单
		void onOrderTakeListener(TechOrder order);
	}

	public interface OrderDetailListener {
		void onOrderDetailDeleteListener(TechOrder order);

		void onOrderDetailCancelListener(TechOrder order);

		void onOrderDetailRateListener(TechOrder order);

		void onOrderDetailLogiticsListener(TechOrder order);

		void onOrderDetailTakeListener(TechOrder order);

		void onOrderDetailPayListener(TechOrder order);

		void onDetailCamera();

		void onDetailPhoto();

		void onDetailSubmit(String url);
	}

	public interface spinnerClickListener {
		void onClickNotify(String time);
	}

	public interface ServiceClickListener {
		void onServiceDeleteListener(Service service);

		void onServicePayListener(Service service);

		void onServiceCancelListener(Service service);

		void onServiceAgainListener(Service service);

		void onServiceRateListener(Service service);

		void onServiceRefreshListener();
	}
	public interface ServiceDetailClickListener {
		void onServiceDetailDeleteListener(Service service);

		void onServiceDetailPayListener(Service service);

		void onServiceDetailCancelListener(Service servicee);

		void onServiceDetailAgainListener(Service service);

		void onServiceDetailRateListener(Service service);

		void onServiceDetailRefreshListener();
	}

	public interface TechFinishDialogListener {
		void onCamera();

		void onPhoto();

		void onSubmit(String url);
	}

	public interface CollectListener {
		void cancelCollect(Product product);

		void collect(Product product);
	}

	private List<CityUpdateListener> mCityUpdateListeners = new ArrayList<CityUpdateListener>();
	private List<AddressListener> mAddressListeners = new ArrayList<AddressListener>();
	private List<OrderCancelListener> mCancelListeners = new ArrayList<OrderCancelListener>();
	private List<OrderDeleteListener> mDeleteListeners = new ArrayList<OrderDeleteListener>();
	private List<OrderLogiticsListener> mLogiticsListeners = new ArrayList<OrderLogiticsListener>();
	private List<OrderPayListener> mPayListeners = new ArrayList<OrderPayListener>();
	private List<OrderRateListener> mRateListeners = new ArrayList<OrderRateListener>();
	private List<OrderTakeListener> mTakeListeners = new ArrayList<OrderTakeListener>();
	private List<spinnerClickListener> mSpinnerClickListener = new ArrayList<spinnerClickListener>();
	private List<ServiceClickListener> mServiceClickListeners = new ArrayList<ManagerListener.ServiceClickListener>();
	private List<TechFinishDialogListener> mDialogListeners = new ArrayList<ManagerListener.TechFinishDialogListener>();
	private List<CollectListener> mCollectListeners = new ArrayList<ManagerListener.CollectListener>();
	private List<OrderDetailListener> mOrderDetailListeners = new ArrayList<ManagerListener.OrderDetailListener>();
	private List<StartServiceListener> mStartServiceListeners = new ArrayList<ManagerListener.StartServiceListener>();
	private List<ServiceDetailClickListener> mServiceDetailClickListeners = new ArrayList<ManagerListener.ServiceDetailClickListener>();
	private static ManagerListener mListener;
	private static Object object = new Object();

	private ManagerListener() {

	}

	public static ManagerListener newManagerListener() {
		if (mListener == null) {
			synchronized (object) {
				if (null == mListener) {
					mListener = new ManagerListener();
				}
			}
		}
		return mListener;
	}
	public void onRegisterServiceDetailClickListener(
			ServiceDetailClickListener serviceDetailClickListener) {
		if (!mServiceDetailClickListeners.contains(serviceDetailClickListener)) {
			mServiceDetailClickListeners.add(serviceDetailClickListener);
		}
	}

	public void onUnRegisterServiceDetailClickListener(
			ServiceDetailClickListener serviceDetailClickListener) {
		if (mServiceDetailClickListeners.contains(serviceDetailClickListener)) {
			mServiceDetailClickListeners.remove(serviceDetailClickListener);
		}
	}
	public void onRegisterStartServiceListener(
			StartServiceListener startServiceListener) {
		if (!mStartServiceListeners.contains(startServiceListener)) {
			mStartServiceListeners.add(startServiceListener);
		}
	}

	public void onUnRegisterStartServiceListener(
			StartServiceListener startServiceListener) {
		if (mStartServiceListeners.contains(startServiceListener)) {
			mStartServiceListeners.remove(startServiceListener);
		}
	}

	public void onRegisterOrderDetailListener(
			OrderDetailListener orderDetailListener) {
		if (!mOrderDetailListeners.contains(orderDetailListener)) {
			mOrderDetailListeners.add(orderDetailListener);
		}
	}

	public void onUnRegisterOrderDetailListener(
			OrderDetailListener orderDetailListener) {
		if (mOrderDetailListeners.contains(orderDetailListener)) {
			mOrderDetailListeners.remove(orderDetailListener);
		}
	}

	public void onRegisterCollectListener(CollectListener collectListener) {
		if (!mCollectListeners.contains(collectListener)) {
			mCollectListeners.add(collectListener);
		}
	}

	public void onUnRegisterCollectListener(CollectListener collectListener) {
		if (mCollectListeners.contains(collectListener)) {
			mCollectListeners.remove(collectListener);
		}
	}
	public void onRegisterTechFinishDialogListener(
			TechFinishDialogListener dialogListener) {
		if (!mDialogListeners.contains(dialogListener)) {
			mDialogListeners.add(dialogListener);
		}
	}
	public void onUnRegisterTechFinishDialogListener(
			TechFinishDialogListener dialogListener) {
		if (mDialogListeners.contains(dialogListener)) {
			mDialogListeners.remove(dialogListener);
		}
	}

	public void onRegisterCityUpdateListener(
			CityUpdateListener cityUpdateListener) {
		if (!mCityUpdateListeners.contains(cityUpdateListener)) {
			mCityUpdateListeners.add(cityUpdateListener);
		}
	}

	public void onUnRegisterCityUpdateListener(
			CityUpdateListener cityUpdateListener) {
		if (mCityUpdateListeners.contains(cityUpdateListener)) {
			mCityUpdateListeners.remove(cityUpdateListener);
		}
	}

	public void onRegisterAddressListener(AddressListener addressListener) {
		if (!mAddressListeners.contains(addressListener)) {
			mAddressListeners.add(addressListener);
		}
	}

	public void onUnRegisterAddressListener(AddressListener addressListener) {
		if (mAddressListeners.contains(addressListener)) {
			mAddressListeners.remove(addressListener);
		}
	}
	public void onRegisterOrderLogiticsListener(
			OrderLogiticsListener logiticsListener) {
		if (!mLogiticsListeners.contains(logiticsListener)) {
			mLogiticsListeners.add(logiticsListener);
		}
	}
	public void onUnRegisterOrderLogiticsListener(
			OrderLogiticsListener logiticsListener) {
		if (mLogiticsListeners.contains(logiticsListener)) {
			mLogiticsListeners.remove(logiticsListener);
		}
	}

	public void onRegisterOrderPayListener(OrderPayListener payListener) {
		if (!mPayListeners.contains(payListener)) {
			mPayListeners.add(payListener);
		}
	}

	public void onUnRegisterOrderPayListener(OrderPayListener payListener) {
		if (mPayListeners.contains(payListener)) {
			mPayListeners.remove(payListener);
		}
	}
	public void onRegisterOrderRateListener(OrderRateListener rateListener) {
		if (!mRateListeners.contains(rateListener)) {
			mRateListeners.add(rateListener);
		}
	}
	public void onUnRegisterOrderRateListener(OrderRateListener rateListener) {
		if (mRateListeners.contains(rateListener)) {
			mRateListeners.remove(rateListener);
		}
	}
	public void onRegisterOrderCancelListener(
			OrderCancelListener cancelListener) {
		if (!mCancelListeners.contains(cancelListener)) {
			mCancelListeners.add(cancelListener);
		}
	}
	public void onUnRegisterOrderCancelListener(
			OrderCancelListener cancelListener) {
		if (mCancelListeners.contains(cancelListener)) {
			mCancelListeners.remove(cancelListener);
		}
	}
	public void onRegisterOrderDeleteListener(
			OrderDeleteListener deleteListener) {
		if (!mDeleteListeners.contains(deleteListener)) {
			mDeleteListeners.add(deleteListener);
		}
	}
	public void onUnRegisterOrderDeleteListener(
			OrderDeleteListener deleteListener) {
		if (mDeleteListeners.contains(deleteListener)) {
			mDeleteListeners.remove(deleteListener);
		}
	}
	public void onRegisterOrderTakeListener(OrderTakeListener takeListener) {
		if (!mTakeListeners.contains(takeListener)) {
			mTakeListeners.add(takeListener);
		}
	}

	public void onUnRegisterOrderTakeListener(OrderTakeListener takeListener) {
		if (mTakeListeners.contains(takeListener)) {
			mTakeListeners.remove(takeListener);
		}
	}

	public void onRegisterSpinnerClickListener(
			spinnerClickListener spinnerClickListener) {
		if (!mSpinnerClickListener.contains(spinnerClickListener)) {
			mSpinnerClickListener.add(spinnerClickListener);
		}
	}

	public void onUnRegisterSpinnerClickListener(
			spinnerClickListener spinnerClickListener) {
		if (mSpinnerClickListener.contains(spinnerClickListener)) {
			mSpinnerClickListener.remove(spinnerClickListener);
		}
	}

	public void onRegisterServiceClickListener(
			ServiceClickListener serviceClickListener) {
		if (!mServiceClickListeners.contains(serviceClickListener)) {
			mServiceClickListeners.add(serviceClickListener);
		}
	}

	public void onUnRegisterServiceClickListener(
			ServiceClickListener serviceClickListener) {
		if (mServiceClickListeners.contains(serviceClickListener)) {
			mServiceClickListeners.remove(serviceClickListener);
		}
	}

	public void notifySpinnerClickListener(String time) {
		int size = mSpinnerClickListener.size();
		for (int i = 0; i < size; i++) {
			mSpinnerClickListener.get(i).onClickNotify(time);
		}
	}

	public void notifyUpdateList(Area area) {
		int size = mCityUpdateListeners.size();
		for (int i = 0; i < size; i++) {
			mCityUpdateListeners.get(i).onUpdateListener(area);
		}
	}

	public void notifyAddressListener(boolean b) {
		int size = mAddressListeners.size();
		for (int i = 0; i < size; i++) {
			mAddressListeners.get(i).onDeleteListener(b);
		}
	}

	public void notifySelectListener(int type, String id) {
		int size = mAddressListeners.size();
		for (int i = 0; i < size; i++) {
			mAddressListeners.get(i).onSelectListener(type, id);
		}
	}

	public void notifyDefaultListener(boolean b, String id) {
		int size = mAddressListeners.size();
		for (int i = 0; i < size; i++) {
			mAddressListeners.get(i).onDefaultListener(b, id);
		}
	}

	 public void notifyOrderDeleteListener(TechOrder order){
	 int size=mDeleteListeners.size();
	 for(int i=0;i<size;i++){
	 mDeleteListeners.get(i).onOrderDeleteListener(order);
	 }
	 }
	 public void notifyOrderRateListener(TechOrder order){
		 int size=mRateListeners.size();
		 for(int i=0;i<size;i++){
			 mRateListeners.get(i).onOrderRateListener(order);
		 }
		 }

	 public void notifyOrderCancelListener(TechOrder order){
	 int size=mCancelListeners.size();
	 for(int i=0;i<size;i++){
	 mCancelListeners.get(i).onOrderCancelListener(order);
	 }
	 }

	public void notifyOrderPayListener(TechOrder order) {
		int size = mPayListeners.size();
		for (int i = 0; i < size; i++) {
			mPayListeners.get(i).onOrderPayListener(order);
		}
	}


	 public void notifyOrderLogiticsListener(TechOrder order){
	 int size=mLogiticsListeners.size();
	 for(int i=0;i<size;i++){
	 mLogiticsListeners.get(i).onOrderLogiticsListener(order);
	 }
	 }

	 public void notifyOrderTakeListener(TechOrder order){
	 int size=mTakeListeners.size();
	 for(int i=0;i<size;i++){
	 mTakeListeners.get(i).onOrderTakeListener(order);
	 }
	 }

	public void notifyServiceDeleteListener(Service service) {
		int size = mServiceClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceClickListeners.get(i).onServiceDeleteListener(service);
		}
	}

	public void notifyServicePayListener(Service service) {
		int size = mServiceClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceClickListeners.get(i).onServicePayListener(service);
		}
	}

	public void notifyServiceCancelListener(Service service) {
		int size = mServiceClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceClickListeners.get(i).onServiceCancelListener(service);
		}
	}

	public void notifyServiceAgainListener(Service service) {
		int size = mServiceClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceClickListeners.get(i).onServiceAgainListener(service);
		}
	}

	public void notifyServiceRateListener(Service service) {
		int size = mServiceClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceClickListeners.get(i).onServiceRateListener(service);
		}
	}

	public void notifyServiceRefreshListener() {
		int size = mServiceClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceClickListeners.get(i).onServiceRefreshListener();
		}
	}
	public void notifyServiceDetailDeleteListener(Service service) {
		int size = mServiceDetailClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceDetailClickListeners.get(i).onServiceDetailDeleteListener(service);
		}
	}

	public void notifyServiceDetailPayListener(Service service) {
		int size = mServiceDetailClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceDetailClickListeners.get(i).onServiceDetailPayListener(service);
		}
	}

	public void notifyServiceDetailCancelListener(Service service) {
		int size = mServiceDetailClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceDetailClickListeners.get(i).onServiceDetailCancelListener(service);
		}
	}

	public void notifyServiceDetailAgainListener(Service service) {
		int size = mServiceDetailClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceDetailClickListeners.get(i).onServiceDetailAgainListener(service);
		}
	}

	public void notifyServiceDetailRateListener(Service service) {
		int size = mServiceDetailClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceDetailClickListeners.get(i).onServiceDetailRateListener(service);
		}
	}

	public void notifyServiceDetailRefreshListener() {
		int size = mServiceDetailClickListeners.size();
		for (int i = 0; i < size; i++) {
			mServiceDetailClickListeners.get(i).onServiceDetailRefreshListener();
		}
	}

	public void notifyDialogCameraListener() {
		int size = mDialogListeners.size();
		for (int i = 0; i < size; i++) {
			mDialogListeners.get(i).onCamera();
		}
	}

	public void notifyDialogPhotoListener() {
		int size = mDialogListeners.size();
		for (int i = 0; i < size; i++) {
			mDialogListeners.get(i).onPhoto();
		}
	}

	public void notifyDialogSubmitListener(String url) {
		int size = mDialogListeners.size();
		for (int i = 0; i < size; i++) {
			mDialogListeners.get(i).onSubmit(url);
		}
	}

	public void notifyCollectListener(Product product) {
		int size = mCollectListeners.size();
		for (int i = 0; i < size; i++) {
			mCollectListeners.get(i).collect(product);
		}
	}

	public void notifyCancelCollectListener(Product product) {
		int size = mCollectListeners.size();
		for (int i = 0; i < size; i++) {
			mCollectListeners.get(i).cancelCollect(product);
		}
	}

	public void notifyDetailOrderDeleteListener(TechOrder order) {
		int size = mOrderDetailListeners.size();
		for (int i = 0; i < size; i++) {
			mOrderDetailListeners.get(i).onOrderDetailDeleteListener(order);
		}
	}

	public void notifyDetailOrderCancelListener(TechOrder order) {
		int size = mOrderDetailListeners.size();
		for (int i = 0; i < size; i++) {
			mOrderDetailListeners.get(i).onOrderDetailCancelListener(order);
		}
	}

	public void notifyDetailOrderPayListener(TechOrder order) {
		int size = mOrderDetailListeners.size();
		for (int i = 0; i < size; i++) {
			mOrderDetailListeners.get(i).onOrderDetailPayListener(order);
		}
	}

	public void notifyDetailOrderRateListener(TechOrder order) {
		int size = mOrderDetailListeners.size();
		for (int i = 0; i < size; i++) {
			mOrderDetailListeners.get(i).onOrderDetailRateListener(order);
		}
	}

	public void notifyDetailOrderLogiticsListener(TechOrder order) {
		int size = mOrderDetailListeners.size();
		for (int i = 0; i < size; i++) {
			mOrderDetailListeners.get(i).onOrderDetailLogiticsListener(order);
		}
	}

	public void notifyDetailOrderTakeListener(TechOrder order) {
		int size = mOrderDetailListeners.size();
		for (int i = 0; i < size; i++) {
			mOrderDetailListeners.get(i).onOrderDetailTakeListener(order);
		}
	}

	public void notifyDetailDialogCameraListener() {
		int size = mOrderDetailListeners.size();
		for (int i = 0; i < size; i++) {
			mOrderDetailListeners.get(i).onDetailCamera();
		}
	}

	public void notifyDetailDialogPhotoListener() {
		int size = mOrderDetailListeners.size();
		for (int i = 0; i < size; i++) {
			mOrderDetailListeners.get(i).onDetailPhoto();
		}
	}

	public void notifyDetailDialogSubmitListener(String url) {
		int size = mOrderDetailListeners.size();
		for (int i = 0; i < size; i++) {
			mOrderDetailListeners.get(i).onDetailSubmit(url);
		}
	}
	public void notifyStartServiceListener(){
		int size = mStartServiceListeners.size();
		for(int i = 0; i < size; i++){
			mStartServiceListeners.get(i).onStartService();
		}
	}

}
