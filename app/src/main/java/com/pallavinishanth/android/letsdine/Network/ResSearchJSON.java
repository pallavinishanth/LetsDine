package com.pallavinishanth.android.letsdine.Network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PallaviNishanth on 7/14/17.
 */

public class ResSearchJSON {

    private String status;
    private ArrayList<Results> results = new ArrayList<Results>();
    private List<Object> html_attributions = new ArrayList<Object>();
    private String next_page_token;

    /*
     * status getter
     */
    public String getStatus() {
        return status;
    }

    /*
     * status setter
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /*
     * results getter
     */
    public ArrayList<Results> getResults() {
        return results;
    }

    /*
     * results setter
     */
    public void setResults(ArrayList<Results> results) {
        this.results = results;
    }

    /*
     * htmlAttributions getter
     */
    public List<Object> getHtmlAttributions() {
        return html_attributions;
    }

    /*
     * html_attributions setter
     */
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.html_attributions = htmlAttributions;
    }

    /*
     * nextPageToken getter
     */
    public String getNextPageToken() {
        return next_page_token;
    }

    /*
     * next_page_token setter
     */
    public void setNextPageToken(String nextPageToken) {
        this.next_page_token = nextPageToken;
    }

}
