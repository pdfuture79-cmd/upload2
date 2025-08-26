package egovframework.admin.basicinf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.admin.basicinf.service.CommCode;
import egovframework.admin.basicinf.service.CommCodeService;
import egovframework.admin.opendt.service.OpenCate;
import egovframework.common.WiseOpenConfig;
import egovframework.common.grid.ComboIBSVo;
import egovframework.common.util.UtilString;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

/**
 * 코드 관리를 위한 서비스 구현 클래스
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */

@Service("CommCodeService")
public class CommCodeServiceImpl extends AbstractServiceImpl implements CommCodeService {

    @Resource(name = "CommCodeDAO")
    private CommCodeDAO commCodeDAO;

    private static final Logger logger = Logger.getLogger(CommCodeServiceImpl.class.getClass());

    /**
     * 조건에 맞는 공통코드 목록을 조회 한다.
     * @param CommCode
     * @return Map<String,Object>
     * @throws Exception
     */
    public Map<String, Object> commCodeListAllIbPaging(CommCode commCode) throws Exception {
    	List<CommCode> result = commCodeDAO.selectCommCodeAllList(commCode);
		int cnt = commCodeDAO.selectCommCodeAllListCnt(commCode);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));
		return map;
    }
    
    /**
	 * 공통코드를 단건 조회한다.
	 * 
	 * @param CommCode
	 * @return CommCode
	 * @throws Exception
	 */
    public CommCode selectCommCodeOne(CommCode commCode) throws Exception {
		return  commCodeDAO.selectCommCodeOne(commCode);
    }
    
    /**
   	 * 공통코드를 등록,변경,삭제한다.
   	 * 
   	 * @param CommCode
   	 * @param String
   	 * @return Integer
   	 * @throws Exception
   	 */
       public int saveCommCodeCUD(CommCode commCode,String status) throws Exception {
       	int result = 0;
       	if((WiseOpenConfig.STATUS_D.equals(status))){
       		if(commCode.getGrpIs().equals("Y") && commCode.getGrpCib().equals("Y")){// 그룹코드이며 하위코드가 존재할때
       			result = commCodeDAO.deleteCommCode(commCode);
       			result = commCodeDAO.deleteSubCommCode(commCode);
       		}else{
       			result = commCodeDAO.deleteCommCode(commCode);
       		}
       	}
       	
       	if(WiseOpenConfig.STATUS_I.equals(status)){
       			if(commCodeDAO.selectCommCodeCheckDup(commCode) > 0) //중복체크
       				return -1;
       			result = commCodeDAO.insertCommCode(commCode);
       		}
   		if(WiseOpenConfig.STATUS_U.equals(status)){
   			if(commCode.getGrpIs().equals("Y") && commCode.getGrpCib().equals("Y")){// 그룹코드이며 하위코드가 존재할때
   				result = commCodeDAO.updateCommCode(commCode);
   				result = commCodeDAO.updateSubCommCode(commCode);
   			}else{
       			result = commCodeDAO.updateCommCode(commCode);
   			}
       	}
   		return result;
       }
       
    /**
	 * 공통코드 중복을 체크한다.
	 * 
	 * @param CommCode
	 * @return Integer
	 * @throws Exception
	 */
    public int commCodeCheckDup(CommCode commCode) throws Exception {
    	int result = 0;
    	if(commCodeDAO.selectCommCodeCheckDup(commCode) > 0){
			return -1;
		}
    	return result;
    }
    
    /**
	 * 공통코드 순서를 변경 한다.
	 * 
	 * @param ArrayList<CommCode>
	 * @return Integer
	 * @throws Exception
	 */
    public int commCodeOrderBySave(ArrayList<CommCode> list) throws Exception {
    	int result = 0;
    	for(CommCode commCode : list){
        	result = commCodeDAO.updateOrderby(commCode);
    	}
		return result;
    }
    
	/**
     * 그룹 코드 목록을 조회 한다.(팝업)
     * @param CommCode 
     * @return Map<CommCode> 
     * @throws Exception
     */
    public Map<String, Object> commCodeGrpCdListIbPaging(CommCode commCode) throws Exception {
		List<CommCode> result = commCodeDAO.selectGrpcodeList(commCode);
		int cnt = commCodeDAO.selectGrpcodeListCnt(commCode);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList",result);
		map.put("resultCnt", Integer.toString(cnt));
		return map;
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * 조건에 맞는 코드 목록을 조회 한다.(분류관리 등록시 표준맵핑 선택에 사용)
     * @param CommCode
     * @return Map<String,Object>
     * @throws Exception
     */
    public Map<String, Object> selectOpenCateDitcList(CommCode commCode) throws Exception {
		List<CommCode> result = commCodeDAO.selectOpenCateDitcList(commCode);
		int cnt = commCodeDAO.selectOpenCateDitcListCnt(commCode);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));
		return map;
    }
    
    
}
