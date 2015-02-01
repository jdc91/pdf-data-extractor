package com.data.extractor.controllers;

import com.data.extractor.model.beans.manage.categories.ManageCategoriesData;
import com.data.extractor.model.beans.manage.categories.Node;
import com.data.extractor.model.beans.user.UserBean;
import com.data.extractor.model.data.access.layer.TemplatesDAO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessRightController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
        Gson gson = new Gson();
        ManageCategoriesData data = gson.fromJson(sb.toString(),ManageCategoriesData.class);

        /* Get the mongo client from the servletContext */
        MongoClient mongoClient = (MongoClient) request.getServletContext().getAttribute("MONGO_CLIENT");
        TemplatesDAO templatesDAO = new TemplatesDAO(mongoClient);

        if(data.getRequest().equals("addUserName")){
            templatesDAO.addUserToNode(data.getId(),data.getParent(),data.getUserName());
        }
        if(data.getRequest().equals("removeUserName")){
            templatesDAO.removeUserFromNode(data.getId(),data.getParent(),data.getUserName());
        }

        String jsonStr = "{\"state\": \"success\"}";
        JsonElement element = gson.fromJson (jsonStr, JsonElement.class);
        JsonObject jsonObj = element.getAsJsonObject();
        response.getWriter().print(gson.toJson(jsonObj));

    }

}
