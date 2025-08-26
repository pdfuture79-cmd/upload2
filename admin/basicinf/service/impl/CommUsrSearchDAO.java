package egovframework.admin.basicinf.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.admin.basicinf.service.CommUsrSearch;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

/**
 * 직원정보 조회 DAO
 * @author KJH
 * @since 2014.07.23
 */
@Repository("CommUsrSearchDAO")
public class CommUsrSearchDAO extends EgovComAbstractDAO {


	/**
	 * 조직 조회
	 * @param commUsrSearch
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
	public List<CommUsrSearch> orgList(CommUsrSearch commUsrSearch) throws Exception {
    	return list("CommUsrSearchDAO.orgList", commUsrSearch);
    }
    
    /**
     * 직원 조회
     * @param commUsrSearch
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommUsrSearch> usrList(CommUsrSearch commUsrSearch) throws Exception {
    	return list("CommUsrSearchDAO.usrList", commUsrSearch);
    }
    
    
}
