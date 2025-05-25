package com.example.demo.Controllers;

import com.example.demo.model.dao.ObjectItemDAO;
import com.example.demo.model.entities.ObjectItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/objects")
@CrossOrigin
public class ObjectItemController {

	@Autowired
	private ObjectItemDAO dao;

	// 1) Recupera tutti gli oggetti per una location
	@GetMapping("/{locationId}")
	public List<ObjectItem> getByLocation(@PathVariable String locationId) {
		return dao.findByLocationId(locationId);
	}

	// 2) Aggiunge un nuovo oggetto con quantità
	//    Body: { "name": "...", "quantity": <numero> }
	@PostMapping("/{locationId}")
	public ObjectItem add(
			@PathVariable String locationId,
			@RequestBody Map<String, Object> body
	) {
		String name = (String) body.get("name");
		Integer qty = body.get("quantity") instanceof Number
					  ? ((Number) body.get("quantity")).intValue()
					  : 1;
		ObjectItem obj = new ObjectItem();
		obj.setName(name);
		obj.setLocationId(locationId);
		obj.setQuantity(qty);
		return dao.save(obj);
	}

	// 3) Aggiorna la quantità di un oggetto
	//    Body: { "quantity": <nuova quantità> }
	@PutMapping("/{id}")
	public ResponseEntity<?> updateQuantity(
			@PathVariable Long id,
			@RequestBody Map<String, Object> body
	) {
		Optional<ObjectItem> o = dao.findById(id);
		if (o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		ObjectItem obj = o.get();
		Integer newQty = body.get("quantity") instanceof Number
						 ? ((Number) body.get("quantity")).intValue()
						 : obj.getQuantity();
		if (newQty <= 0) {
			dao.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			obj.setQuantity(newQty);
			dao.save(obj);
			return ResponseEntity.ok(obj);
		}
	}

	// 4) Elimina un oggetto per ID
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		if (!dao.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		dao.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
