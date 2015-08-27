package guiatv.ml.webapp;

import guiatv.catalog.datatypes.ListChannels;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.repository.ChannelRepository;
import guiatv.realtime.rtmpspying.MutexMonitor;
import guiatv.realtime.rtmpspying.serializable.ChannelData;
import guiatv.realtime.rtmpspying.serializable.ListChannelsData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Controller // ATENCIÓN: CON @RestController NO FUNCIONA
@RequestMapping("/ml")
public class MLViewController {
	
	@Autowired
	ChannelRepository chRep;
	
	@Autowired
	MutexMonitor monitor;
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String viewHomePage(Model model)
	{
		model.addAttribute("header", "My header message");
		return "home";
	}
	
	@RequestMapping(value = "/adminChannels", method = RequestMethod.GET)
	public String adminChannels(Model model)
	{
//		ListChannels lCh = chRep.findAll();
//		ListChannelsData lChData = new ListChannelsData();
//		for (Channel ch: lCh) {
//			lChData.add()
//		}
		List<ChannelData> lChData = monitor.readListChannelData();
		model.addAttribute("lChData", lChData);
		return "adminChannels";
	}
	
}
