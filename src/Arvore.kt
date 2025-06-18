import java.util.Stack

abstract class Node {
    abstract fun evaluate(): Double
}

class NumberNode(val value: Double) : Node() {
    override fun evaluate() = value
}

class OperatorNode(
    private val operator: Char,
    private val left: Node,
    private val right: Node
) : Node() {
    override fun evaluate(): Double {
        val a = left.evaluate()
        val b = right.evaluate()
        return when (operator) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> a / b
            else -> throw IllegalArgumentException("Operador inválido: $operator")
        }
    }
}

fun tokenize(expr: String): List<String> {
    val tokens = mutableListOf<String>()
    val number = StringBuilder()
    val cleanExpr = expr.replace("\\s+".toRegex(), "")

    for (c in cleanExpr) {
        when {
            c.isDigit() || c == '.' -> number.append(c)
            else -> {
                if (number.isNotEmpty()) {
                    tokens.add(number.toString())
                    number.clear()
                }
                tokens.add(c.toString())
            }
        }
    }
    if (number.isNotEmpty()) tokens.add(number.toString())
    return tokens
}

fun infixToPostfix(tokens: List<String>): List<String> {
    val output = mutableListOf<String>()
    val stack = Stack<String>()
    val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)

    for (token in tokens) {
        when {
            token.matches(Regex("\\d+(\\.\\d+)?")) -> output.add(token)
            precedence.containsKey(token) -> {
                while (stack.isNotEmpty() && precedence.containsKey(stack.peek()) &&
                    precedence[stack.peek()]!! >= precedence[token]!!) {
                    output.add(stack.pop())
                }
                stack.push(token)
            }
            token == "(" -> stack.push(token)
            token == ")" -> {
                while (stack.peek() != "(") output.add(stack.pop())
                stack.pop()
            }
        }
    }

    while (stack.isNotEmpty()) output.add(stack.pop())
    return output
}

fun buildExpressionTree(postfix: List<String>): Node {
    val stack = Stack<Node>()
    for (token in postfix) {
        if (token.matches(Regex("\\d+(\\.\\d+)?"))) {
            stack.push(NumberNode(token.toDouble()))
        } else {
            val right = stack.pop()
            val left = stack.pop()
            stack.push(OperatorNode(token[0], left, right))
        }
    }
    return stack.pop()
}
