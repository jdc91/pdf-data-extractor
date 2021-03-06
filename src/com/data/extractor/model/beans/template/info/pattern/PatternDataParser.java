package com.data.extractor.model.beans.template.info.pattern;

import com.data.extractor.controllers.*;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class PatternDataParser {

    @Expose
    String id;
    @Expose
    String extractedId;
    @Expose
    private String dataType;
    @Expose
    private String pdfFile;
    @Expose
    private List<PatternDataElement> patternDataElements = new ArrayList<PatternDataElement>();
    @Expose
    private List<List<PatternDataElement>> complexPatternList = new ArrayList<List<PatternDataElement>>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<PatternDataElement> getPatternDataElements() {
        return patternDataElements;
    }

    public void setPatternDataElements(List<PatternDataElement> patternDataElements) {
        this.patternDataElements = patternDataElements;
    }

    public String getExtractedId() {
        return extractedId;
    }

    public void setExtractedId(String extractedId) {
        this.extractedId = extractedId;
    }

    public List<List<PatternDataElement>> getComplexPatternList() {
        return complexPatternList;
    }

    public void setComplexPatternList(List<List<PatternDataElement>> complexPatternList) {
        this.complexPatternList = complexPatternList;
    }
}
