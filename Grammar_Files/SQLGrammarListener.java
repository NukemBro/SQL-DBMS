// Generated from SQLGrammar.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SQLGrammarParser}.
 */
public interface SQLGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(SQLGrammarParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(SQLGrammarParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(SQLGrammarParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(SQLGrammarParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#relationName}.
	 * @param ctx the parse tree
	 */
	void enterRelationName(SQLGrammarParser.RelationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#relationName}.
	 * @param ctx the parse tree
	 */
	void exitRelationName(SQLGrammarParser.RelationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(SQLGrammarParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(SQLGrammarParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#atomicExpr}.
	 * @param ctx the parse tree
	 */
	void enterAtomicExpr(SQLGrammarParser.AtomicExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#atomicExpr}.
	 * @param ctx the parse tree
	 */
	void exitAtomicExpr(SQLGrammarParser.AtomicExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#selection}.
	 * @param ctx the parse tree
	 */
	void enterSelection(SQLGrammarParser.SelectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#selection}.
	 * @param ctx the parse tree
	 */
	void exitSelection(SQLGrammarParser.SelectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#renaming}.
	 * @param ctx the parse tree
	 */
	void enterRenaming(SQLGrammarParser.RenamingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#renaming}.
	 * @param ctx the parse tree
	 */
	void exitRenaming(SQLGrammarParser.RenamingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#projection}.
	 * @param ctx the parse tree
	 */
	void enterProjection(SQLGrammarParser.ProjectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#projection}.
	 * @param ctx the parse tree
	 */
	void exitProjection(SQLGrammarParser.ProjectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#union}.
	 * @param ctx the parse tree
	 */
	void enterUnion(SQLGrammarParser.UnionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#union}.
	 * @param ctx the parse tree
	 */
	void exitUnion(SQLGrammarParser.UnionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#difference}.
	 * @param ctx the parse tree
	 */
	void enterDifference(SQLGrammarParser.DifferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#difference}.
	 * @param ctx the parse tree
	 */
	void exitDifference(SQLGrammarParser.DifferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#product}.
	 * @param ctx the parse tree
	 */
	void enterProduct(SQLGrammarParser.ProductContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#product}.
	 * @param ctx the parse tree
	 */
	void exitProduct(SQLGrammarParser.ProductContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(SQLGrammarParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(SQLGrammarParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void enterConjunction(SQLGrammarParser.ConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void exitConjunction(SQLGrammarParser.ConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(SQLGrammarParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(SQLGrammarParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#operand}.
	 * @param ctx the parse tree
	 */
	void enterOperand(SQLGrammarParser.OperandContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#operand}.
	 * @param ctx the parse tree
	 */
	void exitOperand(SQLGrammarParser.OperandContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#attributeList}.
	 * @param ctx the parse tree
	 */
	void enterAttributeList(SQLGrammarParser.AttributeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#attributeList}.
	 * @param ctx the parse tree
	 */
	void exitAttributeList(SQLGrammarParser.AttributeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void enterAttributeName(SQLGrammarParser.AttributeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void exitAttributeName(SQLGrammarParser.AttributeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(SQLGrammarParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(SQLGrammarParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(SQLGrammarParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(SQLGrammarParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(SQLGrammarParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(SQLGrammarParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#opencmd}.
	 * @param ctx the parse tree
	 */
	void enterOpencmd(SQLGrammarParser.OpencmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#opencmd}.
	 * @param ctx the parse tree
	 */
	void exitOpencmd(SQLGrammarParser.OpencmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#closecmd}.
	 * @param ctx the parse tree
	 */
	void enterClosecmd(SQLGrammarParser.ClosecmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#closecmd}.
	 * @param ctx the parse tree
	 */
	void exitClosecmd(SQLGrammarParser.ClosecmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#writecmd}.
	 * @param ctx the parse tree
	 */
	void enterWritecmd(SQLGrammarParser.WritecmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#writecmd}.
	 * @param ctx the parse tree
	 */
	void exitWritecmd(SQLGrammarParser.WritecmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#exitcmd}.
	 * @param ctx the parse tree
	 */
	void enterExitcmd(SQLGrammarParser.ExitcmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#exitcmd}.
	 * @param ctx the parse tree
	 */
	void exitExitcmd(SQLGrammarParser.ExitcmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#showcmd}.
	 * @param ctx the parse tree
	 */
	void enterShowcmd(SQLGrammarParser.ShowcmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#showcmd}.
	 * @param ctx the parse tree
	 */
	void exitShowcmd(SQLGrammarParser.ShowcmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#createcmd}.
	 * @param ctx the parse tree
	 */
	void enterCreatecmd(SQLGrammarParser.CreatecmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#createcmd}.
	 * @param ctx the parse tree
	 */
	void exitCreatecmd(SQLGrammarParser.CreatecmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#updatecmd}.
	 * @param ctx the parse tree
	 */
	void enterUpdatecmd(SQLGrammarParser.UpdatecmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#updatecmd}.
	 * @param ctx the parse tree
	 */
	void exitUpdatecmd(SQLGrammarParser.UpdatecmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#insertcmd}.
	 * @param ctx the parse tree
	 */
	void enterInsertcmd(SQLGrammarParser.InsertcmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#insertcmd}.
	 * @param ctx the parse tree
	 */
	void exitInsertcmd(SQLGrammarParser.InsertcmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#deletecmd}.
	 * @param ctx the parse tree
	 */
	void enterDeletecmd(SQLGrammarParser.DeletecmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#deletecmd}.
	 * @param ctx the parse tree
	 */
	void exitDeletecmd(SQLGrammarParser.DeletecmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#typedAttributeList}.
	 * @param ctx the parse tree
	 */
	void enterTypedAttributeList(SQLGrammarParser.TypedAttributeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#typedAttributeList}.
	 * @param ctx the parse tree
	 */
	void exitTypedAttributeList(SQLGrammarParser.TypedAttributeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(SQLGrammarParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(SQLGrammarParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#integer}.
	 * @param ctx the parse tree
	 */
	void enterInteger(SQLGrammarParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#integer}.
	 * @param ctx the parse tree
	 */
	void exitInteger(SQLGrammarParser.IntegerContext ctx);
}