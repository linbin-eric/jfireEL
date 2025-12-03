package cc.jfire.el.expression.format;

public class MinusIndentAndSingleLineToken implements  FormatToken
{
    final  String str;
    final  int minusIndent;

    public MinusIndentAndSingleLineToken(String str, int minusIndent) {this.str         = str;
                                                                       this.minusIndent = minusIndent;
                                                                      }

    @Override
    public void out(StringBuilder builder, FormatContext context)
    {
        int indent            = context.getIndent();
        int lastNotIndentChar = builder.length() - 1 - indent;
        if (lastNotIndentChar > 0)
        {
            if (builder.charAt(lastNotIndentChar) == '\n')
            {
            builder.setLength(lastNotIndentChar+1);
            }
            else
            {
                builder.append("\r\n");
            }
        }
        else
        {
            ;
        }
        context.setIndent(context.getIndent()-minusIndent);
        indent = context.getIndent();
        for (int i = 0; i < indent; i++)
        {
            builder.append(' ');
        }
        builder.append(str).append("\r\n");
        for (int i = 0; i < indent; i++)
        {
            builder.append(' ');
        }
        context.setFirst(true);
    }
}
