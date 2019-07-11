
/**
 * Enumeration class Action represents the three distinct actions
 * on a new solution in the results file
 * 
 * @author Jonathan Fieldsend
 * @version 11/07/2019
 */
public enum Action
{
    RESET_AND_ADD(0), ADD(1), REMOVE(-1);
    
    private int value;
    Action(int value){
        this.value = value;
    }
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
