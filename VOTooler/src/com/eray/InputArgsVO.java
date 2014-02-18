package com.eray;



public class InputArgsVO {
	public String path;
	public String outPutPath;
	public boolean outCAddClass;
	public boolean outASClass;
	public static InputArgsVO parse(String[]  args){
		InputArgsVO iao = new InputArgsVO();
		if(args.length>0){
			iao.path = args[0];
			iao.outPutPath = args[1]+"\\";
			iao.outCAddClass = args[2].equals("1") ? true : false;
			iao.outASClass = args[3].equals("1") ? true : false;
		}else{
			iao.path = "D:\\data";
			iao.outPutPath = "D:\\outputvo\\";
			iao.outCAddClass = false;
			iao.outASClass = true;
		}
		return iao;
	}
}
