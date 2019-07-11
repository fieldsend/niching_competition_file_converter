/**
 * Class will run over all files for competition results in current 
 * directory and process them
 * 
 * @author Jonathan Fieldsend
 * @version 11/07/2019
 */

public class LoopProcess {
    public static void main(String[] args) {
        String[] a = new String[1];
        for (int i =1; i<=20; i++){
            for (int k=1; k<=50; k++) {
                System.out.println(i + " " + k);
                String start = (i<10) ? "problem00" : "problem0";
                String mid = (k<10) ? "run00" : "run0";
                a[0] = start + i  + mid + k + ".dat";
                Converter.main(a);
            }
        }
    }
}
