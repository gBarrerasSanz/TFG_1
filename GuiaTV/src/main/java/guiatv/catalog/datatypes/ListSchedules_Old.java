package guiatv.catalog.datatypes;

import guiatv.persistence.domain.Schedule;

import java.util.List;

import org.hibernate.usertype.UserCollectionType;

public interface ListSchedules_Old extends List<Schedule>, UserCollectionType{

}
