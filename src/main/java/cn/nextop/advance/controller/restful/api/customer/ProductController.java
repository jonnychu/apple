package cn.nextop.advance.controller.restful.api.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.nextop.advance.controller.restful.RestApiController;

@RestController
public class ProductController extends RestApiController {
	
	/**
	 * 
	 */
	public ProductController() {
		super("customer");
	}

	@RequestMapping(value = "/customer", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> getProduction(@RequestParam(name="id", required=false) Integer id) {
		Map<String, Object> resultData = new HashMap<>();
		if(id == null) {resultData.put("resultCode", "all"); return resultData;}
		switch (id) {
		case 1:
			resultData.put("resultCode", "product 1");
			break;
		case 2:
			resultData.put("resultCode", "product 2");
			break;
		case 3:
			resultData.put("resultCode", "product 3");
			break;
		case 4:
			resultData.put("resultCode", "product 4");
			break;
		default:
			resultData.put("resultCode", UUID.randomUUID().toString());
			break;
		}
        return resultData;
    }
}
