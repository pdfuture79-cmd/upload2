package egovframework.common.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.exception.ExceptionUtils;

import egovframework.com.cmm.service.EgovProperties;

/**
 * 포탈에 공개되고 있는 대용량 데이터셋을 CSV 파일로 만들어 준다.
 * @author mjk
 *
 */
public class DatasetCsvCreate {

	private static final Logger log = Logger.getLogger(DatasetCsvCreate.class.getName());

	private final boolean logger = true;
	
	//private final String ENCODING = "UTF-8";  // LINUX
	//private final String ENCODING = "EUC-KR";   // WINDOWS
	
	// 작업 년월일
	private final String SYSDATE = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
	
	// 작업 디렉토리 및 인코딩 정보
	//private String workingDir = "/usr/bin/apache-tomcat-7.0.64/ggopen/job_shell/csvTsf/working/";
	private String  workingDir = EgovProperties.getProperty("Globals.CsvCreateFilePath");
	String tmpEncoding = EgovProperties.getProperty("Globals.CsvCreateEncoding");
	private final String ENCODING = (tmpEncoding.equals(""))? "UTF-8" : tmpEncoding;
	
	// target table 정보
	private String tableName = "";
	private String strWhere = "";
	
	public DatasetCsvCreate(String tName, String sWhere) {
		this.tableName = tName;
		this.strWhere = sWhere;
	}
	
	public void run() {
		if(this.tableName.equals("")) {
			LOG("대상 데이터셋 정보를 입력하세요.");
			return;
		}
		if(this.workingDir.equals("")) {
			LOG("Globals.CsvCreateFilePath 값을 정의하세요.");
			return;
		}
		LOG("=========================================================");
		LOG("데이터셋 CSV 생성 작업 시작 >> 인코딩 : (" + ENCODING + "), table : " + this.tableName  + " WHERE " + this.strWhere);

		// 출력 DIR 확인
		LOG("데이터셋 CSV Output  >> Path : " + this.workingDir);
		String filePath = workingDir;// + SYSDATE + File.separator;
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		// 대상 데이터셋 정보 추출
		List<Map<String, String>> list = getDatasetList(this.tableName);
		if (list.size() <= 0) {
			LOG("처리할 대상이 없습니다." + this.tableName );
			LOG("=========================================================\n");
			insertJobLog(-1, "처리할 대상이 없습니다." + this.tableName );
			return;
		}
		int csvFileSuccessCnt = 0;  // 성공 건수
		int rowCnt = 0; // 처리건수 
			// information_YYYYMMDD.csv 파일 생성 안함
			//BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "information_" + SYSDATE + ".csv"), ENCODING));
			//writer.write("DS_ID,CATE_NM,INF_TAG,INF_NM,INF_EXP,CCL_NM,LAST_DTTM,FILE_NM\r\n");			

		// Csv File 생서
		for(Map<String, String> data : list) {
			String inf_id = data.get("INF_ID");
			String owner = data.get("OWNER");
			String dsId = data.get("DS_ID");
			String fileNm = data.get("FILE_NM").replaceAll("[\\\\\\/\\?\\*\\[\\]]", "");// 툭수문자 제거
			fileNm = fileNm.replace(" ", "");
			//LOG(">>> DS_ID : " + owner + "." + dsId + " Csv 작업시작");
			rowCnt = createCsvFileByDataset(inf_id, owner, dsId, this.strWhere,  fileNm);
			csvFileSuccessCnt = csvFileSuccessCnt + rowCnt;
			//LOG("<<< DS_ID : " + owner + "." + dsId + " Csv 작업종료 >" + fileNm);				
			//createListInfo(data, writer);  // information_YYYYMMDD.csv
			//writer.close(); // information_YYYYMMDD.csv
			
			// table_information_YYYYMMDD.csv 파일 생성 안함
			// createTableInformation();
			// table_information_YYYYMMDD.csv 파일 생성 END
		}

		
		insertJobLog(csvFileSuccessCnt, "데이터셋 CSV 생성 성공");
		LOG("데이터셋 CSV 생성 종료 rows=" + rowCnt);
		LOG("=========================================================\n");
	}

	/**
	 * 데이터셋 목록 (특정 DATASET)
	 * @return
	 */
	private List<Map<String, String>> getDatasetList(String tName) {
		StringBuffer sql = new StringBuffer();
		/*
		sql.append(" SELECT  ");
		sql.append(" 	  A.OWNER ");		
		sql.append(" 	, A.TABLE_NAME AS DS_ID");
		sql.append(" 	, A.TABLE_NAME || '_"+SYSDATE+"' || '.csv' AS FILE_NM");
		sql.append(" FROM ALL_TABLES A ");
		sql.append(" WHERE A.OWNER in ('GGOPENGOV', 'GGOPENPTL', 'GGOPENCLT') ");		
		sql.append(" 	AND A.TABLE_NAME = '").append(tName).append("'");
		*/
		// 실제 서비스 기준
		sql.append(" SELECT A.INF_ID, E.OWNER_CD as OWNER, E.DS_ID ");
		sql.append(" 	, A.INF_NM || '_"+SYSDATE+"' || '.csv' AS FILE_NM ");
		sql.append(" 	FROM TB_OPEN_INF A  JOIN TB_OPEN_INF_SRV B ON A.INF_ID = B.INF_ID  AND A.INF_STATE = 'Y' AND B.SRV_YN = 'Y' and B.SRV_CD = 'S'  ");
		sql.append(" 		JOIN TB_OPEN_DS E ON E.DS_ID = A.DS_ID AND E.USE_YN = 'Y'  ");
		sql.append(" 	WHERE A.OPEN_DTTM <= SYSDATE  ");
		sql.append("	AND A.DS_ID = '").append(tName).append("'");
		//LOG(sql.toString());
		DBConn db = new DBConn();
		List<Map<String, String>> list = db.selectList(sql.toString(), null);
		db.close();
		
		return list;
	}
	
	/**
	 * 데이터셋 목록 (서비스 대상 - 사용안함)
	 * @return
	 */
	/*
	private List<Map<String, String>> getDatasetList() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT");
		sql.append(" 	B.DS_ID");
		sql.append(" 	, D.CATE_NM || ' > ' || C.CATE_NM AS CATE_NM");
		sql.append(" 	, B.INF_TAG");
		sql.append(" 	, B.INF_NM");
		sql.append(" 	, B.INF_EXP");
		sql.append(" 	, E.DITC_NM AS CCL_NM");
		sql.append(" 	, NVL(TO_CHAR(B.LOAD_DTTM, 'YYYY-MM-DD'), '2015-09-23') AS LAST_DTTM");
		sql.append(" 	, DS_ID || '_"+SYSDATE+"' || '.csv' AS FILE_NM");
		sql.append(" FROM ("); 
		sql.append(" 	SELECT ");
		sql.append(" 		A.INF_ID AS INF_ID,");
	    sql.append("         SUM(A.VIEW_CNT) AS VIEW_CNT");
		sql.append(" 	FROM TB_OPEN_INF_SRV A"); 
		sql.append(" 		JOIN TB_OPEN_INF B ON B.INF_ID = A.INF_ID"); 
		sql.append(" 		JOIN TB_OPEN_CATE C ON C.CATE_ID = B.CATE_ID ");
		sql.append(" 		JOIN TB_OPEN_CATE D ON D.CATE_ID = C.CATE_ID_TOP ");
		sql.append(" 		LEFT JOIN TB_OPEN_DS E ON E.DS_ID = B.DS_ID");
		sql.append(" 	WHERE A.SRV_YN = 'Y'");
		sql.append(" 		AND A.SRV_CD IN ('S', 'C', 'M', 'F', 'A', 'L')");
		sql.append(" 		AND B.INF_STATE = 'Y'");
		sql.append(" 		AND B.OPEN_DTTM <= SYSDATE");
		sql.append(" 		AND C.USE_YN = 'Y'");
		sql.append(" 		AND D.USE_YN = 'Y'");
		sql.append(" 		AND CASE A.SRV_CD WHEN 'F' THEN 'Y' WHEN 'L' THEN 'Y' ELSE E.USE_YN END = 'Y'");
		sql.append(" 		AND CASE A.SRV_CD WHEN 'F' THEN 'RAW' WHEN 'L' THEN 'RAW' ELSE E.DS_CD END = 'RAW'");
		sql.append(" 	GROUP BY A.INF_ID");
		sql.append(" 	) A"); 
		sql.append(" 	JOIN TB_OPEN_INF B ON B.INF_ID = A.INF_ID"); 
		sql.append(" 	JOIN TB_OPEN_CATE C ON C.CATE_ID = B.CATE_ID ");
		sql.append(" 	JOIN TB_OPEN_CATE D ON D.CATE_ID = C.CATE_ID_TOP");
		sql.append(" 	LEFT JOIN TB_COMM_CODE E ON E.GRP_CD = 'D1008' AND E.DITC_CD = B.CCL_CD");
		sql.append(" WHERE");
		sql.append(" 	B.DS_ID IS NOT NULL");		
		sql.append(" ORDER BY A.VIEW_CNT DESC		");
		
		DBConn db = new DBConn();
		List<Map<String, String>> list = db.selectList(sql.toString(), null);
		db.close();
		
		return list;
	}
	*/
	
	/**
	 * 데이터셋 데이터를 CSV 파일로 추출
	 * @param dsId
	 */
	private int createCsvFileByDataset(String inf_id, String owner, String dsId, String sWhere, String fileNm) {
		// 컬럼명 얻기
		List<Map<String, String>> columnNames = getDatasetColumnNames(inf_id);
		
		StringBuffer csvHeader = new StringBuffer();
		for(int i = 0 ; i < columnNames.size() ; i ++) {
			csvHeader.append(columnNames.get(i).get("COLUMN_DESC") + ",");
		}       
		csvHeader.delete(csvHeader.length()-1, csvHeader.length());
		csvHeader.append("\r\n");
		
		String filePath = workingDir; // + SYSDATE + File.separator;
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		// Data 얻기
		BufferedWriter writer = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath + fileNm);
			osw = new OutputStreamWriter(fos, ENCODING) ;
			writer = new BufferedWriter(osw);
			writer.write(csvHeader.toString());
			int rowCnt = getDatasetData(owner, dsId, sWhere, columnNames, writer);

			return rowCnt;
		} catch (IOException e) {//시큐어코딩 조치 - Exception to String
			String msg = e.getLocalizedMessage();
			LOG(" ERROR >>> " + msg);
			LOG("=========================================================\n");
			if (msg.length() > 300) {
				msg = msg.substring(0, 300);
			}
			insertJobLog(-1, msg);
			return -1;
		}finally{
			try{
				if(writer!=null){writer.close();}
				if(osw!=null){osw.close();}
				if(fos!=null){fos.close();}
			}catch(IOException e){//시큐어코딩 조치 - Exception to String
					String msg = e.getLocalizedMessage();
					LOG(" ERROR >>> " + msg);
					LOG("=========================================================\n");
					if (msg.length() > 300) {
						msg = msg.substring(0, 300);
					}
					insertJobLog(-1, msg);
			}
		}
	}

	/**
	 * 데이터셋 컬럼명 목록
	 * @param dsId
	 * @return
	 */
	private List<Map<String, String>> getDatasetColumnNames(String inf_id) {
			StringBuffer sql = new StringBuffer();
			// String sql = "SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE OWNER = '" + owner +"' AND TABLE_NAME = '"+dsId+"'";
			//	sql.append(" SELECT C.INF_ID, C.INF_SEQ, C.COL_SEQ, D.COL_ID, D.COL_NM ");
			sql.append(" SELECT D.COL_ID as COLUMN_NAME, D.COL_NM as COLUMN_DESC ");  
		 	sql.append(" FROM TB_OPEN_INF A JOIN TB_OPEN_INF_SCOL C ON A.INF_ID = C.INF_ID ");
		 	sql.append(" 	JOIN TB_OPEN_DSCOL D ON A.DS_ID = D.DS_ID AND C.COL_SEQ = D.COL_SEQ ");
		 	sql.append(" WHERE A.INF_ID = '").append(inf_id).append("'");
		 	sql.append(" AND C.USE_YN = 'Y' AND C.VIEW_YN = 'Y' ");
		 	sql.append(" ORDER BY C.V_ORDER ");
		 	
		DBConn db = new DBConn();
		List<Map<String, String>> list = db.selectList(sql.toString(), null);
		db.close();
		
		return list;
	}
	
	/**
	 * 데이터셋 데이터 조회
	 * @param dsId
	 * @return
	 * @throws IOException 
	 */
	private int getDatasetData(String owner, String dsId, String sWhere, List<Map<String, String>> columnNames, BufferedWriter writer) {
		int rowCnt = 0;
		StringBuffer sql = new StringBuffer();
		// DATA SELECT 
		sql.append(" SELECT ");
		for(int i = 0 ; i < columnNames.size() ; i ++) {
			sql.append(columnNames.get(i).get("COLUMN_NAME") + ",");
		}
		sql.delete(sql.length()-1, sql.length());
		sql.append(" FROM " + owner + "."+dsId);
		if (!sWhere.equals("")) {
			sql.append(" WHERE ").append(sWhere);
		}
		//LOG("Data Select =" + sql.toString());
		
		DBConn dbConn = new DBConn();		
		Connection conn = dbConn.getConnection();		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
				
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				rowCnt =  rowCnt + 1;
				StringBuffer sb = new StringBuffer();
				for(Map<String, String> entry: columnNames){
					if(entry.get("COLUMN_NAME") == null){
						sb.append("" + ",");
					}else{
						String value = rs.getString(entry.get("COLUMN_NAME"));
						//만건당 로그 출력
						if ((rowCnt % 10000) == 0 ) {
							if (entry.get("COLUMN_NAME").equals("SIGUN_NM")) {
								LOG(" " + rowCnt + " rows..." + entry.get("COLUMN_NAME") + "=" + value);
							}	
						}
		                sb.append(csvParser(value) + ",");
					}
				}                   
				sb.delete(sb.length()-1, sb.length());
				sb.append("\r\n");
				
				writer.write(sb.toString());
			}
			return rowCnt;	
		} catch (SQLException e) {//시큐어코딩 조치
			LOG("ERROR:" + e.getLocalizedMessage());
			return -1;						
		} catch (IOException e) {//시큐어코딩 조치
			LOG("ERROR:" + e.getLocalizedMessage());
			return -1;						
		} finally {
			try {
				if(pstmt != null) { pstmt.close(); }
				if(rs != null) { rs.close(); 	}
				if(dbConn != null) {	dbConn.close();	}
			} catch (SQLException e) {LOG("ERROR:" + e.getLocalizedMessage());}
		
		}
	}
	
	/**
	 * csv 파일 및 데이터셋 정보
	 * @param data
	 * @param writer
	 * @throws IOException
	 */	
	/*
	private void createListInfo(Map<String, String> data, BufferedWriter writer) throws IOException {
		String dsId = data.get("DS_ID");
		String cateNm = data.get("CATE_NM");
		String infTag = data.get("INF_TAG");
		String infNm = data.get("INF_NM");
		String infExp = data.get("INF_EXP");
		String cclNm = data.get("CCL_NM");
		String lastDttm = data.get("LAST_DTTM");
		String fileNm = data.get("FILE_NM");
		
		writer.write(csvParser(dsId) + ","
				+ csvParser(cateNm) + ","
				+ csvParser(infTag) + ","
				+ csvParser(infNm) + ","
				+ csvParser(infExp) + ","
				+ csvParser(cclNm) + ","
				+ csvParser(lastDttm) + ","
				+ csvParser(fileNm) + "\r\n"
				);
	}
	*/
	
	/**
	 * 테이블 정보 csv 파일 생성
	 */
	/*
	private void createTableInformation() {
		LOG(">>> table_information_"+SYSDATE+".csv 만들기 시작");
		String csvHeader = "TABLE_NAME,TABLE_COMMENTS,COLUMN_NAME,COMMENTS,TYPE,NULLABLE,PK_YN\r\n";
		
		String filePath = workingDir + SYSDATE + File.separator;
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "table_information_"+SYSDATE+".csv"), ENCODING));
			writer.write(csvHeader);
			getTableInfoList(writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG("<<< table_information_"+SYSDATE+".csv 만들기 종료");		
	}
	*/
	/*
	private void getTableInfoList(BufferedWriter writer) throws IOException {

		StringBuffer sql = new StringBuffer();
		sql.append(" 		SELECT    ");
		sql.append("         A.TABLE_NAME");
		sql.append("      ,  D.COMMENTS AS TABLE_COMMENTS");
		sql.append("      ,  A.COLUMN_NAME");
		sql.append("      , B.COMMENTS");
		sql.append("      ,  CASE WHEN A.DATA_TYPE = 'NUMBER' THEN A.DATA_TYPE||'('||A.DATA_PRECISION||','||A.DATA_SCALE||')'");
		sql.append("                  WHEN A.DATA_TYPE = 'DATE' THEN A.DATA_TYPE");
		sql.append("                  ELSE  A.DATA_TYPE||'('||A.DATA_LENGTH||')'");
		sql.append("         END TYPE");
		sql.append("      ,  CASE WHEN A.NULLABLE = 'N' THEN 'NOT NULL' ELSE 'NULL' END NULLABLE");
		sql.append("      ,  CASE WHEN C.TABLE_NAME IS NULL THEN 'No' ELSE 'Yes' END PK_YN");
		sql.append(" FROM  ALL_TAB_COLUMNS A");
		sql.append("         INNER JOIN ALL_COL_COMMENTS B");
		sql.append("                     ON A.TABLE_NAME = B.TABLE_NAME AND");
		sql.append("                          A.COLUMN_NAME = B.COLUMN_NAME");
		sql.append("         INNER JOIN ALL_TAB_COMMENTS D");
		sql.append("                     ON A.TABLE_NAME = D.TABLE_NAME");
		sql.append("         LEFT OUTER JOIN (SELECT * FROM ALL_CONS_COLUMNS WHERE CONSTRAINT_NAME LIKE '%PK%' AND OWNER = 'GGOPENGOV') C");
		sql.append("                     ON A.TABLE_NAME = C.TABLE_NAME AND");
		sql.append("                          A.COLUMN_NAME = C.COLUMN_NAME");
		sql.append(" WHERE A.OWNER = 'GGOPENGOV'");
		sql.append(" AND B.OWNER = 'GGOPENGOV'");
		sql.append(" AND D.OWNER = 'GGOPENGOV'");
		sql.append(" AND A.TABLE_NAME IN (");
		sql.append(" 							   SELECT");
		sql.append(" 									B.DS_ID");
		sql.append(" 								FROM ("); 
		sql.append(" 									SELECT"); 
		sql.append(" 										A.INF_ID AS INF_ID,");
		sql.append(" 								    	SUM(A.VIEW_CNT) AS VIEW_CNT");
		sql.append(" 									FROM TB_OPEN_INF_SRV A"); 
		sql.append(" 										JOIN TB_OPEN_INF B ON B.INF_ID = A.INF_ID"); 
		sql.append(" 										JOIN TB_OPEN_CATE C ON C.CATE_ID = B.CATE_ID ");
		sql.append(" 										JOIN TB_OPEN_CATE D ON D.CATE_ID = C.CATE_ID_TOP ");
		sql.append(" 										LEFT JOIN TB_OPEN_DS E ON E.DS_ID = B.DS_ID");
		sql.append(" 									WHERE A.SRV_YN = 'Y'");
		sql.append(" 										AND A.SRV_CD IN ('S', 'C', 'M', 'F', 'A', 'L')");
		sql.append(" 										AND B.INF_STATE = 'Y'");
		sql.append(" 										AND B.OPEN_DTTM <= SYSDATE");
		sql.append(" 										AND C.USE_YN = 'Y'");
		sql.append(" 										AND D.USE_YN = 'Y'");
		sql.append(" 										AND CASE A.SRV_CD WHEN 'F' THEN 'Y' WHEN 'L' THEN 'Y' ELSE E.USE_YN END = 'Y'");
		sql.append(" 										AND CASE A.SRV_CD WHEN 'F' THEN 'RAW' WHEN 'L' THEN 'RAW' ELSE E.DS_CD END = 'RAW'");
		sql.append(" 									GROUP BY A.INF_ID");
		sql.append(" 									) A"); 
		sql.append(" 									JOIN TB_OPEN_INF B ON B.INF_ID = A.INF_ID"); 
		sql.append(" 									JOIN TB_OPEN_CATE C ON C.CATE_ID = B.CATE_ID ");
		sql.append(" 									JOIN TB_OPEN_CATE D ON D.CATE_ID = C.CATE_ID_TOP");
		sql.append(" 									LEFT JOIN TB_COMM_CODE E ON E.GRP_CD = 'D1008' AND E.DITC_CD = B.CCL_CD");
		sql.append(" 								WHERE");
		sql.append(" 									B.DS_ID IS NOT NULL	)");
		sql.append(" ORDER  BY A.TABLE_NAME, A.COLUMN_ID");		

		DBConn dbConn = new DBConn();
		
		Connection conn = dbConn.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
				
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				StringBuffer data = new StringBuffer();
				data.append(csvParser(rs.getString("TABLE_NAME")) + ",");
				data.append(csvParser(rs.getString("TABLE_COMMENTS")) + ",");
				data.append(csvParser(rs.getString("COLUMN_NAME")) + ",");
				data.append(csvParser(rs.getString("COMMENTS")) + ",");
				data.append(csvParser(rs.getString("TYPE")) + ",");
				data.append(csvParser(rs.getString("NULLABLE")) + ",");
				data.append(csvParser(rs.getString("PK_YN")) + ",");
				data.append("\r\n");
				
				writer.write(data.toString());
			}
			pstmt.close();
			rs.close();
				
		} catch (SQLException e) {
//			e.printStackTrace();
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) { pstmt.close(); }
				if(rs != null) { rs.close(); 	}
			} catch (SQLException e) {
//				e.printStackTrace();
				e.printStackTrace();
			}
		}
		dbConn.close();
	}
	*/
	
	private String csvParser(String value) {
		value = value == null?"":value;
		
		if (value.indexOf("\r") >= 0 || value.indexOf("\n") >= 0) {
		    value = value.replaceAll("\\r", "").replaceAll("\\n", " ");
		}
		
        if (value.indexOf("\"") >= 0 || value.indexOf(",") >= 0) {
        	value = "\"" + value.replaceAll("\\\"", "\"\"") + "\"";
        }
		return value;
	}

	/**
	 * 배치 작업 로그
	 */
	private void insertJobLog(int fileCnt, String msg) {
		StringBuffer insert = new StringBuffer();
		insert.append(" INSERT INTO TB_LOG_JOB	(JOB_NO,JOB_NM, JOB_TAG,SOURCE,TARGET,ROWCOUNT,MESSAGE, REG_DTTM)");
		insert.append(" SELECT"); 
		insert.append("		SQ_LOG_JOB.NEXTVAL as JOB_NO"); 
		insert.append("		  ,'DatasetCsvCreate.java' "); 
		insert.append("		  , 'Crontab' "); 
		insert.append("		  , '' "); 
		insert.append("		  , '' "); 
		if (fileCnt <= 0) {
			insert.append("		  , ? ");
		}else {
			insert.append("		  , ? ");
		}
		insert.append("		  , '").append(msg).append("'"); 
		insert.append("		  , SYSDATE from dual "); 

		DBConn db = new DBConn();
		db.insert(insert.toString(), new Object[]{fileCnt});
		db.close();  // 추가
	}
	
	/**
	 * DB 연결 클래스
	 * @author User
	 *
	 */
	public static class DBConn {
		
		Connection conn = null;
		
		public DBConn() {
			String driverClass = EgovProperties.getProperty("Globals.DriverClassName");
			String url = EgovProperties.getProperty("Globals.Url");
			String userName = EgovProperties.getProperty("Globals.UserName");
			String password = EgovProperties.getProperty("Globals.Password");
			
			try {
	            Class.forName(driverClass);
	            conn = DriverManager.getConnection(url, userName, password);
			} catch(Exception e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e)); 
			}
		}
		
		public List<Map<String, String>> selectList(String sql, Object[] params) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			try {
				pstmt = conn.prepareStatement(sql);
				if(params != null) {
					for(int i = 0 ; i < params.length ; i ++) {
						pstmt.setObject(i+1, params[i]);
					}
				}
				rs = pstmt.executeQuery();
				
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCnt = rsmd.getColumnCount();
				while(rs.next()) {
				    Map<String, String> data = new HashMap<String, String>();
				    for(int i = 1; i <= colCnt; i++){
				        String columnName = rsmd.getColumnLabel(i).toUpperCase();
				        data.put(columnName, rs.getString(columnName));
				    }
				    result.add(data);
				}
			} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if(pstmt != null) { pstmt.close(); }
					if(rs != null) { rs.close(); 	}
				} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
				}
			}
			return result;
		}
		
		public int dsCount(String owner, String dsId, String sWhere) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuffer sql = new StringBuffer();
			int count = 0;
			try {
				sql.append("SELECT COUNT(*) TOTAL_CNT FROM " + owner + "."+dsId);
				if (!sWhere.equals("")) {
					sql.append(" WHERE ").append(sWhere);
				}

				pstmt = conn.prepareStatement(sql.toString());
				rs = pstmt.executeQuery();
				while(rs.next()) {
					count = rs.getInt("TOTAL_CNT");
				}
				
			} catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if(pstmt != null) { pstmt.close(); }
					if(rs != null) { rs.close(); 	}
				} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
				}
			}
			return count;
		}

		public void insert(String insert, Object[] params) {
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(insert);
				if(params != null) {
					for(int i = 0 ; i < params.length ; i ++) {
						pstmt.setObject(i+1, params[i]);
					}
				}
				pstmt.executeUpdate();
			} catch (SQLException e) {		//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			} finally{
				try{
					if(pstmt!=null){pstmt.close();}
				}catch(SQLException e){//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		
		public Connection getConnection() {
			return this.conn;
		}
		
		public void close() {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}
	
	public void LOG(Object obj) {
		if(logger) {
			//log.info(obj.toString());
			System.out.println("[INFO] " + obj.toString());
		}
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.out.print("arguments 갯수가 맞지 않습니다. \nUSAGE : DatasetCsvCreate TABLE_NAME where_cond(option)");
			return;
		}
		
		String tName = args[0];
		String sWhere = "";
		if(args.length > 1) {			
			sWhere = args[1];
		}	
		new DatasetCsvCreate(tName, sWhere).run();
	}
}
