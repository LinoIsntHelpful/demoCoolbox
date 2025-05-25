package com.example.demo.model.dao;

import com.example.demo.model.entities.ObjectItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjectItemDAO extends JpaRepository<ObjectItem, Long>
{
	List<ObjectItem> findByLocationId(String locationId);
}
