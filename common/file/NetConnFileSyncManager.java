package egovframework.common.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.jcraft.jsch.SftpException;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.common.util.SFTPSyncManager;
import egovframework.common.util.UtilZip;

/** CronTab batch
 * 내부망 서버에서 넘어온 ZIP 파일을 관리하는 클래스.
 * 수집서버에서 압축 풀고 1번 2번 서버에 보낸다.
 * @author hsJang
 *
 */
public class NetConnFileSyncManager {
	
	private static final Logger log = Logger.getLogger(NetConnFileSyncManager.class.getName());
	private final boolean logger = false;
	
	/**
	 * 작업 시작
	 */
	public void run() {
		log("망연계 파일 동기화 작업 시작");
		File[] zipFileList = getZipFileList();
		if(zipFileList != null) {
			log("zip 파일 갯수 : " + zipFileList.length);
			if(zipFileList.length > 0) {
				for(File zipFile : zipFileList) {
					zipFileWork(zipFile);
				}
			}
		} else {
			log("폴더가 존재하지 않습니다.");
		}
		log("망연계 파일 동기화 작업 종료");
	}
	
	/**
	 * zip 파일 목록 조회
	 * @return
	 */
	private File[] getZipFileList() {
		File[] zipFiles = new File(EgovProperties.getProperty("FileNetConn.receiveDir")).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				String ext = name.substring(name.lastIndexOf(".")+1).toLowerCase();
				if("zip".equals(ext)) {
					return true;
				}
				return false;
			}
		});
		return zipFiles;
	}
	
	/**
	 * zip 파일 압축 해제
	 * @param zipFile
	 */
	private void zipFileWork(File zipFile) {
		log("["+zipFile.getName() + "] 파일 작업 시작");
		
		String fileName = zipFile.getName().substring(0, zipFile.getName().lastIndexOf("."));
		String localPath = fileName.replaceAll("_", "/").replaceAll(EgovProperties.getProperty("FileNetConn.rootPath0"), EgovProperties.getProperty("FileNetConn.rootPath1"));
		log("0 : localpath : " + localPath);
		UtilZip uz = new UtilZip();
		try {
			uz.unzip(zipFile, new File(localPath));
		} catch (IOException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
		}
		// 파일 전송
		transferFile(localPath);
		// 작업 후 zipFile 삭제
		zipFile.delete();
		log("["+zipFile.getName() + "] 파일 작업 종료");
	}
	
	/**
	 * 파일 was1, was2에 전송
	 * @param path
	 */
	private void transferFile(String path) {
		String targetPath = path.replaceAll(EgovProperties.getProperty("FileNetConn.rootPath1"), EgovProperties.getProperty("FileNetConn.rootPath2"));
		log("1 : targetPath : " + targetPath);
		String subStr = EgovProperties.getProperty("FileNetConn.rootPath2")+"/ggopen";
		if(targetPath.startsWith(subStr)) {
			targetPath = EgovProperties.getProperty("FileNetConn.rootPath2")+targetPath.substring(subStr.length());
		}
		try {
			SFTPSyncManager sm = new SFTPSyncManager();
			// was1 에 업로드
			sm.init(EgovProperties.getProperty("FileNetConn.was1")
					, Integer.parseInt(EgovProperties.getProperty("FileNetConn.port1"))
					, EgovProperties.getProperty("FileNetConn.user1")
					, EgovProperties.getProperty("FileNetConn.pass1"));
			log("was1에 전송 시작");
			sm.uploadSync(path, targetPath);
			sm.disconnect();
			log("was1에 전송 완료");
			// was2 에 업로드
			sm.init(EgovProperties.getProperty("FileNetConn.was2")
					, Integer.parseInt(EgovProperties.getProperty("FileNetConn.port2"))
					, EgovProperties.getProperty("FileNetConn.user2")
					, EgovProperties.getProperty("FileNetConn.pass2"));
			log("was2에 전송 시작");
			sm.uploadSync(path, targetPath);
			sm.disconnect();
			log("was2에 전송 완료");
		} catch (SftpException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void log(String message) {
		if(logger) {
			log.info(message);
		}
	}
	
	public static void main(String[] args) {
		new NetConnFileSyncManager().run();
		
		/**
		 * java -classpath /usr/bin/apache-tomcat-7.0.64/ggopen/wiseopen/WEB-INF/classes:/usr/bin/apache-tomcat-7.0.64/ggopen/wiseopen/WEB-INF/lib/jsch-0.1.53.jar:/usr/bin/apache-tomcat-7.0.64/ggopen/wiseopen/WEB-INF/lib/commons-compress-1.1.jar egovframework.common.file.NetConnFileSyncManager
		 */
	}
}
