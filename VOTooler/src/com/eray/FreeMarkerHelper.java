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

import freemarker.template.Configuration;
import freemarker.template.Template;




public class FreeMarkerHelper {
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
				if("FieldName".equalsIgnoreCase(typeName)){
					b.setFieldName(data[i][j]);
				}else if("Type".equalsIgnoreCase(typeName)){
					isInBaseType = isBaseType(data[i][j]);
					b.setType(data[i][j]);
				}else if("Count".equalsIgnoreCase(typeName)){
					b.setCount(data[i][j]);
				}else if("Comment".equalsIgnoreCase(typeName)){
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
		baseType.add("char");
		baseType.add("int");
		baseType.add("int64_t");
		baseType.add("short");
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
				template = freemarkerCfg.getTemplate("CADD.ftl",Locale.ENGLISH);
				template.setEncoding("UTF-8");
				HashMap<Object, Object> root = new HashMap<Object, Object>();
				root.put("ClassName", icv.classStr);
				root.put("FieldList", fieldList);
				root.put("TypeList", typeList);
				root.put("ICV", icv);
				Writer writer;
				try{
					String packageFolder = defaultPackageString.replace(".", "\\")+"\\";
					File asDic = new File(basePath+"DataVO\\c\\"+packageFolder);
					if(!asDic.exists()){
						asDic.mkdirs();
					}
					writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(basePath+"DataVO\\c\\"+packageFolder+icv.classStr+".h"), "UTF-8"));
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
				template = freemarkerCfg.getTemplate("MessageId.ftl",Locale.ENGLISH);
				template.setEncoding("UTF-8");
				HashMap<Object, Object> root = new HashMap<Object, Object>();
				root.put("ClassName", classNameStr);
				root.put("FieldList", fieldList);
				Writer writer;
				try{
					String packageFolder = defaultPackageString.replace(".", "\\")+"\\";
					File asDic = new File(basePath+"DataVO\\"+packageFolder);
					if(!asDic.exists()){
						asDic.mkdirs();
					}
					writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(basePath+"DataVO\\"+packageFolder+classNameStr+".h"), "UTF-8"));
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
