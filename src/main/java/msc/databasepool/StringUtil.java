package msc.databasepool;


import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static String getStackTraceAsString(Exception e) {
		// StringWriter将包含堆栈信息
		StringWriter stringWriter = new StringWriter();
		// 必须将StringWriter封装成PrintWriter对象，
		// 以满足printStackTrace的要求
		PrintWriter printWriter = new PrintWriter(stringWriter);
		// 获取堆栈信息
		e.printStackTrace(printWriter);
		// 转换成String，并返回该String
		StringBuffer error = stringWriter.getBuffer();
		return error.toString();
	}

	public static int count(String content, String str) {
		int value = 0, pos = -1;
		while (true) {
			pos = content.indexOf(str, pos + 1);
			if (pos == -1)
				break;
			value++;
		}
		return value;
	}

	public static String toUtf8String(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try {
			char c;
			for (int i = 0; i < s.length(); i++) {
				c = s.charAt(i);
				if (c >= 0 && c <= 255) {
					sb.append(c);
				} else {
					byte[] b;
					b = Character.toString(c).getBytes(StandardCharsets.UTF_8);
					for (int j = 0; j < b.length; j++) {
						int k = b[j];
						if (k < 0)
							k += 256;
						sb.append("%" + Integer.toHexString(k).toUpperCase());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String replace(String content, String oldStr, String newStr) {
		StringBuffer newContent = new StringBuffer();
		int len = oldStr.length();
		int i = 0, j = 0;
		while (true) {
			j = content.indexOf(oldStr, i);
			if (j != -1) {
				newContent.append(content.substring(i, j) + newStr);
				i = j + len;
			} else {
				newContent.append(content.substring(i));
				break;
			}
		}
		return new String(newContent);
	}

	public static long ip_string_long(String ip) {
		StringTokenizer st = new StringTokenizer(ip, ".");
		long a = Long.parseLong(st.nextToken()) << 24;
		long b = Long.parseLong(st.nextToken()) << 16;
		long c = Long.parseLong(st.nextToken()) << 8;
		long d = Long.parseLong(st.nextToken());
		return a + b + c + d;
	}

	public static String ip_long_string(long ip) {
		return ((ip >> 24) & 0xff) + "." + ((ip >> 16) & 0xff) + "."
				+ ((ip >> 8) & 0xff) + "." + ((ip) & 0xff);
	}

	public static boolean checkIpAddress(String ip) {
		try {
			StringTokenizer st = new StringTokenizer(ip,
					".");
			String value = "";
			if (st.hasMoreTokens()) {
				value = st.nextToken();
				if (Integer.parseInt(value) >= 0
						&& Integer.parseInt(value) <= 255) {
					if (st.hasMoreTokens()) {
						value = st.nextToken();
						if (Integer.parseInt(value) >= 0
								&& Integer.parseInt(value) <= 255) {
							if (st.hasMoreTokens()) {
								value = st.nextToken();
								if (Integer.parseInt(value) >= 0
										&& Integer.parseInt(value) <= 255) {
									if (st.hasMoreTokens()) {
										value = st.nextToken();
                                        return Integer.parseInt(value) >= 0
                                                && Integer.parseInt(value) <= 255;
									}
								}
							}
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isMobileNumber(String s) {// 判断是不是手机号
		if (s.length() == 11) {
			if (s.startsWith("13")) {
                return isNumberString(s);
			}
		}
		return false;
	}

	public static boolean isLetterString(String s) {// 检查字符串里的每个字符是否都是字母
		return checkString(s,
				"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	public static boolean isNumberString(String s) {// 检查字符串里的每个字符是否都是数字
		return checkString(s, "1234567890");
	}

	public static boolean isLetterAndNumberString(String s) {// 检查字符串里的每个字符是否都是字母和数字
		return checkString(s,
				"1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	public static boolean checkString(String source, String check) {// 检查字符串
		for (int i = 0; i < source.length(); i++) {
			if (check.indexOf(source.charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(String s) {// 判断字符串是否是null或""
		if (s == null || "".equals(s)) {
			return true;
		}
		s = s.trim();
        return "".equals(s);
    }

	public static boolean isNotEmpty(String s) {// 判断字符串是否不是null或""
		return (!isEmpty(s));
	}

	public static String substring(String s, int p1, int p2) {
		if (isEmpty(s))
			return "";
		int len = s.length();
		if (p1 >= len)
			return "";
		if (p2 > len)
			p2 = len;
		s = s.substring(p1, p2);
		if (isNotEmpty(s))
			s += "。。";
		return s;
	}

	public static boolean ifParamInArray(String[] value, String param) {
		if (param == null)
			return false;
		if (value == null)
			return false;
		for (int i = 0; i < value.length; i++) {
			if (param.equals(value[i])) {
				return true;
			}
		}
		return false;
	}

	public static String firstLetterToUpper(String value) {// 字符串中第一个字符转大写
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	public static String firstLetterToLower(String value) {// 字符串中第一个字符转大写
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

	public static String[] split(String str, String regex) {
		if (str == null)
			return new String[0];
		String[] value = new String[count(str, regex) + 1];
		StringTokenizer st = new StringTokenizer(str, regex);
		int i = 0;
		while (st.hasMoreTokens()) {
			value[i++] = st.nextToken();
		}
		return value;
	}

	public static String[] objectArrayToStringArray(Object[] objects) {
		String[] value = new String[objects.length];
		for (int i = 0; i < objects.length; i++) {
			value[i] = (String) objects[i];
		}
		return value;
	}

	/**
	 * 把yyyy-mm-dd格式的字符串转换成Long
	 * 
	 * @param str
	 * @return
	 */
	public static Long strToLongStart(String str) {
		if (str != null && str.matches("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}")) {
			StringBuffer value = new StringBuffer();
			value.append(str, 0, 4);
			value.append(str, 5, 7);
			value.append(str, 8, 10);
			value.append("00");
			return new Long(value.toString());
		} else {
			return null;
		}

	}
	
	/**
	 * yyyy-MM-dd格式变成 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param str
	 * @return
	 */
	public static String strToDate(String str, String suffix) {
		if (str != null && str.matches("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}")) {
			return str + suffix;
		} else {
			return null;
		}
	}

	/**
	 * 把yyyy-mm-dd格式的字符串转换成Long
	 * 
	 * @param str
	 * @return
	 */
	public static Long strToLongEnd(String str) {
		if (str != null && str.matches("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}")) {
			StringBuffer value = new StringBuffer();
			value.append(str, 0, 4);
			value.append(str, 5, 7);
			value.append(str, 8, 10);
			value.append("23");
			return new Long(value.toString());
		} else {
			return null;
		}
	}

	/**
	 * 把形如yyyyMMddHH格式的数值 转换成yyyy-MM-dd HH
	 * 
	 * @param date
	 * @return
	 */
	public static String longToStr(Long date) {
		String time = "";
		// 大于编码时的时间
		if (date != null && date > 2013061900) {
			StringBuffer str = new StringBuffer();
			str.append(date/1000000);
			str.append("年");
			date = date%1000000;
			
			str.append(date/10000);
			str.append("月");
			date = date%10000;
			
			str.append(date/100);
			str.append("日");
			date = date%100;
			str.append(date);
			str.append("时");
			
			time = str.toString();
		}
		return time;
	}
	
	
	/**
	 * 商户状态判断 比方说第二位是注销 chkState(0,2) 则没有注销 chkState(2,2)注销了
	 * 
	 * @param state
	 *            数据库存储的状态(十进制)
	 * @param bit
	 *            所要判断的位数
	 * @return true 表示是这种状态
	 */
	public static boolean chkState(int state, int bit) {
		String state_bit = Integer.toBinaryString(state);

		// 判断位数
		if (state_bit.length() >= bit) {
			// 把二进制串倒置 因为java的最低位在左边
			state_bit = new StringBuffer(state_bit).reverse().toString();
            return state_bit.length() > bit && state_bit.charAt(bit) == '1';
		} else {
			return false;
		}
	}
	

	/**
	 * 商户状态设置 指定位数设置
	 * 
	 * @param state
     * @param bit
     * @param flag
	 *            只有0 1两种 0取消指定位的值 1 设置指定位置的值
	 * @return 返回计算后的状态(十进制数)
	 */
	public static int setState(int state, int bit, int flag) {
		if (flag != 0 && flag != 1) {
			return state;
		}
		String state_bit = Integer.toBinaryString(state);

		// 把二进制串倒置 因为java的最低位在左边
		state_bit = new StringBuffer(state_bit).reverse().toString();

		if (state_bit.length() < bit) {
			int tmp = bit - state_bit.length();
			StringBuffer str = new StringBuffer();
			str.append(state_bit);
			for (int i = 0; i < tmp; i++) {
				str.append("0");
			}
			state_bit = str.toString();
		}

		// 替换第bit为1
		StringBuffer last = new StringBuffer();
		last.append(state_bit, 0, bit);
		last.append(flag);

		bit++;
		if (bit < state_bit.length()) {
			last.append(state_bit.substring(bit));
		}

		String lastState = last.reverse().toString();
		BigInteger last_state = new BigInteger(lastState, 2);
		return last_state.intValue();
	}
	
	/**
	 * 取随机的五位编号
	 * 
	 * @return
	 */
	public static String getAgentId() {

		int n_1 = new Random().nextInt(10);
		if (n_1 == 0 || n_1 == 4) {
			n_1 = n_1 + 1;
		}
		int n_2 = new Random().nextInt(10);
		if (n_2 == 4) {
			n_2 = n_2 - 1;
		}
		int n_3 = new Random().nextInt(10);
		if (n_3 == 4) {
			n_3 = n_3 + 1;
		}
		int n_4 = new Random().nextInt(10);
		if (n_4 == 4) {
			n_4 = n_4 - 1;
		}
		int n_5 = new Random().nextInt(10);
		if (n_5 == 4) {
			n_5 = n_5 + 1;
		}

		StringBuffer id = new StringBuffer();
		id.append(n_1).append(n_2).append(n_3).append(n_4).append(n_5);
		return id.substring(0, 5);
	}
	
	//读取txt文件到list  以行为单位
	public static List<String> readTxtFileToList(String filePath){
		List<String> list = new ArrayList<String>();
		try {
           // String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file)
                //,encoding
                );//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	list.add(lineTxt);
                }
                read.close();
		    }else{
		        System.out.println("找不到指定的文件");
		    }
	    } catch (Exception e) {
	        System.out.println("读取文件内容出错");
	        e.printStackTrace();
	    }
	    return list;
	}

	public static String listToString(List<String> list) {
		String ins = "";
		if(list==null) {
			return ins;
		}
		for (String s : list) {
			ins +="'" + s + "',";
		}
		if(ins.length()>0) {
			ins = ins.substring(0, ins.length()-1);
		}else {
			ins = "''";
		}
		return ins;
	}

	public static String setToString(Set<String> set) {
		StringBuilder sb = new StringBuilder(64);
        if(set==null) {
			return sb.toString();
		}
		for (String s : set) {
			sb.append("'").append(s).append("',");
		}
		String str = "";
		if(sb.length()>0) {
			str = sb.toString();
			str = str.substring(0, str.length()-1);
		}else {
			str = "''";
		}
		return str;
	}


	public static String listToStringNoDH(List<String> list) {
		String ins = "";
		if(list==null) {
			return ins;
		}
		for (String s : list) {
			ins +="" + s + ",";
		}
		if(ins.length()>0) {
			ins = ins.substring(0, ins.length()-1);
		}else {
			ins = "";
		}
		return ins;
	}
	
	public static String cardtrans(String cardID, int bEncryptFlag) {
		char[] strCardID = cardID.toCharArray();
		char[] szKey = "394521863".toCharArray();
        if (strCardID.length <12) return new String(strCardID);
        if (cardID.contains("*")) return "";
        if (cardID.contains(" ")) return "";
        char[] strNewCardID=strCardID;
        int nLen = strCardID.length - 10;
        for (int i=0;(i<nLen && i<9);i++) {
            if (bEncryptFlag == 1) {
                    strNewCardID[i+6] = (char) ((strCardID[i+6]^szKey[i])+'A');
            }
            else {
                    strNewCardID[i+6] = (char) ((strCardID[i+6]-'A')^szKey[i]);
            }
        }
        return new String(strNewCardID);
	}
	
	public static List<String> readFileToList(String path) {
		List<String> list = new ArrayList<String>();
		File file = new File(path);
		try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            	list.add(s.trim());
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
		
		
		return list;
	}
	
	public static String getCardType(int fanOverseas, int fanType) {
		//借贷分离
		if(fanOverseas == 1) {
			//境外卡
			return "境外卡";
		}else {
			//境内卡
			if(fanType == 0) {
				//未知
				return "未知";
			}else if(fanType == 1) {
				//借记卡
				return "借记卡";
			}else {
				//信用卡
				return "信用卡";
			}
		}
	}
	public static boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	}
	
	//读取一行多个字段
	public static List<String[]> readFileToListWithArray(String path) {
		List<String[]> list = new ArrayList<String[]>();
		File file = new File(path);
		try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            	String[] a = s.split(",");
            	list.add(a);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
		
		
		return list;
	}
	public static <T> List<List<T>> splitList(List<T> list, int pageSize) {

		int listSize = list.size(); // list的大小
		int page = (listSize + (pageSize - 1)) / pageSize; // 页数

		List<List<T>> listArray = new ArrayList<List<T>>(); // 创建list数组
															// ,用来保存分割后的list
		for (int i = 0; i < page; i++) { // 按照数组大小遍历
			List<T> subList = new ArrayList<T>(); // 数组每一位放入一个分割后的list
			for (int j = 0; j < listSize; j++) { // 遍历待分割的list
				int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize; // 当前记录的页码(第几页)
				if (pageIndex == (i + 1)) { // 当前记录的页码等于要放入的页码时
					subList.add(list.get(j)); // 放入list中的元素到分割后的list(subList)
				}

				if ((j + 1) == ((j + 1) * pageSize)) { // 当放满一页时退出当前循环
					break;
				}
			}
			listArray.add(subList); // 将分割后的list放入对应的数组的位中
		}
		return listArray;
	}
	
	//按 key的升序组装成K1=V1&K2=V2&…&Kn=Vn字符串
	public static String getSortedKVStr(Map<String, String> paraMap) {
		String result = "";
        List<String> keyList = new ArrayList<String>();
        for(String key : paraMap.keySet()){
        	keyList.add(key);
        }
        Collections.sort(keyList);
        for(String key : keyList){
        	if(result.length() > 0){
        		result += "&";
        	}
        	result += key + "=" + paraMap.get(key);
        }
        return result;
	}
	
	/**
	 * 银行卡号校验
	 * 1.从卡号最后一位数字开始,偶数位乘以2,再将结果值中的个位数和十位数相加
	 * 2.将得到的结果相加，然后再与原数字串的奇数位相加
	 * 3.如果加起来的和模10后为0则验证通过，反之验证不通过
	 * @param bankCardNo
	 * @return
	 */
	public static boolean checkBankCard(String bankCardNo) {
		boolean result = false;
		try{
			if(bankCardNo == null || bankCardNo.length() < 9 || bankCardNo.length() > 24 || !isNumeric(bankCardNo)){
				return false;
			}
			
			int[] cardNoArr = new int[bankCardNo.length()];
			int index = bankCardNo.length();  
	        for(char c : bankCardNo.toCharArray()){  
	        	cardNoArr[--index] = c - '0';  
	        }  
			
			for(int i=1; i<cardNoArr.length; i+=2){  
	            cardNoArr[i] <<= 1;
				cardNoArr[i] = cardNoArr[i] / 10 + cardNoArr[i] % 10;
	        }
			int sum = 0;
	        for(int i=0; i<cardNoArr.length; i++){  
	            sum += cardNoArr[i];  
	        }  
	        result = sum % 10 == 0;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 身份证号码校验：判断第18位校验码是否正确
	 * 1.对前17位数字加权求和， S = Sum(Ai * Wi), i = 0, ... , 16
	 * 	 Ai:第i位置上的身份证号码数字值	
	 * 	 Wi:第i位置上的加权因子，各位置对应的加权因子为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
	 * 2.计算模 Y = mod(S, 11)
	 * 3.根据模的值得到对应的校验码，对应关系为：  
	 * 	 Y值：      0  1  2  3  4  5  6  7  8  9  10
	 * 	   校验码： 1  0  X  9  8  7  6  5  4  3  2 
	 * @param idCardNo
	 * @return
	 */
	public static boolean checkIdCard(String idCardNo) {
		boolean result = false;
		try{
			if(idCardNo == null || idCardNo.length() != 18 || !isNumeric(idCardNo.substring(0, 17))){
				return false;
			}
			String[] verifyCodeArr  = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
	        String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
	        int sum = 0;  
            for(int i=0; i<17; i++) {
               sum += Integer.parseInt(String.valueOf(idCardNo.charAt(i))) * Integer.parseInt(wi[i]);  
            }  
            int modValue = sum % 11;  
            String verifyCode = verifyCodeArr[modValue];  
            result = verifyCode.equals(String.valueOf(idCardNo.charAt(17)).toUpperCase());
            
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static String unicodeToStr(String str){  
	    Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");  
	    Matcher matcher = pattern.matcher(str);  
	    char ch;  
	    while (matcher.find()) {  
	        String group = matcher.group(2);  
	        ch = (char) Integer.parseInt(group, 16);  
	        String group1 = matcher.group(1);  
	        str = str.replace(group1, ch + "");  
	    }  
	    return str;  
	}
	

}
