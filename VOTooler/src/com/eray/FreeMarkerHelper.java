package com.eray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.eray.vo.SheetVO;

import freemarker.template.Configuration;
import freemarker.template.Template;




public class FreeMarkerHelper {
	public static void createAS3VO(String configExcelPath,String classNameString,String[][] data,String outPutBasePath) throws Exception{
		ArrayList<ErayBean> fieldList = new ArrayList<ErayBean>();
		ArrayList<IncludeFileBean> typeList = new ArrayList<IncludeFileBean>();
		String[] valueList = data[0];
		for(int i=2;i<data.length;++i){
			ErayBean b = new ErayBean();
			Boolean isInBaseType = true;
			String type = "";
			Boolean isNum = false;
			for (int j = 0; j < valueList.length; j++) {
				String typeName = valueList[j];
				if(SheetDataItemLabel.SHEET_ITEM_NAME.equalsIgnoreCase(typeName)){
					String filedName = data[i][j];
					b.setFieldName(filedName);
					b.setFieldNa(ExcelUtils.lowerCaseFirstLetter(filedName));
				}else if(SheetDataItemLabel.SHEET_ITEM_TYPE.equalsIgnoreCase(typeName)){
					type = data[i][j];
					isInBaseType = isBaseType(type);
					b.setType(type);
				}else if(SheetDataItemLabel.SHEET_ITEM_COUNT.equalsIgnoreCase(typeName)){
					String countString = data[i][j];
					if(!"".equals(countString)){
						ErayBean countBean = new ErayBean();
						isNum = ExcelUtils.isNumeric(countString);
						
						countBean.setFieldName(countString);
						b.setFieldNa(ExcelUtils.lowerCaseFirstLetter(countString));
						countBean.setType(SheetDataItemLabel.FIELD_TYPE_INT);
						countBean.setComment("个数");
						countBean.setCount("");
						if(isNum){
							countBean.setCountType("2");
						}else{
							countBean.setCountType("1");
							fieldList.add(countBean);
						}
					}
					b.setCount(countString);
				}else if(SheetDataItemLabel.SHEET_ITEM_COMMENT.equalsIgnoreCase(typeName)){
					b.setComment(data[i][j]);
				}else if(SheetDataItemLabel.SHEET_ITEM_VO_LOGIC.equalsIgnoreCase(typeName)){
					b.setVoLogicStr(data[i][j]);
					ArrayList<LogicVO> logicVOList = ExcelUtils.praseVOLogic(data[i][j]);
					for(int k = 0;k<logicVOList.size();++k){
						LogicVO logicVO = logicVOList.get(k);
						SheetVO tempSheetVO =  DataModel.getInstance().getSheetVOBySheetName(logicVO.getObjType());
						String[][] __voData = ExcelUtils.getSheetData(tempSheetVO.getExcelNativePath(),logicVO.getObjType());
						FreeMarkerHelper.createAS3VO(tempSheetVO.getExcelNativePath(),logicVO.getObjType(), __voData, outPutBasePath);
					}
					b.setVoLogic(logicVOList);
				}
			}
			if(isNum){
				b.setCountType("2");
			}else{
				b.setCountType("1");
			}
			if(!isInBaseType){
				IncludeFileBean bb = new IncludeFileBean();
				bb.setIncludeFileName(b.getType());
				if("".equalsIgnoreCase(b.getType())||b.getType()==null){
					System.out.println(classNameString+"异常"+i+"行");
					continue;
				}
				SheetVO sheetVO = DataModel.getInstance().getSheetVOBySheetName(b.getType());
				if(sheetVO!=null){
					String[][] voData = ExcelUtils.getSheetData(sheetVO.getExcelNativePath(), b.getType());
					FreeMarkerHelper.createAS3VO(sheetVO.getExcelNativePath(), b.getType(), voData, outPutBasePath);
					if(!ExcelUtils.isInList(typeList, bb)){
						typeList.add(bb);
					}
				}
			}
			fieldList.add(b);
		}
		String defaultPackageString = SheetDataItemLabel.AS_DEFAULT_PACKAGE;
		String packageFolder = defaultPackageString.replace(".", SheetDataItemLabel.LINE_GAP)+SheetDataItemLabel.LINE_GAP;
		String asFileFolder = outPutBasePath+SheetDataItemLabel.PARENT_FOLDER+SheetDataItemLabel.LINE_GAP+SheetDataItemLabel.AS_FILE_FOLDER+SheetDataItemLabel.LINE_GAP+packageFolder;
		File asFile = new File(asFileFolder+classNameString+".as");
		if(asFile.exists()){
			System.out.println(classNameString+" 已经生成过了");
		}else{
			try{
				Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
				freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
				freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
				Template template;
				try{
					template = freemarkerCfg.getTemplate(SheetDataItemLabel.AS_CLASS_FILE_TEMPLATE,Locale.ENGLISH);
					template.setEncoding("UTF-8");
					HashMap<Object, Object> root = new HashMap<Object, Object>();
					root.put("ClassName", classNameString);
					root.put("FieldList", fieldList);
					root.put("TypeList", typeList);
					Writer writer;
					try{
						
						File asDic = new File(asFileFolder);
						if(!asDic.exists()){
							asDic.mkdirs();
						}
						writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(asFileFolder+classNameString+".as"), "UTF-8"));
						template.process(root, writer);
						writer.flush();
						writer.close();
					}
					catch(Exception e){
						System.out.println(e.toString());
					}
				}catch(Exception e){
					System.out.print(e.toString());
				}
			}catch(Exception e){

				System.out.print(e.toString());
			}
		}
	}
	public static void createVOFactory(String configExcelPath,String className,String[][] data,String outPutBasePath) throws Exception{
		ArrayList<ErayBean> fieldList = new ArrayList<ErayBean>();
		ArrayList<IncludeFileBean> typeList = new ArrayList<IncludeFileBean>();
		if(data==null||data.length<3){
			System.out.println(className+"没有生成");
			return;
		}
		String[] valueList = data[0];
		String[] initItemList = data[1];
		ItemClassVO icv = parseClassVO(initItemList,className);
		for(int i=2;i<data.length;++i){
			ErayBean b = new ErayBean();
			IncludeFileBean bb = new IncludeFileBean();
			Boolean isInBaseType = true;
			for (int j = 0; j < valueList.length; j++) {
				String typeName = valueList[j];
				if(SheetDataItemLabel.SHEET_ITEM_NAME.equalsIgnoreCase(typeName)){
					b.setFieldName(data[i][j]);
				}else if(SheetDataItemLabel.SHEET_ITEM_TYPE.equalsIgnoreCase(typeName)){
					isInBaseType = isBaseType(data[i][j]);
					b.setType(data[i][j]);
				}else if(SheetDataItemLabel.SHEET_ITEM_COUNT.equalsIgnoreCase(typeName)){
					b.setCount(data[i][j]);
				}else if(SheetDataItemLabel.SHEET_ITEM_COUNT.equalsIgnoreCase(typeName)){
					b.setComment(data[i][j]);
				}
			}
			if(!isInBaseType){
				bb.setIncludeFileName(b.getType());
				if("".equalsIgnoreCase(b.getType())||b.getType()==null){
					System.out.println(className+"异常"+i+"行");
					continue;
				}
				String[][] voData = ExcelUtils.getSheetData(configExcelPath, b.getType());
				FreeMarkerHelper.createVOFactory(configExcelPath, b.getType(), voData, outPutBasePath);
				typeList.add(bb);
			}
			fieldList.add(b);
		}
		createVOFile(outPutBasePath,fieldList,icv,typeList);
	}
	private static Queue<String> baseType = new LinkedBlockingQueue<String>();
	static
	{
		baseType.add(SheetDataItemLabel.FIELD_TYPE_CHAR);
		baseType.add(SheetDataItemLabel.FIELD_TYPE_INT);
		baseType.add(SheetDataItemLabel.FIELD_TYPE_INT64);
		baseType.add(SheetDataItemLabel.FIELD_TYPE_SHORT);
		baseType.add(SheetDataItemLabel.FIELD_TYPE_UINT);
		baseType.add(SheetDataItemLabel.FIELD_TYPE_NUMBER);
		baseType.add(SheetDataItemLabel.FIELD_TYPE_STRING);
		baseType.add(SheetDataItemLabel.FIELD_TYPE_OBJECT);
	}

	private static Boolean isBaseType(String Type){
		return baseType.contains(Type);
	}
	private static ItemClassVO parseClassVO(String[] initItemList,String className){
		ItemClassVO icv = new ItemClassVO();
		icv.classStr = className;
		icv.classInitItem = initItemList[0];
		icv.classInitItemValue = initItemList[1];
		return icv;
	}
	private static void createVOFile(String basePath,ArrayList<ErayBean> fieldList,ItemClassVO icv,ArrayList<IncludeFileBean> typeList){
		try{
			String defaultPackageString = "ERAY.OMG";
			Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
			freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
			freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
			Template template;
			try{
				template = freemarkerCfg.getTemplate(SheetDataItemLabel.C_CLASS_FILE_TEMPLATE,Locale.ENGLISH);
				template.setEncoding("UTF-8");
				HashMap<Object, Object> root = new HashMap<Object, Object>();
				root.put("ClassName", icv.classStr);
				root.put("FieldList", fieldList);
				root.put("TypeList", typeList);
				root.put("ICV", icv);
				Writer writer;
				try{
					String packageFolder = defaultPackageString.replace(".", SheetDataItemLabel.LINE_GAP)+SheetDataItemLabel.LINE_GAP;
					String cFolder = basePath+SheetDataItemLabel.PARENT_FOLDER+SheetDataItemLabel.LINE_GAP+SheetDataItemLabel.C_FILE_FOLDER+SheetDataItemLabel.LINE_GAP+packageFolder;
					File asDic = new File(cFolder);
					if(!asDic.exists()){
						asDic.mkdirs();
					}
					writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(basePath+cFolder+icv.classStr+".h"), "UTF-8"));
					template.process(root, writer);
					writer.flush();
					writer.close();
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
			}catch(Exception e){
				System.out.print(e.toString());
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	public static void createMessageDelegateFile(InputArgsVO itvo,ArrayList<MessageIdSheetBean> fieldList){
		try{
			String basePath = itvo.outPutPath;
			String classNameStr = "IMessageDelegate";
			String defaultPackageString = "IMessageDelegate";
			Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
			freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
			freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
			Template template;
			try{
				template = freemarkerCfg.getTemplate(SheetDataItemLabel.C_DELEGATE_CLASS_FILE_TEMPLATE,Locale.ENGLISH);
				template.setEncoding("UTF-8");
				HashMap<Object, Object> root = new HashMap<Object, Object>();
				root.put("ClassName", classNameStr);
				root.put("FieldList", fieldList);
				Writer writer;
				try{
					String packageFolder = defaultPackageString.replace(".", SheetDataItemLabel.LINE_GAP)+SheetDataItemLabel.LINE_GAP;
					String cFolder = basePath+SheetDataItemLabel.PARENT_FOLDER+SheetDataItemLabel.LINE_GAP+SheetDataItemLabel.C_FILE_FOLDER+SheetDataItemLabel.LINE_GAP+packageFolder;
					File asDic = new File(cFolder);
					if(!asDic.exists()){
						asDic.mkdirs();
					}
					writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(cFolder+classNameStr+".h"), "UTF-8"));
					template.process(root, writer);
					writer.flush();
					writer.close();
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
			}catch(Exception e){
				System.out.print(e.toString());
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	public static void createAS3MessageManager(InputArgsVO itvo,ArrayList<MessageIdSheetBean> fieldList){
		try{
			String basePath = itvo.outPutPath;
			String classNameStr = "ErayMessageManager";
			String defaultPackageString = SheetDataItemLabel.AS_MANAGER_PACKAGE;
			Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
			freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
			freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
			Template template;
			try{
				template = freemarkerCfg.getTemplate(SheetDataItemLabel.AS_CLASS_MANAGER_FILE_TEMPLATE,Locale.ENGLISH);
				template.setEncoding("UTF-8");
				HashMap<Object, Object> root = new HashMap<Object, Object>();
				root.put("ClassName", classNameStr);
				root.put("FieldList", fieldList);
				Writer writer;
				try{
					String packageFolder = defaultPackageString.replace(".", SheetDataItemLabel.LINE_GAP)+SheetDataItemLabel.LINE_GAP;
					String asFolder = basePath+SheetDataItemLabel.PARENT_FOLDER+SheetDataItemLabel.LINE_GAP+SheetDataItemLabel.AS_FILE_FOLDER+SheetDataItemLabel.LINE_GAP+packageFolder;
					File asDic = new File(asFolder);
					if(!asDic.exists()){
						asDic.mkdirs();
					}
					writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(asFolder+classNameStr+".as"), "UTF-8"));
					template.process(root, writer);
					writer.flush();
					writer.close();
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
			}catch(Exception e){
				System.out.print(e.toString());
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	
}
