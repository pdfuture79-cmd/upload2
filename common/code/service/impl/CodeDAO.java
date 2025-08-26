package egovframework.common.code.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.admin.basicinf.service.CommCode;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

/**
 * 코드 관리를 위한 데이터 접근 클래스
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */
@Repository("CodeDAO")
public class CodeDAO extends EgovComAbstractDAO {

    /**
     * 조건에 맞는 코드 목록을 조회 한다.
     * @param CommCode
     * @return List<CommCode>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectCodeList() throws Exception {
	return list("CodeDAO.selectCodeList",null);
    }
    
    /**
     * 조건에 맞는 코드 목록을 조회 한다.
     * @param CommCode
     * @return List<CommCode>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectEntityOrgCodeList() throws Exception {
	return list("CodeDAO.selectEntityOrgCodeList",null);
    }
    
    /**
     * 조건에 맞는 코드 목록을 조회 한다.
     * @param CommCode
     * @return List<CommCode>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectEntityFiltCodeList(String value) throws Exception {
	return list("CodeDAO.selectEntityFiltCodeList",value);
    }
    
    /**
     * 조건에 맞는 코드 목록을 조회 한다.
     * @param CommCode
     * @return List<CommCode>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectEntityCateNmCodeList(String value) throws Exception {
	return list("CodeDAO.selectEntityCateNmCodeList",value);
    }
    
    /**
     * 조건에 맞는 코드 목록을 조회 한다.
     * @param CommCode
     * @return List<CommCode>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectCodeDtlList(String value) throws Exception {
    	return list("CodeDAO.selectCodeDtlList",value);
    }
    
    /**
     * 조건에 맞는 코드 목록을 조회 한다.
     * @param CommCode
     * @return List<CommCode>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectEntityItemCdCodeList(String value) throws Exception {
	return list("CodeDAO.selectEntityItemCdCodeList",value);
    }
    
    /**
     * 조건에 맞는 코드 목록을 조회 한다.
     * @param CommCode
     * @return List<CommCode>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectUnitSubCdCodeList(String value) throws Exception {
	return list("CodeDAO.selectUnitSubCdCodeList",value);
    }
    
    
    /**
     * 그룹코드 목록을 조회한다.(그룹코드만 조회)
     * @param CommCode
     * @return List<CommCode>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectEntityGrpCdCodeList(String value) throws Exception {
	return list("CodeDAO.selectEntityGrpCdCodeList",null);
    }
    
    
    /**
     * 팝업VIEW 목록을 조회한다.(팝업VIEW 조회)
     * @param CommCode
     * @return List<CommCode>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectEntityTblCdCdCodeList(String value) throws Exception {
	return list("CodeDAO.selectEntityTblCdCdCodeList",null);
    }
    
    /**
     * 차트 BAR 
     * @param value
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectSeriesCdBarList(String value) throws Exception {
	return list("CodeDAO.selectSeriesCdBarList",null);
    }
    
    /**
     * 차트 PIE
     * @param value
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommCode> selectSeriesCdPieList(String value) throws Exception {
    	return list("CodeDAO.selectSeriesCdPieList",null);
    }
    
    
}
