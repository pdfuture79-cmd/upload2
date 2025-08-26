package egovframework.admin.basicinf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * 메뉴에 서비스를 정의하기 위한 서비스 인터페이스
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */

public interface CommMenuService {
	/**
	 * 관리자 상단 메뉴를 조회한다.
	 * @param commUsr
	 * @return
	 * @throws Exception
	 */
	public List<CommMenu> selectCommMenuTop(CommUsr commUsr) throws Exception;
	
	/**
	 * 전체 리스트 조회
	 * @param commMenu
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectMenuListIbPaging(CommMenu commMenu) throws Exception;
	
	/**
	 * 전체 리스트에서 키워드 조회
	 * @param commMenu
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectMenuListKeywdIbPaging(CommMenu commMenu) throws Exception;
	
	/**
	 * 단건 상세조회
	 * @param commMenu
	 * @return
	 * @throws Exception
	 */
	public CommMenu selectMenuListInfo(CommMenu commMenu) throws Exception;
	
	/**
	 * 데이터 저장/수정/삭제
	 * @param saveVO
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public int saveCommMenuCUD(CommMenu saveVO, String status) throws Exception;
	
	/**
	 * 선택한 행의 하위 메뉴 조회
	 * @param commMenu
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectCommLowMenuList(CommMenu commMenu) throws Exception;
	
	/**
	 * 메뉴정보 메뉴순서 수정
	 * @return
	 */
	public int commMenuListUpdateTreeOrderCUD(ArrayList<CommMenu> list) throws Exception;
}
