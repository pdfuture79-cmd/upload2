
package egovframework.common.util;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.admin.basicinf.service.CommMenu;

/**
 * 트리형태로 변환하는 class
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */
public class UtilTree {
			 
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	/**
	 * 트리 형식으로 가공해서 리턴한다.
	 * @param list
	 * @param itemCnt
	 * @return
	 */
	public static LinkedHashMap<String,String> getTree(List<LinkedHashMap<String,String>> list,int itemCnt) throws Exception{
		LinkedHashMap<String,String> returnmap = new  LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		String neqstartString ="\n<ul>\n<li data=\"#select##level#\" id=\"#id\" class=\"#class\" title=\"#title\">\n";
		String eqstartString ="\n</li>\n<li data=\"#select##level#\" id=\"#id\" class=\"#class\" title=\"#title\">\n"; 
		String levelEnd ="\n</li>\n</ul>";
		String content ="";
		int level = 0;
		String itemCd="";
		String itemNm="";
		int ItemLevel=0;
		int cnt = 0;
		int firstCnt = 0;
		String defaultCheckYn ="";
		for(LinkedHashMap<String,String> map:list){
			itemCd = map.get("ITEM_CD");
			itemNm = map.get("ITEM_NM");
			ItemLevel = Integer.parseInt(map.get("ITEM_LEVEL"));
			defaultCheckYn = map.get("DEFAULT_CHECK_YN");
			String classNm="";
			if(ItemLevel ==1){
				if(firstCnt > 0){
					if(sb.length() > 0){
						for(int i=0; i < level; i++){
							sb.append(levelEnd);
						}
					}
					returnmap.put("data"+0, sb.toString());
					sb= new StringBuffer();
					level = 0;               
				}                          
				firstCnt++;
			}
			if(cnt < list.size()-1){
				if(ItemLevel < Integer.parseInt(list.get(cnt+1).get("ITEM_LEVEL"))){
					classNm ="folder";
				}                                             
			}                            
			cnt++;
			if(level == ItemLevel){//레벨이 같을때
				content = eqstartString;
			}else if(level < ItemLevel){//레벨이 다르면서 기존 레벨 보다 클때
				content = neqstartString;
			}else{//레벨이 다르면서 기존 레벨 보다 작을때  
				int levelMiuns = level - ItemLevel;
				for(int j=0; j < levelMiuns; j++){
					sb.append(levelEnd);
				}
				content = eqstartString;
			}
			addBuffer(sb,UtilString.replace(UtilString.replace(UtilString.replace(UtilString.replace(UtilString.replace(content, "#class", classNm),"#title", itemNm),"#id", itemCd),"#select#","select:"+defaultCheckYn),"#level#", ",level:"+ItemLevel+""),itemNm);
			level = ItemLevel;
		}
				
		if(sb.length() > 0){
			for(int i=0; i < level; i++){
				sb.append(levelEnd);
			}
		}
				
		if(firstCnt == 1){
			returnmap.put("data"+0, sb.toString());
		}else{
			returnmap.put("data"+1, sb.toString());
		}
				
		
		return returnmap;
	}
	
	
	/**
	 * 분류별 형태로 트리를 만들어서 리턴한다.
	 * @param list
	 * @return
	 */
	public static LinkedHashMap<String,String> getCateTree(List<LinkedHashMap<String,String>> list) throws Exception{
		LinkedHashMap<String,String> returnmap = new  LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		String neqstartString ="\n<ul>\n<li data=\"#level#\" id=\"#id\" class=\"#class\" title=\"#title\">\n";
		String eqstartString ="\n</li>\n<li data=\"#level#\" id=\"#id\" class=\"#class\" title=\"#title\">\n"; 
		String levelEnd ="\n</li>\n</ul>";
		String content ="";
		
		int level = 0;
		String itemCd="";
		String itemNm="";
		int ItemLevel=0;
		String itmeType="";
		String openSrv ="";
		for(LinkedHashMap<String,String> map:list){
			itemCd = map.get("ITEM_CD");
			itemNm = map.get("ITEM_NM");
			
			openSrv = UtilString.replace(UtilString.null2Blank(map.get("OPEN_SRV")), "&nbsp;", "");
			ItemLevel = Integer.parseInt(map.get("ITEM_LEVEL"));
			itmeType = map.get("ITEM_TYPE");
			String classNm="";
					
			if(itmeType.equals("C")){
				classNm ="folder";
				itemCd ="";                
			}
			if(level == ItemLevel){//레벨이 같을때
				content = eqstartString;
			}else if(level < ItemLevel){//레벨이 다르면서 기존 레벨 보다 클때
				content = neqstartString;
			}else{//레벨이 다르면서 기존 레벨 보다 작을때  
				int levelMiuns = level - ItemLevel;
				for(int j=0; j < levelMiuns; j++){
					sb.append(levelEnd);
				}
				content = eqstartString;
			}
			addBuffer(sb,UtilString.replace(UtilString.replace(UtilString.replace(UtilString.replace(content, "#class", classNm),"#title", itemNm),"#id", itemCd),"#level#", "level:"+ItemLevel+""),itemNm+openSrv);
			level = ItemLevel;
		}
				
		if(sb.length() > 0){
			for(int i=0; i < level; i++){
				sb.append(levelEnd);
			}
		}
		returnmap.put("data", sb.toString());
		
		return returnmap;
	}
	
	
	/*
	 public static LinkedHashMap<String,String> getCateTree(List<LinkedHashMap<String,String>> list) throws Exception{
		LinkedHashMap<String,String> returnmap = new  LinkedHashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		String neqstartString ="\n<ul>\n<li data=\"openSrv:'#openSrv#'\" id=\"#id\" class=\"#class\" title=\"#title\">\n";
		String eqstartString ="\n</li>\n<li data=\"openSrv:'#openSrv#'\" id=\"#id\" class=\"#class\" title=\"#title\">\n"; 
		String levelEnd ="\n</li>\n</ul>";
		String content ="";
		
		int level = 0;
		String itemCd="";
		String itemNm="";
		int ItemLevel=0;
		String itmeType="";
		String openSrv ="";
		for(LinkedHashMap<String,String> map:list){
			itemCd = map.get("ITEM_CD");
			itemNm = map.get("ITEM_NM");
			
			openSrv = UtilString.replace(UtilString.null2Blank(map.get("OPEN_SRV")), "&nbsp;", "");
			ItemLevel = Integer.parseInt(map.get("ITEM_LEVEL"));
			itmeType = map.get("ITEM_TYPE");
			String classNm="";
					
			if(itmeType.equals("C")){
				classNm ="folder";
				itemCd ="";                
			}
			if(level == ItemLevel){//레벨이 같을때
				content = eqstartString;
			}else if(level < ItemLevel){//레벨이 다르면서 기존 레벨 보다 클때
				content = neqstartString;
			}else{//레벨이 다르면서 기존 레벨 보다 작을때  
				int levelMiuns = level - ItemLevel;
				for(int j=0; j < levelMiuns; j++){
					sb.append(levelEnd);
				}
				content = eqstartString;
			}
			addBuffer(sb,UtilString.replace(UtilString.replace(UtilString.replace(UtilString.replace(content, "#class", classNm),"#title", itemNm),"#id", itemCd),"#openSrv#",openSrv),itemNm);
			level = ItemLevel;
		}
				
		if(sb.length() > 0){
			for(int i=0; i < level; i++){
				sb.append(levelEnd);
			}
		}
		returnmap.put("data", sb.toString());
		
		return returnmap;
	}
	/**
	 * String을 추가한다.
	 * @param sb
	 * @param startString
	 * @param menuNm
	 */
	private static void addBuffer(StringBuffer sb,String startString, String menuNm){
		addBuffer(sb,startString,"",menuNm,"");
	}
	
	/**
	 * String을 추가한다.
	 * @param sb
	 * @param startString
	 * @param url
	 * @param menuNm
	 * @param endString
	 */
	private static void addBuffer(StringBuffer sb,String startString, String url, String menuNm,String endString){
		sb.append(startString);
		sb.append(url);
		sb.append(menuNm);
		sb.append(endString);
	}
	
}
