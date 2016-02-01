package id.zulns.androidmathexpressionparser;

import java.util.Vector;
import java.util.Stack;
import java.util.Random;
import java.util.Locale;

public class Parser {
    private static final String AUTHOR = "ZulNs";
    private static final String VERSION = "1.0";
    private static final int VARIABLE_NAME_MAX_LENGTH = 64;
    private static final int VARIABLES_MAX_SIZE = 1024;

    private static final byte TOKEN_TYPE_NUMBER      = 1;
    private static final byte TOKEN_TYPE_VARIABLE    = 2;
    private static final byte TOKEN_TYPE_FUNCTION    = 3;
    // private static final byte TOKEN_TYPE_STRING      = 4;
    private static final byte TOKEN_TYPE_LEFT_BRACE  = 8;
    private static final byte TOKEN_TYPE_RIGHT_BRACE = 9;
    private static final byte TOKEN_TYPE_PLUS_SIGN   = 10;
    private static final byte TOKEN_TYPE_MINUS_SIGN  = 11;
    private static final byte TOKEN_TYPE_POWER       = 12;
    private static final byte TOKEN_TYPE_PERCENT     = 13;
    private static final byte TOKEN_TYPE_MULTIPLY    = 14;
    private static final byte TOKEN_TYPE_DIVIDE      = 15;
    private static final byte TOKEN_TYPE_ADD         = 16;
    private static final byte TOKEN_TYPE_SUBTRACT    = 17;
    private static final byte TOKEN_TYPE_COMMA       = 18;
    private static final byte TOKEN_TYPE_ASSIGN      = 19;

    private static final char SYMBOL_LEFT_BRACE      = '(';
    private static final char SYMBOL_RIGHT_BRACE     = ')';
    private static final char SYMBOL_POWER           = '^';
    private static final char SYMBOL_PERCENT         = '%';
    private static final char SYMBOL_MULTIPLICATION  = '*';
    private static final char SYMBOL_DIVISION        = '/';
    private static final char SYMBOL_ADDITION        = '+';
    private static final char SYMBOL_SUBTRACTION     = '-';
    private static final char SYMBOL_COMMA           = ',';
    private static final char SYMBOL_EQUAL           = '=';
    private static final char SYMBOL_DOT             = '.';
    private static final char SYMBOL_UNDERSCORE      = '_';
    private static final char SYMBOL_LETTER_E        = 'E';
    private static final char SYMBOL_SPACE           = ' ';
    private static final char SYMBOL_CARRIAGE_RETURN = '\n';
    private static final char SYMBOL_BARRIER         = ';';

    private static final int FUNCTION_ABS         = 0;
    private static final int FUNCTION_ACOS        = 1;
    private static final int FUNCTION_ACOSH       = 2;
    private static final int FUNCTION_ASIN        = 3;
    private static final int FUNCTION_ASINH       = 4;
    private static final int FUNCTION_ATAN        = 5;
    private static final int FUNCTION_ATAN2       = 6;
    private static final int FUNCTION_ATANH       = 7;
    private static final int FUNCTION_AVEDEV      = 8;
    private static final int FUNCTION_AVERAGE     = 9;
    private static final int FUNCTION_CEILING     = 10;
    private static final int FUNCTION_COMBIN      = 11;
    private static final int FUNCTION_COS         = 12;
    private static final int FUNCTION_COSH        = 13;
    private static final int FUNCTION_DEGREES     = 14;
    private static final int FUNCTION_DEVSQ       = 15;
    private static final int FUNCTION_DISTANCE    = 16;
    private static final int FUNCTION_DMS2DEC     = 17;
    private static final int FUNCTION_EVEN        = 18;
    private static final int FUNCTION_EXP         = 19;
    private static final int FUNCTION_FACT        = 20;
    private static final int FUNCTION_FACTDOUBLE  = 21;
    private static final int FUNCTION_FISHER      = 22;
    private static final int FUNCTION_FISHERINV   = 23;
    private static final int FUNCTION_FLOOR       = 24;
    private static final int FUNCTION_FRAC        = 25;
    private static final int FUNCTION_GCD         = 26;
    private static final int FUNCTION_GEOMEAN     = 27;
    private static final int FUNCTION_HEADING     = 28;
    private static final int FUNCTION_INT         = 29;
    private static final int FUNCTION_LATITUDE    = 30;
    private static final int FUNCTION_LCM         = 31;
    private static final int FUNCTION_LN          = 32;
    private static final int FUNCTION_LOG         = 33;
    private static final int FUNCTION_LOG10       = 34;
    private static final int FUNCTION_LONGITUDE   = 35;
    private static final int FUNCTION_MAX         = 36;
    private static final int FUNCTION_MEAN        = 37;
    private static final int FUNCTION_MEDIAN      = 38;
    private static final int FUNCTION_MIN         = 39;
    private static final int FUNCTION_MOD         = 40;
    private static final int FUNCTION_MODE        = 41;
    private static final int FUNCTION_MROUND      = 42;
    private static final int FUNCTION_MULTINOMIAL = 43;
    private static final int FUNCTION_ODD         = 44;
    private static final int FUNCTION_PERMUT      = 45;
    private static final int FUNCTION_PI          = 46;
    private static final int FUNCTION_POWER       = 47;
    private static final int FUNCTION_PRODUCT     = 48;
    private static final int FUNCTION_RADIANS     = 49;
    private static final int FUNCTION_RAND        = 50;
    private static final int FUNCTION_RANDBETWEEN = 51;
    private static final int FUNCTION_ROUND       = 52;
    private static final int FUNCTION_ROUNDDOWN   = 53;
    private static final int FUNCTION_ROUNDUP     = 54;
    private static final int FUNCTION_SIGN        = 55;
    private static final int FUNCTION_SIN         = 56;
    private static final int FUNCTION_SINH        = 57;
    private static final int FUNCTION_SQRT        = 58;
    private static final int FUNCTION_SQRTPI      = 59;
    private static final int FUNCTION_STDEV       = 60;
    private static final int FUNCTION_STDEVP      = 61;
    private static final int FUNCTION_SUM         = 62;
    private static final int FUNCTION_SUMSQ       = 63;
    private static final int FUNCTION_TAN         = 64;
    private static final int FUNCTION_TANH        = 65;
    private static final int FUNCTION_TRUNC       = 66;
    private static final int FUNCTION_VAR         = 67;
    private static final int FUNCTION_VARP        = 68;

    private static final String[] FUNCTIONS = {
            //  0             1             2             3             4
            "ABS",        "ACOS",       "ACOSH",      "ASIN",       "ASINH",   // 0
            "ATAN",       "ATAN2",      "ATANH",      "AVEDEV",     "AVERAGE", // 5
            "CEILING",    "COMBIN",     "COS",        "COSH",       "DEGREES", //10
            "DEVSQ",      "DISTANCE",   "DMS2DEC",    "EVEN",       "EXP",     //15
            "FACT",       "FACTDOUBLE", "FISHER",     "FISHERINV",  "FLOOR",   //20
            "FRAC",       "GCD",        "GEOMEAN",    "HEADING",    "INT",     //25
            "LATITUDE",   "LCM",        "LN",         "LOG",        "LOG10",   //30
            "LONGITUDE",  "MAX",        "MEAN",       "MEDIAN",     "MIN",     //35
            "MOD",        "MODE",       "MROUND",     "MULTINOMIAL","ODD",     //40
            "PERMUT",     "PI",         "POWER",      "PRODUCT",    "RADIANS", //45
            "RAND",       "RANDBETWEEN","ROUND",      "ROUNDDOWN",  "ROUNDUP", //50
            "SIGN",       "SIN",        "SINH",       "SQRT",       "SQRTPI",  //55
            "STDEV",      "STDEVP",     "SUM",        "SUMSQ",      "TAN",     //60
            "TANH",       "TRUNC",      "VAR",        "VARP"                   //65
    };

    private static final int[] FUNCTION_ARGS = {
            //  0             1             2             3             4
            0x0001,       0x0001,       0x0001,       0x0001,       0x0001,    // 0
            0x0001,       0x0002,       0x0001,       0xff01,       0xff01,    // 5
            0x0201,       0x0002,       0x0001,       0x0001,       0x0001,    //10
            0xff01,       0x0004,       0x0301,       0x0001,       0x0001,    //15
            0x0001,       0x0001,       0x0001,       0x0001,       0x0201,    //20
            0x0001,       0xff01,       0xff01,       0x0004,       0x0001,    //25
            0x0004,       0xff01,       0x0001,       0x0201,       0x0001,    //30
            0x0004,       0xff01,       0xff01,       0xff01,       0xff01,    //35
            0x0002,       0xff01,       0x0002,       0xff01,       0x0001,    //40
            0x0002,       0x0000,       0x0002,       0xff01,       0x0001,    //45
            0x0000,       0x0002,       0x0201,       0x0002,       0x0201,    //50
            0x0001,       0x0001,       0x0001,       0x0001,       0x0001,    //55
            0xff01,       0xff01,       0xff01,       0xff01,       0x0001,    //60
            0x0001,       0x0201,       0xff01,       0xff01                   //65
    };

    private static final byte[][] DFA_STATES = {
        /*               0   1   2   3   4   5   6   7   8   9  10  11  12  13 */
        /*              0-9  .   e  a-z  (   %  ^/*  +   -   ,  )   =  spc end */
        /* q0 start */{ 13, 13, 12, 12,  3, -1, -1,  8,  9, -1, -1, 15,  0, 16 },
        /* q1   cN  */{ -2, -2, -2, -2, -2,  4,  5, 10, 11,  6,  7, -2,  1, 16 },
        /* q2   cF  */{ -2, -2, -2, -2,  3,  4,  5, 10, 11,  6,  7, 15,  2, 16 },
        /* q3   (   */{ 13, 13, 12, 12,  3, -2, -2,  8,  9,  6,  7, -2,  3, -3 },
        /* q4   %   */{ -2, -2, -2, -2, -2,  4,  5, 10, 11,  6,  7, -2,  4, 16 },
        /* q5   ^/* */{ 13, 13, 12, 12,  3, -2, -2,  8,  9, -2, -2, -2,  5, -3 },
        /* q6   ,   */{ 13, 13, 12, 12,  3, -2, -2,  8,  9,  6,  7, -2,  6, -3 },
        /* q7   )   */{ -2, -2, -2, -2, -2,  4,  5, 10, 11,  6,  7, -2,  7, 16 },
        /* q8   + P */{ 13, 13, 12, 12,  3, -2, -2,  8,  9, -2, -2, -2,  8, -3 },
        /* q9   - N */{ 13, 13, 12, 12,  3, -2, -2,  8,  9, -2, -2, -2,  9, -3 },
        /* q10  +   */{ 13, 13, 12, 12,  3, -2, -2,  8,  9, -2, -2, -2, 10, -3 },
        /* q11  -   */{ 13, 13, 12, 12,  3, -2, -2,  8,  9, -2, -2, -2, 11, -3 },
        /* q12  F   */{ 12, -2, 12, 12,  3,  4,  5, 10, 11,  6,  7, 15,  2, 16 },
        /* q13  N   */{ 13, 13, 14, 13, -2,  4,  5, 10, 11,  6,  7, -2,  1, 16 },
        /* q14  Ne  */{ 13, 13, 14, 13, -2,  4,  5, 14, 14,  6,  7, -2,  1, 16 },
        /* q15  =   */{ 13, 13, 12, 12,  3, -2, -2,  8,  9, -2, -2, -2, 15, 16 }
    };

    private Locale locale;
    private String expr;
    private double result;
    private String errMsg;
    private int errSelStart;
    private int errSelEnd;
    private boolean isError;
    private VarList varList;
    private Vector<Token> tokens;
    private Vector<Token> rpnTree;

    public Parser() {
        init();
        expr = "";
    }

    public Parser(String expr) {
        init();
        parse(expr);
    }

    private void init() {
        locale = Locale.US;
        result = 0.0d;
        errMsg = "";
        errSelStart = -1;
        errSelEnd = -1;
        isError = false;
        varList = new VarList();
        tokens = new Vector<>();
        rpnTree = new Vector<>();
    }

    public void parse(String expr) {
        if (expr.equals(""))
            return;
        this.expr = expr;
        result = 0.0d;
        errMsg = "";
        errSelStart = -1;
        errSelEnd = -1;
        isError = tokenize(expr.toUpperCase(locale) + Character.toString(SYMBOL_BARRIER));
        if (tokens.size() == 0)
            return;
        if (!isError){
            isError = check();
            if (!isError)
                isError = eval();
        }
    }

    public double getResult() {
        return result;
    }

    public String getResultString() {
        return doubleToString(result);
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorMessage() {
        return errMsg;
    }

    public int getErrorSelectionStart() {
        return errSelStart;
    }

    public int getErrorSelectionEnd() {
        return errSelEnd;
    }

    public String getExpression() {
        if (tokens.size() == 0) {
            return expr;
        }
        String s = "";
        for (int i = 0; i < tokens.size(); i++) {
            s += tokens.elementAt(i).toString();
        }
        return s;
    }

    public static String[] getFunctionsList() {
        int len = FUNCTIONS.length;
        String[] list = new String[len];
        for (int i = 0; i < len; i++) {
            list[i] = FUNCTIONS[i] + "(";
            switch (FUNCTION_ARGS[i]) {
                case 0x0001:
                    list[i] += "n";
                    break;
                case 0x0002:
                    list[i] += "n1, n2";
                    break;
                case 0x0004:
                    list[i] += "n1, n2, n3, n4";
                    break;
                case 0x0201:
                    list[i] += "n1, [n2]";
                    break;
                case 0x0301:
                    list[i] += "n1, [n2], [n3]";
                    break;
                case 0xff01:
                    list[i] += "n1, [n2], ..., [n255]";
            }
            list[i] += ")";
        }
        return list;
    }

    public static int getFunctionsSize() {
        return FUNCTIONS.length;
    }

    public String[] getVariablesList() {
        int len = varList.size();
        String[] list = new String[len];
        for (int i = 0; i < len; i++)
            list[i] = varList.getName(i) + " = " + doubleToString(varList.getValue(i));
        return list;
    }

    public int getVariablesSize() {
        return varList.size();
    }

    public void clearVariables() {
        varList.clear();
    }

    public void removeVariableAt(int index) {
        varList.removeElementAt(index);
    }

    @Override
    public String toString() {
        return Parser.class.getName() + " Version " + VERSION + " by " + AUTHOR;
    }

    public String tokensArrayToString() {
        return vectorTokenToString(tokens);
    }

    public String rpnTreeToString() {
        return vectorTokenToString(rpnTree);
    }

    private static String vectorTokenToString(Vector<Token> vt) {
        if (vt.size() == 0)
            return "";
        String s = "";
        Token t;
        for (int i = 0; i < vt.size(); i++) {
            t = vt.elementAt(i);
            s += "[";
            switch (t.type) {
                case TOKEN_TYPE_NUMBER:
                    s += "N";
                    break;
                case TOKEN_TYPE_VARIABLE:
                    s += "V";
                    break;
                case TOKEN_TYPE_FUNCTION:
                    s += "F";
                    break;
                case TOKEN_TYPE_PLUS_SIGN:
                case TOKEN_TYPE_MINUS_SIGN:
                    s += "S";
                    break;
                case TOKEN_TYPE_POWER:
                case TOKEN_TYPE_MULTIPLY:
                case TOKEN_TYPE_DIVIDE:
                case TOKEN_TYPE_ADD:
                case TOKEN_TYPE_SUBTRACT:
                    s += "O";
                    break;
                default:
                    s += t.toString();
            }
            s += ":" + t.toString() + "] ";
        }
        return s;
    }

    public String toDMSString() {
        double r = Math.abs(result),
                d = Math.floor(r),
                m = Math.floor((r - d) * 60),
                s = ExtMath.round((r - d - m / 60) * 3600, 3);
        String str = doubleToString(Math.signum(result) * d);
        str += "\u00b0 " + doubleToString(m);
        str += "' " + doubleToString(s) + "\"";
        return str;
    }

    public String toOriginalHexString() {
        long lb = Double.doubleToLongBits(result);
        String r = Long.toString((lb >> 60) & 15L, 16);
        r += (Long.toString((lb & 0x7fffffffffffffffL) |
                0x4000000000000000L, 16)).substring(1);
        return "0x" + r; // r.toUpperCase();
    }

    public String toHexString() {
        if (Double.isNaN(result))
            return "NaN";
        if (Double.isInfinite(result))
            return Double.toString(result);
        long lb = Double.doubleToLongBits(result);
        String rslt = (((lb >> 63) & 1L) == 1L) ? "-" : "";
        if ((lb & 0x7fffffffffffffffL) == 0) return rslt + "0";
        int exp = (int) ((lb >> 52) & 0x7ffL) - 1023; // extract exponent
        lb &= 0x000fffffffffffffL;
        if (exp > -1023) lb |= 0x0010000000000000L; // case for normal value
        else exp++;                                 // case for subnormal value
        exp -= 52;
        while ((Math.abs(exp) % 4) != 0) {
            lb <<= 1;
            exp--;
        }
        exp /= 4;
        while ((lb & 15L) == 0L) {
            lb >>= 4;
            exp++;
        }
        String hex = Long.toString(lb, 16);
        int dp = hex.length();
        while (exp < 0 && dp > 1) {
            dp--;
            exp++;
        }
        while (exp < 0 && exp > -4) {
            hex = "0" + hex;
            exp++;
        }
        while (exp > 0 && hex.length() + exp < 15) {
            hex += "0";
            exp--;
            dp++;
        }
        while (exp > hex.length() && dp > 1) {
            dp--;
            exp++;
        }
        rslt += hex.substring(0, dp);
        if (hex.length() > dp )
            rslt += "." + hex.substring(dp);
        if (exp != 0) rslt += " * 16 ^ " + Integer.toString(exp);
        return rslt; // rslt.toUpperCase();
    }

    private boolean tokenize(String expr) {
        tokens.clear();
        String buf = "";
        Token tk = null;
        TokenFunct tf;
        char chr;
        byte type = 0;
        int q = 0, q0, idx;
        for (int i = 0; i < expr.length(); i++) {
            chr = expr.charAt(i);
            q0 = q;
            q = deltaQ(q, chr);
            if (q >= 12 && q <= 14) {     // q  =  F ||  N ||  Ne
                if (q0 < 12 || q0 > 14) { // q0 = !F && !N && !Ne
                    buf = "";
                    errSelStart = i;
                }
                buf += chr;
                continue;
            }
            if (q0 == 12) {              // q0 = F
                idx = getFunctIdx(buf);
                if (idx != -1) {
                    tf = new TokenFunct(idx);
                    tk = new Token(TOKEN_TYPE_FUNCTION, tf);
                    tokens.addElement(tk);
                }
                else {
                    if (buf.equalsIgnoreCase(AUTHOR)) {
                        errMsg = buf + " is my author";
                        tokens.clear();
                        errSelEnd = i;
                        return true;
                    }
                    if (buf.length() > VARIABLE_NAME_MAX_LENGTH)
                        buf = buf.substring(0, VARIABLE_NAME_MAX_LENGTH);
                    idx = varList.append(buf);
                    if (idx == -1) {
                        errMsg = "Insufficient memory to hold variable: " + buf;
                        tokens.clear();
                        errSelEnd = i;;
                        return true;
                    }
                    tk = new Token(TOKEN_TYPE_VARIABLE, idx);
                    tokens.addElement(tk);
                }
            }
            if (q0 == 13 || q0 == 14) {  // q0 = N || Ne
                double num;
                try {
                    num = Double.parseDouble(buf);
                } catch (NumberFormatException nfe) {
                    errMsg = "Invalid number specification: " + buf;
                    tokens.clear();
                    errSelEnd = i;
                    return true;
                }
                if (Double.valueOf(num).isInfinite()) {
                    // Double.MAX_VALUED = 1.7976931348623157E+308
                    // Double.MIN_VALUED = 4.9E-324
                    errMsg = "Overflow value: " + buf;
                    tokens.clear();
                    errSelEnd = i;
                    return true;
                }
                tk = new Token(TOKEN_TYPE_NUMBER, num);
                tokens.addElement(tk);
            }
            if (q == 16) {
                if (tk !=null && tk.type == TOKEN_TYPE_FUNCTION) {
                    errMsg = "Illegal ending: " + buf;
                    tokens.clear();
                    errSelEnd = i;
                    return true;
                }
                break;
            }
            if (chr == SYMBOL_SPACE || chr == SYMBOL_CARRIAGE_RETURN)
                continue;
            if (q < 0) {
                switch (q) {
                    case -1:
                        errMsg = "Illegal beginning: " + chr;
                        errSelStart = i;
                        errSelEnd = ++i;
                        break;
                    case -2:
                        errMsg = "Illegal sequencing: " + tk.toString() + " " + chr;
                        errSelEnd = ++i;
                        break;
                    case -3:
                        errMsg = "Illegal ending: " + tk.toString();
                        errSelEnd = i;
                        break;
                    case -4:
                        errMsg = "Unrecognized symbol: " + chr;
                        errSelStart = i;
                        errSelEnd = ++i;
                }
                tokens.clear();
                return true;
            }
            if (tk != null && tk.type == TOKEN_TYPE_FUNCTION) {
                if (q >= 4 && q <= 7 || q == 10 || q == 11) {
                    errMsg = "Illegal use of variable name: " + buf;
                    tokens.clear();
                    errSelEnd = i;
                    return true;
                }
            }
            switch (q) {
                case 3:  // '('
                    if (tk != null && tk.type == TOKEN_TYPE_VARIABLE) {
                        varList.removeLastElement();
                        errMsg = "Unrecognized function: " + buf;
                        tokens.clear();
                        errSelEnd = i;
                        return true;
                    }
                    type = TOKEN_TYPE_LEFT_BRACE;
                    break;
                case 4:
                    type = TOKEN_TYPE_PERCENT;
                    break;
                case 5:  //  '^' || '*' || '/'  (Operator)
                    switch (chr) {
                        case SYMBOL_POWER:
                            type = TOKEN_TYPE_POWER;
                            break;
                        case SYMBOL_MULTIPLICATION:
                            type = TOKEN_TYPE_MULTIPLY;
                            break;
                        case SYMBOL_DIVISION:
                            type = TOKEN_TYPE_DIVIDE;
                    }
                    break;
                case 6:  //  ','                (Comma or Argument Separator)
                    if (q0 == 3 || q0 == 6)
                        // "(,"   ->   "(0,"   ||   ",,"   ->   ",0,"
                        tokens.addElement(new Token(TOKEN_TYPE_NUMBER, 0.0d));
                    type = TOKEN_TYPE_COMMA;
                    break;
                case 7:  //  ')'                (Right-Parenthesis)
                    if (q0 == 6)  // ",)"   ->   ",0)"
                        tokens.addElement(new Token(TOKEN_TYPE_NUMBER, 0.0d));
                    type = TOKEN_TYPE_RIGHT_BRACE;
                    break;
                case 8:   //  '+'                (Plus Sign)
                    type = TOKEN_TYPE_PLUS_SIGN;
                    break;
                case 9:   //  '-'                (Minus Sign)
                    type = TOKEN_TYPE_MINUS_SIGN;
                    break;
                case 10:  //  '+'                (Addition Operator)
                    type = TOKEN_TYPE_ADD;
                    break;
                case 11:  //  '-'                (Subtraction Operator)
                    type = TOKEN_TYPE_SUBTRACT;
                    break;
                case 15:  //  '='
                    if (tk != null && tk.type == TOKEN_TYPE_FUNCTION) {
                        errMsg = "Illegal assignment: " + buf + " " + chr;
                        tokens.clear();
                        errSelEnd = ++i;
                        return true;
                    }
                    if (tokens.size() > 1) {
                        errMsg = "Invalid assignment: " + tk.toString() + " " + chr;
                        tokens.clear();
                        errSelEnd = ++i;
                        return true;
                    }
                    type = TOKEN_TYPE_ASSIGN;
            }
            tk = new Token(type, chr);
            tokens.addElement(tk);
            errSelStart = i;
        }
        errSelStart = -1;
        return false;
    }

    private boolean check() {
        if (tokens.isEmpty())
            return false;
        Stack<Token> st = new Stack<>();
        Stack<Integer> si = new Stack<>();
        Token tk;
        TokenFunct tf;
        int commaCtr, args, minArgs, maxArgs;
        int idx;
        for (int i = 0; i < tokens.size(); i++) {
            tk = tokens.elementAt(i);
            if (tk.type == TOKEN_TYPE_FUNCTION) {
                st.push(tk);
                si.push(i);
                i++; // bypass an open parenthesis after a function
            }
            else if (tk.type == TOKEN_TYPE_LEFT_BRACE) {
                st.push(tk);
                si.push(i);
            }
            else if (tk.type == TOKEN_TYPE_COMMA) {
                if (st.empty()) {
                    errMsg = "Unknown arguments: " + getTokensString(i - 1, 3);
                    errSelStart = indexToPosition(i - 1);
                    errSelEnd = indexToPosition(i + 2);
                    return true;
                }
                st.push(tk);
            }
            else if (tk.type == TOKEN_TYPE_RIGHT_BRACE) {
                commaCtr = 0;
                while (true) {
                    if (st.empty()) {
                        errMsg = "Unbalanced brackets: excess of close parenthesis";
                        return true;
                    }
                    tk = st.pop();
                    if (tk.type == TOKEN_TYPE_COMMA) {
                        commaCtr++;
                        continue;
                    }
                    break;
                }
                if (tokens.elementAt(i - 1).type == TOKEN_TYPE_LEFT_BRACE)
                    args = 0;
                else
                    args = commaCtr + 1;
                idx = si.pop();
                if (tk.type == TOKEN_TYPE_LEFT_BRACE) {
                    if (commaCtr > 0) {
                        errMsg = "Arguments without function: " + getTokensString(idx, i - idx + 1);
                        errSelStart = indexToPosition(idx);
                        errSelEnd = indexToPosition(i + 1);
                        return true;
                    }
                    if (args == 0) {
                        errMsg = "Detected an empty parentheses: " + getTokensString(idx - 1, 4);
                        errSelStart = indexToPosition(idx);
                        errSelEnd = indexToPosition(idx + 2);
                        return true;
                    }
                    continue;
                }
                // when token type is a function
                tf = (TokenFunct) tk.token;
                minArgs = tf.numArgs;
                maxArgs = minArgs >> 8;
                minArgs &= 0x00ff;
                if (maxArgs == 0)
                    maxArgs = minArgs;
                if (args < minArgs && args == 0) {
                    errMsg = "No arguments for this function: " + getTokensString(idx, i - idx + 1);
                    errSelStart = indexToPosition(idx);
                    errSelEnd = indexToPosition(i + 1);
                    return true;
                }
                else if (args < minArgs) {
                    errMsg = "Too few arguments for this function: " + getTokensString(idx, i - idx + 1);
                    errSelStart = indexToPosition(idx);
                    errSelEnd = indexToPosition(i + 1);
                    return true;
                }
                else if (args > maxArgs) {
                    errMsg = "Too many arguments for this function: " + getTokensString(idx, i - idx + 1);
                    errSelStart = indexToPosition(idx);
                    errSelEnd = indexToPosition(i + 1);
                    return true;
                }
                if (minArgs != maxArgs)
                    tf.numArgs = args;
                // ((TokenFunct) ((Token) tokens.elementAt(idx)).token).numArgs = args;
            }
        }
        if (!st.empty()) {
            errMsg = "Unbalanced brackets: excess of open parenthesis";
            return true;
        }
        return false;
    }

    private boolean eval() {
        boolean err = false;
        if (tokens.isEmpty())
            return false;
        boolean isAssign = buildRPN();
        if (isAssign && rpnTree.isEmpty()) {
            result = varList.getValue(((Integer) tokens.elementAt(0).token));
            return false;
        }
        err = execute();
        if (isAssign && !err)
            varList.setValue(((Integer) tokens.elementAt(0).token), result);
        return err;
    }

    // Builds Reverse Polish Notation (RPN) tokens.
    private boolean buildRPN() {
        boolean isAssign = false;
        rpnTree.clear();
        Stack<Token> st = new Stack<>();
        Token tk, tk0;
        byte type;
        for (int i = 0; i < tokens.size(); i++) {
            tk  = tokens.elementAt(i);
            type = tk.type;
            if (type == TOKEN_TYPE_ASSIGN) {
                if (i == 1) {
                    isAssign = true;
                    rpnTree.clear();
                }
                continue;
            }
            if (type == TOKEN_TYPE_NUMBER || type == TOKEN_TYPE_VARIABLE) {
                rpnTree.addElement(tk);
                continue;
            }
            if (type == TOKEN_TYPE_LEFT_BRACE || type == TOKEN_TYPE_FUNCTION) {
                st.push(tk);
                continue;
            }
            if (type == TOKEN_TYPE_COMMA) {
                while (peekType(st) != TOKEN_TYPE_LEFT_BRACE) {
                    rpnTree.addElement(st.pop());
                }
                continue;
            }
            if (type == TOKEN_TYPE_RIGHT_BRACE) {
                while (peekType(st) != TOKEN_TYPE_LEFT_BRACE) {
                    rpnTree.addElement(st.pop());
                }
                st.pop(); // remove '(' from stack
                if (peekType(st) == TOKEN_TYPE_FUNCTION)
                    rpnTree.addElement(st.pop());
                continue;
            }
            while (!st.empty()) {
                tk0 = st.peek();
                if ((tk0.type == TOKEN_TYPE_PLUS_SIGN || tk0.type == TOKEN_TYPE_MINUS_SIGN) &&
                        (tk.type == TOKEN_TYPE_PLUS_SIGN || tk.type == TOKEN_TYPE_MINUS_SIGN))
                    break;
                if (tk0.type != TOKEN_TYPE_LEFT_BRACE &&
                        getPriority(tk0.type) <= getPriority(tk.type)) {
                    rpnTree.addElement(st.pop());
                    continue;
                }
                break;
            }
            st.push(tk);
        }
        while (!st.empty()) {
            rpnTree.addElement(st.pop());
        }
        return isAssign;
    }

    private boolean execute() {
        Stack<Double> st = new Stack<Double>();
        Vector<Double> args = new Vector<Double>();
        Token tk;
        TokenFunct tf;
        int numArgs;
        double oprn1, oprn2;
        // byte type;
        for (int i = 0; i < rpnTree.size(); i++) {
            tk = rpnTree.elementAt(i);
            args.clear();
            if (tk.type == TOKEN_TYPE_VARIABLE)
                result = varList.getValue((Integer) tk.token);
            else if (tk.type == TOKEN_TYPE_NUMBER)
                result = (Double) tk.token;
            else if (tk.type == TOKEN_TYPE_FUNCTION) {
                tf = (TokenFunct) tk.token;
                numArgs = tf.numArgs;
                if (numArgs > 0) {
                    for (int j = numArgs; j > 0; j--)
                        args.addElement(st.pop());
                }
                result = performMathFunctions(tf, args);
                if (isResultHasError()) {
                    errMsg += tf.toString() + "(";
                    for (int j = numArgs; j > 0; j--)
                        errMsg += doubleToString(args.elementAt(j - 1)) + ",";
                    errMsg = errMsg.substring(0, errMsg.length() - 1) + ")";
                    return true;
                }
            }
            else if (tk.type == TOKEN_TYPE_PLUS_SIGN || tk.type == TOKEN_TYPE_MINUS_SIGN) {
                result = st.pop();
                if (tk.type == TOKEN_TYPE_MINUS_SIGN)
                    result = -result;
            }
            else if (tk.type == TOKEN_TYPE_PERCENT)
                result = st.pop() / 100.0d;
            else { // c == 'O'
                oprn2 = st.pop();
                oprn1 = st.pop();
                result = performBasicOpr(tk.type, oprn1, oprn2);
                if (isResultHasError()) {
                    errMsg += doubleToString(oprn1) + tk.toString() + doubleToString(oprn2);
                    return true;
                }
            }
            if (i < rpnTree.size() - 1) {
                st.push(result);
            }
        }
        return false;
    }

    private static double performBasicOpr(byte type, double oprn1, double oprn2) {
        switch (type) {
            case TOKEN_TYPE_POWER:
                return Math.pow(oprn1, oprn2);
            case TOKEN_TYPE_MULTIPLY:
                return oprn1 * oprn2;
            case TOKEN_TYPE_DIVIDE:
                return oprn1 / oprn2;
            case TOKEN_TYPE_ADD:
                return oprn1 + oprn2;
            case TOKEN_TYPE_SUBTRACT:
                return oprn1 - oprn2;
        }
        return 0.0; // never reach here...
    }

    private static double performMathFunctions(TokenFunct tf, Vector<Double> args) {
        if (tf.functCode == FUNCTION_PI)
            return Math.PI;
        if (tf.functCode == FUNCTION_RAND)
            return Math.random();
        int numArgs = args.size();
        double a[] = new double[numArgs];
        for (int i = 0; i < numArgs; i++)
            a[i] = args.elementAt(numArgs - 1 - i);
        switch (tf.functCode) {
            case FUNCTION_ABS:
                return Math.abs(a[0]);
            case FUNCTION_ACOS:
                return Math.acos(a[0]);
            case FUNCTION_ACOSH:
                return Math.log(a[0] + Math.sqrt(a[0] * a[0] - 1.0d));
            case FUNCTION_ASIN:
                return Math.asin(a[0]);
            case FUNCTION_ASINH:
                return Math.log(a[0] + Math.sqrt(a[0] * a[0] + 1.0d));
            case FUNCTION_ATAN:
                return Math.atan(a[0]);
            case FUNCTION_ATAN2:
                // reverse x and y param in java
                return Math.atan2(a[1], a[0]);
            case FUNCTION_ATANH:
                return Math.log((1.0d + a[0]) / (1.0d - a[0])) / 2.0d;
            case FUNCTION_AVEDEV:
                return ExtMath.aveDev(a);
            case FUNCTION_AVERAGE:
                return ExtMath.mean(a);
            case FUNCTION_CEILING:
                if (numArgs == 1)
                    return Math.ceil(a[0]);
                return ExtMath.ceiling(a[0], a[1]);
            case FUNCTION_COMBIN:
                return ExtMath.combin(a[0], a[1]);
            case FUNCTION_COS:
                return Math.cos(a[0]);
            case FUNCTION_COSH:
                return Math.cosh(a[0]);
            case FUNCTION_DEGREES:
                return Math.toDegrees(a[0]);
            case FUNCTION_DEVSQ:
                return ExtMath.devSq(a);
            case FUNCTION_DISTANCE:
                return ExtMath.distance(a[0], a[1], a[2], a[3]);
            case FUNCTION_DMS2DEC:
                return ExtMath.dms2dec(a);
            case FUNCTION_EVEN:
                return ExtMath.even(a[0]);
            case FUNCTION_EXP:
                return Math.exp(a[0]);
            case FUNCTION_FACT:
                return ExtMath.fact(a[0]);
            case FUNCTION_FACTDOUBLE:
                return ExtMath.factDouble(a[0]);
            case FUNCTION_FISHER:
                return ExtMath.fisher(a[0]);
            case FUNCTION_FISHERINV:
                return ExtMath.fisherInv(a[0]);
            case FUNCTION_FLOOR:
                if (numArgs == 1)
                    return Math.floor(a[0]);
                return ExtMath.floor(a[0], a[1]);
            case FUNCTION_FRAC:
                return ExtMath.frac(a[0]);
            case FUNCTION_GCD:
                return ExtMath.gcd(a);
            case FUNCTION_GEOMEAN:
                return ExtMath.geoMean(a);
            case FUNCTION_HEADING:
                return ExtMath.heading(a[0], a[1], a[2], a[3]);
            case FUNCTION_INT:
                return Math.floor(a[0]);
            case FUNCTION_LATITUDE:
                return ExtMath.latitude(a[0], a[1], a[2], a[3]);
            case FUNCTION_LCM:
                return ExtMath.lcm(a);
            case FUNCTION_LN:
                return Math.log(a[0]);
            case FUNCTION_LOG:
                if (numArgs == 1)
                    return Math.log10(a[0]);
                return Math.log(a[0]) / Math.log(a[1]);
            case FUNCTION_LOG10:
                return Math.log10(a[0]);
            case FUNCTION_LONGITUDE:
                return ExtMath.longitude(a[0], a[1], a[2], a[3]);
            case FUNCTION_MAX:
                return ExtMath.max(a);
            case FUNCTION_MEAN:
                return ExtMath.mean(a);
            case FUNCTION_MEDIAN:
                return ExtMath.median(a);
            case FUNCTION_MIN:
                return ExtMath.min(a);
            case FUNCTION_MOD:
                return ExtMath.mod(a[0], a[1]);
            case FUNCTION_MODE:
                return ExtMath.mode(a);
            case FUNCTION_MROUND:
                return ExtMath.mRound(a[0], a[1]);
            case FUNCTION_MULTINOMIAL:
                return ExtMath.multiNomial(a);
            case FUNCTION_ODD:
                return ExtMath.odd(a[0]);
            case FUNCTION_PERMUT:
                return ExtMath.permut(a[0], a[1]);
            case FUNCTION_POWER:
                return Math.pow(a[0], a[1]);
            case FUNCTION_PRODUCT:
                return ExtMath.product(a);
            case FUNCTION_RADIANS:
                return Math.toRadians(a[0]);
            case FUNCTION_RANDBETWEEN:
                return ExtMath.randBetween(a[0], a[1]);
            case FUNCTION_ROUND:
                if (numArgs == 1)
                    return ExtMath.round(a[0]);
                return ExtMath.round(a[0], a[1]);
            case FUNCTION_ROUNDDOWN:
                return ExtMath.roundDown(a[0], a[1]);
            case FUNCTION_ROUNDUP:
                if (numArgs == 1)
                    return ExtMath.roundUp(a[0]);
                return ExtMath.roundUp(a[0], a[1]);
            case FUNCTION_SIGN:
                return Math.signum(a[0]);
            case FUNCTION_SIN:
                return Math.sin(a[0]);
            case FUNCTION_SINH:
                return Math.sinh(a[0]);
            case FUNCTION_SQRT:
                return Math.sqrt(a[0]);
            case FUNCTION_SQRTPI:
                return Math.sqrt(a[0] * Math.PI);
            case FUNCTION_STDEV:
                return ExtMath.stDev(a);
            case FUNCTION_STDEVP:
                return ExtMath.stDevP(a);
            case FUNCTION_SUM:
                return ExtMath.sum(a);
            case FUNCTION_SUMSQ:
                return ExtMath.sumSq(a);
            case FUNCTION_TAN:
                return Math.tan(a[0]);
            case FUNCTION_TANH:
                return Math.tanh(a[0]);
            case FUNCTION_TRUNC:
                if (numArgs == 1)
                    return ExtMath.trunc(a[0]);
                return ExtMath.trunc(a[0], a[1]);
            case FUNCTION_VAR:
                return ExtMath.var(a);
            case FUNCTION_VARP:
                return ExtMath.varP(a);
            default:
                return 0.0;
        }
    }

    private static int deltaQ(int q, char chr) {
        int col;
        if (Character.isDigit(chr))
            col = 0;
        else if (Character.isUpperCase(chr) || chr == SYMBOL_UNDERSCORE) {
            if (chr == SYMBOL_LETTER_E)
                col = 2;
            else
                col = 3;
        }
        else {
            switch (chr) {
                case SYMBOL_DOT:
                    col =  1;
                    break;
                case SYMBOL_LEFT_BRACE:
                    col =  4;
                    break;
                case SYMBOL_PERCENT:
                    col =  5;
                    break;
                case SYMBOL_POWER:
                case SYMBOL_MULTIPLICATION:
                case SYMBOL_DIVISION:
                    col =  6;
                    break;
                case SYMBOL_ADDITION:
                    col =  7;
                    break;
                case SYMBOL_SUBTRACTION:
                    col =  8;
                    break;
                case SYMBOL_COMMA:
                    col =  9;
                    break;
                case SYMBOL_RIGHT_BRACE:
                    col = 10;
                    break;
                case SYMBOL_EQUAL:
                    col = 11;
                    break;
                case SYMBOL_SPACE:
                case SYMBOL_CARRIAGE_RETURN:
                    col = 12;
                    break;
                case SYMBOL_BARRIER:   // the code for the end of expression
                    col = 13;
                    break;
                default:
                    return -4;  // Unknown symbol error
            }
        }
        return DFA_STATES[q][col];
    }

    private static int getFunctIdx(String name) {
        for (int i = 0; i < FUNCTIONS.length; i++) {
            if (FUNCTIONS[i].equalsIgnoreCase(name))
                return i;
        }
        return -1;
    }

    private static byte getPriority(byte type) {
        switch (type) {
            case TOKEN_TYPE_LEFT_BRACE:
                return 1;
            case TOKEN_TYPE_FUNCTION:
                return 2;
            case TOKEN_TYPE_PLUS_SIGN:
            case TOKEN_TYPE_MINUS_SIGN:
            case TOKEN_TYPE_PERCENT:
                return 3;
            case TOKEN_TYPE_POWER:
                return 4;
            case TOKEN_TYPE_MULTIPLY:
            case TOKEN_TYPE_DIVIDE:
                return 5;
            case TOKEN_TYPE_ADD:
            case TOKEN_TYPE_SUBTRACT:
                return 6;
            default:
                return 7;
        }
    }

    private String getTokensString(int pos, int length) {
        String s = "";
        int begin = pos, end = begin + length, size = tokens.size();
        if (begin < 0) begin = 0;
        if (end > size) end = size;
        for (int i = begin; i < end; i++)
            s += (tokens.elementAt(i)).toString();
        if (s.length() > 67) {
            s = s.substring(0, 32) + "..." + s.substring(s.length() - 32);
        }
        if (begin > 0) s = "..." + s;
        if (end < size) s += "...";
        return s;
    }

    private static byte peekType(Stack<Token> st) {
        if (st.empty())
            return 0;
        return st.peek().type;
    }

    private boolean isResultHasError() {
        if (!errMsg.equals(""))
            return true;
        if (Double.isNaN(result))
            errMsg = "Undefined result: ";
        else if (Double.isInfinite(result))
            errMsg = "Infinite result: ";
        return !errMsg.equals("");
    }

    private static String doubleToString(double dbl) {
        if (dbl == -0.0)
            dbl = 0.0;
        String s = Double.valueOf(dbl).toString();
        int l = s.length();
        if (s.substring(l - 2).equals(".0"))
            s = s.substring(0, l - 2);
        return s;
    }

    private int indexToPosition(int index) {
        if (index > tokens.size() || index < 0)
            return -1;
        int pos = 0;
        for (int i = 0; i < index; i++)
            pos += tokens.elementAt(i).toString().length();
        return pos;
    }

    private class Token {
        public byte type;
        public Object token;

        public Token(byte type, Object token) {
            this.type = type;
            this.token = token;
        }

        @Override
        public String toString() {
            switch (type) {
                case TOKEN_TYPE_NUMBER:
                    return doubleToString((Double) token);
                case TOKEN_TYPE_VARIABLE:
                    return varList.getName((Integer) token);
                case TOKEN_TYPE_FUNCTION:
                    return token.toString();
                default:
                    return Character.toString((Character) token);
            }
        }
    }

    private class TokenFunct {
        public int functCode;
        public int numArgs;

        public TokenFunct(int code) {
            this.functCode = code;
            numArgs = FUNCTION_ARGS[code];
        }

        @Override
        public String toString() {
            return FUNCTIONS[functCode];
        }
    }

    private class Variable {
        public String name;
        public double value;

        public Variable(String name, double value) {
            this.name = name;
            this.value = value;
        }
    }

    private class VarList {
        public Vector<Variable> varList;

        public VarList() {
            varList = new Vector<>();
        }

        public int append(String name) {
            int idx = indexOf(name);
            int size = varList.size();
            if (idx == -1) {
                if (size < VARIABLES_MAX_SIZE) {
                    varList.addElement(new Variable(name, 0.0d));
                    return size;
                }
                return -1;
            }
            return idx;
        }

        public String getName(int idx) {
            return varList.elementAt(idx).name;
        }

        public double getValue(int idx) {
            return varList.elementAt(idx).value;
        }

        public void setValue(int idx, double value) {
            varList.elementAt(idx).value = value;
        }

        public void removeElementAt(int idx) {
            varList.removeElementAt(idx);
        }

        public void removeLastElement() {
            varList.removeElementAt(varList.size() - 1);
        }

        public void clear() {
            varList.clear();
        }

        public int size() {
            return varList.size();
        }

        private int indexOf(String name) {
            if (varList.size() == 0)
                return -1;
            for (int i = 0; i < varList.size(); i++)
                if (varList.elementAt(i).name.equals(name))
                    return i;
            return -1;
        }
    }

    public static class ExtMath {
        public static final double aveDev(double[] args) {
            double mean = mean(args);
            double sum = 0.0d;
            for (double arg : args)
                sum += arg - mean;
            return sum / args.length;
        }

        public static final double ceiling(double n, double s) {
            if (n == 0.0d || s == 0.0d)
                return 0.0d;
            if (Math.signum(n) != Math.signum(s))
                return Double.NaN;
            double nds = (n / s);
            if (nds < 0.0d)
                nds = Math.floor(nds);
            else
                nds = Math.ceil(nds);
            return nds * s;
        }

        public static final double combin(double n, double k) {
            int in = Double.valueOf(n).intValue();
            int ik = Double.valueOf(k).intValue();
            if (in < 0 || ik < 0 || ik > in)
                return Double.NaN;
            int r = in - ik;
            if (r == 0)
                return 1.0d;
            if (r > ik) {
                int tmp = ik;
                ik = r;
                r = tmp;
            }
            double res = 1.0d;
            while (r > 1 || in > ik) {
                if (in > ik) {
                    res *= in;
                    in--;
                }
                if (r > 1) {
                    res /= r;
                    r--;
                }
            }
            return res;
        }

        public static final double devSq(double[] args) {
            double mean = mean(args);
            double sum = 0.0d, d;
            for (double arg : args) {
                d = arg - mean;
                sum += d * d;
            }
            return sum;
        }

        public static final double dms2dec(double[] args) {
            double deg = args[0], min = 0.0d, sec = 0.0d;
            if (args.length >= 2) min = args[1];
            if (args.length == 3) sec = args[2];
            return Math.abs(deg) + Math.abs(min) / 60.0d + Math.abs(sec) / 3600.0d;
        }

        public static final double even(double n) {
            return roundUp(n / 2.0d) * 2.0d;
        }

        private static double exp10(double d) {
            int dig = Double.valueOf(d).intValue();
            double rs = Double.parseDouble("1e" + Integer.toString(dig));
            return rs;
        }

        public static final double fact(double n) {
            int in = Double.valueOf(n).intValue();
            if (in < 0)
                return Double.NaN;
            if (in == 0)
                return 1.0d;
            return fact(in, 1, 1);
        }

        private static final double fact(int from, int to, int step) {
            double res = 1.0d;
            int f = from;
            while (f != to && res < Double.MAX_VALUE) {
                res *= f;
                f -= step;
            }
            return res;
        }

        public static final double factDouble(double n) {
            int in = Double.valueOf(n).intValue();
            if (in < 0)
                return Double.NaN;
            if (in % 2 == 0)
                return fact(in, 0, 2);
            else
                return fact(in, 1, 2);
        }

        public static final double fisher(double x) {
            return Math.log((1.0d + x) / (1.0d - x)) / 2.0d;
        }

        public static final double fisherInv(double y) {
            double tmp = Math.exp(2.0d * y);
            return (tmp - 1.0d) / (tmp + 1.0d);
        }

        public static final double floor(double n, double s) {
            if (n == 0.0)
                return n;
            if (s == 0.0)
                return Double.POSITIVE_INFINITY;
            if (Math.signum(n) != Math.signum(s))
                return Double.NaN;
            return trunc(n / s) * s;
        }

        public static final double frac(double n) {
            return n - trunc(n);
        }

        public static final double geoMean(double[] args) {
            int n = args.length;
            double res = 1.0d;
            for (int i = 0; i < n; i++) {
                if (args[i] <= 0.0d)
                    return Double.NaN;
                res *= args[i];
            }
            return Math.pow(res, 1.0d / n);
        }

        public static final double max(double[] args) {
            int l = args.length, i = 1;
            double res = args[0];
            while (i < l) {
                res = Math.max(res, args[i]);
                i++;
            }
            return res;
        }

        public static final double mean(double[] args) {
            return sum(args) / args.length;
        }

        public static final double median(double[] args) {
            int l = args.length;
            double tmp;
            for (int i = 0; i < l - 1; i++) { // sorting args[]
                for (int j = i + 1; j < l; j++) {
                    if (args[i] > args[j]) {
                        tmp = args[i];
                        args[i] = args[j];
                        args[j] = tmp;
                    }
                }
            }
            int mid = l / 2;
            if (l % 2 == 1) return args[mid];
            return (args[mid - 1] + args[mid]) / 2.0;
        }

        public static final double min(double[] args) {
            int l = args.length, i = 1;
            double res = args[0];
            while (i < l) {
                res = Math.min(res, args[i]);
                i++;
            }
            return res;
        }

        public static final double mod(double n, double div) {
            double realMod = n % div;
            if (Math.signum(n) != Math.signum(div))
                return realMod + div;
            return realMod;
        }

        public static final double mode(double[] args) {
            int l = args.length;
            int count, maxCount = 0;
            double value = 0;
            for (int i = 0; i < l; i++) {
                count = 0;
                for (int j = 0; j < l; j++)
                    if (args[i] == args[j]) count++;
                if (count > maxCount) {
                    maxCount = count;
                    value = args[i];
                }
            }
            if (maxCount == 1)
                return Double.NaN;
            return value;
        }

        public static final double mRound(double n, double m) {
            if (m == 0.0d)
                return 0.0d;
            if (Math.signum(n) != Math.signum(m))
                return Double.NaN;
            return round(n / m) * m;
        }

        public static final double multiNomial(double[] args) {
            int n, sum = 0;
            for (double arg : args) {
                n = Double.valueOf(arg).intValue();
                if (n < 0)
                    return Double.NaN;
                sum += n;
            }
            double res = fact(sum);
            for (double arg : args) {
                n = Double.valueOf(arg).intValue();
                res /= fact(n);
            }
            return res;
        }

        public static final double odd(double n) {
            double r = roundUp(n);
            if (r % 2 == 0.0)
                return r + Math.signum(n);
            return r;
        }

        public static final double permut(double n, double k) {
            int in = Double.valueOf(n).intValue();
            int ik = Double.valueOf(k).intValue();
            if (in < 0 || ik < 0 || ik > in)
                return Double.NaN;
            return fact(in, in - ik, 1);
        }

        public static final double product(double[] args) {
            int l = args.length;
            double res = args[0];
            for (int i = 1; i < l; i++)
                res *= args[i];
            return res;
        }

        public static final double radians(double d) {
            return Math.toRadians(d);
        }

        //public static final double rand() {
        //    return new Random().nextDouble();
        //}

        public static final double randBetween(double bottom, double top) {
            double b = trunc(bottom), t = trunc(top);
            if (b > t)
                return Double.NaN;
            return trunc(new Random().nextDouble() * (t - b + 1.0d)) + b;
        }

        public static final double round(double n) {
            if (n < 0.0d)
                return Math.ceil(n - 0.5d);
            return Math.floor(n + 0.5d);
        }

        public static final double round(double n, double digit) {
            double d = exp10(digit);
            return round(n * d) / d;
        }

        public static final double roundDown(double n, double digit) {
            return trunc(n, digit);
        }

        public static final double roundUp(double n) {
            if (n < 0.0d)
                return Math.floor(n);
            if (n > 0.0)
                return Math.ceil(n);
            return n;
        }

        public static final double roundUp(double n, double digit) {
            double d = exp10(digit);
            double tmp = n * d;
            if (tmp < 0.0d)
                tmp = Math.floor(tmp);
            else
                tmp = Math.ceil(tmp);
            return tmp / d;
        }

        public static final double stDev(double[] args) {
            return Math.sqrt(var(args));
        }

        public static final double stDevP(double[] args) {
            return Math.sqrt(varP(args));
        }

        public static final double sum(double[] args) {
            double res = args[0];
            for (int i = 1; i < args.length; i++)
                res += args[i];
            return res;
        }

        public static final double sumSq(double[] args) {
            double res = 0.0d;
            for (double arg : args)
                res += arg * arg;
            return res;
        }

        public static final double trunc(double n) {
            if (n < 0.0d)
                return Math.ceil(n);
            if (n > 0.0d)
                return Math.floor(n);
            return n;
        }

        public static final double trunc(double n, double digit) {
            double d = exp10(digit);
            return trunc(n * d) / d;
        }

        public static final double var(double[] args) {
            return devSq(args) / (args.length - 1);
        }

        public static final double varP(double[] args) {
            return devSq(args) / args.length;
        }

        /*************************************************************************
         * All these formula are for calculations on the basis of a spherical     *
         * earth (ignoring ellipsoidal effects) � which is accurate enough for    *
         * most purposes...                                                       *
         * (In fact, the earth is very slightly ellipsoidal; using a spherical    *
         * model gives errors typically up to 0.3%)                               *
         *========================================================================*
         * Source: http://www.movable-type.co.uk/scripts/latlong.html             *
         *************************************************************************/

        private static final double earthRad = 6371;
        // the Earth radius (mean radius)

        public static final double distance(double lat1, double lon1, double lat2, double lon2) {
            double la1 = Math.toRadians(lat1),
                    lo1 = Math.toRadians(lon1),
                    la2 = Math.toRadians(lat2),
                    lo2 = Math.toRadians(lon2),
                    dla = Math.sin((la2 - la1) / 2),
                    dlo = Math.sin((lo2 - lo1) / 2),
                    a   = dla * dla + dlo * dlo * Math.cos(la1) * Math.cos(la2),
                    c   = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return earthRad * c; // Earth radius (mean radius) = 6,371 km
        }

        public static final double heading(double lat1, double lon1, double lat2, double lon2) {
            double la1 = Math.toRadians(lat1),
                    lo1 = Math.toRadians(lon1),
                    la2 = Math.toRadians(lat2),
                    lo2 = Math.toRadians(lon2),
                    dlo = lo2 - lo1,
                    x   = Math.cos(la1) * Math.sin(la2) - Math.sin(la1) * Math.cos(la2) * Math.cos(dlo),
                    y   = Math.sin(dlo) * Math.cos(la2),
                    hdg = Math.toDegrees(Math.atan2(y, x));
            return (hdg + 360) % 360;
        }

        public static final double latitude(double lat1, double lon1, double hdng, double dist) {
            double la1 = Math.toRadians(lat1),
                    hdg = Math.toRadians(hdng),
                    ad  = dist / earthRad,
                    la2 = Math.asin(Math.sin(la1) * Math.cos(ad) + Math.cos(la1) * Math.sin(ad) * Math.cos(hdg));
            return Math.toDegrees(la2);
        }

        public static final double longitude(double lat1, double lon1, double hdng, double dist) {
            double la1 = Math.toRadians(lat1),
                    lo1 = Math.toRadians(lon1),
                    hdg = Math.toRadians(hdng),
                    ad  = dist / earthRad,
                    la2 = Math.asin(Math.sin(la1) * Math.cos(ad) + Math.cos(la1) * Math.sin(ad) * Math.cos(hdg)),
                    lo2 = lo1 + Math.atan2(Math.sin(hdg) * Math.sin(ad) * Math.cos(la1),
                            Math.cos(ad) - Math.sin(la1) * Math.sin(la2));
            lo2 = Math.toDegrees(lo2);
            if (lo2 > 180) lo2 -= 360;
            return lo2;
        }

        /**
         * ====================================================================== *
         * Implements the Euclidean Algorithm a.k.a Euclid's Algorithm method for *
         * computing Greatest Common Divisor (GCD) & Least Common Multiple (LCM). *
         * ====================================================================== *
         * Source: http://en.wikipedia.org/wiki/Euclid's_algorithm                *
         * ====================================================================== *
         */

        private static final long gcd(long n1, long n2) {
            long a = n1, b = n2, tmp;
            while (b > 0) {
                tmp = b;
                b = a % b;
                a = tmp;
            }
            return a;
        }

        public static final double gcd(double[] args) {
            long n, res = Double.valueOf(args[0]).longValue();
            for (double arg : args) {
                n = Double.valueOf(arg).longValue();
                if (n < 0)
                    return Double.NaN;
                res = gcd(res, n);
            }
            return Long.valueOf(res).doubleValue();
        }

        private static final long lcm(long n1, long n2) {
            return n1 * (n2 / gcd(n1, n2));
        }

        public static final double lcm(double[] args) {
            long n, res = Double.valueOf(args[0]).longValue();
            for (double arg : args) {
                n = Double.valueOf(arg).longValue();
                if (n < 0)
                    return Double.NaN;
                res = lcm(res, n);
            }
            return Long.valueOf(res).doubleValue();
        }
    }
}
