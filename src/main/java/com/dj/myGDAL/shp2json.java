package com.dj.myGDAL;

import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.ogr;

public class shp2json {
	
	/**
	 * shape�ļ�ת����json������shp�ļ�·�������json·��
	 * 1.��shp�ļ�Ϊ����Դ
	 * 2.����json����
	 * 3.����json�ĸ�������Դ���߽�shp�ļ�ת��Ϊjson�ļ�
	 * @param input_path
	 * @param output_path
	 * @param driverName	��������
	 */
	public void shp_json(String input_path, String output_path, String driverName){
        //���ļ�
        DataSource ds = ogr.Open(input_path,0);
        
        if (ds == null)
        {
            System.out.println("���ļ�ʧ�ܣ�" );
            return;
        }
        Driver dv = ogr.GetDriverByName(driverName);
        
        if (dv == null)
        {
            System.out.println("������ʧ�ܣ�" );
            return;
        }
        
        dv.CopyDataSource(ds, output_path);
        
        System.out.println("ת���ɹ���" );
	}
	
	
}
