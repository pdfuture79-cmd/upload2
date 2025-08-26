package egovframework.admin.basicinf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.admin.basicinf.service.CommOrg;
import egovframework.admin.basicinf.service.CommOrgService;
import egovframework.admin.opendt.service.OpenCate;
import egovframework.admin.opendt.service.OpenInfTcolItem;
import egovframework.admin.openinf.service.OpenInf;
import egovframework.common.WiseOpenConfig;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

/**
 * 조직정보 관리를 위한 ServiceImpl 클래스
 * @author KJH
 * @since 2014.07.17
 */

@Service("CommOrgService")
public class CommOrgServiceImpl extends AbstractServiceImpl implements CommOrgService {

    @Resource(name = "CommOrgDAO")
    private CommOrgDAO CommOrgDAO;
    
    private static final Logger logger = Logger.getLogger(CommOrgServiceImpl.class.getClass());
	
 
    /**
     * 조직정보 팝업
     */
    public List<CommOrg> selectCommOrgAll(CommOrg commOrg) throws Exception {
		return  CommOrgDAO.selectCommOrgListAll(commOrg);
    }


    /**
     * 조직정보 전체조회
     */
	@Override
	public Map<String, Object> commOrgListTree(CommOrg commOrg) throws Exception {
		List<CommOrg> result = CommOrgDAO.selectCommOrgListTree(commOrg); 
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		return map;
	}

	
	/**
	 * 조직코드 중복체크
	 */
	@Override
	public int commOrgCdDup(CommOrg commOrg) throws Exception {
		int result=0;
		if (CommOrgDAO.selectCommOrgCdDup(commOrg) > 0) {
			return -1;
		}
		return result;
	}

	
	/**
	 * 조직정보 CUD
	 */
	@Override
	public int saveCommOrgCUD(CommOrg commOrg, String status) throws Exception{
		int result = 0;
    	if((WiseOpenConfig.STATUS_D.equals(status))){
        	result = CommOrgDAO.deleteLowCommOrg(commOrg);
        	
    	} else if ( WiseOpenConfig.STATUS_I.equals(status) ) {
    		if(CommOrgDAO.selectCommOrgCdDup(commOrg) > 0){ //중복체크
				return -1;
			} else {
				result = CommOrgDAO.insertCommOrg(commOrg);
			}
    		
    	} else if ( WiseOpenConfig.STATUS_U.equals(status) ) {
    		if ( CommOrgDAO.updateCommOrg(commOrg) > 0 )  {
    			
    			List<CommOrg> UpdOrg = CommOrgDAO.getOrgFullNmQuery(commOrg); //조직명 fullname 조회한다.
				for(int i=0;i<UpdOrg.size();i++){
					result = CommOrgDAO.actOrgFullNmUpd(UpdOrg.get(i)); //변경된 조직명 fullname을 update한다.
				}
    			
    			if ( commOrg.getUseYn().equals("N") ) {
    				result = CommOrgDAO.updateLowUseYnCommOrg(commOrg);
    			} else {
    				return 1;
    			}
    		} else {
    			return -1;
    		}
    	}
		return result;
	}


	/**
	 * 조직정보 단건 조회
	 */
	//@Override
	//public CommOrg commOrgRetr(CommOrg commOrg) throws Exception {
	//	return CommOrgDAO.commOrgRetr(commOrg);
	//}
	
	/**
	 * 조직정보 단건 조회
	 */
	public List<CommOrg> commOrgRetr(CommOrg commOrg) throws Exception {
		return CommOrgDAO.commOrgRetr(commOrg);
    }
	
	/**
	 * 조직정보 트리순서 수정
	 */
	public int commOrgListUpdateTreeOrderCUD(ArrayList<CommOrg> list) throws Exception {
    	int result = 0;
    	for(CommOrg commOrg : list){
        	result = CommOrgDAO.commOrgListUpdateTreeOrder(commOrg);
    	}
		return result;
    }
	



	
    
}
