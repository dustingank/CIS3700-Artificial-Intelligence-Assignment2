
public class Literal {
    boolean sign;
    String symbol;

    public Literal(String literal) {
        setliteral(literal);
    }

    public void setliteral(String literal) {
        sign = false;
        literal = literal.trim();
        if(literal.charAt(0) == '~') {
            this.sign = true;
            this.symbol = literal.replaceFirst("\\~", "");
        } else {
            this.symbol = literal;
        }
    }

    public String getliteral() {
        if (!sign) {
            return symbol.trim();
        }
        return "~"+symbol.trim();
    }

    public boolean getSign() {
        return sign;
    }

    public String getSymbol() {
        return symbol.trim();
    }

}