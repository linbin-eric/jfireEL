package cc.jfire.el.expression.format;

public interface FormatToken
{
    static FormatToken of(String str)
    {
        switch (str)
        {
            case "(", "if", "else", "." -> {return new NotFrontSpaceToken(str);}
            case ";" -> {return new NewLineToken(str);}
            case "{" -> {return new SingleLineAndAddIndentToken(str, 4);}
            case "}" -> {return new MinusIndentAndSingleLineToken(str, 4);}
            case ")" -> {return new RightParenToken();}
            default -> {return new RegularToken(str);}
        }
    }

    void out(StringBuilder builder, FormatContext context);
}
