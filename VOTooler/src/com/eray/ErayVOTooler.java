package com.eray;

import com.eray.InputArgsVO;

public class ErayVOTooler {
	public static void main(String[] args) throws Exception{
		InputArgsVO iao = InputArgsVO.parse(args);
		long s = System.currentTimeMillis();
		VOUtils vt = new VOUtils();
		vt.parse(iao);
		System.out.println("build is ok   Spend time mills:  " + (System.currentTimeMillis() - s));
		System.exit(0);
	}
}
