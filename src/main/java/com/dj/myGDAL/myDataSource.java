package com.dj.myGDAL;

import org.gdal.ogr.DataSource;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;

public class myDataSource {
	
	public myDataSource(){
		
	}
	
	public Layer create_layer(String fileName, String layerName){
		 //�������ݣ������Դ���ESRI��shp�ļ�Ϊ��  
       String strDriverName = "ESRI Shapefile";  
       org.gdal.ogr.Driver oDriver =ogr.GetDriverByName(strDriverName);  
       if (oDriver == null)  
       {          	
	        System.out.println(fileName+ " ���������ã�\n");  
	        return null;  
       }  
 
       // ��������Դ  
       DataSource oDS = oDriver.CreateDataSource(fileName,null);  
       if (oDS == null)  
       {  
             System.out.println("����ʸ���ļ���"+ fileName +"��ʧ�ܣ�\n" );  
             return null;  
       }  
 
       // ����ͼ�㣬����һ�������ͼ�㣬����û��ָ���ռ�ο��������Ҫ�Ļ�����Ҫ���������ָ��  
       Layer oLayer =oDS.CreateLayer(layerName, null, ogr.wkbPolygon, null);  
       
       if (oLayer == null)  
       {  
             System.out.println("ͼ�㴴��ʧ�ܣ�\n");  
             return null;  
       }  
       return oLayer;
	}
	
	
}
