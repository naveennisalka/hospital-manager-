package com.example.hospital_manager.service;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * Gives numeric ids (1, 2, 3 ...) instead of MongoDB ObjectIds.
 * Keeps one counter per collection in the "counters" collection.
 */
@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {

    private final MongoOperations mongo;

    public long nextId(String collection) {
        Document counter = mongo.findAndModify(
                new Query(Criteria.where("_id").is(collection)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Document.class,
                "counters");
        return ((Number) counter.get("seq")).longValue();
    }
}
