package xmltv.grabber;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import common.CommonUtility;


public class XMLTVGrabber {
	
	private enum Platform {
		WINDOWS
	}
	
	Platform platform;
	String[] xmltvSources = {
			"tv_grab_es_laguiatv"
	};
	
	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
	CommonUtility utils;
	
	public XMLTVGrabber() {
		// TODO: Detectar plataforma e inicializar platform
		platform = Platform.WINDOWS;
	}
	
	public File doGrabbing() {
		Resource capDirRes = ctx.getResource("/META-INF/xmltv/cap");
		File tmpDir = null;
		try {
			tmpDir = new File(capDirRes.getFile().getAbsolutePath()+
					File.separator+"tmp");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		if (tmpDir.exists() == false) { tmpDir.mkdirs(); }
		Resource binDirRes = ctx.getResource("/META-INF/xmltv/grabber/windows_bin/xmltv-0.5.66-win32");
		File resFile = null, errFile = null;
		resFile = new File(tmpDir.getAbsolutePath()+
				File.separator+"xmltvDump_"+utils.getDateString()+".xml");
		errFile = new File(tmpDir.getAbsolutePath()+
				File.separator+"errorLog_"+utils.getDateString()+".txt");
		switch(platform) {
		case WINDOWS:
			try {
				File binDir = binDirRes.getFile();
				String[] cmd = { 
					binDir.getAbsolutePath()+File.separator+"xmltv.exe",
					xmltvSources[0]		
				};
				Process p = null;
				ProcessBuilder pb = new ProcessBuilder(cmd);
				pb.directory(binDir);
				pb.redirectOutput(resFile);
				pb.redirectError(errFile);
				p = pb.start();
		        p.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {

			}
			break;
		default:
			break;
		}
		return resFile;
	}
}
