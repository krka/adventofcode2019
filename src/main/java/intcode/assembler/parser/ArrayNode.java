package intcode.assembler.parser;

import intcode.assembler.AddOp;
import intcode.assembler.Assembler;
import intcode.assembler.Constant;
import intcode.assembler.DeferredParameter;
import intcode.assembler.Op;
import intcode.assembler.Parameter;
import intcode.assembler.ParameterMode;
import intcode.assembler.SetOp;
import intcode.assembler.TempVariable;
import intcode.assembler.Variable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ArrayNode implements ExprNode {
  private final ExprNode array;
  private final ExprNode index;

  public ArrayNode(ExprNode array, IndexNode index) {
    this(array, index.getIndex());
  }

  private ArrayNode(ExprNode array, ExprNode index) {
    this.array = array;
    this.index = index;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArrayNode arrayNode = (ArrayNode) o;
    return array.equals(arrayNode.array) &&
            index.equals(arrayNode.index);
  }

  @Override
  public int hashCode() {
    return Objects.hash(array, index);
  }

  @Override
  public String toString() {
    return "ArrayNode{" +
            "array=" + array +
            ", index=" + index +
            '}';
  }

  @Override
  public ExprNode optimize() {
    return new ArrayNode(array.optimize(), index.optimize());
  }

  @Override
  public BigInteger value() {
    return null;
  }

  @Override
  public void assignTo(Variable target, Assembler assembler, Assembler.IntCodeFunction function, String context) {
    Set<TempVariable> tempParams = new HashSet<>();
    Parameter arrayParam = array.toParameter(assembler, function, tempParams);
    Parameter indexParam = index.toParameter(assembler, function, tempParams);

    arrayLookup(function.operations, target, arrayParam, indexParam, context);

    tempParams.forEach(TempVariable::release);
  }

  @Override
  public Parameter toParameter(Assembler assembler, Assembler.IntCodeFunction function, Set<TempVariable> tempParams) {
    Parameter arrayParam = array.toParameter(assembler, function, tempParams);
    Parameter indexParam = index.toParameter(assembler, function, tempParams);

    TempVariable target = assembler.tempSpace.getAny();
    tempParams.add(target);

    arrayLookup(function.operations, target, arrayParam, indexParam, "# " + target + " = " + toString());

    return target;
  }

  @Override
  public void assignValue(Assembler assembler, Assembler.IntCodeFunction function, String context, ExprNode expr) {
    Set<TempVariable> tempParams = new HashSet<>();
    Parameter arrayParam = array.toParameter(assembler, function, tempParams);
    Parameter indexParam = index.toParameter(assembler, function, tempParams);
    Parameter valueParam = expr.toParameter(assembler, function, tempParams);

    AddOp rewriteParam = new AddOp(context, arrayParam, indexParam, Constant.PLACEHOLDER_POSITION);
    SetOp addOp = new SetOp("# write to array from value", valueParam, Constant.PLACEHOLDER_POSITION);

    rewriteParam.setTarget(DeferredParameter.ofInt(ParameterMode.POSITION, () -> addOp.getAddress() + 3));
    function.operations.add(rewriteParam);
    function.operations.add(addOp);
    tempParams.forEach(TempVariable::release);
  }

  private void arrayLookup(List<Op> operations, Variable target, Parameter arrayParam, Parameter indexParam, String context) {
    AddOp rewriteParam = new AddOp(context, arrayParam, indexParam, Constant.PLACEHOLDER_POSITION);
    SetOp addOp = new SetOp("# write to variable", Constant.PLACEHOLDER_POSITION, target);

    rewriteParam.setTarget(DeferredParameter.ofInt(ParameterMode.POSITION, () -> addOp.getAddress() + 1));
    operations.add(rewriteParam);
    operations.add(addOp);
  }

}
