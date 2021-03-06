package com.data.extractor.model.extract.pdf.inserter;

import com.data.extractor.model.beans.extract.pdf.ExtractStatus;
import com.data.extractor.model.beans.template.info.image.ImageDataElement;
import com.data.extractor.model.beans.template.info.image.ImageDataParser;
import com.data.extractor.model.beans.template.info.pattern.PatternDataElement;
import com.data.extractor.model.beans.template.info.pattern.PatternDataParser;
import com.data.extractor.model.beans.template.info.regex.RegexDataElement;
import com.data.extractor.model.beans.template.info.regex.RegexDataParser;
import com.data.extractor.model.beans.template.info.table.TableDataElement;
import com.data.extractor.model.beans.template.info.table.TableDataParser;
import com.data.extractor.model.beans.template.info.text.TextDataElement;
import com.data.extractor.model.beans.template.info.text.TextDataParser;
import com.data.extractor.model.data.access.layer.ExtractedDataDAO;
import com.mongodb.MongoClient;

import java.util.List;

public class ExtractedDataInserter {

    public void insert(TextDataParser textDataParser,ExtractStatus extractStatus,MongoClient mongoClient){

        List<TextDataElement> textDataElements = textDataParser.getTextDataElements();
        ExtractedDataDAO extractedDataDAO = new ExtractedDataDAO(mongoClient);
        TextDataElement textDataElement;
        int recordsSize=0;

        for(int i=0;i<textDataElements.size();i++){

            textDataElement=textDataElements.get(i);

            /* Only check once from the DB when the loop starts */
            if(i==0){
                recordsSize = extractedDataDAO.getRecordsSizeOfId(extractStatus.getId(),textDataParser.getDataType());
            }

            if (recordsSize == 0) {
                /* If there is no record exists create a new record and insert */
                extractedDataDAO.createTemplateInfo(extractStatus.getId() , extractStatus.getParent() , textDataParser.getDataType(),textDataElement);
                recordsSize = 1;

            } else {
                /* If record exists update the record */
                extractedDataDAO.updateTemplateInfo(extractStatus.getId(),textDataParser,textDataElement);
            }
        }
    }

    public void insert(ImageDataParser imageDataParser,ExtractStatus extractStatus,MongoClient mongoClient) {

        List<ImageDataElement> imageDataElements = imageDataParser.getImageDataElements();
        ExtractedDataDAO extractedDataDAO = new ExtractedDataDAO(mongoClient);
        ImageDataElement imageDataElement;
        int recordsSize = 0;

        for (int i = 0; i < imageDataElements.size(); i++) {

            imageDataElement = imageDataElements.get(i);

            /* Only check once from the DB when the loop starts */
            if (i == 0) {
                recordsSize = extractedDataDAO.getRecordsSizeOfId(extractStatus.getId(), imageDataParser.getDataType());
            }

            if (recordsSize == 0) {
                /* If there is no record exists create a new record and insert */
                extractedDataDAO.createTemplateInfo(extractStatus.getId(), extractStatus.getParent(), imageDataParser.getDataType(), imageDataElement);
                recordsSize = 1;

            } else {
                /* If record exists update the record */
                extractedDataDAO.updateTemplateInfo(extractStatus.getId(),imageDataParser, imageDataElement);
            }
        }
    }

    public void insert(TableDataParser tableDataParser,ExtractStatus extractStatus , MongoClient mongoClient){

        List<TableDataElement> tableDataElements= tableDataParser.getTableDataElements();
        ExtractedDataDAO extractedDataDAO = new ExtractedDataDAO(mongoClient);
        TableDataElement tableDataElement;
        int recordsSize=0;

        for(int i=0;i<tableDataElements.size();i++){

            tableDataElement=tableDataElements.get(i);

            /* Only check once from the DB when the loop starts */
            if(i==0){

                recordsSize = extractedDataDAO.getRecordsSizeOfId(extractStatus.getId(),tableDataParser.getDataType());
            }

            if (recordsSize == 0) {
                /* If there is no record exists create a new record and insert */
                extractedDataDAO.createTemplateInfo(extractStatus.getId() , extractStatus.getParent() , tableDataParser.getDataType(),tableDataElement);
                recordsSize = 1;

            } else {
                /* If record exists update the record */
                extractedDataDAO.updateTemplateInfo(extractStatus.getId(),tableDataParser,tableDataElement);
            }
        }
    }

    public void insert(RegexDataParser regexDataParser,ExtractStatus extractStatus,MongoClient mongoClient){

        List<RegexDataElement> regexDataElementList= regexDataParser.getRegexDataElements();
        ExtractedDataDAO extractedDataDAO = new ExtractedDataDAO(mongoClient);
        RegexDataElement regexDataElement;
        int recordsSize=0;

        for(int i=0;i<regexDataElementList.size();i++){

            regexDataElement = regexDataElementList.get(i);

            /* Only check once from the DB when the loop starts */
            if(i==0){
                recordsSize = extractedDataDAO.getRecordsSizeOfId(extractStatus.getId(),regexDataParser.getDataType());
            }

            if (recordsSize == 0) {
                /* If there is no record exists create a new record and insert */
                extractedDataDAO.createTemplateInfo(extractStatus.getId() , extractStatus.getParent() , regexDataParser.getDataType(),regexDataElement);
                recordsSize = 1;

            } else {
                /* If record exists update the record */
                extractedDataDAO.updateTemplateInfo(extractStatus.getId(),regexDataParser,regexDataElement);
            }
        }
    }

    public void insert(PatternDataParser patternDataParser,ExtractStatus extractStatus, MongoClient mongoClient){
        List<PatternDataElement> patternDataElementList = patternDataParser.getPatternDataElements();
        List<List<PatternDataElement>> complexPatternList = patternDataParser.getComplexPatternList();
        ExtractedDataDAO extractedDataDAO = new ExtractedDataDAO(mongoClient);
        PatternDataElement patternElement;
        int recordsSize = 0;


        for (int i = 0; i < complexPatternList.size(); i++) {

            List<PatternDataElement> patternElements = complexPatternList.get(i); // One table type set..
            for (int j = 0; j < patternElements.size(); j++) {
                patternElement = patternElements.get(j);

                 /* Only check once from the DB when the loop starts */
                if(j==0){
                    recordsSize = extractedDataDAO.getRecordsSizeOfId(extractStatus.getId(),patternDataParser.getDataType());
                }

                if (recordsSize == 0) {
                /* If there is no record exists create a new record and insert */
                    extractedDataDAO.createTemplateInfo(extractStatus.getId() , extractStatus.getParent() , patternDataParser.getDataType(),patternElement);
                    recordsSize = 1;

                } else {
                /* If record exists update the record */
                    extractedDataDAO.updateTemplateInfo(extractStatus.getId(),patternDataParser,patternElement);
                }

            }
        }

//        for(int i=0;i<patternDataElementList.size();i++){
//
//            patternDataElement = patternDataElementList.get(i);
//
//            /* Only check once from the DB when the loop starts */
//            if(i==0){
//                recordsSize = extractedDataDAO.getRecordsSizeOfId(extractStatus.getId(),patternDataParser.getDataType());
//            }
//
//            if (recordsSize == 0) {
//                /* If there is no record exists create a new record and insert */
//                extractedDataDAO.createTemplateInfo(extractStatus.getId() , extractStatus.getParent() , patternDataParser.getDataType(),patternDataElement);
//                recordsSize = 1;
//
//            } else {
//                /* If record exists update the record */
//                extractedDataDAO.updateTemplateInfo(extractStatus.getId(),patternDataParser,patternDataElement);
//            }
//        }
    }

}
