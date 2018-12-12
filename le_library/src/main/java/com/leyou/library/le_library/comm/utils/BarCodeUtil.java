package com.leyou.library.le_library.comm.utils;

public class BarCodeUtil {
    /**
     * bar code
     * @Title: main 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param 
     * @return void    返回类型 
     * @throws 
     * @author Illidan  
     * @date 2017年7月27日 上午11:20:19
     */
	/*0727100700111111111

	偶数坐标和基数坐标互换位置（坐标从0开始）

	换位后中间位置不变 前9换后9

	前三个和后三个

	第四个换第十四个

	第五个换第十三个

	解密 第五个换第十三个
	     第四个换第十四个
	     前三个和后三个
	     换位后中间位置不变 前9换后9
	     偶数坐标和基数坐标互换位置（坐标从0开始）
	     */
	   
	public static String enCode(String value) throws Exception{
		if(value == null || value.length()!=19){
			throw new Exception("加密串错误");
		}
		//奇偶互换
		char[] valueChars = value.toCharArray();
		for( int i=0;i<valueChars.length;i++){
			if(i%2==1){
				char fortemt = valueChars[i];
				valueChars[i]=valueChars[i-1];
				valueChars[i-1] = fortemt;
			}
		}
		
		//前9换后9
		for( int i=0;i<valueChars.length;i++){
			if(i<9){
				char fortemt = valueChars[i];
				valueChars[i]=valueChars[i+10];
				valueChars[i+10] = fortemt;
			}
		}
		//前三个和后三个
		for( int i=0;i<valueChars.length;i++){
			if(i<3){
				char fortemt = valueChars[i];
				valueChars[i] = valueChars[i+16];
				valueChars[i+16] = fortemt;
			}
		}
		//第四个换第十四个
		char temt = valueChars[3];
		valueChars[3] = valueChars[13];
		valueChars[13] = temt;
	
		//第五个换第十三个
		char temt1 = valueChars[4];
		valueChars[4] = valueChars[12];
		valueChars[12] = temt1;
		
		String returns = "";
		for(char  c:valueChars){
			returns = returns+c;
		}
		return returns;
	}
	
	   
		public static String deCode(String value) throws Exception{
			if(value == null || value.length()!=19){
				throw new Exception("加密串错误");
			}
			
			char[] valueChars = value.toCharArray();
			
			//第五个换第十三个
			char temt1 = valueChars[4];
			valueChars[4] = valueChars[12];
			valueChars[12] = temt1;
			//第四个换第十四个
			char temt = valueChars[3];
			valueChars[3] = valueChars[13];
			valueChars[13] = temt;
			//前三个和后三个
			for( int i=0;i<valueChars.length;i++){
				if(i<3){
					char fortemt = valueChars[i];
					valueChars[i] = valueChars[i+16];
					valueChars[i+16] = fortemt;
				}
			}
			//前9换后9
			for( int i=0;i<valueChars.length;i++){
				if(i<9){
					char fortemt = valueChars[i];
					valueChars[i]=valueChars[i+10];
					valueChars[i+10] = fortemt;
				}
			}
			
			//奇偶互换
			for( int i=0;i<valueChars.length;i++){
				if(i%2==1){
					char fortemt = valueChars[i];
					valueChars[i]=valueChars[i-1];
					valueChars[i-1] = fortemt;
				}
			}
	
			String returns = "";
			for(char  c:valueChars){
				returns = returns+c;
			}
			return returns;
		}

 
    public static void main(String[] args) throws Exception {
     String s = enCode("0123456789123456789");
       System.out.println(s );
       
       System.out.println(deCode(s));
    }
}
