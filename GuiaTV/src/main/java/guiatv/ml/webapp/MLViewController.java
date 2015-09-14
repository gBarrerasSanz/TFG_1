package guiatv.ml.webapp;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.blobFrame;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MyCh;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.persistence.repository.service.BlobService;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.realtime.rtmpspying.MonitorMyCh;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Controller // ATENCIÓN: CON @RestController NO FUNCIONA
@RequestMapping("/ml")
public class MLViewController {
	
	@Autowired
	ChannelService chServ;
	@Autowired
	MonitorMyCh monitorMyCh;
	@Autowired
	BlobService blobServ;
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String viewHomePage(Model model)
	{
		model.addAttribute("header", "My header message");
		return "home";
	}
	
	@RequestMapping(value = "/adminChannels", method = RequestMethod.GET)
	public String adminChannels(Model model)
	{
		List<MyCh> lMyCh = monitorMyCh.getListMyCh();
		model.addAttribute("lMyCh", lMyCh);
		return "adminChannels";
	}
	
	@RequestMapping(value = "/blobClassification/{hashIdChBusiness}", method = RequestMethod.GET)
	public String blobClassification(Model model, Pageable pageable,
			@PathVariable(value="hashIdChBusiness") String hashIdChBusiness)
	{
		PageWrapper<blobFrame> page = null;
		Channel ch = chServ.findByHashIdChBusiness(hashIdChBusiness, true);
		if (ch == null) {
			return null;
		}
		MyCh myCh = monitorMyCh.getByChannel(ch);
		if (myCh == null) {
			return null;
		}
		page = new PageWrapper<blobFrame>(blobServ.findByChannel(myCh.getChannel(), pageable), "/ml/blobClassification");
		page.setUrl(page.getUrl()+"/"+hashIdChBusiness+"/");
		model.addAttribute("page", page);
		return "blobClassification";
	}
	
	@RequestMapping(value = "/blobClassification", method = RequestMethod.GET)
	public String blobClassificationAllChannels(Model model, Pageable pageable)
	{
		PageWrapper<blobFrame> page = null;
		page = new PageWrapper<blobFrame>(blobServ.findAll(pageable), "/ml/blobClassification");
		model.addAttribute("page", page);
		return "blobClassification";
		
	}
	
	
	@RequestMapping(
			value="/blobImg/{idBlobPersistence}", 
			method = RequestMethod.GET, 
			produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
    public ResponseEntity<byte[]> getBlobImg(@PathVariable("idBlobPersistence") Long idBlobPersistence) throws IOException {
        blobFrame blob = blobServ.findOneByIdBlobPersistence(idBlobPersistence);
        byte[] blobImgContent = CvUtils.convertByteArrayToPngDecodeable(blob.getBlobImg(), blob.getBlobCols(), blob.getBlobRows());
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(blobImgContent, headers, HttpStatus.OK);
        
//        return new ResponseEntity<byte[]>(imageContent, HttpStatus.OK);
    }
	
}
