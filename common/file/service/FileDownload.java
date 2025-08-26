package egovframework.common.file.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.servlet.view.AbstractView;

import egovframework.common.util.UtilString;

public class FileDownload extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> map,
			HttpServletRequest req, HttpServletResponse res)
			throws Exception {

		String fileName = (String) map.get("fileName");		//사용자에게 보여질 저장파일명 
		File file = (File) map.get("file");
		boolean bEncode = (Boolean) map.get("bEncode") == null ? true : false;
		res.setContentType("application/download;");
		int length = (int) file.length();
		res.setContentLength(length);
		
		String userAgent = req.getHeader("User-Agent").toUpperCase();
		boolean ie = userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("TRIDENT") > -1;
		
		String browser = getBrowser(req);
		
		//브라우저 체크   --------- 기존 기재부 (로컬용)
//		if ( true ) {
//			if ( ie ) {
//				fileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20").replaceAll("\\+", "%20");
//			} else {
//				fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1").replace("+", "%20").replaceAll("\\+", "%20");
//			}
//		}
		
		
		
		
		/*if ( userAgent.indexOf("Windows") > -1 ) {	//윈도우 일경우
			if ( userAgent.indexOf("Firefox") > -1 ) {	//파이어폭스
				System.out.println("WINDOW.FIREROX");
				fileName = URLDecoder.decode(fileName, "UTF-8");
			} else if ( userAgent.indexOf("Safari") > -1 ) {	//사파리
				System.out.println("WINDOW.SAFARI");
				fileName = URLDecoder.decode(fileName, "UTF-8");
			} else {
				System.out.println("WINDOW.ETC");
				fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			}
		} else if ( userAgent.indexOf("Android") > -1 ) {	//모바일
			if ( userAgent.indexOf("Firefox") > -1 ) {	//파이어폭스
				System.out.println("MOBILE.FIREROX");
				fileName = URLDecoder.decode(fileName, "UTF-8");
			} else {
				System.out.println("MOBILE.ETC");
				fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			}
		}
		*/
		
		res.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
//		res.setHeader("Content-Disposition", "attachment;" + "filename=\"" + fileName + "\";");
		res.setHeader("Content-Disposition", getDisposition(fileName, browser));
		res.setHeader("Content-Transfer-Encoding","binary;");
		res.setHeader("pragma","no-cache;" );
		FileInputStream fis = null;
		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;
		try {
		/*	int temp;
			fis = new FileInputStream(file);
			while ( (temp = fis.read()) != -1 ) {
				out.write(temp);
			}*/
			byte[] buffer = new byte[1024];
			
			fin = new BufferedInputStream(new FileInputStream(file));
			outs = new BufferedOutputStream(res.getOutputStream());
			int read =0;
			while((read = fin.read(buffer)) != -1){
				outs.write(buffer,0,read);
			}
		} catch(FileNotFoundException fne) {//시큐어코딩 조치 - Exception to String
        	logger.debug(ExceptionUtils.getStackTrace(fne)); 
		} catch(IOException ioe) {//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(ioe)); 
		} catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(e)); 
		} finally {
			if ( fis != null ) {
				try {
					fis.close();
				} catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(e)); 
					throw e;
				}
			}
			
			if ( fin != null ) {
				try {
					fin.close();
				} catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(e)); 
					throw e;
				}
			}
			if ( outs != null ) {
				try {
					outs.close();
				} catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(e)); 
					throw e;
				}
			}
		}
	}
	
	/*----------------- 경기도청 서버용 인코딩 -------------------------*/
	/**
	* 브라우저 종류
	* @param request
	* @return
	*/
	public String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1 || header.indexOf("TRIDENT") > -1 ) {
			return "MSIE";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Safari") > -1) {
			return "Safari";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}
	
	/**
	* 다국어 파일명 처리
	* @param filename
	* @param browser
	* @return
	* @throws UnsupportedEncodingException
	*/
	public String getDisposition(String filename, String browser) throws UnsupportedEncodingException {
		String dispositionPrefix = "attachment;filename=";
		String encodedFilename = null;
		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "utf-8").replace("+", "%20").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			StringBuffer sb = new StringBuffer();
			sb.append('"');
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				sb.append(c);
			}
			sb.append('"');
			encodedFilename = sb.toString();
		} else if (browser.equals("Opera")) {
			encodedFilename = "" + new String(filename.getBytes("UTF-8"), "8859_1") + "";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else if (browser.equals("Safari")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				sb.append(c);
			}
			encodedFilename = sb.toString();
		} else {
			throw new RuntimeException("Not supported browser");
		}
		return dispositionPrefix + encodedFilename;
	}

	/*----------------- 경기도청 서버용 인코딩 -------------------------*/
}
