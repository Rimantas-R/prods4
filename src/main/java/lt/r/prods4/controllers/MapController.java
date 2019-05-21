package lt.r.prods4.controllers;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapController {

	
	
	private static final Map<String, String> PRODMAP = new HashMap<>();
	@Autowired
	private static final Gson gson = new Gson();
	
	{
		PRODMAP.put("name1", "desc1");
		PRODMAP.put("name2", "desc2");
		PRODMAP.put("name3", "desc3");
	}
	
	@GetMapping(value="/prodmap", produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String,String> getProdMaps(){
		return PRODMAP;
	}
	
	@GetMapping(value = "/prodmap/{name}", produces=MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Object> findOneProd(@PathVariable("name") String name){
		String valueString = PRODMAP.get(name);
		
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set(name, valueString);
		if(valueString==null) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("req not found");//build();
		}
		
		Map<String,String> item = new HashMap<>();
		item.put(name, valueString);
//		String jsonString = "{\"" + name + "\":\"" + valueString + "\"}";
//		String[] inst = {name,valueString};
//		return valueString.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
//		return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.OK);
//		return ResponseEntity.ok().header("val1","val2").body(jsonString);
//		return ResponseEntity.ok(gson.toJson(inst));
//		return ResponseEntity.ok(jsonString);
		return ResponseEntity.ok(item);
	}
	
	@PostMapping(value="/prodmap", consumes=MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE )
	ResponseEntity<Map> createItems(@RequestBody Map<String,String> items) {
//		String keyS = (String)item.keySet().toArray()[0];
//		String[] outS = {keyS,item.get(keyS)};
		PRODMAP.putAll(items);
		return ResponseEntity.ok(items);
	}
	
	@PutMapping(value="/prodmap", consumes=MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE )
	ResponseEntity<Map> updateItem(@RequestBody Map<String,String> item) {
		PRODMAP.putAll(item);
		return ResponseEntity.ok(item);
	}
	
	@PutMapping(value="/prodmap/{itemKey}", consumes=MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE )
	ResponseEntity<Map> updateItemByKey(@PathVariable String itemKey, @RequestBody Map<String,String> itemIn) {
		String description = PRODMAP.get(itemKey);
		Map<String,String> item = new HashMap<>();
		String newDesc = itemIn.get(itemKey);
		Boolean succ = PRODMAP.replace(itemKey,description,newDesc);
		if(!succ) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(item);
	}
	
	@DeleteMapping(value="/prodmap", consumes=MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE )
	ResponseEntity<Map> deleteItem(@RequestBody Map<String, String> item){
		String keyS = (String)item.keySet().toArray()[0];
		Boolean succ = PRODMAP.remove(keyS, item.get(keyS));
		if(!succ) {
			return ResponseEntity.notFound().build();
			}
		return ResponseEntity.ok(item);
	}
}
