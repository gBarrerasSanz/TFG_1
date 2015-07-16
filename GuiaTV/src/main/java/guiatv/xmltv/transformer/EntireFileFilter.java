package guiatv.xmltv.transformer;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.springframework.integration.file.filters.AbstractFileListFilter;
import org.w3c.dom.Document;

public class EntireFileFilter<File> extends AbstractFileListFilter<File> {

	private Logger logger=Logger.getLogger("debugLog");
	
	@Override
	protected boolean accept(File file) {
		java.io.File f = (java.io.File) file;
		// TODO: (Arreglar) Solución cutre para no comprobar ficheros que no serían acceptados el RegexPatternFileListFilter
		if (f.getName().equals("xmltv.dtd")) { return false; }
		
		try {
			// Validación que no entiendo
			FileInputStream fStream = new FileInputStream(f);
			//If file is entirely OK the stream can be created
//			logger.debug("File is ready:"+f.getName());
			fStream.close();
			// Mi validación: intentar parsear el dom
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(f);
			return true;
		} catch (Exception e) {
//			logger.debug("File is not ready yet! "+(f).getName());
			return false;
		}
	}

}
