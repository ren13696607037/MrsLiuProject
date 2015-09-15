package com.techfly.liutaitai.bizz.paymanage;

import com.techfly.liutaitai.util.Constant;

public class PayImplFactory {
  public static PayImplFactory mFactory;
  private static Object object = new Object();
    
  private PayImplFactory(){
      
  }
  
  public static PayImplFactory getInstance(){
      if(mFactory ==null){
          synchronized (object) {
              mFactory = new PayImplFactory();
        }
      }
      return mFactory;
  }
  
  public PayInterface getPayImpl(int payMethod){
      PayInterface payImpl;
      if(payMethod==Constant.PAY_ALIPAY){
          payImpl = new AlipayImpl();
      }else if(payMethod==Constant.PAY_BALANCE){
          payImpl = new BalancePayImpl();
      }else if(payMethod==Constant.PAY_WENXIN){
          payImpl = new WenxinPayImpl();
      }else{
          payImpl = new OffLinePayImpl();
      }
      return payImpl;
  }
}
