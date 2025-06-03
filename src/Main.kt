import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket

object CalculadoraClientSocket {
    @JvmStatic
    fun main(args: Array<String>) {
        print("Insira a operacao que deseja usar \nSoma (1), Subtracao (2), Multiplicacao (3) e Divisao (4): ")
        val operacao = readln()

        print("Insira o primeiro numero: ")
        val oper1 = readln()

        print("Insira o segundo numero: ")
        val oper2 = readln()

        var result : String?
        try {
            val clientSocket = Socket("192.168.1.99", 9090)
            val socketSaidaServer = DataOutputStream(clientSocket.getOutputStream())
            
            socketSaidaServer.writeBytes(operacao + "\n")
            socketSaidaServer.writeBytes(oper1 + "\n")
            socketSaidaServer.writeBytes(oper2 + "\n")
            socketSaidaServer.flush()
            
            val messageFromServer = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            result = messageFromServer.readLine()

            println("resultado=$result")
            clientSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}