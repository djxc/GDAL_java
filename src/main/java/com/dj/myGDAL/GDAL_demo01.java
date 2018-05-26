package com.dj.myGDAL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;

public class GDAL_demo01 {
	
	public static void main(String[] args) {
		GDAL_demo01 myGdal = new GDAL_demo01();
		myGdal.initGDAL();
		
		myGeometry operate_geom = new myGeometry();
//		String json_str = myGdal.readFileByLines("F:\\test\\gdal\\testShap09.json");
//		Geometry djgeom = operate_geom.create_geometry_geojson(json_str);
//		System.out.println(json_str);
		
		
		DataSource ds = ogr.Open("F:\\test\\gdal\\testShap09.json");
		operate_geom.loop_feature(ds.GetLayer(0));
		ds.delete();
		myDataSource myDB = new myDataSource();
		Layer layer = myDB.create_layer("F:/test/intersect2.shp", "result");
		myGdal.create_intersect(layer);
//		shp2json djshp2json = new shp2json();
//		djshp2json.shp_json("F:\\test\\gdal\\testShap09.json", "F:\\test\\gdal\\testShapdj.shp", "ESRI Shapefile");
//		djshp2json.shp_json("F:\\test\\gdal\\testShap09.shp", "F:\\test\\gdal\\testShap09.json");
		
//		myGdal.create_shp();
//		myGdal.read_Raster("F:/test/a.TIF");
		
	}
	
	/**
	 * 相交分析
	 * @param output_layer
	 */
	private void create_intersect(Layer output_layer){
		DataSource ds_input = ogr.Open("F:/GISData/ab_buff.shp");
		DataSource ds_intersect = ogr.Open("F:/GISData/aa_buffer.shp");
		Layer layer = ds_input.GetLayer(0);
		Layer intersect = ds_intersect.GetLayer(0);
		layer.Intersection(intersect, output_layer);
	}
	
	/** 
     * 以行为单位读取文件，常用于读面向行的格式化文件 
     */  
    public String readFileByLines(String fileName) {  
        File file = new File(fileName);  
        BufferedReader reader = null;  
        StringBuffer sb = new StringBuffer();
        try {  
            System.out.println("以行为单位读取文件内容，一次读一整行：");  
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));  
            String tempString = null;  
            int line = 1;  
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {  
                sb.append(tempString + "\n");
                line++;  
            }  
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }
		return sb.toString();  
    }  
	
	
	private String read_File(String path) {
		byte[] b = new byte[5];
		char[] chars = new char[30];
		try{
			Reader reader = new InputStreamReader(new FileInputStream(path));			
			int index = 0;
			int i = 0;
			while((i = reader.read(chars))!=-1){
//				b[index] = (byte) i;
//				index++;
				System.out.println((char)i);		
			}
			reader.close();
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return new String(b);
	}
	
	
	/**
	 * 读取栅格数据，获取行列数
	 * @param fileName_tif	栅格文件路径名称
	 */
	private void read_Raster(String fileName_tif){
       
        Dataset ds = gdal.Open(fileName_tif, gdalconst.GA_ReadOnly);  
          
        int w = ds.getRasterXSize();  
        int h = ds.getRasterYSize();  
          
        System.out.println("Width: " + w + "," + "Height: " + h);  
	}
	
	/**
	 * 对GDAL进行配置，注册驱动，设置中文支持
	 */
	private void initGDAL(){
		ogr.RegisterAll();	// 注册所有驱动
		 // 为了支持中文路径，请添加下面这句代码  
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","yes");  
        // 为了使属性表字段支持中文，请添加下面这句  
        gdal.SetConfigOption("SHAPE_ENCODING","CP936");  
	}
	
	/**
	 * 1.获取驱动，要创建的文件的类型的驱动（这里为ESRI Shapefile）
	 * 2.创建数据源，要指定文件路径
	 * 3.根据数据源创建图层，定义图层名称、空间参考、几何类型等
	 * 4.创建属性表，创建字段：指定字段的名称、类型、长度
	 * 5.创建要素，指定图层、属性表各个字段的值，几何信息
	 * 6.图层数据源的写入
	 */
	private void create_shp(){
		String strVectorFile ="F:\\test\\gdal\\testShap09.shp";                           
  
        //创建数据，这里以创建ESRI的shp文件为例  
        String strDriverName = "ESRI Shapefile";  
        org.gdal.ogr.Driver oDriver =ogr.GetDriverByName(strDriverName);  
        if (oDriver == null)  
        {          	
	        System.out.println(strVectorFile+ " 驱动不可用！\n");  
	        return;  
        }  
  
        // 创建数据源  
        DataSource oDS = oDriver.CreateDataSource(strVectorFile,null);  
        if (oDS == null)  
        {  
              System.out.println("创建矢量文件【"+ strVectorFile +"】失败！\n" );  
              return;  
        }  
  
        // 创建图层，创建一个多边形图层，这里没有指定空间参考，如果需要的话，需要在这里进行指定  
        Layer oLayer =oDS.CreateLayer("TestPolygon", null, ogr.wkbPolygon, null);  
        
        if (oLayer == null)  
        {  
              System.out.println("图层创建失败！\n");  
              return;  
        }  
  
        // 下面创建属性表  
        // 先创建一个叫FieldID的整型属性  
        create_Table("FieldID", ogr.OFTInteger, -1, oLayer);         
        create_Table("FieldName", ogr.OFTString, 100, oLayer);              
  
        FeatureDefn oDefn =oLayer.GetLayerDefn();  
  
        // 创建三角形要素  
        create_feature(oLayer, oDefn, 0, "三角形", "POLYGON ((0 0,20 0,10 15,0 0))");        
  
        // 创建矩形要素  
        create_feature(oLayer, oDefn, 1, "矩形", "POLYGON ((30 0,60 0,60 30,30 30,30 0))");        
  
        // 创建五角形要素  
        create_feature(oLayer, oDefn, 2, "五角星", "POLYGON ((70 0,85 0,90 15,80 30,65 15,70 0))");
          
        //写入文件  
        oLayer.SyncToDisk();  
        oDS.SyncToDisk();  
  
        System.out.println("\n数据集创建完成！\n");  
	}
	
	/**
	 * 创建要素
	 * @param oLayer	要素所属的图层
	 * @param oDefn		要素的定义初始化，借此创建要素
	 * @param field1	字段
	 * @param field2	字段
	 * @param geoInfo	几何信息，由此创建几何要素
	 */
	private void create_feature(Layer oLayer, FeatureDefn oDefn, int field1, String field2, String geoInfo){
		Feature oFeature = new Feature(oDefn);  
        oFeature.SetField(0, field1);  
        oFeature.SetField(1, field2);  
        Geometry geomtry =Geometry.CreateFromWkt(geoInfo);  
        oFeature.SetGeometry(geomtry);  
  
        oLayer.CreateFeature(oFeature);  
	}
	
	/**
	 * 属性表添加字段
	 * @param fieldName 字段名称
	 * @param type	字段类型
	 * @param width	字段长度，-1为默认长度
	 * @param oLayer	属性表所属的图层
	 */
	private void create_Table(String fieldName, int type, int width, Layer oLayer){
		FieldDefn oField = new FieldDefn(fieldName, type);
		if(width>0){			
			oField.SetWidth(width);
		}
        oLayer.CreateField(oField, 1);  
	}
	
}
