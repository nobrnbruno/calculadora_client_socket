import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket

fun main() {
    print("Digite a expressão matemática (ex: 2 + 3 * (4 - 1)): ")
    val expressao = readln()

    val resultado = try {
        val tokens = tokenize(expressao)
        val postfix = infixToPostfix(tokens)
        val tree = buildExpressionTree(postfix)
        tree.evaluate()
    } catch (e: Exception) {
        println("Erro ao avaliar expressão: ${e.message}")
        return
    }

    try {
        val clientSocket = Socket("localhost", 9090)
        val socketSaidaServer = DataOutputStream(clientSocket.getOutputStream())
        val socketEntrada = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

        socketSaidaServer.writeBytes("1\n") // operação fictícia (soma)
        socketSaidaServer.writeBytes("$resultado\n") // resultado calculado
        socketSaidaServer.writeBytes("0\n") // oper2 = 0, ignorado
        socketSaidaServer.flush()

        val response = socketEntrada.readLine()
        println("Resposta do servidor: $response")

        clientSocket.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}