package Models;
/*
* A classe FuncaoSigmoid foi implementada para organizar e juntar os métodos necessários para o treinamento do MultiLayer Perceptron.
* Consiste de dois métodos estáticos: "sigmoid" e "derivadaSigmoid".
* O funcionamento dos dois métodos está explicado em suas definições.
* */
public class FuncaoSigmoid {
    /*
    * A execução desse método é responsável pela função de ativação do algoritmo de treinamento do MultiLayer Perceptron.
    * Recebe como parâmetro um dado do tipo double.
    * O parâmetro recebido é o y_in de um Neuronio, calculado no estágio Feed-Forward do algoritmo.
    * A função sigmoid é calculada e o resultado é retornado.
    * */
    public static double sigmoid(double y_in) {
        y_in *= -1;
        double denominador = 1 + Math.pow(Math.E, y_in);
        double resultado = 1 / denominador;
        return resultado;
    }

    /*
    * A execução desse método é responsável pelo cálculo do termo de erro de um Neuronio.
    * Recebe como parâmetro um dado do tipo double, que consiste no valor que deve ser passado como argumento da derivada da função sigmoid.
    * Utiliza-se do método "sigmoid" definido acima.
    * Calcula-se a derivada da função sigmoid e então, retorna-se o resultado.
    * */
    public static double derivadaSigmoid(double valor) {
        return sigmoid(valor) * (1 - sigmoid(valor));
    }
}
