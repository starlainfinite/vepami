package com.technologies.pixelbox.vepami;

import java.util.ArrayList;
import java.util.List;

public class ExecuteTaskParameters {
    private List<String> columns;
    private String url;

    public ExecuteTaskParameters() {
        super();
        columns = new ArrayList<String>();
    }
    public ExecuteTaskParameters(String url, List<String> columns) {
        super();
        this.columns = columns;
        this.url = url;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
