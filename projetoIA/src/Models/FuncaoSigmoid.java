package Models;

public class FuncaoSigmoid {
    //Calcula o resultado da funcao de ativacao sigmoid
    public static double sigmoid(double y_in) {
        y_in *= -1;
        double denominador = 1 + Math.pow(Math.E, y_in);
        double resultado = 1 / denominador;
        return resultado;
    }

    public static double derivadaSigmoide(double valor) {
        return sigmoid(valor) * (1 - sigmoid(valor));
    }
}
