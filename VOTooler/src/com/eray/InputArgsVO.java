package com.eray;



public class InputArgsVO {
	public String path;
	public String outPutPath;
	public static InputArgsVO parse(String[]  args){
		InputArgsVO iao = new InputArgsVO();
		if(args.length>0){
			iao.path = args[0];
			iao.outPutPath = args[1]+"\\";
		}else{
			iao.path = "D:\\data";
			iao.outPutPath = "D:\\outputvo\\";
		}
		return iao;
	}
}
