package guiatv.realtime.rtmpspying;

import guiatv.common.CommonUtility;
import guiatv.common.datatypes.Frame_OLD;
import guiatv.computervision.CvUtils;
import guiatv.computervision.Imshow;
import guiatv.persistence.domain.Blob;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.service.AsyncTransactionService;
import guiatv.realtime.rtmpspying.serializable.ChannelData;
import guiatv.realtime.servicegateway.CapturedBlobsGateway;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RtmpSpyingService {
	
	private static final Logger logger = Logger.getLogger("debugLog"); 
	
	private static final int numReqPerSecond = 1;
	@Value("${platform}")
	private String platform;
	
	@Autowired
	ChannelRepository chRep;
	
	@Autowired
	CapturedBlobsGateway capturedBlobsGateway;
	
    @Autowired
    AsyncTransactionService asyncTransactionService;
	
	@Autowired
	MutexMonitor monitor;
	
//	ChannelData chData;
    
	public RtmpSpyingService() {
	}
	
//	public RtmpSpyingService(String saludo) {
//		this.chData = monitor.getAnAvailableChannel();
//		logger.debug(saludo);
//	}
	
	
	@Async("rtmpSpyingTaskExecutor")
	public void doSpying(MLChannel mlChannel) {
		assert(mlChannel.getChannel() != null);
//		ChannelData chData = null;
//		chData = monitor.getAnAvailableChannel();
		URL binDirUrl = this.getClass().getClassLoader()
				.getResource("META-INF/ffmpeg/windows_bin/ffmpeg-win64/bin");
		
		switch(platform) {
		case "Windows8.1":
			BufferedInputStream in = null;
			try {
				File binDir = new File(binDirUrl.toURI());
				String[] cmd = { 
					binDir.getAbsolutePath()+File.separator+"ffmpeg.exe",
					"-r", String.valueOf(numReqPerSecond),
					"-i", "\""+mlChannel.getStreamSource().getUrl()+"\"", // ********************* TODO: URL
					"-vcodec", "mjpeg",
					"-f", "image2pipe",
					"-pix_fmt", "yuvj420p",
					"-vf", "scale="+mlChannel.getImgCols()+":"+mlChannel.getImgRows(),
					"-r", "24",
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
				File errFile = new File("errFile.txt");
				pb.redirectError(errFile);
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
		        				try {
		        					Mat imgGrayMat = CvUtils.getGrayMatAndDecodeFromByteArray(
		        							data.toByteArray(), mlChannel.getImgCols(), mlChannel.getImgRows());
		        					Blob blob = new Blob(imgGrayMat, mlChannel); 
		        					Highgui.imwrite("imgSpy.jpeg", CvUtils.getGrayMatFromByteArray(
		        							blob.getBlob(), blob.getBlobCols(), blob.getBlobRows()));
		        					capturedBlobsGateway.sendBlob(blob);
		        				} catch(Exception e) {
		        					e.printStackTrace();
		        				}
//		        				logger.info("Sending frame from "+this.toString());
		        				
		    
		        				/**
		        				 * DEBUG: Show Image
		        				 */
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
