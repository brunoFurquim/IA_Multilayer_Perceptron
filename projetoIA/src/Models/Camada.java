package Models;

public class Camada {
    public Neuronio neuronios[];
    public int tamanho;

    public Camada(int tamanho) {
        this.tamanho = tamanho;
        neuronios = new Neuronio[tamanho];

        for (int i = 0; i < tamanho; i++) {
            neuronios[i] = new Neuronio();
        }
    }

    public Camada(int tamanho, int tamanhoCamadaAnterior) {
        this.tamanho = tamanho;
        neuronios = new Neuronio[tamanho];

        for (int i = 0; i < tamanho; i++) {
            neuronios[i] = new Neuronio(tamanhoCamadaAnterior);
        }
    }
}
