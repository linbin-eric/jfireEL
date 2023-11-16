package com.jfirer.jfireel.expression.format;

public class NotFrontSpaceToken implements FormatToken
{
    private final String str;

    public NotFrontSpaceToken(String str)
    {
        this.str = str;
    }

    @Override
    public void out(StringBuilder builder,FormatContext context)
    {
        builder.append(str);
        context.setFirst(false);
    }
}
