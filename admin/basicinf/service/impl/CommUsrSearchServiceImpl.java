package egovframework.admin.basicinf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import egovframework.admin.basicinf.service.CommUsr;
import egovframework.admin.basicinf.service.CommUsrSearch;
import egovframework.admin.basicinf.service.CommUsrSearchService;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

/**
 * 직원정보 조회 ServiceImpl
 * @author KJH
 * @since 2014.07.23
 */

@Service("CommUsrSearchService")
public class CommUsrSearchServiceImpl extends AbstractServiceImpl implements CommUsrSearchService {

    @Resource(name = "CommUsrSearchDAO")
    private CommUsrSearchDAO CommUsrSearchDAO;

    private static final Logger logger = Logger.getLogger(CommUsrSearchServiceImpl.class.getClass());
   
    /**
     * 조직 조회 
     */
    public Map<String, Object> selectCommOrgSearchAllIbPaging(CommUsrSearch commUsrSearch) throws Exception {
    	List<CommUsrSearch> result = CommUsrSearchDAO.orgList(commUsrSearch);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		return map;
    }
    
    /**
     * 직원 조회
     */
    public Map<String, Object> selectCommUsrSearchAllIbPaging(CommUsrSearch commUsrSearch) throws Exception {
    	List<CommUsrSearch> result = CommUsrSearchDAO.usrList(commUsrSearch);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("resultList", result);
    	return map;
    }
   
}
