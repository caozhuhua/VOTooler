package com.eray;

import java.util.ArrayList;

import com.eray.vo.SheetVO;

public class DataModel {
	private static DataModel _instance;
	public static DataModel getInstance(){
		if(_instance==null){
			_instance = new DataModel();
		}
		return _instance;
	}
	private ArrayList<SheetVO> _list;
	public void setSheetVOList(ArrayList<SheetVO> list){
		_list = list;
	}
	
	public SheetVO getSheetVOBySheetName(String sheetName){
		SheetVO vo = null;
		if(_list!=null){
			for(int i = 0;i<_list.size();++i){
				vo = _list.get(i);
				if(vo.getSheetName().equals(sheetName)){
					return vo;
				}
			}
		}
		return vo;
	}
	
}
