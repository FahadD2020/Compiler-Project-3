import java.lang.*;
import java.util.*;

public class LitListener extends LittleBaseListener {

    Stack<String> scopeStack = new Stack<>();
    public SymbolTable st;
    public int blockCount =1;

    public LitListener(SymbolTable st){
        this.st = st;
    }



    @Override public void enterFunc_decl(LittleParser.Func_declContext ctx) {
        if(ctx.getText().compareTo("END") != 0){
            String txt = ctx.getText().split("BEGIN")[0];
            txt = txt.split("INT|FLOAT|VOID|STRING")[1];
            txt = txt.split("\\(")[0];
//            System.out.println(txt);

            st.table = new SymbolTable(txt);
            st = st.table;
        }
    }



    @Override public void enterIf_stmt(LittleParser.If_stmtContext ctx) {

        String txt = ctx.getText();


        if(txt.substring(0,2).equals("IF")){
            scopeStack.push("\nBlock "+ blockCount++);
            st.table = new SymbolTable("BLOCK");
            st = st.table;
        }
    }
    @Override public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {


        scopeStack.push("\nBlock "+ blockCount++);
        st.table = new SymbolTable("BLOCK");
        st = st.table;
    }


    @Override public void enterElse_part(LittleParser.Else_partContext ctx) {
        if((ctx.getText().compareTo("") != 0) &&
                (ctx.getText().compareTo("ENDIF") != 0)){
            scopeStack.push("\nBlock "+ blockCount++);
            st.table = new SymbolTable("BLOCK");
            st = st.table;

        }
    }




    @Override public void enterParam_decl_list(LittleParser.Param_decl_listContext ctx) {
        String txt = ctx.getText();
        if(txt.compareTo("") != 0) {
            String [] vars = txt.split(",");
            for (String var : vars) {
                String name = var.split("INT|FLOAT")[1];
                String type = var.split(name)[0];


                ArrayList<String> temp = new ArrayList<>();
                temp.add(name);
                temp.add(type);
                ArrayList<List<String>> stHash = SymbolTable.map.get(st.scope);
                if (stHash == null) {
                    stHash = new ArrayList<>();
                }
                st.checkDeclError(name);
                stHash.add(temp);
                SymbolTable.map.put(st.scope, stHash);
            }
        }
    }


    @Override public void enterVar_decl(LittleParser.Var_declContext ctx){
        String idlist = ctx.getText().split("INT|FLOAT")[1];
        String type = ctx.getText().split(idlist)[0];

        idlist = idlist.split(";")[0];
        String [] ids = idlist.split(",");
        for (String id : ids) {

            ArrayList<String> temp = new ArrayList<>();
//            System.out.println(ids[i]);
            temp.add(id);
            temp.add(type);
            ArrayList<List<String>> stHash = SymbolTable.map.get(st.scope);
            if (stHash == null) {
                stHash = new ArrayList<>();
            }
            st.checkDeclError(id);
            stHash.add(temp);
            SymbolTable.map.put(st.scope, stHash);
        }
    }


    @Override public void enterString_decl(LittleParser.String_declContext ctx) {
        String txt = ctx.getText();
        String [] id_val = txt.split(":=");
        String val = id_val[1].split(";")[0];
        String id = id_val[0].split("STRING")[1];


        ArrayList<List<String>> table = SymbolTable.map.get(st.scope);
        if(table == null){
            table = new ArrayList<>();
        }
        ArrayList<String> temp = new ArrayList<>();
        temp.add(id);
        temp.add("STRING");
        temp.add(val);
        st.checkDeclError(id);
        table.add(temp);
        SymbolTable.map.put(st.scope, table);
    }

}