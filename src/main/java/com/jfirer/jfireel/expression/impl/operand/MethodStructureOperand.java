package com.jfirer.jfireel.expression.impl.operand;

import com.jfirer.jfireel.expression.ControlFlag;
import com.jfirer.jfireel.expression.Operand;
import com.jfirer.jfireel.expression.ProcessControlResult;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Setter
public class MethodStructureOperand implements Operand
{
    private final Operand[] execs;
    private final String[]  localVariableNames;
    private final boolean   outest;

    public MethodStructureOperand(Operand[] execs, boolean outest)
    {
        this.execs  = execs;
        this.outest = outest;
        Set<String> localVariableNames = new HashSet<>();
        for (Operand exec : execs)
        {
            if (exec instanceof CreateVariableOperand createVariableOperand)
            {
                localVariableNames.add(createVariableOperand.variableName);
            }
            else if (exec instanceof AssignOperand.CreateVariableAndAssignOperand createVariableAndAssignOperand)
            {
                localVariableNames.add(createVariableAndAssignOperand.variableName);
            }
        }
        this.localVariableNames = localVariableNames.toArray(String[]::new);
    }

    @Override
    public Object calculate(Map<String, Object> contextParam)
    {
        Object calculate = null;
        for (Operand each : execs)
        {
            calculate = each.calculate(contextParam);
            if (calculate instanceof ProcessControlResult processControlResult)
            {
                for (String localVariableName : localVariableNames)
                {
                    contextParam.remove(localVariableName);
                }
                if (outest && processControlResult.getFlag() == ControlFlag.RETURN_WITH_VALUE)
                {
                    return processControlResult.getResultValue();
                }
                else
                {
                    return calculate;
                }
            }
        }
        for (String localVariableName : localVariableNames)
        {
            contextParam.remove(localVariableName);
        }
        if (outest && calculate instanceof ProcessControlResult processControlResult && processControlResult.getFlag() == ControlFlag.RETURN_WITH_VALUE)
        {
            return processControlResult.getResultValue();
        }
        else
        {
            return calculate;
        }
    }
}
