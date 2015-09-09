package com.techfly.liutaitai.util;

import java.util.ArrayList;
import java.util.List;

import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.pcenter.bean.Area;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;


public class ManagerListener {
	public interface CityUpdateListener{
		void onUpdateListener(Area area);
	}
	public interface AddressListener{
		void onDeleteListener(boolean b);
		void onSelectListener(int type,String id);
		void onDefaultListener(boolean b,String id);
	}
	public interface OrderPayListener{
		void onOrderPayListener(TechOrder order);
	}
	public interface OrderDeleteListener{
		void onOrderDeleteListener(TechOrder order);
	}
	public interface OrderCancelListener{
		void onOrderCancelListener(TechOrder order);
	}
	public interface OrderRateListener{
		void onOrderRateListener(TechOrder order);
	}
	public interface OrderLogiticsListener{
		void onOrderLogiticsListener(TechOrder order);
	}
	public interface spinnerClickListener{
		void onClickNotify(String time);
	}
	
	public interface ServiceClickListener{
		void onServiceDeleteListener(Service service);
		void onServicePayListener(Service service);
		void onServiceCancelListener(Service service);
		void onServiceAgainListener(Service service);
		void onServiceRateListener(Service service);
		void onServiceRefreshListener();
	}
	
	
	private List<CityUpdateListener> mCityUpdateListeners=new ArrayList<CityUpdateListener>();
	private List<AddressListener> mAddressListeners=new ArrayList<AddressListener>();
	private List<OrderCancelListener> mCancelListeners=new ArrayList<OrderCancelListener>();
	private List<OrderDeleteListener> mDeleteListeners=new ArrayList<OrderDeleteListener>();
	private List<OrderLogiticsListener> mLogiticsListeners=new ArrayList<OrderLogiticsListener>();
	private List<OrderPayListener> mPayListeners=new ArrayList<OrderPayListener>();
	private List<OrderRateListener> mRateListeners=new ArrayList<OrderRateListener>();
	private List<spinnerClickListener> mSpinnerClickListener=new ArrayList<spinnerClickListener>();
	private List<ServiceClickListener> mServiceClickListeners = new ArrayList<ManagerListener.ServiceClickListener>();
	private static ManagerListener mListener;
	private static Object object = new Object();
    private ManagerListener(){
        
    }
    
    public static ManagerListener newManagerListener(){
        if(mListener==null){
            synchronized (object) {
                if(null==mListener){
                	mListener = new ManagerListener();
                }
            }
        }
        return mListener;
    }
    public void onRegisterCityUpdateListener(CityUpdateListener cityUpdateListener){
    	if(!mCityUpdateListeners.contains(cityUpdateListener)){
    		mCityUpdateListeners.add(cityUpdateListener);
    	}
    }
    public void onUnRegisterCityUpdateListener(CityUpdateListener cityUpdateListener){
    	if(mCityUpdateListeners.contains(cityUpdateListener)){
    		mCityUpdateListeners.remove(cityUpdateListener);
    	}
    }
    public void onRegisterAddressListener(AddressListener addressListener){
    	if(!mAddressListeners.contains(addressListener)){
    		mAddressListeners.add(addressListener);
    	}
    }
    public void onUnRegisterAddressListener(AddressListener addressListener){
    	if(mAddressListeners.contains(addressListener)){
    		mAddressListeners.remove(addressListener);
    	}
    }
    public void onRegisterOrderLogiticsListener(OrderLogiticsListener logiticsListener){
    	if(!mLogiticsListeners.contains(logiticsListener)){
    		mLogiticsListeners.add(logiticsListener);
    	}
    }
    public void onUnRegisterOrderLogiticsListener(OrderLogiticsListener logiticsListener){
    	if(mLogiticsListeners.contains(logiticsListener)){
    		mLogiticsListeners.remove(logiticsListener);
    	}
    }
    public void onRegisterOrderPayListener(OrderPayListener payListener){
    	if(!mPayListeners.contains(payListener)){
    		mPayListeners.add(payListener);
    	}
    }
    public void onUnRegisterOrderPayListener(OrderPayListener payListener){
    	if(mPayListeners.contains(payListener)){
    		mPayListeners.remove(payListener);
    	}
    }
    public void onRegisterOrderRateListener(OrderRateListener rateListener){
    	if(!mRateListeners.contains(rateListener)){
    		mRateListeners.add(rateListener);
    	}
    }
    public void onUnRegisterOrderRateListener(OrderRateListener rateListener){
    	if(mRateListeners.contains(rateListener)){
    		mRateListeners.remove(rateListener);
    	}
    }
    public void onRegisterOrderCancelListener(OrderCancelListener cancelListener){
    	if(!mCancelListeners.contains(cancelListener)){
    		mCancelListeners.add(cancelListener);
    	}
    }
    public void onUnRegisterOrderCancelListener(OrderCancelListener cancelListener){
    	if(mCancelListeners.contains(cancelListener)){
    		mCancelListeners.remove(cancelListener);
    	}
    }
    public void onRegisterOrderDeleteListener(OrderDeleteListener deleteListener){
    	if(!mDeleteListeners.contains(deleteListener)){
    		mDeleteListeners.add(deleteListener);
    	}
    }
    public void onUnRegisterOrderDeleteListener(OrderDeleteListener deleteListener){
    	if(mDeleteListeners.contains(deleteListener)){
    		mDeleteListeners.remove(deleteListener);
    	}
    }
    public void onRegisterSpinnerClickListener(spinnerClickListener spinnerClickListener){
    	if(!mSpinnerClickListener.contains(spinnerClickListener)){
    		mSpinnerClickListener.add(spinnerClickListener);
    	}
    }
    public void onUnRegisterSpinnerClickListener(spinnerClickListener spinnerClickListener){
    	if(mSpinnerClickListener.contains(spinnerClickListener)){
    		mSpinnerClickListener.remove(spinnerClickListener);
    	}
    }
    public void onRegisterServiceClickListener(ServiceClickListener serviceClickListener){
    	if(!mServiceClickListeners.contains(serviceClickListener)){
    		mServiceClickListeners.add(serviceClickListener);
    	}
    }
    public void onUnRegisterServiceClickListener(ServiceClickListener serviceClickListener){
    	if(mServiceClickListeners.contains(serviceClickListener)){
    		mServiceClickListeners.remove(serviceClickListener);
    	}
    }
    public void notifySpinnerClickListener(String time){
    	int size=mSpinnerClickListener.size();
    	for(int i=0;i<size;i++){
    		mSpinnerClickListener.get(i).onClickNotify(time);
    	}
    }
   
    public void notifyUpdateList(Area area){
    	int size=mCityUpdateListeners.size();
    	for(int i=0;i<size;i++){
    		mCityUpdateListeners.get(i).onUpdateListener(area);
    	}
    }
    public void notifyAddressListener(boolean b){
    	int size=mAddressListeners.size();
    	for(int i=0;i<size;i++){
    		mAddressListeners.get(i).onDeleteListener(b);
    	}
    }
    public void notifySelectListener(int type,String id){
    	int size=mAddressListeners.size();
    	for(int i=0;i<size;i++){
    		mAddressListeners.get(i).onSelectListener(type,id);
    	}
    }
    public void notifyDefaultListener(boolean b,String id){
    	int size=mAddressListeners.size();
    	for(int i=0;i<size;i++){
    		mAddressListeners.get(i).onDefaultListener(b,id);
    	}
    }
    public void notifyOrderDeleteListener(TechOrder order){
    	int size=mDeleteListeners.size();
    	for(int i=0;i<size;i++){
    		mDeleteListeners.get(i).onOrderDeleteListener(order);
    	}
    }
    public void notifyOrderCancelListener(TechOrder order){
    	int size=mCancelListeners.size();
    	for(int i=0;i<size;i++){
    		mCancelListeners.get(i).onOrderCancelListener(order);
    	}
    }
    public void notifyOrderPayListener(TechOrder order){
    	int size=mPayListeners.size();
    	for(int i=0;i<size;i++){
    		mPayListeners.get(i).onOrderPayListener(order);
    	}
    }
    public void notifyOrderRateListener(TechOrder order){
    	int size=mRateListeners.size();
    	for(int i=0;i<size;i++){
    		mRateListeners.get(i).onOrderRateListener(order);
    	}
    }
    public void notifyOrderLogiticsListener(TechOrder order){
    	int size=mLogiticsListeners.size();
    	for(int i=0;i<size;i++){
    		mLogiticsListeners.get(i).onOrderLogiticsListener(order);
    	}
    }
    public void notifyServiceDeleteListener(Service service){
    	int size = mServiceClickListeners.size();
    	for(int i=0; i<size; i++){
    		mServiceClickListeners.get(i).onServiceDeleteListener(service);
    	}
    }
    public void notifyServicePayListener(Service service){
    	int size = mServiceClickListeners.size();
    	for(int i=0; i<size; i++){
    		mServiceClickListeners.get(i).onServicePayListener(service);
    	}
    }
    public void notifyServiceCancelListener(Service service){
    	int size = mServiceClickListeners.size();
    	for(int i=0; i<size; i++){
    		mServiceClickListeners.get(i).onServiceCancelListener(service);
    	}
    }
    public void notifyServiceAgainListener(Service service){
    	int size = mServiceClickListeners.size();
    	for(int i=0; i<size; i++){
    		mServiceClickListeners.get(i).onServiceAgainListener(service);
    	}
    }
    public void notifyServiceRateListener(Service service){
    	int size = mServiceClickListeners.size();
    	for(int i=0; i<size; i++){
    		mServiceClickListeners.get(i).onServiceRateListener(service);
    	}
    }
    public void notifyServiceRefreshListener(){
    	int size = mServiceClickListeners.size();
    	for(int i=0; i<size; i++){
    		mServiceClickListeners.get(i).onServiceRefreshListener();
    	}
    }
    
}
