
package yago2minner;


public class Triple {
    
    private String first;
    private String rule;
    private String second;
    
    public Triple(String first, String rule, String second) {
        this.first = first;
        this.rule = rule;
        this.second = second;
    }
    
    public String getFirst() {
        return first;
    }

    public String getRule() {
        return rule;
    }

    public String getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Triple{"+ first + " " + rule + " " + second + '}';
    }
    
    
}
