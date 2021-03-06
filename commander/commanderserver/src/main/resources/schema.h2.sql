drop table global_events if exists;
CREATE TABLE global_events
(
	Id BIGINT NOT NULL generated by default as identity,
	EventId BIGINT NOT NULL COMMENT '事件全局唯一id',
	EventType TINYINT NOT NULL COMMENT '事件类型',
	Name VARCHAR(50) NOT NULL COMMENT '事件名称',
	Status TINYINT NOT NULL COMMENT '事件状态',
	Params TEXT COMMENT '事件参数',
	Source VARCHAR(50) NOT NULL COMMENT '事件来源',
	LocalCreateTime DATETIME NOT NULL COMMENT '事件在本地服务中创建的时间',
	CreateTime DATETIME NOT NULL COMMENT '在事件管理器中创建的时间',
	PRIMARY KEY (id),
	UNIQUE uq_global_events_id(id),
	UNIQUE uq_global_events_eventid(EventId)
);

drop table processed_events if exists;
CREATE TABLE processed_events
(
	Id BIGINT NOT NULL generated by default as identity,
	EventId BIGINT NOT NULL COMMENT '事件全局唯一id',
	EventType TINYINT NOT NULL COMMENT '事件类型',
	Name VARCHAR(50) NOT NULL COMMENT '事件名称',
	Status TINYINT NOT NULL COMMENT '事件状态',
	Params TEXT COMMENT '事件参数',
	Source VARCHAR(50) NOT NULL COMMENT '事件来源',
	LocalCreateTime DATETIME NOT NULL COMMENT '事件在本地服务中创建的时间',
	CreateTime DATETIME NOT NULL COMMENT '在事件管理器中创建的时间',
	PRIMARY KEY (id),
	UNIQUE uq_processed_events_id(id),
	UNIQUE uq_processed_events_eventid(EventId)
);


