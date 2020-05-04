import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class ExptoCnf {
    static String[] logicSymbols = {"<", "=", "^", "v"};
    static List<String> logicList = Arrays.asList(logicSymbols);

    public ExptoCnf() {}; // well, Java OO bullshit, have to this thing here, dose nothing at this time
    public static void main (String[] args) {
        ExptoCnf dummyObject = new ExptoCnf();
        String inputFileName = "";
        String outputFileName = "";
        ArrayList<String> fileData = new ArrayList<String>();
        //String path = System.getProperty("user.dir");

        inputFileName = args[0];
        outputFileName = args[1];

        inputFileName = ((inputFileName.contains(".txt")) ? inputFileName : inputFileName.concat(".txt")); //ternary operator
        //inputFileName = path + "\\\\" + inputFileName;
        outputFileName = ((outputFileName.contains(".txt")) ? outputFileName : outputFileName.concat(".txt")); //ternary operator


        fileData = dummyObject.getFileInfo(inputFileName);

        for (String item:fileData) { // convert each exp into Cnf 
            String tempItem = item;
            item = dummyObject.getCnf(item);
            item = dummyObject.checkTruthness(item);
            System.out.println("Exp: " + tempItem + "\nCNF: " + item);
            dummyObject.inputInfoToFile(item, outputFileName); // wirte Cnf into the output file
        }

    }

    private String checkTruthness(String item) {
        String toBeReturn = "";

        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(item.split("\\^")));
        Iterator<String> arrayIterator = arrayToList.iterator();

        while (arrayIterator.hasNext()) {
            String currentItem = arrayIterator.next();
            String tempSentence = currentItem.replace(")","");
            tempSentence = tempSentence.replace(" ", "");
            tempSentence = tempSentence.replace("(", "");
            String[] currentSentence = tempSentence.split("v");

            for(int i = 0; i < currentSentence.length; i++){
                for(int j = 0; j < currentSentence.length; j++){
                    if(i != j){
                        if(("~" + currentSentence[i]).trim().equals(currentSentence[j].trim())){
                            arrayIterator.remove();
                        }
                    }
                }
            }
        }

        for(int k = 0; k < arrayToList.size(); k++){
            toBeReturn = toBeReturn + arrayToList.get(k).trim();
            if(k < arrayToList.size() - 1){
                toBeReturn = toBeReturn + " ^ ";
            }
        }
        return toBeReturn;

    }


    private ArrayList<String> getFileInfo (String fileName) {
        ArrayList<String> tempFileData = new ArrayList<String>();
        
        try { // reading data from the target file

            File fileObject = new File(fileName);
            Scanner fileScanner = new Scanner(fileObject);
        
            while (fileScanner.hasNextLine()) {
                tempFileData.add(fileScanner.nextLine()); // store it into arraylist
                //System.out.println(fileScanner.nextLine());
            }

            fileScanner.close(); // close the scanner

        } catch(FileNotFoundException e) { // error handling
            System.out.println("Error Occured.");
            e.printStackTrace();
        }

        return tempFileData;

    }
    
    private void inputInfoToFile(String CnfString, String fileName) {

        File newFile = new File(fileName);
        String[] arrayofCnfString = CnfString.split("\\^");
        String toBeWritten = "";
        
        for(String item: arrayofCnfString) {
            item = item.replaceAll("\\(", "");
            item = item.replaceAll("\\)", "");
            item = removeOutisdeBrackets(item);
            toBeWritten = toBeWritten + item.trim() + "\n";
        }

        try {
            if(!newFile.exists()){
                newFile.createNewFile();
            }
           
            FileWriter fileWriter = new FileWriter(newFile, true);
            fileWriter.write(toBeWritten);
            fileWriter.close();

        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private String getParserString(String originalSentence) {
        String toBeReturn = null;
        String tempSentence = null;
        String currentOperator = null;
        String[] arrayOfString = {};
        String leftSentence = null;
        String rightSentence = null;

        tempSentence = originalSentence;
      
        for (int index = 0; index < tempSentence.length(); index++) {
            if (logicList.contains(String.valueOf(tempSentence.charAt(index)))) { // check if only logic symbol from sentence match with the logic list
                char tempChar = tempSentence.charAt(index);
                switch(tempChar) {
                    case '<':
                        arrayOfString = tempSentence.split("<->",2);
                        currentOperator = "<->";
                        tempSentence = tempSentence.substring(0,index) + 'o' + tempSentence.substring(index + 1);
                        break;
                    case '=':
                        arrayOfString = tempSentence.split("=>",2);
                        tempSentence = tempSentence.substring(0,index) + 'i' + tempSentence.substring(index + 1);
                        currentOperator = "=>";
                        break;
                    case '^':
                        arrayOfString = tempSentence.split("\\^",2);
                        tempSentence = tempSentence.substring(0, index) + 'c' + tempSentence.substring(index + 1); // so the program can just splits the next operator
                        currentOperator = "^";
                        break;
                    case 'v':
                        arrayOfString = tempSentence.split("v",2);
                        tempSentence = tempSentence.substring(0, index) + 'd' + tempSentence.substring(index + 1); // so the program can just splits the next operator
                        currentOperator = "v";
                        break;
                }
                if (checkBalance(arrayOfString)) {
                    break;
                }
            }    
        }
        
        //System.out.println("Parser String Test: " + tempSentence);
        
        if (arrayOfString.length == 0 ) {
            toBeReturn = tempSentence.replaceFirst("\\~", "") + "| |~";
        } else {

            leftSentence = arrayOfString[0];
            rightSentence = arrayOfString[1];

            if (leftSentence.charAt(0) == '~' && !checkBalance(arrayOfString)) { // the negative thing
                toBeReturn = leftSentence.replaceFirst("\\~", "") + currentOperator + rightSentence + "| |~";
            } else {
                toBeReturn = arrayOfString[0] + "|" + arrayOfString[1] + "|" + currentOperator;
            }
        }
        

        toBeReturn = toBeReturn.replaceAll("c", "^"); 
        toBeReturn = toBeReturn.replaceAll("d", "v"); 
        toBeReturn = toBeReturn.replaceAll("o", "<");
        toBeReturn = toBeReturn.replaceAll("i", "=");
       // System.out.println("Before return to getCnf(From getParserString()): " + toBeReturn);
        return toBeReturn;

    }

    private boolean checkBalance(String[] toBeChecked) {
        int frontCount = 0;
        int endCount = 0;

        for (String item: toBeChecked) {
            //System.out.println("Now Cheacking: " + item);
            if (item.contains("(") || item.contains(")")) {
                //System.out.println("break");
                frontCount = item.length() - item.replaceAll("\\(", "").length(); // count how many (
                //System.out.println(frontCount);
                endCount = item.length() - item.replaceAll("\\)", "").length(); //  count how many )
                //System.out.println(endCount);
                if (frontCount == endCount) {  // balance check
                   // System.out.println("font count: " + frontCount);
                   // System.out.println("font count: " + endCount);
                    continue;
                } else {
                   // System.out.println("Return False");
                    return false;
                }
            } else {
                continue;
            }
        }
        
       // System.out.println("Return True");
        return true;
    }

    private boolean isLiteral(String sentence) {
        boolean noSpaces = sentence.length() == sentence.replaceAll(" ", "").length();
        boolean hasLogicSymbol = sentence.contains("<->") || sentence.contains("=>") || sentence.contains("^") || sentence.contains("v") || sentence.contains("~");
        boolean hasBrackets = sentence.contains("(") || sentence.contains(")");
        return noSpaces && !hasLogicSymbol && !hasBrackets;

    }

    private String removeOutisdeBrackets(String toBeRemoved) {
        String toBeReturn = null;

        if (toBeRemoved.charAt(0) == '(' && toBeRemoved.charAt(toBeRemoved.length() - 1) == ')') { // get rid of the opeaning brakets, if it has them
            toBeRemoved = toBeRemoved.replaceFirst("\\(","");
            toBeRemoved = toBeRemoved.substring(0,toBeRemoved.length() - 1);
        }

        toBeReturn = toBeRemoved;

        return toBeReturn;
    }

    private String getCnf(String sentence) {   
        String[] arrayOfParserString = {};
        String parserString = null;
        String sentenceP = null;
        String sentenceQ = null;
        String logicSymbol = null;
        String toBeReturn = null;

        
        System.out.println("Convert " + sentence);
     
        if (isLiteral(sentence)) { // implement Rule 1 P
            toBeReturn = removeOutisdeBrackets(sentence);
            System.out.println("\tReturn " + toBeReturn);
            return toBeReturn;
        }

        parserString = getParserString(sentence);  // parse the sentence into (bod, p, q) or (~, p, "") form.
        arrayOfParserString = parserString.split("\\|", 3); // split the result

        logicSymbol = arrayOfParserString[2].trim();

        sentenceP = arrayOfParserString[0].trim();
        sentenceP = removeOutisdeBrackets(sentenceP);

        if (!logicSymbol.equals("~")) {
            sentenceQ = arrayOfParserString[1].trim();
            sentenceQ = removeOutisdeBrackets(sentenceQ);
        } 
       
        /**
         * The following section is acturally convertion
         */
        if (logicSymbol.equals("~")) {
            
            if(isLiteral(sentenceP)) { // implement Rule 2 ~(P)
                toBeReturn = "~" + sentenceP;
                System.out.println("\tReturn " + toBeReturn);
                return toBeReturn;
            }

            String paserSubString = getParserString(sentenceP);
            //System.out.println("\tpaserSubSting(inside ~): " + paserSubString + "<---->" + sentenceP);
            String[] arrayOfSubParserString = paserSubString.split("\\|", 3);
            String subLogicSymbol = arrayOfSubParserString[2];
            String subSentenceP = arrayOfSubParserString[0].trim();
            subSentenceP = removeOutisdeBrackets(subSentenceP);
            
            if (subLogicSymbol.equals("~")) { // implement Rule 2 ~(~(p))
                toBeReturn = subSentenceP;
                System.out.println("\tReturn " + toBeReturn);
                return getCnf(toBeReturn);
            } else {
                String subSentenceQ = arrayOfSubParserString[1].trim();
                subSentenceQ = removeOutisdeBrackets(subSentenceQ);

                if (subLogicSymbol.equals("v")) {
                    toBeReturn = "~(" + subSentenceP.trim() + ") ^ ~(" + subSentenceQ.trim() + ")";
                    System.out.println("\tReturn " + toBeReturn);
                    return getCnf(toBeReturn);
                } else if (subLogicSymbol.equals("^")) {
                    toBeReturn = "~(" + subSentenceP.trim() + ") v ~(" + subSentenceQ.trim() + ")";
                    System.out.println("\tReturn " + toBeReturn);
                    return getCnf(toBeReturn);
                } else {
                    toBeReturn = getCnf(sentenceP);
                    toBeReturn = "~(" + toBeReturn + ")";
                    System.out.println("\tReturn " + toBeReturn);
                    return getCnf(toBeReturn);
                }
    
            }

        } else {
            if (logicSymbol.equals("<->")) {
                if(!isLiteral(sentenceP)){
                    sentenceP = "(" + sentenceP + ")"; 
    
                }
                if(!isLiteral(sentenceQ)){
                    //System.out.println("SentenceQ: " + sentenceQ);
                    sentenceQ = "(" + sentenceQ + ")";
                }
    
                toBeReturn = "(" + sentenceP + " ^ " + sentenceQ + ")" + " v " + "(~" + sentenceP + " ^ " + "~" + sentenceQ + ")";
               
                System.out.println("\tReturn " + toBeReturn);
                return getCnf(toBeReturn);
    
            } else if (logicSymbol.equals("v")) {
                String sentenceX = getCnf(sentenceP);
                String sentenceY = getCnf(sentenceQ);
                String[] arratOfSentenceX = sentenceX.split("\\^");
                String[] arratOfSentenceY = sentenceY.split("\\^");

                String curLeft, curRight;

                toBeReturn = "";

                for(int i = 0; i < arratOfSentenceX.length; i++){
                    for(int j = 0; j < arratOfSentenceY.length; j++){

                        curLeft = arratOfSentenceX[i];
                        curRight = arratOfSentenceY[j];

                        curLeft = curLeft.replace("(", "");
                        curLeft = curLeft.replace(")", "");
                        curRight = curRight.replace("(", "");
                        curRight = curRight.replace(")", "");

                        curLeft = curLeft.trim();
                        curRight = curRight.trim();

                        toBeReturn += "(" + curLeft + " v " + curRight + ")";

                        if(i + j < arratOfSentenceY.length + arratOfSentenceX.length - 2){
                            toBeReturn += " ^ ";
                        }
                    }
                }
                System.out.println("\tReturn " + toBeReturn);
                return toBeReturn;

            } else if (logicSymbol.equals("^")) {
                toBeReturn = getCnf(sentenceP) + " ^ " +getCnf(sentenceQ);
                System.out.println("\tReturn " + toBeReturn);
                return toBeReturn;
    
            } else if (logicSymbol.equals("=>")) {
                if(!isLiteral(sentenceP)){
                    sentenceP = "(" + sentenceP + ")"; 
    
                }
                if(!isLiteral(sentenceQ)){
                    sentenceQ = "(" + sentenceQ + ")";
                }
    
                toBeReturn = "(~" + sentenceP + " v " + sentenceQ + ")";
               
                System.out.println("\tReturn " + toBeReturn);
                return getCnf(toBeReturn);
            }
        }
      
        return "Error";
    }
}


