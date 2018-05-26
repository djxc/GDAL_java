package com.dj.myGDAL;

import org.gdal.ogr.DataSource;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;

public class myDataSource {
	
	public myDataSource(){
		
	}
	
	public Layer create_layer(String fileName, String layerName){
		 //创建数据，这里以创建ESRI的shp文件为例  
       String strDriverName = "ESRI Shapefile";  
       org.gdal.ogr.Driver oDriver =ogr.GetDriverByName(strDriverName);  
       if (oDriver == null)  
       {          	
	        System.out.println(fileName+ " 驱动不可用！\n");  
	        return null;  
       }  
 
       // 创建数据源  
       DataSource oDS = oDriver.CreateDataSource(fileName,null);  
       if (oDS == null)  
       {  
             System.out.println("创建矢量文件【"+ fileName +"】失败！\n" );  
             return null;  
       }  
 
       // 创建图层，创建一个多边形图层，这里没有指定空间参考，如果需要的话，需要在这里进行指定  
       Layer oLayer =oDS.CreateLayer(layerName, null, ogr.wkbPolygon, null);  
       
       if (oLayer == null)  
       {  
             System.out.println("图层创建失败！\n");  
             return null;  
       }  
       return oLayer;
	}
	
	
}
