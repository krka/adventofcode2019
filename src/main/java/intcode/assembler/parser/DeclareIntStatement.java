package intcode.assembler.parser;

import intcode.assembler.Assembler;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class DeclareIntStatement implements Statement {
  private final SetStatement setStatement;

  public DeclareIntStatement(SetStatement setStatement) {
    this.setStatement = setStatement;
  }

  @Override
  public void apply(Assembler assembler, Assembler.IntCodeFunction caller, String context) {
    List<ExprNode> targets = setStatement.getTarget().expressions;
    List<ExprNode> sources = setStatement.getExpr().expressions;

    List<ExprNode> newTargets = new ArrayList<>();
    List<ExprNode> newSources = new ArrayList<>();

    for (int i = 0; i < targets.size(); i++) {
      ExprNode target = targets.get(i);
      if (!(target instanceof VarNode)) {
        throw new RuntimeException("Can't declare variable " + target);
      }

      String name = ((VarNode) target).getName();

      if (caller == assembler.main && !caller.hasBlocks()) {
        BigInteger constantValue = null;
        if (i < sources.size()) {
          constantValue = sources.get(i).value();
        }
        if (constantValue != null) {
          assembler.addGlobalVariable(Variable.intVar(name, constantValue, context));
        } else {
          assembler.addGlobalVariable(Variable.intVar(name, BigInteger.ZERO, context));

          newTargets.add(target);
          if (i < sources.size()) {
            newSources.add(sources.get(i));
          }
        }
      } else {
        caller.addStackVariable(name);
        newTargets.add(target);
        if (i < sources.size()) {
          newSources.add(sources.get(i));
        }
      }
    }

    new ExpressionList(newTargets).assignValue(assembler, caller, context, new ExpressionList(newSources));
  }
}
