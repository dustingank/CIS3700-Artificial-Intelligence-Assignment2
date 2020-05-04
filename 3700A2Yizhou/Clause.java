import java.util.ArrayList;

public class Clause {

    ArrayList<Literal> literals = new ArrayList<Literal>();

    public Clause(String sentence) {
        setClause(sentence);
    }

    public Clause (Clause clause1, Clause clause2 ) {
        for(Literal literal1: clause1.literals){
            literals.add(literal1);
        }
        for(Literal literal2: clause2.literals){
            literals.add(literal2);
        }
    }

    public Clause() {
        literals = new ArrayList<Literal>();
    }

    public void setClause(String sentence) {

        sentence = sentence.replaceAll("\\(", "");
        sentence = sentence.replaceAll("\\)", "");
        String[] arrayofLiterals = sentence.split("v");
        for (String item: arrayofLiterals) {
            literals.add(new Literal(item));
        }
    }

    public void removeLiterals(Literal toBeRemove1, Literal toBeRemove2) {
        literals.remove(toBeRemove1);
        literals.remove(toBeRemove2);
    }

    public String getClause() {
        String toBeReturn = "";

        for (Literal literal: literals ) {
            toBeReturn += literal.getliteral() + " v " ;
        }
        toBeReturn = toBeReturn.trim();
        toBeReturn = toBeReturn.substring(0,toBeReturn.length() - 1);
       
        return toBeReturn;
    } 

    public int getClauseLenght() {
        return literals.size();
    }

    public Literal getLiteral() { // only if the clause is literal
        return literals.get(0);
    }

    public boolean hasAllLiterals(Clause other){
        boolean isMatch = false;

        for(Literal o: other.literals){
            for(Literal l: literals){
                if(o.getliteral().equals(l.getliteral())){
                    isMatch = true;
                } 
            }
            if(!isMatch){
                return false;
            }
            isMatch = false;
        }
        return true;
    }

}