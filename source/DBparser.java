import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.*;
import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

class DBparser {
	
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    public static void main(String[] args) {

        // System user-input loop
        boolean sys_run = true;

        // Scanner reads entire user-input line
        Scanner sc = new Scanner(System.in);

        while (sys_run) {

            // Command line prompt
            System.out.print("@DBterminal> ");

            // Get entire user-input as one string
            String command = sc.nextLine();
            //System.out.println();

            // retrieves all the commands on one-line which are delimited by a single whitespace
            if (command.equals("") || command.charAt(command.length()-1) != ';') {
                    System.out.println("User command must be terminated by \";\" character");
            } 
            else {

                // array that holds all the user commands per call
                String[] commandList = command.split("(\\; +)");
                commandList[commandList.length-1] = removeLastChar(commandList[commandList.length-1]);
                
                // parse/lex all user-commands
                for (String cmd : commandList) {
                    //System.out.println(cmd);
                    
                    // File stream or CharStream to pass to lexer/parser
                    //ANTLRInputStream input = new ANTLRFileStream(args[0]);
                    CharStream stream = new ANTLRInputStream(cmd);
                    
                    // initalize DBLexer
                    final SQLGrammarLexer lexer = new SQLGrammarLexer(stream);
                    
                    // pass Lexer into Token Stream
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    
                    // initialiaze DBparser
                    final SQLGrammarParser parser = new SQLGrammarParser(tokens);
                    
                    tokens.fill();
                    List<Token> tokenList = tokens.getTokens();
                    System.out.println();

                    // handles different commands, makes parsetree, passes command to DBengine 
                    switch (tokenList.get(0).getText()) {
                    	case "select":
                            ParseTree select_tree = parser.selection();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), select_tree);
                            break;
                        case "rename":
                            ParseTree rename_tree = parser.renaming();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), rename_tree);
                            break;
                        case "project":
                            ParseTree project_tree = parser.projection();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), project_tree);
                            break;
                        case "OPEN":
                            ParseTree open_tree = parser.opencmd();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), open_tree);
                            break;
                        case "CLOSE":
                            ParseTree close_tree = parser.closecmd();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), close_tree);
                            break;
                        case "WRITE":
                            ParseTree write_tree = parser.writecmd();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), write_tree);
                            break;
                        case "EXIT":
                            ParseTree exit_tree = parser.exitcmd();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), exit_tree);
                            break;
                        case "SHOW":
                            ParseTree show_tree = parser.showcmd();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), show_tree);
                            break;
                        case "CREATE TABLE":
                            ParseTree create_tree = parser.createcmd();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), create_tree);
                            break;
                        case "UPDATE":
                            ParseTree update_tree = parser.updatecmd();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), update_tree);
                            break;
                        case "INSERT INTO":
                            ParseTree insert_tree = parser.insertcmd();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), insert_tree);
                            break;
                        case "DELETE FROM":
                            ParseTree delete_tree = parser.deletecmd();
                            ParseTreeWalker.DEFAULT.walk(new SQLGrammarCustomListener(), delete_tree);
                            break;
                        default:
                            System.out.println("ERROR! " + "\"" + cmd + "\"" + " is not a valid command! Please re-enter the proper command!");
                            continue;
                    }
                }
                System.out.println();
            }
        }
    }
}