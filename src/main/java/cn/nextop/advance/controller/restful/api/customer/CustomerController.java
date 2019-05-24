package cn.nextop.advance.controller.restful.api.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController extends AbstractCustomerController {
	
	/**
	 * 
	 */
	public CustomerController() {
		super("customer");
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> getCustomer(@RequestParam(name="id", required=false) Integer id) {
		Map<String, Object> resultData = new HashMap<>();
		if(id == null) {resultData.put("resultCode", "all"); return resultData;}
		switch (id) {
		case 1:
			resultData.put("resultCode", "customer 1");
			break;
		case 2:
			resultData.put("resultCode", "customer 2");
			break;
		case 3:
			resultData.put("resultCode", "customer 3");
			break;
		case 4:
			resultData.put("resultCode", "customer 4");
			break;
		case 5:
			throw new IllegalArgumentException();
		default:
			resultData.put("resultCode", UUID.randomUUID().toString());
			break;
		}
        return resultData;
    }
}
