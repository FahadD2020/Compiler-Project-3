
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;


public class Driver {
    public static void main(String[] args) throws IOException {

        ANTLRInputStream input = new ANTLRInputStream(System.in);
        LittleLexer lex = new LittleLexer(input);
        CommonTokenStream tok = new CommonTokenStream(lex);
        LittleParser parser = new LittleParser(tok);
        LittleParser.ProgramContext programContext = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        SymbolTable st = new SymbolTable();
        LitListener listener = new LitListener(st);
        walker.walk(listener, programContext);
        if(st.declErr != null){
            System.out.println(st.declErr);
        }else{
            SymbolTable tab = st;
            while(tab != null){
                tab.printTable();
                tab = tab.table;
                System.out.println("\n");
            }
        }
    }
}
