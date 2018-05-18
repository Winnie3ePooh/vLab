package vlab.server_java.model;

public class UserCodeTests {

    public String testInput;
    public String testOutputRight;
    public String testOutputUser;
    public boolean compareResult;

    public UserCodeTests(String tI, String tOR, String tOU, boolean cR) {
        this.testInput = tI;
        this.testOutputRight = tOR;
        this.testOutputUser = tOU;
        this.compareResult = cR;
    }

}
