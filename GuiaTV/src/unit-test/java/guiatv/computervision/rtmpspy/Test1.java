package guiatv.computervision.rtmpspy;

import guiatv.common.CommonUtility;
import guiatv.computervision.CvUtils;
import guiatv.computervision.Imshow;
import guiatv.persistence.domain.Blob;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class Test1 {
	
	private static String platform = "Windows8.1";
	static String[] rtmpSources = {
			"rtmp://antena3fms35livefs.fplive.net:1935/antena3fms35live-live/stream-lasexta_1",
			"http://antena3-aos1-apple-live.adaptive.level3.net/apple/antena3/channel03/index.m3u8"
			};
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		doRtmpSpying();
	}
	
	public static File doRtmpSpying() {
//		Resource capDirRes = ctx.getResource("META-INF/xmltv/cap");
		URL capDirUrl = Test1.class.getClassLoader().getResource("META-INF/ffmpeg/cap/");
		File capDir = null;
		try {
			capDir = new File(capDirUrl.toURI());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File tmpDir = null;
		tmpDir = new File(capDir.getAbsolutePath()+
				File.separator+"tmp");
		if (tmpDir.exists() == false) { tmpDir.mkdirs(); }
//		Resource binDirRes = ctx.getResource("META-INF/xmltv/grabber/windows_bin/xmltv-0.5.66-win32");
		String binDirStr = "C:/Users/VAIO/GitHub/GuiaTV/bin/META-INF/ffmpeg/windows_bin/ffmpeg-win64/bin";
		File resFile = null, errFile = null;
		resFile = new File("res");
		errFile = new File("C:/Users/VAIO/GitHub/GuiaTV/bin/META-INF/ffmpeg/cap/tmp/errorLog_"+CommonUtility.getDateString()+".txt");
		switch(platform) {
		case "Windows8.1":
			BufferedInputStream in = null;
			try {
				File binDir = new File(binDirStr);
//				ffmpeg -i "rtmp://antena3fms35livefs.fplive.net:1935/antena3fms35live-live/stream-lasexta_1 live=1" -vcodec mjpeg -f image2pipe -pix_fmt yuvj420p -r 1 -
				String[] cmd = { 
						binDir.getAbsolutePath()+File.separator+"ffmpeg.exe",
						"-i", "http://a3live-lh.akamaihd.net/i/antena3_1@35248/master.m3u8", // ********************* TODO: URL
						"-vcodec", "mjpeg",
						"-f", "image2pipe",
						"-pix_fmt", "yuvj420p",
						"-vf", "scale=1280:720",
						"-r", "1",
						"-"
					};
				Process p = null;
				ProcessBuilder pb = new ProcessBuilder(cmd);
				pb.directory(binDir);
				pb.redirectError(errFile);
				
				/////////////////////////
				final int BUFFSIZE = 10240;
				ByteArrayOutputStream data = new ByteArrayOutputStream();
				byte[] readData = new byte[BUFFSIZE];
				boolean skip = true;
				boolean imgReady = false;
				boolean ff = false;
				int readBytes = -1;
				/////////////////////////
				p = pb.start();
				
				in = new BufferedInputStream(p.getInputStream());
		        while ((readBytes = in.read(readData, 0, BUFFSIZE)) != -1) {
		            //
		        	
		        	for (byte b: readData) {
		        		char c = (char) (b & 0xff);
		        		if (ff && c == 0xd8){
		        			skip = false;
		        			data.write(0xff);
		        		}
		        		if (ff && c == 0xd9) {
		        			imgReady = true;
		        			data.write(0xd9);
		        			skip = true;
		        		}
		        		ff = c == 0xff;
		        		if ( ! skip) {
		        			data.write(b);
		        		}
		        		if (imgReady) {
		        			if (data.size() != 0) {
		        				Mat dataMat = new Mat(720, 1280, CvType.CV_8UC1);
		        				dataMat.put(0, 0, data.toByteArray());
		        				Mat mat = Highgui.imdecode(dataMat, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
	        					Highgui.imwrite("imgSpy.jpeg", mat);
		        			}
		        		}
		        	}
		        }
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();	
			} finally {
		        if (in != null) {
		            try { in.close(); } catch(Exception e){}
		        }
		    }
			break;
		default:
			break;
		}
		return resFile;
	}
}
