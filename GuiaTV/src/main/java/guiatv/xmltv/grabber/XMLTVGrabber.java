package guiatv.xmltv.grabber;

import guiatv.common.CommonUtility;
import guiatv.computervision.Imshow;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;


public class XMLTVGrabber {
	
	static Logger logger = Logger.getLogger("debugLog");
	
	private enum Platform {
		WINDOWS
	}
	
	Platform platform;
	String[] xmltvSources = {
			"tv_grab_es_laguiatv"
	};
	
	public XMLTVGrabber() {
		// TODO: Detectar plataforma e inicializar platform
		platform = Platform.WINDOWS;
	}
	
	public File doGrabbing() {
//		Resource capDirRes = ctx.getResource("META-INF/xmltv/cap");
		URL capDirUrl = this.getClass().getClassLoader().getResource("META-INF/xmltv/cap");
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
				.getResource("META-INF/xmltv/grabber/windows_bin/xmltv-0.5.67-win32");
		
		File resFile = null, errFile = null;
		/**
		 * Asignar nombre en función de la fecha de obtención
		 */
//		resFile = new File(tmpDir.getAbsolutePath()+
//				File.separator+"xmltvDump_"+CommonUtility.getDateString()+".xml");
		/**
		 * Asignar nombre (el mismo para todos, con lo que se reemplazará)
		 */
		resFile = new File(tmpDir.getAbsolutePath()+
				File.separator+"xmltvDump.xml");
		errFile = new File(tmpDir.getAbsolutePath()+
				File.separator+"errorLog_"+CommonUtility.getDateString()+".txt");
		switch(platform) {
		case WINDOWS:
//			BufferedInputStream in = null;
			BufferedReader in = null;
			Writer out = null; 
			try {
				File binDir = new File(binDirUrl.toURI());
				String[] cmd = { 
					binDir.getAbsolutePath()+File.separator+"xmltv.exe",
					xmltvSources[0]		
				};
				Process p = null;
				ProcessBuilder pb = new ProcessBuilder(cmd);
				pb.directory(binDir);
				
//				pb.redirectOutput(resFile);
				pb.redirectError(errFile);
//				p = pb.start();
//		        p.waitFor();
/////////////////////////
//				final int BUFFSIZE = 10240;
//				byte[] readData = new byte[BUFFSIZE];
//				int readBytes = -1;
				/////////////////////////
				p = pb.start();
				out = new BufferedWriter(new OutputStreamWriter(
					    new FileOutputStream(resFile), "UTF-8"));
				in = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
				String readLine = null;
		        while ((readLine = in.readLine()) != null) {
		        	// ELIMINAR LOS CARACTERES NO-ASCII (http://stackoverflow.com/questions/2869072/remove-non-utf-8-characters-from-xml-with-declared-encoding-utf-8-java)
//		        	readLine = readLine.replaceAll("[^\\x20-\\x7e]", "");
		        	out.write(readLine+"\n");
//		        	logger.debug(readData.toString());
		        }
		        
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
		return resFile;
	}
}
