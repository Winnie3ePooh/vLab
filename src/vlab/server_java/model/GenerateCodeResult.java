package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenerateCodeResult {

    public String taskText;
    public String inputDataText;
    public String outputDataText;

    public  GenerateCodeResult(String t, String i, String o) {
        this.taskText = t;
        this.inputDataText = i;
        this.outputDataText = o;
    }


}
