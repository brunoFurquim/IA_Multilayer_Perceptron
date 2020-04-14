package Models;

public class Neuronio {
    public double valorInicial = 0;
    public double y_in = 0;
    public double[] pesos;
    public double[] correcoes;
    public double delta = 0;

    //Construtor para neuronios da primeira camada que nao possuem peso, ja que o peso é armazenado nos neuronios das camadas escondidas e na camada de saida
    public Neuronio() {};

    public Neuronio(int tamanhoCamadaAnterior) {
        pesos = new double[tamanhoCamadaAnterior];
        correcoes = new double[tamanhoCamadaAnterior];
    }
}
