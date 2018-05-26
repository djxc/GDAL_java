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
	 * �ཻ����
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
     * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ� 
     */  
    public String readFileByLines(String fileName) {  
        File file = new File(fileName);  
        BufferedReader reader = null;  
        StringBuffer sb = new StringBuffer();
        try {  
            System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");  
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));  
            String tempString = null;  
            int line = 1;  
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����  
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
	 * ��ȡդ�����ݣ���ȡ������
	 * @param fileName_tif	դ���ļ�·������
	 */
	private void read_Raster(String fileName_tif){
       
        Dataset ds = gdal.Open(fileName_tif, gdalconst.GA_ReadOnly);  
          
        int w = ds.getRasterXSize();  
        int h = ds.getRasterYSize();  
          
        System.out.println("Width: " + w + "," + "Height: " + h);  
	}
	
	/**
	 * ��GDAL�������ã�ע����������������֧��
	 */
	private void initGDAL(){
		ogr.RegisterAll();	// ע����������
		 // Ϊ��֧������·�������������������  
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","yes");  
        // Ϊ��ʹ���Ա��ֶ�֧�����ģ�������������  
        gdal.SetConfigOption("SHAPE_ENCODING","CP936");  
	}
	
	/**
	 * 1.��ȡ������Ҫ�������ļ������͵�����������ΪESRI Shapefile��
	 * 2.��������Դ��Ҫָ���ļ�·��
	 * 3.��������Դ����ͼ�㣬����ͼ�����ơ��ռ�ο����������͵�
	 * 4.�������Ա������ֶΣ�ָ���ֶε����ơ����͡�����
	 * 5.����Ҫ�أ�ָ��ͼ�㡢���Ա�����ֶε�ֵ��������Ϣ
	 * 6.ͼ������Դ��д��
	 */
	private void create_shp(){
		String strVectorFile ="F:\\test\\gdal\\testShap09.shp";                           
  
        //�������ݣ������Դ���ESRI��shp�ļ�Ϊ��  
        String strDriverName = "ESRI Shapefile";  
        org.gdal.ogr.Driver oDriver =ogr.GetDriverByName(strDriverName);  
        if (oDriver == null)  
        {          	
	        System.out.println(strVectorFile+ " ���������ã�\n");  
	        return;  
        }  
  
        // ��������Դ  
        DataSource oDS = oDriver.CreateDataSource(strVectorFile,null);  
        if (oDS == null)  
        {  
              System.out.println("����ʸ���ļ���"+ strVectorFile +"��ʧ�ܣ�\n" );  
              return;  
        }  
  
        // ����ͼ�㣬����һ�������ͼ�㣬����û��ָ���ռ�ο��������Ҫ�Ļ�����Ҫ���������ָ��  
        Layer oLayer =oDS.CreateLayer("TestPolygon", null, ogr.wkbPolygon, null);  
        
        if (oLayer == null)  
        {  
              System.out.println("ͼ�㴴��ʧ�ܣ�\n");  
              return;  
        }  
  
        // ���洴�����Ա�  
        // �ȴ���һ����FieldID����������  
        create_Table("FieldID", ogr.OFTInteger, -1, oLayer);         
        create_Table("FieldName", ogr.OFTString, 100, oLayer);              
  
        FeatureDefn oDefn =oLayer.GetLayerDefn();  
  
        // ����������Ҫ��  
        create_feature(oLayer, oDefn, 0, "������", "POLYGON ((0 0,20 0,10 15,0 0))");        
  
        // ��������Ҫ��  
        create_feature(oLayer, oDefn, 1, "����", "POLYGON ((30 0,60 0,60 30,30 30,30 0))");        
  
        // ���������Ҫ��  
        create_feature(oLayer, oDefn, 2, "�����", "POLYGON ((70 0,85 0,90 15,80 30,65 15,70 0))");
          
        //д���ļ�  
        oLayer.SyncToDisk();  
        oDS.SyncToDisk();  
  
        System.out.println("\n���ݼ�������ɣ�\n");  
	}
	
	/**
	 * ����Ҫ��
	 * @param oLayer	Ҫ��������ͼ��
	 * @param oDefn		Ҫ�صĶ����ʼ������˴���Ҫ��
	 * @param field1	�ֶ�
	 * @param field2	�ֶ�
	 * @param geoInfo	������Ϣ���ɴ˴�������Ҫ��
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
	 * ���Ա�����ֶ�
	 * @param fieldName �ֶ�����
	 * @param type	�ֶ�����
	 * @param width	�ֶγ��ȣ�-1ΪĬ�ϳ���
	 * @param oLayer	���Ա�������ͼ��
	 */
	private void create_Table(String fieldName, int type, int width, Layer oLayer){
		FieldDefn oField = new FieldDefn(fieldName, type);
		if(width>0){			
			oField.SetWidth(width);
		}
        oLayer.CreateField(oField, 1);  
	}
	
}
