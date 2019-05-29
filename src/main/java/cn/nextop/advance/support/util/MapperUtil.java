package cn.nextop.advance.support.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author qutl
 *
 */
@SuppressWarnings("unchecked")
public class MapperUtil {
	//
	private static ObjectMapper mapper = new ObjectMapper();
	
	public final static String write(Object obj) {
		
		String json = "";
		try {
			json = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public final static Map<String, Object> read(String payload) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(payload, Map.class);
	}
}
