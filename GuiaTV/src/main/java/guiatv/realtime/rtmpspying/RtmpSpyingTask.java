package guiatv.realtime.rtmpspying;

import guiatv.common.CommonUtility;
import guiatv.cv.classificator.Imshow;

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


public class RtmpSpyingTask implements Runnable {
	
	@Value("${platform}")
	private String platform;
	
	@Autowired
	protected MessageChannel rtmpSpierChOut;
	
	protected String rtmpSource;
	
	public RtmpSpyingTask(String rtmpSource) {
		this.rtmpSource = rtmpSource;
	}

	@Override
	public void run() throws InstantiationError {
		
		if (rtmpSource == null) {
			throw new InstantiationError("No rtmpSource set");
		}
		URL binDirUrl = this.getClass().getClassLoader()
				.getResource("META-INF/ffmpeg/windows_bin/ffmpeg-win64/bin");
		
		switch(platform) {
		case "Windows8.1":
			BufferedInputStream in = null;
			try {
				File binDir = new File(binDirUrl.toURI());
				String[] cmd = { 
					binDir.getAbsolutePath()+File.separator+"ffmpeg.exe",
					"-i", rtmpSource+" live=1",
					"-vcodec", "mjpeg",
					"-f", "image2pipe",
					"-pix_fmt", "yuvj420p",
					"-r", "1",
					"-"
				};
				Process p = null;
				ProcessBuilder pb = new ProcessBuilder(cmd);
				pb.directory(binDir);
				
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
//				Imshow im = new Imshow("Image");
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
		        		        
//		        				im.showImage(frameMat);
		        				Message<?> frameMsg = MessageBuilder.
		        						withPayload(frameMat).build();
		        				rtmpSpierChOut.send(frameMsg);
		        				
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
		
	}
}
