package com.techfly.liutaitai.dao;

import java.util.List;

public interface IDbAdapter {
    
    
   /**
    * 
    * @param @return
    * @Description: get all attention flight list
    * @throws
    */
   public List<Persistence> getDataList();

   /**
    * @return void
    * @param @param Persistence data
    * @Description: add data task
    * @throws
    */
   public void addData(Persistence data);
   /**
    * update Persistence data
    * @return void
    * @param @param Persistence data
    * @Description:   
    * @throws
    */
   public void updateData(Persistence data);
     /**
    * delete Persistence data
    * @return List<DownloadItem>
    * @Description: delete Persistence data
    * @throws
    */
   public void deleteData(String unique);

   
   public void deleteData() ;
   

   public void addDataList(List<Persistence> data);
   
}
