package com.data.extractor.controllers;

import com.data.extractor.model.beans.markup.template.MarkUpResponse;
import com.data.extractor.model.template.edit.RequestProcessor;
import com.google.gson.Gson;
import com.mongodb.MongoClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class EditMarkupController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestProcessor requestProcessor = new RequestProcessor();
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
        /* Get the mongo client from the servletContext */
        MongoClient mongoClient = (MongoClient) request.getServletContext().getAttribute("MONGO_CLIENT");
        String rootPath = getServletContext().getRealPath(File.separator);

        MarkUpResponse markUpResponse=requestProcessor.processRequest(sb.toString(),mongoClient,request);

        Gson gson = new Gson();
        response.getWriter().print(gson.toJson(markUpResponse));
    }
}
