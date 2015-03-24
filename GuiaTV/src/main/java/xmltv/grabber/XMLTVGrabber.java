package xmltv.grabber;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

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
	
	public XMLTVGrabber() {
		// TODO: Detectar plataforma e inicializar platform
		platform = Platform.WINDOWS;
	}
	
	public File doGrabbing() {
		Resource capDir = ctx.getResource("/META-INF/xmltv/cap/tmp");
		Resource binDirRes = ctx.getResource("/META-INF/xmltv/grabber/windows_bin/xmltv-0.5.66-win32");
		File resFile = null, errFile = null;
		try {
			resFile = new File(capDir.getFile().getAbsolutePath()+
					File.separator+"xmltvDump_"+getDateString()+".xml");
			errFile = new File(capDir.getFile().getAbsolutePath()+
					File.separator+"errorLog_"+getDateString()+".txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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

	private String getDateString() {
		Date date = new Date();
		final Locale SPAIN_LOCALE = new Locale("es", "ES");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", SPAIN_LOCALE);
		return formatter.format(date);
	}
}
