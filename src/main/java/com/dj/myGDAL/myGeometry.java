package com.dj.myGDAL;

import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;

public class myGeometry {
	
	public myGeometry(){
		
	}
	
	private Geometry create_point(double x, double y){
		Geometry point = new Geometry(ogr.wkbPoint);
		point.AddPoint(x, y);
		return point;
	}
	
	private Geometry create_geometry(String geometry_str){
		Geometry geometry = Geometry.CreateFromWkt(geometry_str);
		return geometry;
	}
	
	/**
	 * 从geojson创建几何
	 * @param json
	 * @return
	 */
	public Geometry create_geometry_geojson(String json){		
		return Geometry.CreateFromJson(json);
	}
	
	/**
	 * 统计点的个数
	 * @param geometry
	 * @return
	 */
	private int getpointCount(Geometry geometry){
		int count = geometry.GetPointCount();
		return count;
	}
	
	/**
	 * 统计几何体的个数
	 * @param geometry
	 * @return
	 */
	private int getGeometryCount(Geometry geometry){
		int count = geometry.GetGeometryCount();
		return count;
	}
	
	/**
	 * 遍历集合体
	 * @param geometry
	 */
	private void overGeometry(Geometry geometry){
		for(int i=0; i<geometry.GetGeometryCount();i++){
			Geometry g = geometry.GetGeometryRef(i);
		}
	}
	
	/**
	 * 缓冲区分析
	 * @param geom
	 * @param dis
	 * @return
	 */
	private Geometry create_buffer(Geometry geom, double dis){
	    return geom.Buffer(dis);
	}
	
	/**
	 * 相交分析
	 * @param geom1
	 * @param geom2
	 * @return
	 */
	private Geometry create_instersection(Geometry geom1, Geometry geom2){
		return geom1.Intersection(geom2);
	}
	
	/**
	 * 复制图层
	 * @param ds
	 * @param layer_name
	 * @return
	 */
	public Layer createLayer_datasource(DataSource ds, String layer_name){
		int count = 0;
		Layer layer = null;
		for(int i=0; i< ds.GetLayerCount();i++){			
			if(layer_name.equals(ds.GetLayer(i).GetName())){			
				count++;
				layer = ds.GetLayer(i);
			}			
		}
		if(count>0){
			return ds.CopyLayer(layer, layer_name);
		}else{
			System.out.println("数据源中没有" + layer_name + "图层！");
			return null;
		}		
	}

	/**
	 * 遍历图层中的要素,获取属性信息以及几何信息
	 * @param layer
	 */
	public void loop_feature(Layer layer){
		long count = layer.GetFeatureCount();
		for(long i = 0; i<count; i++){
			Feature feature = layer.GetFeature(i);
			String name = feature.GetFieldAsString("FieldName");
			int id = feature.GetFieldAsInteger("FieldID");
			Geometry geom = feature.GetGeometryRef();
			System.out.println(geom.ExportToJson());
			System.out.println(id + " ----> " + name);
		}
	}
	
		
	
}
