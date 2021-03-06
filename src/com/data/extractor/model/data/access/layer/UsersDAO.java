package com.data.extractor.model.data.access.layer;


import com.data.extractor.model.beans.authenticate.login.LoginRequest;
import com.data.extractor.model.beans.authentication.AuthenticationRequest;
import com.data.extractor.model.beans.dictionary.Dictionary;
import com.data.extractor.model.beans.user.UserBean;
import com.data.extractor.model.db.connect.dbInitializer;
import com.google.gson.Gson;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;

public class UsersDAO {

    /*
    users Collection Data Format :
    {'userName':'userName',pass:'pass','org':'brandix'}
     */
    private static final String dbName="staging";
    private static final String coll="users";

    private DBCollection usersColl;

    public UsersDAO(MongoClient mongoClient){

        dbInitializer dbInitializer =new dbInitializer();

        DB db=dbInitializer.getDB(mongoClient,dbName);
        this.usersColl = dbInitializer.getCollection(db,coll);
    }

    public Boolean isUserExists(AuthenticationRequest loginRequest){
        BasicDBObject basicDBObject = new BasicDBObject();

        basicDBObject.put("userName", loginRequest.getUserName());
        basicDBObject.put("pass", loginRequest.getPass());

        DBCursor templateCursor = usersColl.find(basicDBObject);

        return templateCursor.size() == 1;
    }

    public int getRecordSize (String userName){
        BasicDBObject searchQuery = new BasicDBObject();

        searchQuery.put("userName",userName);

        DBCursor userCursor = usersColl.find(searchQuery);

        return userCursor.size();
    }

    public UserBean getUser(String userId){
        BasicDBObject searchQuery = new BasicDBObject();

        searchQuery.put("id",userId);
        DBObject dbObject = usersColl.findOne(searchQuery);

        Gson gson=new Gson();
        UserBean userBean = gson.fromJson(dbObject.toString(),UserBean.class);
        return userBean;
    }

    public UserBean getUserByName(String userName){
        BasicDBObject searchQuery = new BasicDBObject();

        searchQuery.put("userName",userName);
        DBObject dbObject = usersColl.findOne(searchQuery);

        Gson gson=new Gson();
        UserBean userBean = gson.fromJson(dbObject.toString(),UserBean.class);
        return userBean;
    }

    public void createUser(String id,String userName,String pass,String role,String fullName){
        BasicDBObject basicDBObject = new BasicDBObject();

        basicDBObject.put("id",id);
        basicDBObject.put("userName",userName);
        basicDBObject.put("pass",pass);
        basicDBObject.put("role",role);
        basicDBObject.put("fullName",fullName);

        usersColl.insert(basicDBObject);
    }

    public void removeUser(String id){
        BasicDBObject removeQuery =new BasicDBObject();

        removeQuery.put("id",id);
        usersColl.remove(removeQuery);
    }

    public List<UserBean> getAllUsers(){

        List<UserBean> userBeanList = new ArrayList<UserBean>();
        UserBean userBean;
        DBCursor dbCursor = usersColl.find();
        Gson gson = new Gson();

        while(dbCursor.hasNext()){
            userBean = gson.fromJson(dbCursor.next().toString(),UserBean.class);
            userBeanList.add(userBean);
        }
        return userBeanList;
    }

    public void addNodeToUser(String userId ,String nodeId){
        BasicDBObject searchQuery= new BasicDBObject();

        searchQuery.put("id",userId);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$push", new BasicDBObject("nodes", nodeId));
        usersColl.update(searchQuery, updateObject);
    }

    public void removeNodeFromUser(String userId ,String nodeId){
        BasicDBObject searchQuery= new BasicDBObject();

        searchQuery.put("id",userId);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$pull", new BasicDBObject("nodes", nodeId));
        usersColl.update(searchQuery, updateObject);
    }




}
