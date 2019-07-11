import java.util.ArrayList;

/**
 * Item holds details relating to a solution (a single line in the results file)
 * 
 * @author Jonathan Fieldsend
 * @version 11/07/2019
 */
public class Item
{
    ArrayList<Double> x;
    double y;
    int fes;
    double time;
    Action action;
    
    public Item(){ }
        
    public Item(Item item, Action action){ 
        this.x = item.x;
        this.y = item.y;
        this.fes = item.fes;
        this.time = item.time;
        this.action = action;
    }    
    
    @Override
    public String toString(){
        String line = "";
        for (double d : x){
            line += d + " ";
        }
        line += "= " + y + " @ "+ fes + " " + time + " " + action;
        return line;
    }
    
    /**
     * All items must have a unique fes number, so equality just checks for this
     */
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Item) 
            if (((Item) obj).fes == this.fes)
                return true;
        return false;
    }
    
    
}
