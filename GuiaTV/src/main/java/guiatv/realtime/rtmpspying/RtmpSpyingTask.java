package guiatv.realtime.rtmpspying;

import guiatv.common.CommonUtility;
import guiatv.common.datatypes.Frame;
import guiatv.cv.classificator.Imshow;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.RtmpSource;
import guiatv.persistence.repository.AsyncTransactionService;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.RtmpSourceRepository;
import guiatv.realtime.servicegateway.CapturedFramesGateway;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.annotation.Transactional;



public class RtmpSpyingTask implements Runnable {
	
	private static final Logger logger = Logger.getLogger("debugLog"); 
	
	@Value("${platform}")
	private String platform;
	
	@Autowired
	ChannelRepository chRep;
	
	@Autowired
	RtmpSourceRepository rtmpRep;
	
	@Autowired
	CapturedFramesGateway capturedFramesGateway;
	
    @Autowired
    AsyncTransactionService asyncTransactionService;
    
	private String nameIdCh;
	private String rtmpUrl;
	
	
	
	public RtmpSpyingTask(String nameIdCh, String rtmpUrl) {
		this.nameIdCh = nameIdCh;
		this.rtmpUrl = rtmpUrl;
	}
	
	
	
//	@Transactional(readOnly=false)
	@Override
	public void run() throws InstantiationError {
		
//		Channel ch = chRep.findByNameIdCh(nameIdCh);
//		RtmpSource rtmpSource = rtmpRep.findByChannelAndRtmpUrl(ch, rtmpUrl);
		
		RtmpSource rtmpSource = asyncTransactionService.
				getRtmpSourceFromNameIdChAndRtmpUrl(nameIdCh, rtmpUrl);
		
//		List<RtmpSource> ListrtmpSource = rtmpRep.findAll();
		
		
		
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
					"-i", rtmpSource.getRtmpUrl()+" live=1",
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
		        				Frame frame = new Frame(data.toByteArray(), rtmpSource, new Date());
//		        				logger.info("Sending frame from "+this.toString());
		        				capturedFramesGateway.sendFrame(frame);
		        				
//		        				Mat dataMat = new Mat(512, 512, CvType.CV_8UC1);
//		        				dataMat.put(0, 0, data.toByteArray());
//		        				Mat frameMat = Highgui.imdecode(dataMat, 1);
		        		        
//		        				im.showImage(frameMat);
//		        				Message<?> frameMsg = MessageBuilder.
//		        						withPayload(frameMat).build();
		        				
		        				
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
