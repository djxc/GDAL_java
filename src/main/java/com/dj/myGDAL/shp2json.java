package com.dj.myGDAL;

import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.ogr;

public class shp2json {
	
	/**
	 * shape文件转换成json。输入shp文件路径，输出json路径
	 * 1.打开shp文件为数据源
	 * 2.创建json驱动
	 * 3.利用json的复制数据源工具将shp文件转化为json文件
	 * @param input_path
	 * @param output_path
	 * @param driverName	驱动名称
	 */
	public void shp_json(String input_path, String output_path, String driverName){
        //打开文件
        DataSource ds = ogr.Open(input_path,0);
        
        if (ds == null)
        {
            System.out.println("打开文件失败！" );
            return;
        }
        Driver dv = ogr.GetDriverByName(driverName);
        
        if (dv == null)
        {
            System.out.println("打开驱动失败！" );
            return;
        }
        
        dv.CopyDataSource(ds, output_path);
        
        System.out.println("转换成功！" );
	}
	
	
}
