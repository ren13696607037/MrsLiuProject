package com.hylsmart.yihui.util;


public interface RequestParamConfig {

    String PRINCIPAL = "principal";
    String PAGE = "page";
    String ORDER = "order";//排序 0-默认排序 1-销量 2-价格 3-上架时间
    String SORT = "sort";//0-降序排列 1-升序排列
    String NAME = "name";//商品名 当传入此参数时候，查询的是所有分类下的商品    
    String FID = "fid";//分类id
    String COMMODITY_ID = "commodityId";//idS
    String GOODS = "buyInfo";
    String VERSION = "code";
   
}
