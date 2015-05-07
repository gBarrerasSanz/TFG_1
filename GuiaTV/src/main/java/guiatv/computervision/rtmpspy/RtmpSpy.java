package guiatv.computervision.rtmpspy;

import guiatv.common.CommonUtility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;


public class RtmpSpy {
	
	private enum Platform {
		WINDOWS
	}
	
	Platform platform;
	String[] rtmpSources = {
	"rtmp://antena3fms35livefs.fplive.net:1935/antena3fms35live-live/stream-lasexta_1 live=1"
	};
	
	public RtmpSpy() {
		// TODO: Detectar plataforma e inicializar platform
		platform = Platform.WINDOWS;
	}
	
	public File doRtmpSpying() {
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
		case WINDOWS:
			OutputStream stream = null;
			BufferedInputStream buf = null;
			try {
				File binDir = new File(binDirUrl.toURI());
				String[] cmd = { 
					binDir.getAbsolutePath()+File.separator+"ffmpeg.exe",
					"-i",
					rtmpSources[0],
					"-f jpg",
					"-"
				};
				Process p = null;
				ProcessBuilder pb = new ProcessBuilder(cmd);
				pb.directory(binDir);
				pb.redirectOutput(resFile);
				pb.redirectError(errFile);
				p = pb.start();
//		        p.waitFor();
			    if (p != null) {
			        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
//			        BufferedReader inputFile = new BufferedReader(new InputStreamReader(new FileInputStream(capDirUrl+"")));
			        File jpg = new File("file.jpg");
			        FileInputStream input = new FileInputStream(jpg);
			        buf = new BufferedInputStream(input);
			        int readBytes = 0;
			        while ((readBytes = buf.read()) != -1) {
			            stream.write(readBytes);
			        }
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} finally {
		        if (stream != null) {
		            try { stream.close(); } catch(Exception e){}
		        }
		        if (buf != null) {
		            try { buf.close(); } catch(Exception e){}
		        }
		    }
			break;
		default:
			break;
		}
		return resFile;
	}
}
