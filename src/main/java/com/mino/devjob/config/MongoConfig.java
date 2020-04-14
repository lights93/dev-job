package com.mino.devjob.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 참고자료
 * https://github.com/spring-projects/spring-boot/issues/16337
 * https://stackoverflow.com/questions/60003179/please-use-mongomappingcontextsetautoindexcreationboolean-or-override-mong
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MongoConfig {
	private final MongoTemplate mongoTemplate;

	private final MongoConverter mongoConverter;

	@EventListener(ApplicationReadyEvent.class)
	public void initIndicesAfterStartup() {
		var init = System.currentTimeMillis();

		var mappingContext = this.mongoConverter.getMappingContext();

		if (mappingContext instanceof MongoMappingContext) {
			MongoMappingContext mongoMappingContext = (MongoMappingContext)mappingContext;
			for (BasicMongoPersistentEntity<?> persistentEntity : mongoMappingContext.getPersistentEntities()) {
				var clazz = persistentEntity.getType();
				if (clazz.isAnnotationPresent(Document.class)) {
					var resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);

					var indexOps = mongoTemplate.indexOps(clazz);
					resolver.resolveIndexFor(clazz).forEach(indexOps::ensureIndex);
				}
			}
		}

		log.info("Mongo InitIndicesAfterStartup take: {}", (System.currentTimeMillis() - init));
	}
}
