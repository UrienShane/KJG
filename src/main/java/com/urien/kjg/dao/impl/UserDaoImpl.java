package com.urien.kjg.dao.impl;

import com.urien.kjg.dao.UserDao;
import com.urien.kjg.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository(value="userDao")
public class UserDaoImpl implements UserDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User findUserByName(String username){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        return user;
    }

    @Override
    public User findUserByTelephoneNum(String telephoneNum){
        Query query = new Query();
        query.addCriteria(Criteria.where("telephoneNum").is(telephoneNum));
        User user = mongoTemplate.findOne(query, User.class);
        return user;
    }

    @Override
    public void insertOneUser(User user){
        String telephoneNum = user.getTelephoneNum();
        String password = user.getPassword();

        Query query = new Query();
        query.addCriteria(Criteria.where("telephoneNum").is(telephoneNum));
        Update update = Update.update("password", password).set("username", telephoneNum).set("perms", "USER");

        mongoTemplate.upsert(query ,update, User.class);
    }

}
