//package guiatv.persistence.repository;
//
//import guiatv.persistence.domain.Channel;
//import guiatv.persistence.domain_NOT_USED.LearnedRtmpSource;
//import guiatv.persistence.domain_NOT_USED.RtmpSource;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Types;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
//import org.apache.log4j.Logger;
//import org.hibernate.service.spi.InjectService;
//import org.springframework.beans.DirectFieldAccessor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementCreator;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.stereotype.Repository;
//
//
//@Repository
//public class LearnedRtmpSourceRepositoryImpl implements LearnedRtmpSourceRepositoryCustom {
//	private static Logger logger = Logger.getLogger("debugLog");
//	
//	// TODO: Poner @Autowired o @PersistenceContext? Y por qué?
//	@PersistenceContext
//    private EntityManager em;
//
//	@Override
//	public boolean isTrained(RtmpSource rtmpSource) {
//		return true;
////		Query query = em.createQuery(
////				"SELECT a FROM Account a"
////                + "WHERE a.username LIKE ?1 "
////                +"OR a.namefirst LIKE ?1 "
////                +"OR a.namelast LIKE ?1");
////        query.setParameter(1, "%"+check+"%");
////        List accounts = query.getResultList();
////        if (accounts.size() == 0) {
////            return null;
////        } else {
////            return new AccountList(accounts);
////        }
//		///
////		LearnedRtmpSource learnedCh = (LearnedRtmpSource) em.createQuery(
////				"Select * from learnedChannel lc"+
////						"where lc.rtmpSource like '"+rtmpSource+"'"
////				).getSingleResult();
////		return learnedCh.isLearned();	
//	}
//
//	//	@Override
//	//	public void findByOverrridingMethod() {
//	//		// TODO Auto-generated method stub
//	//		
//	//	}
//
//	//	@Override
//	//	public void someCustomMethod(Channel ch) {
//	//		// TODO Auto-generated method stub
//	//		
//	//	}
//
//
//
//
//}
