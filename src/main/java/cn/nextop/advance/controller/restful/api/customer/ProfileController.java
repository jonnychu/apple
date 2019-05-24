package cn.nextop.advance.controller.restful.api.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController extends AbstractCustomerController {
	
	/**
	 * 
	 */
	public ProfileController() {
		super("customer.profile");
	}
	
	@Async
	@RequestMapping(value="/profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CompletableFuture<Object> getProfile(@RequestParam(name = "id", required = false) Integer id) {
		return CompletableFuture.supplyAsync(() -> {
			
			Map<String, Object> resultData = new HashMap<>();
			if (id == null) {
				resultData.put("resultCode", "all"); return new AsyncResult<>(resultData);
			}
			switch (id) {
			case 1:
				resultData.put("resultCode", "profile 1");
				break;
			case 2:
				resultData.put("resultCode", "profile 2");
				break;
			case 3:
				resultData.put("resultCode", "profile 3");
				break;
			case 4:
				resultData.put("resultCode", "profile 4");
				break;
			case 5:
				throw new IllegalArgumentException();
			default:
				resultData.put("resultCode", UUID.randomUUID().toString());
				break;
			}
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return resultData;
		});
	}
}
