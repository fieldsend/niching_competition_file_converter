import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.charset.StandardCharsets;

/**
 * Coverter class is the main colass to convert between 
 * formats for the niching compeition output files.
 * 
 * @author Jonathan Fieldsend
 * @version 11/07/2019
 */
public class Converter
{
    public static void main(String[] args) {
        String fileToConvert = args[0];
        boolean notProcessedFirstLine = true;
        String firstLine="";
        ArrayList<Item> contents = new ArrayList<>();
        
        // try and open file and process
        try (BufferedReader br = new BufferedReader(new FileReader(fileToConvert))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (notProcessedFirstLine){
                    firstLine = line;
                    notProcessedFirstLine = false;
                } else {
                    if (line.length()>0) {
                        //System.out.println(line);
                        Item item = process(line);
                        //System.out.println("processed");
                        contents.add(item);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Process failed with exception: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("File loaded");
        // contents now contains a list of items, ordered as the contents of the file
        // now convert and write
        
        // ASSUME unique fes for contents -- will need to right a sanity check for that here
        
        System.out.println("Reformatted contents");
        ArrayList<Item> reformattedContents = reconstruct(contents);
        String filenameWithoutPath = (Paths.get(fileToConvert)).getFileName().toString();
        
        System.out.println("Writing out reformatted line");
        writeToFile(reformattedContents, firstLine,filenameWithoutPath);
    }
    
    private static void writeToFile(ArrayList<Item> reformattedContents, String firstLine, String filename) {
        
        System.out.println(firstLine);
        Path path = FileSystems.getDefault().getPath("reformated", filename);
        Path parentDir = path.getParent();
        try {
            if (!Files.exists(parentDir))
                Files.createDirectories(parentDir);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8) ) {
            writer.write(firstLine);
            writer.newLine();
            for (Item item : reformattedContents){
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    private static ArrayList<Item> reconstruct(ArrayList<Item> contents) {
        // array lists holding the previous set of solutions defining a mode, 
        // and the new set
//         System.out.println("OLD FILE");
//         for (Item solution : contents) {
//             System.out.println(solution);
//         }
        
        ArrayList<Item> previousModeSet = new ArrayList<>();
        ArrayList<Item> currentModeSet = new ArrayList<>();
        ArrayList<Item> reformattedContent = new ArrayList<>();
        boolean firstSetProcessed = false;
        for (Item solution : contents) {
            if (solution.action.equals(Action.RESET_AND_ADD)) {
                if (!firstSetProcessed) {
                    if (currentModeSet.size()==0) {
                        currentModeSet.add(solution);
                    } else {
                        firstSetProcessed = true;
                        reformattedContent.addAll(currentModeSet);
                        previousModeSet = currentModeSet;
                        currentModeSet = new ArrayList<>();
                        currentModeSet.add(solution);
                    }
                } else {
                    reformattedContent.addAll(convertStatus(previousModeSet, currentModeSet));    
                    previousModeSet = currentModeSet;
                    currentModeSet = new ArrayList<>();
                    currentModeSet.add(solution);
                }
            } else {
                currentModeSet.add(solution);
            }
        }
        // now process final batch 
        reformattedContent.addAll(convertStatus(previousModeSet, currentModeSet));    
                    
        
//         System.out.println("NEW FILE");
//         for (Item solution : reformattedContent) {
//             System.out.println(solution);
//         }
        return reformattedContent;
    }
    
    private static ArrayList<Item> convertStatus(ArrayList<Item> previousModeSet, ArrayList<Item> currentModeSet) {
        // any elements of previousModeSet not in currentModeSet need to have -1 (REMOVE)
        // flags, and any in currentModeSet not in previousModeSet need to have 1 (ADD)
        // action flags, all others can be ignored
        
        ArrayList<Item> reflaggedContents = new ArrayList<>();
        // add those not in previous
        for (Item c : currentModeSet){
            if (!previousModeSet.contains(c)) {
                reflaggedContents.add(new Item(c,Action.ADD));
            }
        }
        
        // remove those not in current
        for (Item p : previousModeSet){
            if (!currentModeSet.contains(p)) {
                reflaggedContents.add(new Item(p,Action.REMOVE));
            }
        }
        
        return reflaggedContents;
    }
    
    
    private static Item process(String line){
        String[] tokens = line.split("\\s+");
        ArrayList<Double> x = new ArrayList<>();
        Item item = new Item();
        // process x members first
        int index = 0;
        while (!tokens[index].equals("=")){
            x.add(Double.parseDouble(tokens[index++]));
        }
        item.x = x;
        item.y = Double.parseDouble(tokens[++index]); // skip = symbol 
        index++; // skip @ symbol
        item.fes = Integer.parseInt(tokens[++index]);
        item.time = Double.parseDouble(tokens[++index]);
        int a = Integer.parseInt(tokens[++index]);
        item.action = (a == 0) ? Action.RESET_AND_ADD : Action.ADD;
        return item;
    }
}
