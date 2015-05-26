package guiatv.persistence.repository;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.LearnedChannel;
import guiatv.persistence.domain.RtmpSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


@Repository
public class LearnedChannelRepositoryImpl implements LearnedChannelRepositoryCustom {
	private static Logger logger = Logger.getLogger("debugLog");
	
	
	// TODO: Poner @Autowired o @PersistenceContext? Y por qué?
	@PersistenceContext
    private EntityManager em;

	@Override
	public boolean isTrained(Channel ch, RtmpSource rtmp) {
		LearnedChannel learnedCh = (LearnedChannel) em.createQuery(
				"Select lc from learnedChannel lc"+
						"where lc.idCh = "+ch.getIdCh()+" and "+
						"lc.rtmpUrl like '"+rtmp.getRtmpUrl()+"'"
				).getSingleResult();

		return learnedCh.isLearned();	
	}

	//	@Override
	//	public void findByOverrridingMethod() {
	//		// TODO Auto-generated method stub
	//		
	//	}

	//	@Override
	//	public void someCustomMethod(Channel ch) {
	//		// TODO Auto-generated method stub
	//		
	//	}




}
