
package yago2minner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Yago2Minner {
    
    private static ArrayList<Triple> rules;
    private static ArrayList<String[]> pairs = new ArrayList<>();
    private static boolean allAdded = false;
    
    private static ArrayList<ArrayList<Triple[]>> result = new ArrayList<>();
    
    
    public static void main(String[] args) throws InterruptedException {
        
        rules = loadFile();
        Thread t1 = new Thread(new Runnable(){
            @Override
            public void run() {
                addAllRulesPairs();
                exe();
            }
        });
        Thread t2 = new Thread(new Runnable(){@Override public void run() {exe();}});
        Thread t3 = new Thread(new Runnable(){@Override public void run() {exe();}});
        Thread t4 = new Thread(new Runnable(){@Override public void run() {exe();}});
        Thread t5 = new Thread(new Runnable(){@Override public void run() {exe();}});
        Thread t6 = new Thread(new Runnable(){@Override public void run() {exe();}});
        Thread t7 = new Thread(new Runnable(){@Override public void run() {exe();}});
        Thread t8 = new Thread(new Runnable(){@Override public void run() {exe();}});
        
        
        long st = System.currentTimeMillis();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        t7.join();
        t8.join();
        
        System.out.println("Znaleziono: " + result.size() + " zasad w " + (System.currentTimeMillis() - st)/1000.0+ " s.");
        //findRule("isCitizenOf","livesIn");
    }
    
    
    private static void exe(){
        while(true){
            if(allAdded && pairs.size() == 0) break;
            while(pairs.size() == 0) System.out.print("");
            String[] pair = pairs.remove(0);
            
            ArrayList<Triple[]> rule = findRule(pair[0], pair[1]);
            if(rule != null) result.add(rule);
        }
    }
    
    private static void addAllRulesPairs(){
        int all = 0;
        ArrayList<String> allRules = getAllRules();
        for(int i=0; i<allRules.size(); i++){
            for(int j=0; j<allRules.size(); j++){
                while(pairs.size() > 1000000) System.out.print("");
                if(i!=j) {
                    pairs.add(new String[]{allRules.get(i),allRules.get(j)});
                    all++;
                }
            }
        }
        System.out.println("Do przeszukania: " + all);
        allAdded = true;
    }
    
    private static ArrayList<String> getAllRules(){
        long st = System.currentTimeMillis();
        ArrayList<String> ret = new ArrayList<>();
        for(Triple t : rules) if(!ret.contains(t.getRule())) {
            ret.add(t.getRule());
        }
        System.out.println("Ekstrakcja zasad: " + (System.currentTimeMillis() - st)/1000.0+ " s.");
        return ret;
    }
    
    private static ArrayList<Triple[]> findRule(String rule1, String rule2){
        long st = System.currentTimeMillis();
        
        ArrayList<Triple> allForRule1 = new ArrayList<>();
        for(Triple t : rules) if(t.getRule().equals(rule1)) allForRule1.add(t);
        
        ArrayList<Triple> allForRule2 = new ArrayList<>();
        for(Triple t : rules) if(t.getRule().equals(rule2)) allForRule2.add(t);
        
        //System.out.println("Ekstrakcja zasad: " + (System.currentTimeMillis() - st)/1000.0+ " s.");
        //System.out.println("Rule1: " + allForRule1.size() + " zasad");
        //System.out.println("Rule2: " + allForRule2.size() + " zasad");
        int pcaA = 0;
        int pcaB = 0;
        ArrayList<Triple[]> answer = new ArrayList<>();
        for(Triple first : allForRule1){
            for(Triple second : allForRule2){
                boolean firstRule = false;
                
                if(first.getFirst().equals(second.getFirst())){
                    pcaA++;
                    firstRule = true;
                }
                if(first.getSecond().equals(second.getSecond())){
                    pcaB++;
                    if(firstRule) {
                        answer.add(new Triple[]{first,second});
                        break;
                    }
                }
                
            }
        }
        
        if(answer.size() == 0) return null;
        if(answer.size() * 1.0 / (pcaA<pcaB? pcaA:pcaB) < 0.1){
            //System.out.println("Zasada " + rule1 + " + " + rule2 + ": " + answer.size() + " poniżej progu - " + allForRule1.size() + "(PCA_A: " + pcaA + ", PCA_B: " + pcaB + ")");
            return null;
        }
        else {
            System.out.println(
                    "Znaleziono " + rule1 + " + " + rule2 + ": " + 
                            answer.size() + " pasujących (" + allForRule1.size() + " PCA: " + (pcaA<pcaB? pcaA:pcaB) + " = " + answer.size() * 1.0 / (pcaA<pcaB? pcaA:pcaB) + ")");
            return answer;
        }
    }
    
    private static ArrayList<Triple[]> findRule(String rule1, String rule2, String rule3){
        long st = System.currentTimeMillis();
        
        ArrayList<Triple> allForRule1 = new ArrayList<>();
        for(Triple t : rules) if(t.getRule().equals(rule1)) allForRule1.add(t);
        
        ArrayList<Triple> allForRule2 = new ArrayList<>();
        for(Triple t : rules) if(t.getRule().equals(rule2)) allForRule2.add(t);
        
        ArrayList<Triple> allForRule3 = new ArrayList<>();
        for(Triple t : rules) if(t.getRule().equals(rule3)) allForRule3.add(t);
        
        //System.out.println("Ekstrakcja zasad: " + (System.currentTimeMillis() - st)/1000.0+ " s.");
        //System.out.println("Rule1: " + allForRule1.size() + " zasad");
        //System.out.println("Rule2: " + allForRule2.size() + " zasad");
        int pcaA = 0;
        int pcaB = 0;
        int pcaC = 0;
        ArrayList<Triple[]> answer = new ArrayList<>();
        for(Triple first : allForRule1){
            for(Triple second : allForRule2){
                for(Triple third : allForRule3){
                    
                    // a->b, b->c, a->c
                    boolean firstRule = false;
                    boolean secondRule = false;

                    if(first.getSecond().equals(second.getFirst())){
                        pcaA++;
                        firstRule = true;
                    }
                    if(second.getSecond().equals(third.getFirst())){
                        pcaB++;
                        secondRule = true;
                        if(firstRule) {
                            answer.add(new Triple[]{first,second});
                            break;
                        }
                    }
                    if(first.getFirst().equals(third.getSecond())){
                        pcaC++;
                        if(firstRule && secondRule) {
                            answer.add(new Triple[]{first,second,third});
                            break;
                        }
                    }
                }
            }
        }
        
        if(answer.size() == 0) return null;
        if(answer.size() * 1.0 / (pcaA<pcaB? pcaA:pcaB) < 0.1){
            //System.out.println("Zasada " + rule1 + " + " + rule2 + ": " + answer.size() + " poniżej progu - " + allForRule1.size() + "(PCA_A: " + pcaA + ", PCA_B: " + pcaB + ")");
            return null;
        }
        else {
            System.out.println(
                    "Znaleziono " + rule1 + " + " + rule2 + ": " + 
                            answer.size() + " pasujących (" + allForRule1.size() + " PCA: " + (pcaA<pcaB? pcaA:pcaB) + " = " + answer.size() * 1.0 / (pcaA<pcaB? pcaA:pcaB) + ")");
            return answer;
        }
    }
    
    private static ArrayList<Triple> loadFile(){
        long st = System.currentTimeMillis();
        
        String file = "C:\\Users\\milos\\Documents\\NetBeansProjects\\yago2.tsv";
        char start = '<';
        char end = '>';
        ArrayList<Triple> ret = new ArrayList<>();
        
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String first = "";
                String rule = "";
                String second = "";
                int reading = 0;
                for(char c : data.toCharArray()){
                    if(c == start){
                        if(first.isEmpty()) reading = 1;
                        else if(rule.isEmpty()) reading = 2;
                        else if(second.isEmpty()) reading = 3;
                    }
                    else if(c == end){
                        reading = 0;
                    }
                    else {
                        if(reading == 1) first += c;
                        else if(reading == 2) rule += c;
                        else if(reading == 3) second += c;
                    }
                }
                Triple t = new Triple(first,rule,second);
                ret.add(t);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        System.out.println("Czas ładowania pliku: " + (System.currentTimeMillis() - st)/1000.0+ " s.");
        return ret;
    }
    
}
