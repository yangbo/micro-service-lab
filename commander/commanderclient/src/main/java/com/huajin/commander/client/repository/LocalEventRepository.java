package com.huajin.commander.client.repository;

import org.springframework.data.repository.CrudRepository;

import com.huajin.commander.client.domain.LocalEventEntity;


public interface LocalEventRepository extends CrudRepository<LocalEventEntity, Long>{

	LocalEventEntity findByEventId(Long eventId);

}
