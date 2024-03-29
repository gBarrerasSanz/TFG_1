package guiatv.catalog.serializers;

import guiatv.common.CommonUtility;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;



import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TimestampDateSerializer extends JsonSerializer<Timestamp>{

	@Override
	public void serialize(Timestamp timestamp, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		
//		SimpleDateFormat sdf = new SimpleDateFormat(CommonUtility.zonelessDateFormat);
//		jgen.writeString(sdf.format(new Date(timestamp.getTime())));
		
		jgen.writeString(CommonUtility.localTimestampToGmtStr(timestamp));
	}
}
