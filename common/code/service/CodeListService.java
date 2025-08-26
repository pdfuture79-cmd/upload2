package egovframework.common.code.service;

import java.util.List;

import egovframework.admin.basicinf.service.CommCode;
import egovframework.common.grid.ComboIBSVo;



/**
 * 콩통코드를 
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */
public interface CodeListService {
	
    /**
     * 그룹정보를 코드형태로 리턴한다.
     * 
     * @param 조회조건정보 vo
     * @return 그룹정보 List
     * @throws Exception
     */
	
	public void init() throws Exception;
	/**
	 * 코드 리스트를 조회한다.
	 * @param codenm
	 * @return
	 * @throws Exception
	 */
	public List<CommCode> getCodeList(String codenm) throws Exception;
	/**
	 * 목록성 코드 리스트틀 조회한다.
	 * @param codenm
	 * @return
	 * @throws Exception
	 */
	public List<CommCode> getEntityCodeList(String codenm) throws Exception;
	/**
	 * 목록성 코드 리스트를 조회한다.
	 * @param codenm
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public List<CommCode> getEntityCodeList(String codenm,String value) throws Exception;
	/**
	 * 코드를 IbSheet 형태로 조회한다.
	 * @param codenm
	 * @return
	 * @throws Exception
	 */
	public ComboIBSVo getCodeListIBS(String codenm) throws Exception;
	
	/**코드를 IbSheet 형태로 조회한다. 첫번째 리스트는 공백으로 조회
	 * @param codenm
	 * @param firstBlankYn
	 * @return
	 * @throws Exception
	 */
	public ComboIBSVo getCodeListIBS(String codenm,boolean firstBlankYn) throws Exception;
	
	/**
	 *  목록성 코드를 IbSheet 형태로 조회한다.
	 * @param codenm
	 * @return
	 * @throws Exception
	 */
	
	public ComboIBSVo getEntityCodeListIBS(String codenm) throws Exception;
	
	/** 
	 * 목록성 코드를 IbSheet 형태로 조회한다.
	 * @param codenm
	 * @param firstBlankYn
	 * @return
	 * @throws Exception
	 */
	public ComboIBSVo getEntityCodeListIBS(String codenm,boolean firstBlankYn) throws Exception;
	
	/**
	 * 코드를 IbSheet 형태로 조회한다.
	 * @param codenm
	 * @param firstBlankYn
	 * @param langType
	 * @return
	 * @throws Exception
	 */
	ComboIBSVo getCodeListIBS(String codenm, boolean firstBlankYn,
			String langType) throws Exception;
	
	/**
	 * 목록성코드 조회시 다국어로 조회한다.
	 * @param codenm
	 * @param value
	 * @param langType
	 * @return
	 * @throws Exception
	 */
	List<CommCode> getEntityCodeList(String codenm, String value,
			String langType) throws Exception;
	/**
	 * 목록성코드 IbSheet형태 조회시 다국어로 조회한다.
	 * @param codenm
	 * @param firstBlankYn
	 * @param langType
	 * @return
	 * @throws Exception
	 */
	ComboIBSVo getEntityCodeListIBS(String codenm, boolean firstBlankYn,
			String langType) throws Exception;

}
