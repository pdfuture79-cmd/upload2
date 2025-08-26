package egovframework.common.grid;

import java.util.List;
import java.util.Map;

/**
 * ibsheet 리스트를 리턴하는 class
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */

public class IBSheetListVO<T> {
	
	public List<T> DATA;
	public int TOTAL;
	public Map<String, String> ETC;
	public String MESSAGE;
	
	public IBSRes RESULT = new IBSRes();
	
	public class IBSRes {
		public int CODE;
		public String MESSAGE;
	}
	
	public IBSheetListVO() {
		// TODO Auto-generated constructor stub
	}
	
	public IBSheetListVO (List<T> data, int code, String message) {
		this.DATA = data;
		this.MESSAGE = message;
		this.RESULT.CODE = code;
	}
	
	public IBSheetListVO (List<T> data, int total) {
		this.DATA = data;
		this.TOTAL = total;
	}
	
	public IBSheetListVO (List<T> data, String message, int total) {
		this.DATA = data;
		this.MESSAGE = message;
		this.TOTAL = total;
	}

}
