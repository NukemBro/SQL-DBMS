import java.io.*;
import java.util.*;
import java.util.Vector;
import java.io.File;

public class SQLGrammarCustomListener extends SQLGrammarBaseListener
{
    // global stack for query/command execution
    private static Stack<String> traversalStack = new Stack<String>();
    
    // keeps track of currently opened db file only one can be opened at a time
    private static File currentRelationFile = null;
    
    // current relationTable being worked on
    private static relationTable currentRelation;

    //We have an ArrayList of all teh temp tables so we can search for them by name
    private static ArrayList<relationTable> allTempTables = new ArrayList<relationTable>();
    private static int tempTableCounter = 0;
    
    // cute ANSI color codes
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    
    // relation ADT that holds relationName and vector of attributes only used when creating new relations
    public class relationTable {
        String relationName;
        String[] key;
        Vector attributeVec = new Vector<attribute>();
        
        // relationTable constructor
        public relationTable(String relationName, String[] key, Vector<attribute> attributeVec) {
            this.relationName = relationName;
            this.key = key;
            this.attributeVec = attributeVec;
        }
        
        // returns the attribute at a specified index in the attributeVec
        public attribute cast(int index) {
            return ((attribute)this.attributeVec.elementAt(index));
        }
    };
    
    // attibute ADT that holds attributeName and vector of generic data types
    public class attribute<T> {
        String attributeName;
        Vector DataVec = new Vector<T>();
        
        // attribute constuctor
        public attribute(String attributeName, Vector<T> DataVec) {
            this.attributeName = attributeName;
            this.DataVec = DataVec;
        }
        
        // returns the generic object at a specified index in DataVec
        public T cast(int index) {
            return ((T)this.DataVec.elementAt(index));
        }
    };
    
    // prints the current relation (file formatting changes will be made here)
    public void printCurrentRelation(String relation) 
    {
        if (currentRelation == null) {
            System.out.println(ANSI_RED + "No relation is currently loaded!" + ANSI_RESET);
        }
        else if (!currentRelation.relationName.equals(relation)) {
            System.out.println(ANSI_RED + "User-Specified relation \"" + relation + "\" is not currently loaded!" + ANSI_RESET);
        }
        else if (currentRelation.relationName.equals(relation)) {
            // print relation name of current global relation
            System.out.println("RELATION NAME: [" + currentRelation.relationName + "]");

            // print global relation keys to DBterminal
            System.out.print("RELATION KEYS: ");
            System.out.print("[");
            for (int i = 0; i < currentRelation.key.length; ++i) {
                if (i == currentRelation.key.length-1) {
                   System.out.print(currentRelation.key[i]); 
                } 
                else {
                    System.out.print(currentRelation.key[i] + ", ");
                }
            }
            System.out.print("]");
            System.out.println();

            // print our global relation to DBterminal
            System.out.println("PRINTING RELATION TABLE: ");
            for (int i = 0; i < currentRelation.attributeVec.size(); ++i) {
                System.out.print(currentRelation.cast(i).attributeName + ": ");
                if (currentRelation.cast(i).DataVec.isEmpty()) {
                    System.out.print("<empty>");
                    System.out.println();
                }
                else {
                    for (int j = 0; j < currentRelation.cast(i).DataVec.size(); ++j) {
                        // formatting
                        if (j == currentRelation.cast(i).DataVec.size()-1) {
                            System.out.print(currentRelation.cast(i).cast(j));
                        }
                        // formatting
                        else {
                            System.out.print(currentRelation.cast(i).cast(j) + ", ");
                        }
                    }
                    System.out.println();
                }
            }
        }
        else {
            System.out.println("Error occured in \"printCurrentRelation()\"");
        }
        System.out.println();
    }
    
    // prints the current relation (file formatting changes will be made here)
    public void printTempRelation(String relation) 
    {
    	relationTable temporaryTable = null;
    	for(int i = 0; i < allTempTables.size() ; i++)
    	{
    		if(allTempTables.get(i).relationName.equals(relation))
    		{
    			temporaryTable = allTempTables.get(i);
    		}
    		else if (temporaryTable == null && i == allTempTables.size()-1)
    		{
    			System.out.println(ANSI_RED + "No relation called " + relation + " exists in table storage! (in print)" + ANSI_RESET);
    			return;
    		}

    	}

    	if(temporaryTable != null)
    	{
            // print relation name of current global relation
            System.out.println("\nTEMPORARY RELATION NAME: [" + temporaryTable.relationName + "]");

	    // print global relation keys to DBterminal
            System.out.print("RELATION KEYS: ");
            System.out.print("[ N/A");
            System.out.print("]");
            System.out.println();

            // print our global relation to DBterminal
            System.out.println("PRINTING TEMPORARY RELATION TABLE: ");
            for (int i = 0; i < temporaryTable.attributeVec.size(); ++i) {
                System.out.print(temporaryTable.cast(i).attributeName + ": ");
                if (temporaryTable.cast(i).DataVec.isEmpty()) 
                {
                    System.out.print("<empty>");
                    System.out.println();
                }
                else {
                    for (int j = 0; j < temporaryTable.cast(i).DataVec.size(); ++j) {
                        // formatting
                        if (j == temporaryTable.cast(i).DataVec.size()-1) {
                            System.out.print(temporaryTable.cast(i).cast(j));
                        }
                        // formatting
                        else {
                            System.out.print(temporaryTable.cast(i).cast(j) + ", ");
                        }
                    }
                    System.out.println();
                }
             }
            System.out.println();
        }
    }
	
    // loads relation stored in db file to current global relation
    public void loadRelationFromDBFile(File relationFile) {
        try {
            // Load in stored relation from .db file to the currentRelation
            Scanner sc = new Scanner(relationFile);
            String relationName;
            String[] relationKeys;
            String[] relationData;
            
            // temp variables
            String line;
            String attributeName;
            attribute initAttribute;

            // retrieve database relation name
            line = sc.nextLine();
            relationName = line.substring(line.indexOf("[")+1, line.indexOf("]"));

            // retrieve database relation keys
            line = sc.nextLine();
            line = line.substring(line.indexOf("[")+1, line.indexOf("]"));
            relationKeys = line.split(", ");
            
            // create new relationTable
            relationTable relation = new relationTable(relationName, relationKeys, new Vector<attribute>());
            
            // skip "PRINTING RELATION TABLE: " line (line exists for readability purposes)
            sc.nextLine();

            // retrieve the relation data
            while (sc.hasNextLine()) {
                
                // get next line
                line = sc.nextLine();
                
                if (!line.trim().isEmpty()) {
                    // get attribute name from line
                    attributeName = line.substring(0, line.indexOf(":"));

                    // get rest of data as a string
                    line = line.substring(line.indexOf(" ")+1, line.length());

                    // get all data entries on line
                    relationData = line.split(", ");

                    // create new attribute to hold our attribute name and all data
                    initAttribute = new attribute(attributeName, new Vector<String>(Arrays.asList(relationData)));

                    // push attribute to our relationTable
                    relation.attributeVec.add(initAttribute);
                }
            }
            sc.close();
            
            // update global current relation to the newly loaded-from-file relation
            currentRelation = relation;
        } 
        catch (Exception e) {
            System.out.println(ANSI_RED + "Exception Occurred: FileNotFound!" + ANSI_RESET);
            e.printStackTrace();
        }
    }
    
    // loads relation stored in csv file to current global relation
    public void loadRelationFromCSVFile(File relationFile, String relationAsString) {
        try {
            // Load in stored relation from .db file to the currentRelation
            FileReader reader = new FileReader(relationFile);
            Scanner sc = new Scanner(reader);
            String[] relationKeys;
            String[] relationAttributes;
            String[] relationData;
            String line;
            
            // get relation name from file
            String relationName = relationAsString;

            // retrieve database relation keys, attributes = keys
            line = sc.nextLine();
            relationAttributes = line.split(",");
            relationKeys = relationAttributes;

            // create new relationTable
            relationTable relation = new relationTable(relationName, relationKeys, new Vector<attribute>());

            // create attribute and insert into relationTable
            for (int i = 0; i < relationAttributes.length; ++i) {
                attribute initAttribute = new attribute(relationAttributes[i], new Vector<String>());
                relation.attributeVec.add(initAttribute);
            }
           
            // retrieve the relation data
            while (sc.hasNextLine()) {
                
                // get next line
                line = sc.nextLine();
                
                if (!line.isEmpty()) {
                    
                    // get relation data
                    relationData = line.split(",");
                    
                    // push relation data to respective attributes
                    try {
                        for (int i = 0; i < relationData.length; ++i) {
                            if (relationData[i].isEmpty()) {
                                relation.cast(i).DataVec.add("<empty>");
                            } 
                            else {
                                if (i >= relation.attributeVec.size()) {
                                    //System.out.println("Was the file parsed correctly? " + i);
                                } 
                                else {
                                    relation.cast(i).DataVec.add(relationData[i]);
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        //System.out.println(ANSI_RED + "Exception Occurred: OOB" + ANSI_RESET);
                        //e.printStackTrace();
                    }
                }
            }
            sc.close();
            
            // update global current relation to the newly loaded-from-file relation
            currentRelation = relation;
        }
        catch (Exception e) {
            System.out.println(ANSI_RED + "Exception Occurred: loadRelationFromCSVFile" + ANSI_RESET);
            e.printStackTrace();
        }
    }

    @Override 
    public void exitSelection(SQLGrammarParser.SelectionContext ctx) 
    { 
        String relationName = traversalStack.pop();
        String condition = traversalStack.pop();
        relationTable temporaryTable = null;

        if(currentRelation != null && currentRelation.relationName.equals(relationName))
        {
            temporaryTable = currentRelation;
        }
        else
        {
            for(int i = 0; i < allTempTables.size() ; i++)
            {
                if(allTempTables.get(i).relationName.equals(relationName))
                {
                    temporaryTable = allTempTables.get(i);
                }
                else
                {
                    if(i == allTempTables.size() -1)
                	{
	                    System.out.println(ANSI_RED + "No relation called " + relationName + " exists in table storage!" + ANSI_RESET);
	                    return;
                	}
                }
            }
        }

        if(temporaryTable != null)
        {
            String[] conditionArray = condition.split(" ");
            ArrayList<String> conditionChunks = new ArrayList<String>();
            ArrayList<String> columnsToFind = new ArrayList<String>();

            //Parses out our column names from the conditional expression and stores in columnsToFind
            for(int i = 0; i < conditionArray.length; i++)
            {
                if(conditionArray[i].equals("<") || conditionArray[i].equals("<=") || conditionArray[i].equals(">") || conditionArray[i].equals(">=") || conditionArray[i].equals("==") || conditionArray[i].equals("!="))
                {
                    if(i != 0 && i != conditionArray.length-1)
                    {
                    	if(!columnsToFind.contains(conditionArray[i - 1]))
                    	{
                        	columnsToFind.add(conditionArray[i - 1]);
                   		}
                        conditionChunks.add(conditionArray[i-1] + " " + conditionArray[i] + " " + conditionArray[i+1]);
                    }
                }
            }

            //We search through our table header for every column we are looking for
            //We then make an ArrayList of our columns to search through and store in headerColumns.
            ArrayList<attribute<String>> headerColumns  = new ArrayList<attribute<String>>();
            boolean foundColumn = false;
            int columnElement = 0;
            for(int k = 0; k < columnsToFind.size() ; k++)
            {
                columnElement = 0;
                for(;columnElement < temporaryTable.attributeVec.size(); columnElement++)
                {
                    attribute<String> headerColumn = temporaryTable.cast(columnElement);
                    if(headerColumn.attributeName.equals(columnsToFind.get(k)))
                    {
                    	if(!headerColumns.contains(headerColumn))
                        	headerColumns.add(headerColumn);
                        foundColumn = true;
                    }
                }
            }

            //Step through each column we need to check and compare each value to its appropriate conditional
            //If it matches add that roww number to our print results if it isn't already in it
            if(foundColumn)
            {
                Vector<Integer> rowsToPrint = new Vector<Integer>();
                //The outer loop will loop through our columns to check
                for(int j = 0; j < headerColumns.size() ; j++)
                {    			
                	//for every element in the column
                    for(int dataElement = 0; dataElement < headerColumns.get(j).DataVec.size(); dataElement++)
                    {
                    	String tempString = condition;

                    		if( conditionChunks.size() == 1)
	                        {
	                        	String[] conditionToCheck = conditionChunks.get(0).split(" ");
		                        	tempString = tempString.replace(conditionToCheck[0] , headerColumns.get(j).DataVec.get(dataElement).toString());
	                        }
                    		else if(conditionChunks.size() == 2 && j != headerColumns.size() -1)
                    		{
		                        String[] conditionToCheck = conditionChunks.get(0).split(" ");
		                        	tempString = tempString.replace(conditionToCheck[0] , headerColumns.get(j).DataVec.get(dataElement).toString());
		                        conditionToCheck = conditionChunks.get(1).split(" ");
		                        	tempString = tempString.replace(conditionToCheck[0] , headerColumns.get(j+1).DataVec.get(dataElement).toString());
	                        }
	                        else if(conditionChunks.size() == 3 && j != headerColumns.size() -2)
                    		{
		                        String[] conditionToCheck = conditionChunks.get(0).split(" ");
		                        	tempString = tempString.replace(conditionToCheck[0] , headerColumns.get(j).DataVec.get(dataElement).toString());
		                        conditionToCheck = conditionChunks.get(1).split(" ");
		                        	tempString = tempString.replace(conditionToCheck[0] , headerColumns.get(j+1).DataVec.get(dataElement).toString());
		                        conditionToCheck = conditionChunks.get(1).split(" ");
		                        	tempString = tempString.replace(conditionToCheck[0] , headerColumns.get(j+2).DataVec.get(dataElement).toString());
	                        }
	                        else if( conditionChunks.size() > 3)
	                        {
	                        	System.err.println("We cant handle more than 3 arguments at this moment.");
	                        }
                    	
                        if(ExpressionParser.evaluate(tempString))
                        {
                            if(!rowsToPrint.contains(dataElement))
                            	rowsToPrint.add(dataElement);
                        }
                    }
                }

                // arrays to hold our relation keys, and our relation attributes
                Vector<attribute> attributeListArgs = new Vector<attribute>();
                
                //Loops through each column
                for(int j = 0; j < temporaryTable.attributeVec.size(); j++)
                {
                    Vector<String> dataToAdd = new Vector<String>();
                    //Loop through each row
                    for(int k = 0; k < rowsToPrint.size() ; k++)
                    {
                        dataToAdd.add(temporaryTable.cast(j).DataVec.get(rowsToPrint.get(k)).toString());    
                    }
                    attribute tempAttribute = new attribute(temporaryTable.cast(j).attributeName , dataToAdd);
                    attributeListArgs.add(tempAttribute);
                }

                // Get teh new table name from the user
                Scanner reader = new Scanner(System.in);
                System.out.println("Enter the temp tables name: ");
                String newName = reader.next(); 

                //Add temporary relation table to the set
                relationTable tempRelation = new relationTable(newName, null ,attributeListArgs);
                allTempTables.add(tempRelation);  
                printTempRelation(newName);
            }
        }
    }

    @Override 
    public void exitRenaming(SQLGrammarParser.RenamingContext ctx) 
    { 
    	//Get the name of the table we are renaming columns in
    	String relationName = traversalStack.pop();
        relationTable editingTable = null;

        if(currentRelation.relationName.equals(relationName))
        {
            editingTable = currentRelation;
        }
        else
        {
            for(int i = 0; i < allTempTables.size() ; i++)
            {
                if(allTempTables.get(i).relationName.equals(relationName))
                {
                    editingTable = allTempTables.get(i);
                }
                else
                {
                	if(i == allTempTables.size() -1)
                	{
	                    System.out.println(ANSI_RED + "No relation called " + relationName + " exists in table storage!" + ANSI_RESET);
	                    return;
                	}
                }
            }
    	}

    	//Remove all Parenthesis and strings and then split column names into an array
    	String newColumnNames = traversalStack.pop();
        newColumnNames = newColumnNames.replace("(" , "");
        newColumnNames = newColumnNames.replace(")" , "");
        newColumnNames = newColumnNames.replace("," , "");
    	String[] individualColumns = newColumnNames.split(" ");

    	//First lets see if we're renaming the current table
    	if(editingTable != null)
    	{
    		//Next we need to be sure they aren't trying to change more columns than we have
    		if(individualColumns.length <= editingTable.attributeVec.size())
    		{
                    //Loop through and change the column names
                    for(int i = 0; i < individualColumns.length ; i++)
                    {
                        String columnToChange = editingTable.cast(i).attributeName;
                        editingTable.cast(i).attributeName = individualColumns[i];

                        //Print out success
                        System.out.println(ANSI_GREEN + "Succesfully changed column \"" + columnToChange + "\" to \"" + individualColumns[i] + "\"" + ANSI_RESET);
                    }
    		}
                else {
                    System.out.println(ANSI_RED + "ERROR: You are trying to rename columns in " + relationName + " that don't exist!" + ANSI_RESET);
                }
    	}
        //File doesn't exist
        else
        {
            System.out.println(ANSI_RED + "Relation \"" + relationName + "\" does not exist!" + ANSI_RESET);
        }
    }

    @Override 
    public void exitProjection(SQLGrammarParser.ProjectionContext ctx) 
    {
        //Get the name of the table we are renaming columns in
    	String relationName = traversalStack.pop();
        relationTable editingTable = null;

        if(currentRelation.relationName.equals(relationName))
        {
            editingTable = currentRelation;
        }
        else
        {
            for(int i = 0; i < allTempTables.size() ; i++)
            {
                if(allTempTables.get(i).relationName.equals(relationName))
                {
                    editingTable = allTempTables.get(i);
                }
                if(i == allTempTables.size() -1)
                {
	                System.out.println(ANSI_RED + "No relation called " + relationName + " exists in table storage!" + ANSI_RESET);
	                return;
                }
            }
    	}

    	//Remove all Parenthesis and strings and then split column names into an array
    	String newColumnNames = traversalStack.pop();
        newColumnNames = newColumnNames.replace("(" , "");
        newColumnNames = newColumnNames.replace(")" , "");
        newColumnNames = newColumnNames.replace("," , "");
    	String[] individualColumns = newColumnNames.split(" ");

    	//If we found a table to project
    	if(editingTable != null)
    	{
            // arrays to hold our relation keys, and our relation attributes
            Vector<attribute> attributeListArgs = new Vector<attribute>();

            //Loops through each column we are copying
            for(int i = 0; i < individualColumns.length; i++)
            {
                //Loops through all possible combinations looking for things to add
                for(int j = 0; j < editingTable.attributeVec.size(); j++)
                {
                    Vector<String> dataToAdd = new Vector<String>();
                    if(individualColumns[i].equals(editingTable.cast(j).attributeName))
                    {
                        //Loop through every row
                        for(int k = 0; k < editingTable.cast(j).DataVec.size() ; k++)
                        {

                                dataToAdd.add(editingTable.cast(j).DataVec.get(k).toString());

                        }
                    }
                    if(!dataToAdd.isEmpty())
                    {
                        attribute tempAttribute = new attribute(editingTable.cast(j).attributeName , dataToAdd);
                        attributeListArgs.add(tempAttribute);
                    }
                }
            }

            // Get teh new table name from the user
            Scanner reader = new Scanner(System.in);
            System.out.println("Enter the projected tables name: ");
            String newName = reader.next(); 

            //Overwrite the old table
            relationTable tempRelation = new relationTable(newName, null ,attributeListArgs);
            allTempTables.add(tempRelation);  
            printTempRelation(newName);
        }
        //File doesn't exist
        else
        {
            System.out.println(ANSI_RED + "Relation \"" + relationName + "\" does not exist!" + ANSI_RESET);
        }
    }

    @Override 
    public void exitUnion(SQLGrammarParser.UnionContext ctx) 
    { 
        System.out.println("IN Union\n");
    }

    @Override 
    public void exitDifference(SQLGrammarParser.DifferenceContext ctx) 
    { 
        System.out.println("IN Difference\n");
    }

    @Override 
    public void exitProduct(SQLGrammarParser.ProductContext ctx) 
    {
        System.out.println("IN Product\n");
    }

    @Override 
    public void exitCondition(SQLGrammarParser.ConditionContext ctx) 
    { 
        String nextConjunction;
        String fullCondition;

        //This counts the number of |'s in the command
        int numberOfOrStatements = ctx.getText().length() - ctx.getText().replace("|", "").length();
        int numberOfConditions;

        if(numberOfOrStatements > 0)
        {
            fullCondition = ")";
            numberOfConditions = numberOfOrStatements / 2 + 1;

            for(int i = 0; i < numberOfConditions; i++)
            {
                nextConjunction = traversalStack.pop();

                if(i < numberOfConditions - 1) 
                    fullCondition = "|| " + nextConjunction + " " + fullCondition;
                else
                    fullCondition = "( " + nextConjunction + " " + fullCondition;
            }
            traversalStack.push(fullCondition);
        }
        else
        {
            numberOfConditions = 1;
            //fullCondition = traversalStack.pop();
        }

        //traversalStack.push(fullCondition);
    }

    @Override 
    public void exitConjunction(SQLGrammarParser.ConjunctionContext ctx) 
    { 
        String nextComparison;
        String fullConjunction;

        //This counts the number of &'s in the command and then determines 
        //The number of conjunctions by / by 2 (each conjunction has either 0,2,4,6,...)
        int numberOfAmpersands = ctx.getText().length() - ctx.getText().replace("&", "").length();
        int numberOfConjunctions;

        if(numberOfAmpersands > 0)
        {
            fullConjunction = ")";
            numberOfConjunctions = numberOfAmpersands / 2 + 1;

            for(int i = 0; i < numberOfConjunctions; i++)
            {
                nextComparison = traversalStack.pop();

                if(i < numberOfConjunctions - 1)
                    fullConjunction = "&& " + nextComparison + " " + fullConjunction;
                else
                    fullConjunction = "( " + nextComparison + " " + fullConjunction;
            }
        }
        else
        {
            numberOfConjunctions = 1;
            fullConjunction = traversalStack.pop();
        }

        traversalStack.push(fullConjunction);
    }

    @Override 
    public void exitComparison(SQLGrammarParser.ComparisonContext ctx)
    { 
        String rightOperand = traversalStack.pop();
        String leftOperand = traversalStack.pop();

        //We need to get discern what operator is being used and concatenate to our full expression
        String operatorsAndOperand = ctx.getText();
        String operator = "";
        if(operatorsAndOperand.toLowerCase().contains("<"))
        {
            operator = "<";
        }
        else if(operatorsAndOperand.toLowerCase().contains(">"))
        {
            operator = ">";
        }
        else if(operatorsAndOperand.toLowerCase().contains("<="))
        {
            operator = "<=";
        }
        else if(operatorsAndOperand.toLowerCase().contains(">="))
        {
            operator = ">=";
        }
        else if(operatorsAndOperand.toLowerCase().contains("=="))
        {
            operator = "==";
        }
        else if(operatorsAndOperand.toLowerCase().contains("!="))
        {
            operator = "!=";
        }
        else
        {
            System.out.println(ANSI_RED + "IMPROPER OPERATOR!" + ANSI_RESET);
            System.exit(0);
        }

        String fullComparison = "( " + leftOperand + " " + operator + " " + rightOperand + " )";
        traversalStack.push(fullComparison);
    }

    @Override 
    public void exitAttributeList(SQLGrammarParser.AttributeListContext ctx) 
    {         
        // get number of commas in attributeList
        int numberOfCommas = ctx.getText().length() - ctx.getText().replace(",", "").length();
        String currentAttributes = "";
        
        // only 1 key case
        if (numberOfCommas == 0) {
            currentAttributes = "(" + traversalStack.pop() + ")";
        } 
        // multiple key case
        else {
            //currentAttributes += "(";
            for (int i = 0; i < numberOfCommas + 1; ++i) {
                if (i == 0) {
                    currentAttributes = traversalStack.pop();
                } else {
                    currentAttributes = traversalStack.pop() + ", " + currentAttributes;
                }
            }
            currentAttributes = "(" + currentAttributes + ")";
        }
        traversalStack.push(currentAttributes);
    }

    @Override 
    public void exitIdentifier(SQLGrammarParser.IdentifierContext ctx) 
    { 
        String identifierAsString = ctx.getText();
        traversalStack.push(identifierAsString);
    }

    @Override 
    public void exitOpencmd(SQLGrammarParser.OpencmdContext ctx) 
    {
        try {
            // retrieve the relation name from the command (whitespaces?)
            String relationName = traversalStack.pop();

            // file to load relation from
            File relationFile = new File(relationName + ".csv");
            File relationFileCSV;
            File relationFileDB;
            
            // if 0 load CSV, if 1 load DB, CSV by default (safer)
            int fileTypeToLoad = 0;
            
            // file loader (CSV case)
            if (relationName.equals(("csceCSV")) || relationName.equals("rosterCSV")) {
                relationFileCSV = new File(relationName + ".csv");
                relationFileDB = new File(relationName + ".db");
                
                // load CSV case
                if (!relationFileDB.exists() && relationFileCSV.exists()) {
                    relationFile = relationFileCSV;
                    fileTypeToLoad = 0;
                } 
                // load DB case
                else if (relationFileDB.exists()) {
                    relationFile = relationFileDB;
                    fileTypeToLoad = 1;
                } 
                else {
                    System.out.println(ANSI_RED + "File (CSV/DB) Load Error for \"" + relationName + "\" Check exitOpencmd()" + ANSI_RESET);
                    System.out.println();
                }
            } 
            // file loader (DB case)
            else {
                relationFile = new File(relationName + ".db");
                fileTypeToLoad = 1;
            }

            // file does not exist case
            if (!relationFile.exists()) {
                System.out.println(ANSI_RED + "Relation \"" + relationName + "\" does not exist" + ANSI_RESET);
            }
            // file is already opened case
            else if (currentRelationFile != null && !currentRelationFile.equals(relationFile)) {
                System.out.println(ANSI_RED + "Relation \"" + currentRelationFile.getName() + "\" is already opened" + ANSI_RESET);
            }
            // file open case (success)
            else {
                // load relation from csv file
                if (fileTypeToLoad == 0) {
                    loadRelationFromCSVFile(relationFile, relationName);
                    currentRelationFile = relationFile;
                    System.out.println(ANSI_GREEN + "Relation \"" + relationName + "\" is loaded from \"" + relationFile.getName() + "\"" + ANSI_RESET);
                }
                // load relation from db file
                else {
                    loadRelationFromDBFile(relationFile);
                    currentRelationFile = relationFile;
                    System.out.println(ANSI_GREEN + "Relation \"" + relationName + "\" is loaded from \"" + relationFile.getName() + "\"" + ANSI_RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Exception Occurred:" + ANSI_RESET);
            e.printStackTrace();
        }
    }

    @Override 
    public void exitClosecmd(SQLGrammarParser.ClosecmdContext ctx) 
    {
        PrintStream PS_FILE = null;
        PrintStream PS_CONSOLE = null;
        
        try {
            // retrieve the relation name from the command
            String relationName = traversalStack.pop();
            
            // file handler
            File relationFile = new File(relationName + ".db");

            // file does not exist case
            if (!relationFile.exists()) {
                System.out.println(ANSI_RED + "Relation \"" + relationName + "\" does not exist" + ANSI_RESET);
            }
            // currentRelation is null case (empty)
            else if (currentRelation == null) {
                System.out.println(ANSI_RED + "No relation is currently loaded!" + ANSI_RESET);
            }
            // currentRelation != user-specified relation
            else if (!currentRelation.relationName.equals(relationName)) {
                System.out.println(ANSI_RED + "User-Specified relation \"" + relationName + "\" is not currently loaded!" + ANSI_RESET);
            }
            // file is not already open case
            else if (currentRelationFile == null || !currentRelationFile.equals(relationFile)) {
                System.out.println(ANSI_RED + "Relation \"" + relationFile.getName() + "\" is not currently open" + ANSI_RESET);
            }
            // file close case (success) file and relation name must both match current file and relation name
            else if (currentRelation.relationName.equals(relationName) && currentRelationFile.equals(relationFile)) {
                
                // file stream for our current relation db file
                FileOutputStream relationFileStream = new FileOutputStream(relationFile);
                
                // set up print stream to print to file/console
                PS_FILE = new PrintStream(relationFileStream);
                PS_CONSOLE = System.out;
                
                // redirect System.out to output to our file instead of DBterminal
                System.setOut(PS_FILE);
                
                // print current relation
                printCurrentRelation(relationName);
                
                // redirect output back to System.out (DBterminal)
                System.setOut(PS_CONSOLE);
                
                // update global currentRelation
                currentRelationFile = null;
                currentRelation = null;
                
                // sucess message
                System.out.println(ANSI_GREEN + "Sucessfully closed database file \"" + relationFile.getName() + "\"" + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Exception Occurred: FileNotFound!" + ANSI_RESET);
            e.printStackTrace();
        } finally {
            // save changes to file and close the file stream here
            if (PS_FILE != null) PS_FILE.close();
        }
    }

    @Override 
    public void exitWritecmd(SQLGrammarParser.WritecmdContext ctx) 
    {
        PrintStream PS_FILE = null;
        PrintStream PS_CONSOLE = null;
        boolean isTempTable = false;
        boolean isCurrentTable = false;

        try {
            
            // retrieve the relation name from the command
            String relationName = traversalStack.pop();
            
            // relation file name with file extension
            File relationFile = new File(relationName + ".db");
            if (currentRelation == null) {
                System.out.println(ANSI_RED + "No relation is currently loaded!" + ANSI_RESET);
            } 
            else if (!currentRelation.relationName.equals(relationName)) {
                System.out.println(ANSI_RED + "User-Specified relation \"" + relationName + "\" is not currently loaded!" + ANSI_RESET);
            }
            else if (currentRelation.relationName.equals(relationName)) {
                isCurrentTable = true;
            }
            else {
                for(int i = 0; i < allTempTables.size() ; i++) {
                    if(allTempTables.get(i).relationName.equals(relationName)) {
                        isTempTable = true;
                        break;
                    }
                    //We didn't find it and were on teh last one
                    else if(i == allTempTables.size() - 1) {
                        System.out.println(ANSI_RED + "No relation called " + relationName + " exists in table storage!" + ANSI_RESET);
                    }
                }
            }
            
            if(isCurrentTable || isTempTable) {
                // create file and save relation to it
                if (relationFile.createNewFile()) {

                    // file stream for our current relation db file
                    FileOutputStream relationFileStream = new FileOutputStream(relationFile);

                    // set up print stream to print to file/console
                    PS_FILE = new PrintStream(relationFileStream);
                    PS_CONSOLE = System.out;

                    // redirect System.out to output to our file instead of DBterminal
                    System.setOut(PS_FILE);

                    if(isCurrentTable)
                    {
                        // print current relation
                        printCurrentRelation(relationName);
                    }
                    else if(isTempTable)
                    {
                        printTempRelation(relationName);
                    }

                    // redirect output back to System.out (DBterminal)
                    System.setOut(PS_CONSOLE);
                    
                    // Update currentRelationFile to be our newly written-to .db file if source is csv
                    if (relationName.equals("csceCSV") || relationName.equals("rosterCSV")) {
                        currentRelationFile = relationFile;
                    }
                    
                    System.out.println(ANSI_GREEN + "Sucessfully saved " + relationName + " to a .db file" + ANSI_RESET);
                } 
                else {
                    System.out.println(ANSI_RED + "File already present at the specified directory" + ANSI_RESET);
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Exception Occurred: FileNotFound!" + ANSI_RESET);
            e.printStackTrace();
        } finally {
            if (PS_FILE != null) PS_FILE.close();
        }
    }

    @Override 
    public void exitExitcmd(SQLGrammarParser.ExitcmdContext ctx) 
    {
        System.exit(0);
    }

    @Override 
    public void exitShowcmd(SQLGrammarParser.ShowcmdContext ctx) 
    {         
        String relationName = traversalStack.pop();     
		
        if (currentRelation.relationName.equals(relationName)) {
            printCurrentRelation(relationName);
        }
        else
        {
            // sarch through temp table
            boolean inTemp = false;
            for(int i = 0; i < allTempTables.size() ; i++)
            {
                if(allTempTables.get(i).relationName.equals(relationName))
                {
                    printTempRelation(relationName);
                    inTemp = true;
                }
            }
            if(!inTemp) {
                System.out.println(ANSI_RED + "No relation called " + relationName + " exists in table storage!" + ANSI_RESET);
            }
        }
    }

    @Override 
    public void exitCreatecmd(SQLGrammarParser.CreatecmdContext ctx) 
    { 
        // arrays to hold our relation keys, and our relation attributes
        String[] attributeListArgs;
        String[] attributeListTypedArgs;
        
        // Get our relation arguments from stack
        String attributeList = traversalStack.pop();
        String attributeListTyped = traversalStack.pop();
        String relationName = traversalStack.pop();
        
        // remove parenthesis from attributeList and attributeListTyped
        attributeList = attributeList.substring(1, attributeList.length()-1);
        attributeListTyped = attributeListTyped.substring(1, attributeListTyped.length()-1);
        
        // split up string to store args in array
        attributeListArgs = attributeList.split(", ");
        attributeListTypedArgs = attributeListTyped.split(", ");
        
        // construct new (empty data) relation table and set current global relation
        relationTable relation = new relationTable(relationName, attributeListArgs, new Vector<attribute>());
        currentRelation = relation;
        
        // construct attributes, push back attributes into vector of attributes held by our global relationTable
        for (int i = 0; i < attributeListTypedArgs.length; ++i) {
            // construct an attribute of "int" data type
            if (attributeListTypedArgs[i].contains("INTEGER")) {
                String arg = attributeListTypedArgs[i].substring(0, attributeListTypedArgs[i].indexOf(" "));
                attribute initAttribute = new attribute(arg, new Vector());
                relation.attributeVec.addElement(initAttribute);
            }
            // construct an attribute of "string" data type
            else if (attributeListTypedArgs[i].contains("VARCHAR")) {
                String arg = attributeListTypedArgs[i].substring(0, attributeListTypedArgs[i].indexOf(" "));
                attribute initAttribute = new attribute(arg, new Vector<String>());
                relation.attributeVec.addElement(initAttribute);
            }
        }
    }

    @Override 
    public void exitUpdatecmd(SQLGrammarParser.UpdatecmdContext ctx) 
    { 
        System.out.println("In Update\n");
    }

    @Override 
    public void exitInsertcmd(SQLGrammarParser.InsertcmdContext ctx) 
    { 
        String relationName;
        
        // no relation loaded case
        if (currentRelation == null) {
            System.out.println(ANSI_RED + "No relation is currently loaded!" + ANSI_RESET);
        }
        // userRelation != currentRelation case
        else if (!traversalStack.firstElement().equals(currentRelation.relationName)){
            System.out.println(ANSI_RED + "User-specified relation \"" + traversalStack.firstElement() + "\" is not currently loaded!" + ANSI_RESET);
        } 
        // userRelation == currentRelation case (Success)
        else if (traversalStack.firstElement().equals(currentRelation.relationName)) {
            // push back into row the specified insert data
            for (int i = currentRelation.attributeVec.size()-1; i >= 0; --i) {
                currentRelation.cast(i).DataVec.addElement(traversalStack.pop());
            }
            relationName = traversalStack.pop();
            System.out.println(ANSI_GREEN + "Inserted data successfully into relation \"" + relationName + "\"" + ANSI_RESET);
        }
        System.out.println();
    }

    @Override 
    public void exitDeletecmd(SQLGrammarParser.DeletecmdContext ctx) 
    { 
        System.out.println("In Delete\n");
        
        String[] termList;
        
        // get condition and relationName from stack
        String condition = traversalStack.pop();
        String relationName = traversalStack.pop();
        
        System.out.println(condition);
        System.out.println(relationName);
    }

    @Override 
    public void exitTypedAttributeList(SQLGrammarParser.TypedAttributeListContext ctx) 
    {      
        // get number of commas in attributeList
        int numberOfCommas = ctx.getText().length() - ctx.getText().replace(",", "").length();
        String currentAttributes = "";
        
        // only 1 key case
        if (numberOfCommas == 0) {
            currentAttributes = "(" + traversalStack.pop() + ")";
        } 
        // multiple key case
        else {
            for (int i = 0; i < numberOfCommas + 1; ++i) {
                if (i == 0) {
                    currentAttributes = traversalStack.pop();
                } else {
                    currentAttributes = traversalStack.pop() + ", " + currentAttributes;
                }
            }
            currentAttributes = "(" + currentAttributes + ")";
        }
        traversalStack.push(currentAttributes);
    }

    @Override 
    public void exitType(SQLGrammarParser.TypeContext ctx) 
    { 
        String isItVarchar = ctx.getStart().getText();

        if(isItVarchar.equals("VARCHAR"))
        {
            String varcharInteger = traversalStack.pop();
            String varcharArgument = traversalStack.pop();
            String fullVarcharStatement = varcharArgument + " VARCHAR(" + varcharInteger + ")";
                traversalStack.push(fullVarcharStatement);
        }
        else if(isItVarchar.equals("INTEGER"))
        {
            String integerArgument = traversalStack.pop();
            String fullIntegerStatement = integerArgument + " INTEGER";
                traversalStack.push(fullIntegerStatement);
        }
    }
    
    @Override 
    public void exitInteger(SQLGrammarParser.IntegerContext ctx) 
    { 
        String integerAsString = ctx.getStart().getText();
            traversalStack.push(integerAsString);
    }
};