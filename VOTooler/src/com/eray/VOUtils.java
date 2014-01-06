package com.eray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;



public class VOUtils {
	private InputArgsVO itvo = null;
	public void parse(InputArgsVO itv) throws Exception{
		itvo =  itv;
		List<String> list = getAllFiles(itv.path);
		for (String s : list) {
			if (FilenameUtils.isExtension(s, "xls")) {
				System.out.println("file:" + s);
				this.tranformExcel(s);
			}
		}
	}
	private void tranformExcel(String configExcelPath) throws Exception{
		//String[] sheetNames = ExcelUtils.getSheetName(configExcelPath);
		String[][] data = ExcelUtils.getSheetData(configExcelPath, "MessageIdSheet");
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
				if("MessageId".equalsIgnoreCase(typeName)){
					MessageId = data[i][j];
				}else if("MessageVO".equalsIgnoreCase(typeName)){
					messageClassName = data[i][j];
					if(!"".equalsIgnoreCase(messageClassName)){
						voData = ExcelUtils.getSheetData(configExcelPath, data[i][j]);
					}
				}else if("ActionHandler".equalsIgnoreCase(typeName)){
					ActionHandler = data[i][j];
				}else if("MessageIDStr".equalsIgnoreCase(typeName)){
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
			FreeMarkerHelper.createVOFactory(configExcelPath,messageClassName, voData, itvo.outPutPath);
		}
		FreeMarkerHelper.createMessageDelegateFile(itvo, fieldList);
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
