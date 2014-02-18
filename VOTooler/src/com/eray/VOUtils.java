package com.eray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.eray.vo.SheetVO;



public class VOUtils {
	private InputArgsVO itvo = null;
	
	public void parse(InputArgsVO itv) throws Exception{
		itvo =  itv;
		if(itvo.outASClass&&itvo.outCAddClass){
			System.out.println("不需要作任何处理打包操作");
			return;
		}
		List<String> list = getAllFiles(itv.path);
		ArrayList<SheetVO> sheetVOList = ExcelUtils.getSheetList(list);
		DataModel.getInstance().setSheetVOList(sheetVOList);
		for (String s : list) {
			if (FilenameUtils.isExtension(s, "xls")) {
				System.out.println("file:" + s);
				this.tranformExcel(s);
			}
		}
	}
	private void tranformExcel(String configExcelPath) throws Exception{
		//String[] sheetNames = ExcelUtils.getSheetName(configExcelPath);

//		String xlsName = FilenameUtils.getBaseName(configExcelPath);
		String[][] data = ExcelUtils.getSheetData(configExcelPath, SheetDataItemLabel.MESSAGE_ID_SHEET_NAME);
		if (data.length == 0) {
			return;
		}
		String[] valueList = data[0];
		ArrayList<MessageIdSheetBean> fieldList = new ArrayList<MessageIdSheetBean>();
		for (int i = 1; i < data.length; i++) {
			String messageClassName= null;
			String[][] voData = null;
			String MessageId = null;
			String ActionHandler = null;
			String MessageIDStr = null;
			MessageIdSheetBean bb = new MessageIdSheetBean();
			for (int j = 0; j < valueList.length; j++) {
				String typeName = valueList[j];
				if(SheetDataItemLabel.MESSAGE_ID_ITEM_NAME.equalsIgnoreCase(typeName)){
					MessageId = data[i][j];
				}else if(SheetDataItemLabel.MESSAGE_ID_VO_NAME.equalsIgnoreCase(typeName)){
					messageClassName = data[i][j];
					if(!"".equalsIgnoreCase(messageClassName)){
						voData = ExcelUtils.getSheetData(configExcelPath, data[i][j]);
					}
				}else if(SheetDataItemLabel.MESSAGE_ID_HANDLER.equalsIgnoreCase(typeName)){
					ActionHandler = data[i][j];
				}else if(SheetDataItemLabel.MESSAGE_ID_STR_KEY.equalsIgnoreCase(typeName)){
					MessageIDStr = data[i][j];
				}
				
			}
			if(MessageId!=null&&(!"".equalsIgnoreCase(MessageId))&&!"-1".equalsIgnoreCase(MessageId)){
				bb.setMessageId(MessageId);
				bb.setMessageVO(messageClassName);
				bb.setActionHandler(ActionHandler);
				bb.setMessageIDStr(MessageIDStr);
				fieldList.add(bb);
			}
			if(itvo.outCAddClass){
				FreeMarkerHelper.createVOFactory(configExcelPath,messageClassName, voData, itvo.outPutPath);
			}
			if(itvo.outASClass){
				FreeMarkerHelper.createAS3VO(configExcelPath, messageClassName, voData, itvo.outPutPath);
			}
		}
		if(itvo.outCAddClass){
			FreeMarkerHelper.createMessageDelegateFile(itvo, fieldList);
		}
	}
	
	public static List<String> getAllFiles(String absDir) {
		List<String> files = new ArrayList<String>();
		File fileDir = new File(absDir);
		String[] list = fileDir.list();
		for (String s : list) {
			String name = absDir + "/" + s;
			File ins = new File(name);
			if (ins.isFile()) {
				files.add(name);
			} else {
				files.addAll(getAllFiles(name));
			}
		}
		return files;
	}
}
