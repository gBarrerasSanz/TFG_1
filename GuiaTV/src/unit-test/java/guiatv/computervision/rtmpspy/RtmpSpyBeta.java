package guiatv.computervision.rtmpspy;

import guiatv.common.CommonUtility;
import guiatv.computervision.Imshow;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


public class RtmpSpyBeta {
	
//	@Value("${platform}") // TODO: Averiguar por que no funciona 
	String platform = "Windows8.1";
	
	@Autowired
	MessageChannel rtmpSpyChOut;
	
	//	String platform = "Windows8.1";
	
	String[] rtmpSources = {
	"rtmp://antena3fms35livefs.fplive.net:1935/antena3fms35live-live/stream-lasexta_1",
	"rtmp://antena3fms35livefs.fplive.net/antena3fms35live-live/stream-antena3_1"
	};
	
	public RtmpSpyBeta() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public boolean doRtmpSpying() {
//		Resource capDirRes = ctx.getResource("META-INF/xmltv/cap");
		URL capDirUrl = this.getClass().getClassLoader().getResource("META-INF/ffmpeg/cap");
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
		URL binDirUrl = this.getClass().getClassLoader()
				.getResource("META-INF/ffmpeg/windows_bin/ffmpeg-win64/bin");
		
		File resFile = null, errFile = null;
		resFile = new File(tmpDir.getAbsolutePath()+
				File.separator+"xmltvDump_"+CommonUtility.getDateString()+".xml");
		errFile = new File(tmpDir.getAbsolutePath()+
				File.separator+"errorLog_"+CommonUtility.getDateString()+".txt");
		switch(platform) {
		case "Windows8.1":
			BufferedInputStream in = null;
			try {
				File binDir = new File(binDirUrl.toURI());
//				ffmpeg -i "rtmp://antena3fms35livefs.fplive.net:1935/antena3fms35live-live/stream-lasexta_1 live=1" -vcodec mjpeg -f image2pipe -pix_fmt yuvj420p -r 1 -
				String[] cmd = { 
					binDir.getAbsolutePath()+File.separator+"ffmpeg.exe",
					"-i", rtmpSources[1]+" live=1",
					"-vcodec", "mjpeg",
					"-f", "image2pipe",
					"-pix_fmt", "yuvj420p",
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
				Imshow im = new Imshow("Image");
				in = new BufferedInputStream(p.getInputStream());
		        while ((readBytes = in.read(readData, 0, BUFFSIZE)) != -1) {
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
		        				Mat dataMat = new Mat(512, 512, CvType.CV_8UC1);
		        				dataMat.put(0, 0, data.toByteArray());
		        				Mat frameMat = Highgui.imdecode(dataMat, 1);
		        		        
		        		        Highgui.imwrite("a3.jpeg", frameMat);
		        				im.showImage(frameMat);
//		        				System.out.println("Done");
		        				System.exit(0);
//		        				Message<?> frameMsg = MessageBuilder.
//		        						withPayload(frameMat).build();
//		        				rtmpSpyChOut.send(frameMsg);
		        				
		        		        // Reinicializar 
		        		        skip = true;
		        				imgReady = false;
		        				ff = false;
		        		        data.reset();
		        			}
		        		}
		        	}
		        }
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
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
		return true;
	}
}
