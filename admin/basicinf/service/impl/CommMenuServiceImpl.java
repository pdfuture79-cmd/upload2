package egovframework.admin.basicinf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.admin.basicinf.service.CommCode;
import egovframework.admin.basicinf.service.CommMenu;
import egovframework.admin.basicinf.service.CommMenuService;
import egovframework.admin.basicinf.service.CommOrg;
import egovframework.admin.basicinf.service.CommUsr;
import egovframework.admin.openinf.service.OpenDs;
import egovframework.admin.openinf.service.OpenDscol;
import egovframework.admin.service.service.OpenInfSrv;
import egovframework.common.WiseOpenConfig;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

/**
 * 메뉴를 관리를 위한 서비스 구현 클래스
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */

@Service("CommMenuService")
public class CommMenuServiceImpl extends AbstractServiceImpl implements CommMenuService {

    @Resource(name = "CommMenuDAO")
    private CommMenuDAO commMenuDAO;

    private static final Logger logger = Logger.getLogger(CommMenuServiceImpl.class.getClass());
	

    public List<CommMenu> selectCommMenuTop(CommUsr commUsr) throws Exception {
		return  commMenuDAO.selectCommMenuList(commUsr);
    }
    
    /**
     * 전체 리스트 조회
     */
    public Map<String, Object> selectMenuListIbPaging(CommMenu commMenu) throws Exception {
    	List<CommMenu> result = commMenuDAO.selectMenuList(commMenu);
		//int cnt = commMenuDAO.selectMenuListCnt(commMenu);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		//map.put("resultCnt", Integer.toString(cnt));
		return map;
    }
    
    /**
     * 전체 리스트 중 키워드 조회
     */
    public Map<String, Object> selectMenuListKeywdIbPaging(CommMenu commMenu) throws Exception {
    	List<CommMenu> result = commMenuDAO.selectMenuListKeywd(commMenu);
    	//int cnt = commMenuDAO.selectMenuListKeywdCnt(commMenu);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("resultList", result);
    	//map.put("resultCnt", Integer.toString(cnt));
    	return map;
    }
    
    /**
     * 단건 상세 조회
     */
    public CommMenu selectMenuListInfo(CommMenu commMenu) throws Exception {
		return commMenuDAO.selectMenuListInfo(commMenu);
    }
    
    /**
     * 메뉴 데이터 저장/수정/삭제
     */
    public int saveCommMenuCUD(CommMenu saveVO, String status) throws Exception{
		int result = 0;
    	
    	if(WiseOpenConfig.STATUS_I.equals(status)){
    		int menuId = commMenuDAO.getMenuId(saveVO); 
    		saveVO.setMenuId(menuId);
        	result = commMenuDAO.insertMenu(saveVO);
    	}else if((WiseOpenConfig.STATUS_U.equals(status))){
        	result = commMenuDAO.updateMenu(saveVO);
    	}else if((WiseOpenConfig.STATUS_D.equals(status))){
        	result = commMenuDAO.deleteMenu(saveVO);
    	}else{
    		result = WiseOpenConfig.STATUS_ERR;
    	}
    	
    	return result;
    }
    
    /**
     * 선택한 행의 하위메뉴 조회
     */
    public Map<String, Object> selectCommLowMenuList(CommMenu commMenu) throws Exception {
    	List<CommUsr> result = commMenuDAO.selectCommLowMenuList(commMenu);
		int cnt = commMenuDAO.selectCommLowMenuListCnt(commMenu);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));
		return map;
    }

    /**
	 * 메뉴정보 트리순서 수정
	 */
	public int commMenuListUpdateTreeOrderCUD(ArrayList<CommMenu> list) throws Exception {
    	int result = 0;
    	for(CommMenu commMenu : list){
        	result = commMenuDAO.commMenuListUpdateTreeOrder(commMenu);
    	}
		return result;
    }
    
    

}
