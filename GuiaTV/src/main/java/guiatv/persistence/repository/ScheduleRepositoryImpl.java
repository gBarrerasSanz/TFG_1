package guiatv.persistence.repository;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {
	
	@PersistenceContext
    private EntityManager em;
	
	public ScheduleRepositoryImpl() {
	}


	@Override
	public void merge(List<Schedule> lSched) {
        for (Iterator<Schedule> it = lSched.iterator(); it.hasNext();) {
            Schedule sched = em.merge(it.next());
            em.flush();
            em.clear();
        }
	}



}
