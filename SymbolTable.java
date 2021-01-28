import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class SymbolTable{


    public static int blockNo;
    //tracks if declaration error occurs
    public static String declErr = null;


    public static LinkedHashMap<String, ArrayList<List<String>>> map;

    public SymbolTable table;
    public String scope;


    public SymbolTable(){
        this.table = null;
        this.scope = "GLOBAL";
        blockNo = 0;
        map = new LinkedHashMap<>();
    }

    public SymbolTable(String scope){
        if(scope.equals("BLOCK")){
            this.scope = "BLOCK " + getBlockNumber();
        }else{
            this.scope = scope;
        }
    }

    public int getBlockNumber(){
        return ++blockNo;
    }

    public void printTable(){
        System.out.println("Symbol table " + scope);
        ArrayList<List<String>> vars = map.get(scope);
        if(vars != null){
            for(List<String> varData : vars){
                System.out.print("name " + varData.get(0) + " type " + varData.get(1));
                if(varData.size() == 3){
                    System.out.print(" value " + varData.get(2));
                }
                System.out.println();
            }
        }
    }

    public void checkDeclError(String id){
        ArrayList<List<String>> vars = map.get(scope);
        if(vars != null){
            for(List<String> varData : vars){
                if ((varData.get(0).equals(id)) && (declErr == null)) {
                    declErr = "DECLARATION ERROR " + id;
                    break;
                }
            }
        }
    }
}