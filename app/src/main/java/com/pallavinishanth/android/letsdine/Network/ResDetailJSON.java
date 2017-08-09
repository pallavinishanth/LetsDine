package com.pallavinishanth.android.letsdine.Network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PallaviNishanth on 7/14/17.
 */

public class ResDetailJSON {

    private List<Object> html_attributions = new ArrayList<Object>();
    private DetailResult result;
    private String status;

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
    public DetailResult getResults() {
        return result;
    }

    /*
     * results setter
     */
    public void setResults(DetailResult result) {
        this.result = result;
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
}
