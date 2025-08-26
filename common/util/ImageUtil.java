package egovframework.common.util;
/*
 * ImageUtil.java 
 * 
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 */
  
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import egovframework.common.imageConv.AnimatedGifEncoder;
import egovframework.common.imageConv.GifDecoder;
 
public class ImageUtil {
	private static final Logger logger = Logger.getLogger(ImageUtil.class);
 
  public static void createThumbnail(String load,String save,String type,int w,int h) {
 
    try {
      BufferedInputStream stream_file = new BufferedInputStream(new FileInputStream(load));
      createThumbnail(stream_file,save,type,w,h);
    } catch (Exception e) {//시큐어코딩 조치 - Exception to String
    	logger.debug(ExceptionUtils.getStackTrace(e)); 
    	logger.error(e);
    }
 
  }
 
  public static void createThumbnail(BufferedInputStream stream_file,String save,String type,int w,int h) {
    try {
 
    if (StringUtils.equals(StringUtils.lowerCase(type),"gif")) {
      getGifImageThumbnail(stream_file,save,type,w,h);
    } else {
      getImageThumbnail(stream_file,save,type,w,h);
    }
 
    } catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(e)); 
    	logger.error(e);
    }
 
  }
 
  public static void getImageThumbnail(BufferedInputStream stream_file,String save,String type,int w,int h) {
 
    try {
      File  file = new File(save);
      BufferedImage bi = ImageIO.read(stream_file);
 
      int width = bi.getWidth();
      int height = bi.getHeight();
      if (w < width) { width = w; }
      if (h < height) { height = h; }
 
      BufferedImage bufferIm = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
       
      Image atemp = bi.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
 
      Graphics2D  g2 = bufferIm.createGraphics();
      g2.drawImage(atemp, 0, 0, width, height, null);
      ImageIO.write(bufferIm, type, file);
    } catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(e)); 
      logger.error(e);
    }
 
  }
 
  public static void getGifImageThumbnail(BufferedInputStream stream_file,String save,String type,int w,int h) {
 
      GifDecoder dec = new GifDecoder();
      AnimatedGifEncoder enc = new AnimatedGifEncoder();
      dec.read(stream_file);
 
      int cnt = dec.getFrameCount();
 
      int delay = 0;
      int width = 0;
      int height = 0;
 
      try{
        enc.start(save);
        enc.setRepeat(0);
 
        for (int i = 0; i < cnt; i++) {
        BufferedImage frame = dec.getFrame(i);
        delay = dec.getDelay(i);
 
        width = frame.getWidth();
        height = frame.getHeight();
        if (w < width) { width = w; }
        if (h < height) { height = h; }
 
        BufferedImage destimg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = destimg.createGraphics();
 
        g.drawImage(frame, 0, 0, width, height, null);
        enc.setDelay(delay);
 
        enc.addFrame(destimg);
        }
 
        enc.finish();
      }catch(Exception ex){//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(ex)); 
        logger.error(ex);
      }
 
  }
 
}