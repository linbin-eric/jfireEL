package com.jfireframework.jfireel.node;

public interface MethodNode extends CalculateNode
{
	enum ConvertType
	{
		INT, LONG, SHORT, FLOAT, DOUBLE, BYTE, BOOLEAN, CHARACTER, OTHER
	}
	
	void setArgsNodes(CalculateNode[] argsNodes);
}
