package com.bookface.postsservice.repository;//package com.bookface.postsservice.repository;
//
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import com.bookface.postsservice.model.Post;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.repository.query.FluentQuery;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//import java.util.Optional;
//import java.util.function.Function;
//
//@Repository
//public class PostsRepositoryImpl implements PostsRepository {
//
//    private final MongoTemplate mongoTemplate;
//
//    public PostsRepositoryImpl(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    @Override
//    public void editPost(Long id, String postContent, String updatePart) {
//        Query query = new Query(Criteria.where("id").is(id));
//        Update update = new Update().set("content", postContent);
//        mongoTemplate.updateFirst(query, update, Post.class);
//    }
//
//    @Override
//    public <S extends Post> List<S> saveAll(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public List<Post> findAll() {
//        return null;
//    }
//
//    @Override
//    public List<Post> findAll(Sort sort) {
//        return null;
//    }
//
//    @Override
//    public Page<Post> findAll(Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> S insert(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> List<S> insert(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends Post> List<S> findAll(Example<S> example) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> List<S> findAll(Example<S> example, Sort sort) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends Post> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends Post> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override
//    public <S extends Post, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//        return null;
//    }
//}
//
