import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class ClauseBase {
    String[] filesPaths = new String[3];
    ArrayList<Clause> clausesList = new ArrayList<Clause>();

    public ClauseBase(String KbCnfFilePath, String QueryFilePath, String PerceptFilePath) {
        filesPaths[0] = KbCnfFilePath;
        filesPaths[1] = PerceptFilePath;
        filesPaths[2] = QueryFilePath;
    }

    public void loadFiles() {
        //String path = System.getProperty("user.dir");
        for (String filePath: filesPaths) {
            System.out.println("\nClause(s) in " + filePath + ":");
            filePath = ((filePath.contains(".txt")) ? filePath : filePath.concat(".txt")); //ternary operator
            //ilePath = path + "\\\\" + filePath;
            ArrayList<String> fileData = getFileInfo(filePath);
            for (String data: fileData) {
                System.out.println("\t" + data);
                clausesList.add(new Clause(data));
            }

        }
    }

    public boolean Resolution() {
        ArrayList<Clause> newList = new ArrayList<Clause>();
        int count = 0;
        Clause resolvent;
        
        System.out.println("<-----------Strart Resolution----------------->\n");

        while(true) {
            count++;

            System.out.println("\nResolution round: " + count);
            System.out.println("\tClause(s) in knowledge base: " + clausesList.size()); 
            System.out.println("\tClause(s) in new Clause list: " + newList.size());

            newList = new ArrayList<Clause>();
            for (int i = 0; i < clausesList.size() - 1; i++) {
                for (int j = i; j < clausesList.size(); j++) {
                    resolvent = Resolve(clausesList.get(i), clausesList.get(j));
                    if (resolvent == null) {
                        System.out.println("\n<-----------End Resolution----------------->");
                        return true;
                    } else if (resolvent.getClauseLenght() != 0) {
                        //System.out.println("Add to newList: " + resolvent.getClause());
                        newList.add(resolvent);
                    }
                }
            }

            if(isCycled(clausesList, newList)) {
                System.out.println("\n<-----------End Resolution----------------->");
                return false;
            }
            for(Clause newClause: newList){
                //System.out.println("Add to clausesList: " + newClause.getClause());
                clausesList.add(newClause);
            }
        }

    }

    private Boolean isCycled(ArrayList<Clause>subClauses, ArrayList<Clause>Clauses) { //  check if the subCluases is all belong clauses
        boolean isMatch = false;
        for(Clause subClause: subClauses){
            for(Clause orgialClause: Clauses){
                if(orgialClause.hasAllLiterals(subClause)){
                    isMatch = true;
                    break;
                }
            }
            if(!isMatch){
                return false;
            }
            isMatch = false;
        }
        return true;

    }

    private Clause Resolve(Clause clause1, Clause clause2) {
        Clause toBeReturn;
        //System.out.println("Test: " + clause1.getClause() + "| |" + clause2.getClause());

        if (clause1.getClauseLenght() == clause2.getClauseLenght() && clause1.getClauseLenght() == 1) { // find a empty clause
            if ( (clause1.getLiteral()).getSymbol().equals((clause2.getLiteral()).getSymbol()) && (clause1.getLiteral()).getSign() != (clause2.getLiteral()).getSign())  {
                //System.out.println("Test: " + clause1.getClause() + "| |" + clause2.getClause());
                //System.out.println("BREEEEEKKKKKK");
                return null;
            }
        }

        for (Literal literal1: clause1.literals) { // not an empty clause
            for (Literal literal2: clause2.literals) {
                //System.out.println("Test: " + literal1.getliteral() + "| |" + literal2.getliteral() + "| |");
                //System.out.println("\tTest: " + literal1.getSymbol() + "| |" + literal2.getSymbol());
                //System.out.println("\tTest: " + literal1.getSign() + "| |" + literal2.getSign());
                if (literal1.getSymbol().equals(literal2.getSymbol()) && literal1.getSign() != literal2.getSign())  {
                    toBeReturn = new Clause(clause1, clause2);
                    toBeReturn.removeLiterals(literal1, literal2);
                    //System.out.println("Test: " + clause1.getClause() + "| |" + clause2.getClause() + "| |" + toBeReturn.getClause());
                    return toBeReturn;
                }
            }
        }

        toBeReturn = new Clause();
        return toBeReturn;

    }

    private ArrayList<String> getFileInfo (String fileName) {
        ArrayList<String> tempFileData = new ArrayList<String>();
        
        try { // reading data from the target file
            File fileObject = new File(fileName);
            Scanner fileScanner = new Scanner(fileObject);
        
            while (fileScanner.hasNextLine()) {
                tempFileData.add(fileScanner.nextLine()); // store it into arraylist
            }
            fileScanner.close(); // close the scanner

        } catch(FileNotFoundException e) { // error handling
            System.out.println("Error Occured.");
            e.printStackTrace();
        }

        return tempFileData;

    }

    public void check() {
        for (Clause clause: clausesList) {
            System.err.println(clause.getClause() + clause.getClauseLenght());
        }

    }


}