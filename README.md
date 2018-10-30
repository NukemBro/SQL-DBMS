# SQL Database Management System
An SQL Database Management System created for CSCE-315 in Spring of 2018

1. Team Members Include:
    * Reed Hampton
    * Grant Hruzek
    * Hanzhi Guo

## DBparser
This Project implements a basic Lexer and Parser for our "SQL-Like" grammar using ANTLR, an online parser generator. More information about ANTLR can be found at 'https://github.com/antlr/antlr4/blob/master/doc/getting-started.md'

Included in this project are two .jar files and this readme:
1. DBparser.jar
    * Contains all of the auto generated parser and lexer java, token, and interpret files
    * Contains the all of the SQL-DBMS functionality in order to execute the CLI-program
2. antlr-4.7.1-complete.jar
    * The library of necessary packages needed when linking the ANTLR generated parser and the DBparser.java files
    * This was provided and is the property of ANTLR

### Installation
When preparing the parser for testing there are a few things you need to do first:
* Determine which operating system you will be using
    * Linux & UNIX OS was used during testing, will also work with CLI on Windows OS (7/8/10)
* Copy both .jar files mentioned above into your directory

### Execution
Execution is very simple.
1. Be sure you have a compatible Java version installed. We used:
    * OpenJDK version "1.8.0_161"
    * OpenJDK Runtime Environment (build 1.8.0_161-b14)
    * OpenJDK 64-Bit Server VM (build 25.161-b14, mixed mode)
2. Run the following command through the UNIX/Linux terminal or Windows CLI
    * "java -cp DBparser.jar:antlr-4.7.1-complete.jar DBparser"

This will open the database terminal and allow you to start entering commands!

### Usage
WARNING: This Parser is very strict with its' expected syntax! Here are a few reminders to get you started:
* The input is case sensitive
    * Command keywords are in all caps
    * Query keywords are in all lower case
* Every command MUST end with a ';'
* In some instances it is advised that "conditions" are enclosed in '(' ')'

## Final Notes
* When running the database engine we only have the functionality for running **two** CSV's! To access these use the following commands:
    * "OPEN csceCSV;"
    * "OPEN rosterCSV;"
* They are then accessed using those same names through out the program.
* This project serves as a proof-of-concept, so only these two CSV files are provided as external data-sets.
* Although, the user can still create and manage their databases and relations internally through this DBMS via the provided CLI-environment.
* We were unable to implement the following functions due to unbalanced work load:
    * Update
    * Delete
    * Union
    * Product
    * Difference
	
* Have fun and enjoy!
