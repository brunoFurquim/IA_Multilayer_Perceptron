import Models.Camada;
import Models.FuncaoSigmoid;
import Models.Neuronio;
import Utils.Utils;
import Utils.FileUtils;


/*
 * A classe MultiLayerPerceptron reúne todos os atributos e métodos necessários para treinar e testar um MultiLayer Perceptron.
 * Existem dois tipos de problema para esse MultiLayer Perceptron.
 * O problema para reconhecimento de caracteres tem um método de treinamento próprio.
 * Os problemas lógicos OR, AND e XOR tem um método de treinamento próprio.
 *
 * A explicação dos métodos de treinamento está em suas definições.
 * */
public class MultiLayerPerceptron {
    public static double taxaDeAprendizado;
    public Camada camadas[];
    public static int linhaDoArquivo = 0;
    private String nomeArquivoPCE = "";
    private String nomeArquivoPCS = "";
    private String nomeArquivoEntrada = "";
    private String categoriaDoProblema = "";

    public MultiLayerPerceptron() {
    }

    /*
    * A classe MultiLayerPerceptron possui o seguinte construtor (além do construtor padrão acima).
    * Ao instanciar um objeto da classe MultiLayerPerceptron, recebe-se a taxa de aprendizado que será utilizada no treinamento, o número de camadas do MLP,
    * o tamanho de cada camada (em um arranjo do tipo inteiro, onde em cada posição do arranjo está o tamanho da camada respectiva (primeiro elemento é o tamanho da primeira camada e assim sucessivamente),
    * o nome do arquivo onde os pesos dos neuronios da camada intermediária (ou escondida) serão salvos,
    * o nome do arquivo para os pesos da camada de saída, o nome do arquivo de entrada (de onde o conjunto de dados será lido) e
    * a categoria do problema, que pode ter o valor "Caracteres" ou "Logico"
    * */
    public MultiLayerPerceptron(double taxaDeAprendizado, int numeroDeCamadas, int[] tamanhoDasCamadas, String nomeArquivoPCE, String nomeArquivoPCS, String nomeArquivoEntrada, String categoriaDoProblema) {
        this.taxaDeAprendizado = taxaDeAprendizado;
        this.camadas = inicializaCamadas(numeroDeCamadas, tamanhoDasCamadas);

        this.nomeArquivoPCE = nomeArquivoPCE;
        this.nomeArquivoPCS = nomeArquivoPCS;
        this.nomeArquivoEntrada = nomeArquivoEntrada;
        this.categoriaDoProblema = categoriaDoProblema;
    }

    /*
     * Esse método é um método auxiliar responsável pelo teste do MultiLayer Perceptron.
     * Recebe como parâmetros um dado do tipo inteiro que corresponde ao numero de linhas do arquivo de entradad.
     *
     * No caso do problema dos caracteres, por exemplo, itera-se sobre as 21 linhas do arquivo de dados de entrada.
     * Para cada linha (cada uma correspondendo a uma letra entre A, B, C, D, E, J ou K, em 3 fontes diferentes), inicializa-se as camadas com os dados correspondentes:
     * Dados de entrada vindos do arquivo .CSV e da linha sendo iterada, pesos dos neuronios das outras camadas vindos também de arquivos .CSV, um para cada camada.
     *
     * E, então, executa-se o método "teste".
     * O funcionamento do método "teste" está explicado em sua definição.
     * */
    public void testar(int numeroLinhasArquivo) {
        int i = 0;

        /*
         * A linha 0 corresponde a primeira linha do arquivo.
         * */
        while (i < numeroLinhasArquivo) {
            /*
             * Carrega os valores para a camada de entrada do CSV.
             * O nome do arquivo deve ser mudado abaixo para realizar os testes. Não é necessário colocar a extensão do arquivo, porém o arquivo deve, necessáriamente, ser um .CSV.
             * */
            camadas[0] = Utils.carregaCamadaCSV(camadas[0], nomeArquivoEntrada, i);
            //Carrega os valores dos pesos dos neuronios da camada escondida do arquivo CSV correspondente (PCE = Pesos Camada Escondida)
            camadas[1] = FileUtils.lerPesosCSV(camadas[1], nomeArquivoPCE);
            //Carrega os valores dos pesos dos neuronios da camada de saida do arquivo CSV correspondente (PCS = Pesos Camada Saida)
            camadas[2] = FileUtils.lerPesosCSV(camadas[2], nomeArquivoPCS);

            teste();
            i++;
        }
    }

    /*
     * A execução desse método é responsável por rodar o MultiLayer Perceptron usando o conjunto de dados como entrada e ler o resultado.
     * Recebe como parâmetro um arranjo de objetos do tipo Camada, cada uma correspondente a uma das três camadas do MultiLayer Perceptron.
     * Executa-se o estágio Feed-Forward utilizando o método "calcularYInEntreCamadas",
     * passando primeiramente a camada de entrada e a intermediária (escondida) e, subsequentemente, passando a camada escondida e a camada de saída como parâmetros.
     *
     * O funcionamento do método "calcularYInEntreCamadas" está explicado em sua definição.
     *
     * Após o estágio Feed-Forward ser completado, executa-se o método "leResposta" da classe Utils.Utils, passando o objeto do tipo Camada correspondente a camada de saída como parâmetro.
     * O funcionamento do método "leResposta" está explicado em sua definição.
     * */
    private void teste() {
        Camada entrada = camadas[0];
        Camada escondida = camadas[1];
        Camada saida = camadas[2];

        calcularYInEntreCamadas(entrada, escondida);
        calcularYInEntreCamadas(escondida, saida);

        if (categoriaDoProblema.toLowerCase().equals("caracteres")) Utils.leRespostaProblemaCaracteres(saida);
        else Utils.leRespostaProblemaLogico(saida);
    }

    /*
     * Esse método é um método auxiliar responsável pelo treinamento do MultiLayer Perceptron.
     * Recebe como parâmetros um arranjo de objetos do tipo Camada contendo as camadas já inicializadas e um dado do tipo inteiro, correspondente ao número de épocas
     * que o algoritmo de treinamento deve ser rodado.
     *
     * Nesse caso, itera-se sobre as 21 linhas do arquivo de dados de entrada.
     * Para cada linha (cada uma correspondendo a uma letra entre A, B, C, D, E, J ou K, em 3 fontes diferentes), inicializa-se as camadas com os dados correspondentes:
     * Dados de entrada vindos do arquivo .CSV e da linha sendo iterada, pesos dos neuronios das outras camadas vindos também de arquivos .CSV, um para cada camada.
     *
     * E, então, executa-se o método "treinamento", passando como argumento o arranjo de objetos do tipo Camada e o número de épocas.
     * O funcionamento do método "treinamento" está explicado em sua definição.
     * */
    public void treinarCaracteres(int numeroDeEpocas, int numeroLinhasArquivo, String nomeArquivoEntrada) {
        int i = 0;
        //Loop itera sobre todas as entradas do conjunto de treinamento
        while (i < numeroLinhasArquivo) {
            linhaDoArquivo = i;
            //Carrega os valores para a camada de entrada do CSV
            camadas[0] = Utils.carregaCamadaCSV(camadas[0], nomeArquivoEntrada, linhaDoArquivo);
            //Carrega os valores dos pesos dos neuronios da camada escondida do arquivo CSV correspondente (PCE = Pesos Camada Escondida)
            camadas[1] = FileUtils.lerPesosCSV(camadas[1], nomeArquivoPCE);
            //Carrega os valores dos pesos dos neuronios da camada de saida do arquivo CSV correspondente (PCS = Pesos Camada Saida)
            camadas[2] = FileUtils.lerPesosCSV(camadas[2], nomeArquivoPCS);

            //Passa as camadas inicializadas para o algoritmo de treinamento
            treinamentoCaracteres(numeroDeEpocas);
            i++;
        }
    }

    /*
     * Esse método é um método auxiliar responsável pelo treinamento do MultiLayer Perceptron para os problemas lógicos AND, OR e XOR.
     * Recebe como parâmetros um dado do tipo inteiro que corresponde ao número de épocas que o algoritmo de treinamento deve rodar e um dado do tipo inteiro que corresponde ao número de linhas do arquivo de entrada.
     *
     * Itera-se sobre as linhas do arquivo de entrada (que contém o conjunto de dados)
     * E, então, executa-se o método "treinamento", passando como argumento o arranjo de objetos do tipo Camada e o número de épocas.
     * O funcionamento do método "treinamento" está explicado em sua definição.
     * */
    public void treinarProblemaLogico(int numeroDeEpocas, int numeroLinhasArquivo) {
        int i = 0;
        int valorEsperado;
        while (i < numeroLinhasArquivo) {
            camadas[0] = Utils.carregaCamadaCSV(camadas[0], nomeArquivoEntrada, i);
            //Carrega os valores dos pesos dos neuronios da camada escondida do arquivo CSV correspondente (PCE = Pesos Camada Escondida)
            camadas[1] = FileUtils.lerPesosCSV(camadas[1], nomeArquivoPCE);
            //Carrega os valores dos pesos dos neuronios da camada de saida do arquivo CSV correspondente (PCS = Pesos Camada Saida)
            camadas[2] = FileUtils.lerPesosCSV(camadas[2], nomeArquivoPCS);

            if (i == 0) valorEsperado = 0;
            else if (i == 1 || i== 2) valorEsperado = 1;
            else valorEsperado = 0;

            treinamentoProblemaLogico(numeroDeEpocas, valorEsperado);
            i++;
        }

    }

    /*
     * Esse método é responsável pela inicialização da estrutura de dados Camada.
     * Recebe como parâmetro um inteiro que representa o número de camadas que o MultiLayer Perceptron irá possuir.
     * Cria-se um arranjo do tipo Camada para armazenar as instâncias criadas.
     * Nesse caso, as camadas abaixo estão inicializadas com 63 neurônios, 63 neurônios e 7 neurônios. Sendo, respectivamente, a camada de entrada (ou de dados), a camada intermediária (escondida) e a camada de saída.
     * Como cada instância do tipo Camada possui um arranjo de objetos do tipo Neuronio, passa-se para o construtor o tamanho da camada anterior.
     * Desse modo, cada Neuronio da camada atual terá um arranjo de pesos do tamanho correto: um peso para cada neuronio da camada anterior
     * */
    private Camada[] inicializaCamadas(int numeroDeCamadas, int[] tamanhosDasCamadas) {
        Camada camadas[] = new Camada[numeroDeCamadas];
        camadas[0] = new Camada(tamanhosDasCamadas[0]);

        for (int i = 1; i < numeroDeCamadas; i++) {
            camadas[i] = new Camada(tamanhosDasCamadas[i], tamanhosDasCamadas[i - 1]);
        }
        return camadas;
    }


    /*
     * O método de treinamento recebe as camadas já inicializadas. A primeira camada (ou camada de entrada) é inicializada com 63 neurônios sem pesos e correções, apenas valores iniciais,
     * esses valores vêm do .CSV contendo o conjunto de dados.
     * A camada escondida é inicializada contendo 63 neurônios. Cada neurônio da segunda camada possui um arranjo do tipo double denominado "pesos" contendo 63 elementos.
     * Nesse arranjo, dentro da estrutura de dados Neuronio, são guardados o peso referente a camada anterior (nesse caso a camada de entrada). Cada neuronio armazena um peso para cada neuronio da camada anterior.
     * Desse modo, os neuronios da camada de entrada não armazenam pesos, já que não há camada anterior.
     * A camada final (ou camada de saída) é inicializada com 7 neurônios. Da mesma forma que a camada intermediária, cada neurônio da camada de saída possui um arranjo do tipo "double" contendo os pesos referentes a camada anterior.
     */
    private void treinamentoCaracteres(int numeroDeEpocas) {
        //Aqui são criadas variáveis para armazenar as camadas recebidas como parametro para facilitar a manipulacao
        Camada entrada = camadas[0];
        Camada escondida = camadas[1];
        Camada saida = camadas[2];

        int entradaAtual = 0;
        /*
         * Aqui calcula-se qual deve ser o valor esperado para cada neuronio da camada de saida.
         * Caso a linha do arquivo sendo lida seja 0, 7 ou 14, por exemplo, significa que os dados sendo lidos pertencem a letra A.
         * Desse modo, o primeiro neuronio da camada de saida (que é o neuronio responsável por reconhecer a letra A, independente da fonte ou ruídos no conjunto de dados passado)
         * deve ter seu valor final o mais próximo de 1 possível. Ao mesmo tempo, os outros neuronios (do segundo até o sétimo) devem ter os valores o mais próximo de 0 possivel.
         * */
        if (linhaDoArquivo == 0 || linhaDoArquivo == 7 || linhaDoArquivo == 14)
            entradaAtual = 0;
        if (linhaDoArquivo == 1 || linhaDoArquivo == 8 || linhaDoArquivo == 15)
            entradaAtual = 1;
        if (linhaDoArquivo == 2 || linhaDoArquivo == 9 || linhaDoArquivo == 16)
            entradaAtual = 2;
        if (linhaDoArquivo == 3 || linhaDoArquivo == 10 || linhaDoArquivo == 17)
            entradaAtual = 3;
        if (linhaDoArquivo == 4 || linhaDoArquivo == 11 || linhaDoArquivo == 18)
            entradaAtual = 4;
        if (linhaDoArquivo == 5 || linhaDoArquivo == 12 || linhaDoArquivo == 19)
            entradaAtual = 5;
        if (linhaDoArquivo == 6 || linhaDoArquivo == 13 || linhaDoArquivo == 20)
            entradaAtual = 6;

        //Aqui inicializa-se a variável contadora responsável pelo controle da iteração das épocas
        int epoca = 0;
        //Cada iteração do laço é uma época, como condição de parada do algoritmo de treinamento foi escolhido o número de épocas.
        while (epoca < numeroDeEpocas) {
            /*
             * De acordo com o algoritmo de treinamento estudado em sala, cada seção do código a seguir é uma etapa do algoritmo.
             * A inicialização dos pesos, bias, taxa de aprendizado e número de épocas é feita antes do método "treinamento" ser chamado.
             * */

            /*
             * As duas próximas linhas são responsáveis pelo estágio Feed-Forward do algoritmo.
             * O método que está sendo executado abaixo é responsável por calcular o y_in de cada neuronio da camada escondida e da camada de saida.
             * O funcionamento do método está comentado em sua definição.
             *
             * Desse modo, é feito o cálculo de todos os valores que os neurônios da camada intermediária (escondida) e da camada de saída possuem,
             * e esses valores são armazenados nos Neuronios de cada camada.
             * */
            calcularYInEntreCamadas(entrada, escondida);
            calcularYInEntreCamadas(escondida, saida);

            /*
             * A subrotina abaixo é responsável por imprimir os valores dos neuronios da camada intermediária e da camada de saída.
             * O intuito da subtrotina é verificar se as chamadas ao método "calcularYInEntreCamadas" obtiveram sucesso e se os neurônios tiveram seus valores atualizados.
             *
             * A explicação do método "leValoresNeuronios" está sendo feita em sua definição
             * */
            System.out.println("Neuronios da camada escondida");
            for (int i = 0; i < escondida.tamanho; i++) {
                Utils.leValoresNeuronio(escondida.neuronios[i]);
            }
            System.out.println("Neuronios da camada de saida");
            for (int i = 0; i < saida.tamanho; i++) {
                Utils.leValoresNeuronio(saida.neuronios[i]);
            }

            /*
             * De acordo com a sexta etapa do algoritmo de treinamento do MultiLayer Perceptron, no laço a seguir:
             * Itera-se sobre todos os neuronios presentes na camada de saída e, para cada um deles, calcula-se o termo de informação de erro.
             * A explicação do método "calculaErroCamadaSaida" está em sua definição.
             * */
            for (int i = 0; i < saida.tamanho; i++) {
                //O valor esperado deve ser ajustado de acordo com o treinamento, de forma que:
                //Caso os dados de entrada sejam da letra A, por exemplo, o valorEsperado deve ser 1 para o primeiro neuronio da camada de saida (de indice 0) e -1 para todos os outros neuronios
                calculaErroCamadaSaida(saida.neuronios[i], escondida, i == entradaAtual ? 1 : -1);
            }

            /*
             * Nessa etapa do algoritmo, itera-se sobre os neuronios da camada intermediária.
             * Para cada neuronio da camada escondida, calcula-se a soma do termo de erro de todos os neuronios da camada seguinte (nesse caso, a camada de saída) que são influenciados pelo neuronio sendo iterado
             * com seus respectivos pesos.
             * O termo de erro do neuronio da camada escondida (seu atributo "delta") é, portanto, igual ao somatório dos valores mencionados acima (termo de erro dos neuronios da camada subsequente e peso) multiplicado pela derivada da função sigmoid.
             * O argumento da derivada da função sigmoid é o valor y_in do neuronio da camada escondida sendo iterado.
             *
             * */
            for (int i = 0; i < escondida.tamanho; i++) {
                Neuronio neuronioEscondido = escondida.neuronios[i];
                double somatorio = 0;
                //Aqui, itera-se sobre todos os neuronios da camada de saída, todos são afetados pelo neuronio da camada escondida sendo iterado no laço exterior.
                for (int j = 0; j < saida.tamanho; j++) {
                    Neuronio neuronioSaida = saida.neuronios[j];
                    double peso = neuronioSaida.pesos[i];
                    double termoDeInfoErro = neuronioSaida.delta;
                    somatorio += (peso * termoDeInfoErro);
                }
                neuronioEscondido.delta = somatorio * FuncaoSigmoid.derivadaSigmoid(neuronioEscondido.y_in);

                /*
                 * Itera-se sobre os pesos do neuronio sendo iterado.
                 * Para cada peso, é calculada a correção que deve ser feita, que é calculada como a multiplicação da taxa de aprendizado pelo termo de erro do neuronio sendo iterado e pelo valor dos neuronios da camada de entrada.
                 * A informação é, então, armazenada no arranjo do tipo double "correcoes" do objeto do tipo "Neuronio", na posicao correspondente a posicao do peso que deve ser corrigido (no arranjo "pesos" do mesmo neuronio).
                 * */
                for (int j = 0; j < neuronioEscondido.pesos.length; j++) {
                    neuronioEscondido.correcoes[j] = taxaDeAprendizado * neuronioEscondido.delta * entrada.neuronios[j].valorInicial;
                }
            }

            /*
             * Após as correções necessárias para os pesos dos neuronios da camada de saida e da camada escondida serem calculados,
             * executa-se o método "atualizarPesosCamada", passando como parâmetros a camada que deve ser atualizada e o nome do arquivo onde os pesos atualizados da camada devem ser salvos.
             * A explicação do método "atualizarPesosCamada" está em sua definição.
             * */
            atualizarPesosCamada(saida, "PCS");
            //O mesmo acontece para os neuronios da camada escondida
            atualizarPesosCamada(escondida, "PCE");

            //Após todos as etapas do algoritmo de treinamento, o contador do laço exterior é incrementado e uma nova época começa.
            epoca++;
        }

    }

    private void treinamentoProblemaLogico(int numeroDeEpocas, int valorEsperado) {
        //Aqui são criadas variáveis para armazenar as camadas recebidas como parametro para facilitar a manipulacao
        Camada entrada = camadas[0];
        Camada escondida = camadas[1];
        Camada saida = camadas[2];

        //Aqui inicializa-se a variável contadora responsável pelo controle da iteração das épocas
        int epoca = 0;
        //Cada iteração do laço é uma época, como condição de parada do algoritmo de treinamento foi escolhido o número de épocas.
        while (epoca < numeroDeEpocas) {
            /*
             * De acordo com o algoritmo de treinamento estudado em sala, cada seção do código a seguir é uma etapa do algoritmo.
             * A inicialização dos pesos, bias, taxa de aprendizado e número de épocas é feita antes do método "treinamento" ser chamado.
             * */

            /*
             * As duas próximas linhas são responsáveis pelo estágio Feed-Forward do algoritmo.
             * O método que está sendo executado abaixo é responsável por calcular o y_in de cada neuronio da camada escondida e da camada de saida.
             * O funcionamento do método está comentado em sua definição.
             *
             * Desse modo, é feito o cálculo de todos os valores que os neurônios da camada intermediária (escondida) e da camada de saída possuem,
             * e esses valores são armazenados nos Neuronios de cada camada.
             * */
            calcularYInEntreCamadas(entrada, escondida);
            calcularYInEntreCamadas(escondida, saida);

            /*
             * A subrotina abaixo é responsável por imprimir os valores dos neuronios da camada intermediária e da camada de saída.
             * O intuito da subtrotina é verificar se as chamadas ao método "calcularYInEntreCamadas" obtiveram sucesso e se os neurônios tiveram seus valores atualizados.
             *
             * A explicação do método "leValoresNeuronios" está sendo feita em sua definição
             * */
            System.out.println("Neuronios da camada escondida");
            for (int i = 0; i < escondida.tamanho; i++) {
                Utils.leValoresNeuronio(escondida.neuronios[i]);
            }
            System.out.println("Neuronios da camada de saida");
            for (int i = 0; i < saida.tamanho; i++) {
                Utils.leValoresNeuronio(saida.neuronios[i]);
            }

            /*
             * De acordo com a sexta etapa do algoritmo de treinamento do MultiLayer Perceptron, no laço a seguir:
             * Itera-se sobre todos os neuronios presentes na camada de saída e, para cada um deles, calcula-se o termo de informação de erro.
             * A explicação do método "calculaErroCamadaSaida" está em sua definição.
             * */
            for (int i = 0; i < saida.tamanho; i++) {
                //O valor esperado deve ser ajustado de acordo com o treinamento, de forma que:
                //Caso os dados de entrada sejam da letra A, por exemplo, o valorEsperado deve ser 1 para o primeiro neuronio da camada de saida (de indice 0) e -1 para todos os outros neuronios
                calculaErroCamadaSaida(saida.neuronios[i], escondida, valorEsperado);
            }

            /*
             * Nessa etapa do algoritmo, itera-se sobre os neuronios da camada intermediária.
             * Para cada neuronio da camada escondida, calcula-se a soma do termo de erro de todos os neuronios da camada seguinte (nesse caso, a camada de saída) que são influenciados pelo neuronio sendo iterado
             * com seus respectivos pesos.
             * O termo de erro do neuronio da camada escondida (seu atributo "delta") é, portanto, igual ao somatório dos valores mencionados acima (termo de erro dos neuronios da camada subsequente e peso) multiplicado pela derivada da função sigmoid.
             * O argumento da derivada da função sigmoid é o valor y_in do neuronio da camada escondida sendo iterado.
             *
             * */
            for (int i = 0; i < escondida.tamanho; i++) {
                Neuronio neuronioEscondido = escondida.neuronios[i];
                double somatorio = 0;
                //Aqui, itera-se sobre todos os neuronios da camada de saída, todos são afetados pelo neuronio da camada escondida sendo iterado no laço exterior.
                for (int j = 0; j < saida.tamanho; j++) {
                    Neuronio neuronioSaida = saida.neuronios[j];
                    double peso = neuronioSaida.pesos[i];
                    double termoDeInfoErro = neuronioSaida.delta;
                    somatorio += (peso * termoDeInfoErro);
                }
                neuronioEscondido.delta = somatorio * FuncaoSigmoid.derivadaSigmoid(neuronioEscondido.y_in);

                /*
                 * Itera-se sobre os pesos do neuronio sendo iterado.
                 * Para cada peso, é calculada a correção que deve ser feita, que é calculada como a multiplicação da taxa de aprendizado pelo termo de erro do neuronio sendo iterado e pelo valor dos neuronios da camada de entrada.
                 * A informação é, então, armazenada no arranjo do tipo double "correcoes" do objeto do tipo "Neuronio", na posicao correspondente a posicao do peso que deve ser corrigido (no arranjo "pesos" do mesmo neuronio).
                 * */
                for (int j = 0; j < neuronioEscondido.pesos.length; j++) {
                    neuronioEscondido.correcoes[j] = taxaDeAprendizado * neuronioEscondido.delta * entrada.neuronios[j].valorInicial;
                }
            }

            /*
             * Após as correções necessárias para os pesos dos neuronios da camada de saida e da camada escondida serem calculados,
             * executa-se o método "atualizarPesosCamada", passando como parâmetros a camada que deve ser atualizada e o nome do arquivo onde os pesos atualizados da camada devem ser salvos.
             * A explicação do método "atualizarPesosCamada" está em sua definição.
             * */
            atualizarPesosCamada(saida, nomeArquivoPCS);
            //O mesmo acontece para os neuronios da camada escondida
            atualizarPesosCamada(escondida, nomeArquivoPCE);

            //Após todos as etapas do algoritmo de treinamento, o contador do laço exterior é incrementado e uma nova época começa.
            epoca++;
        }

    }


    /*
     * Esse método é responsável por atualizar os neurônios da Camada calculo.
     * Itera-se sobre todos os neurônios da Camada calculo, para cada um deles, é executado o método calculaYInNeuronio, passando a camada de entrada (camada anterior à camada calculo)
     * e o Neuronio sendo iterado.
     * O funcionamento do método "calculoYInNeuronio" está sendo explicado em sua definição.
     * Após ser feito o calculo do Y_in do neuronio sendo iterado, o valor retornado pelo método "calculaYInNeuronio" é passado como argumento da função de ativação Sigmoid.
     * O funcionamento do método "sigmoid" está sendo explicado em sua definição, dentro da classe FuncaoSigmoid.
     * O valor retornado pelo método "sigmoid" é armazenado, então, no atributo "valorInicial" do objeto Neuronio sendo iterado.
     * O valor passado à função sigmoid (que corresponde ao y_in do Neuronio) também é armazenado para que possa ser usado em outra etapa do algoritmo.
     * */
    private void calcularYInEntreCamadas(Camada entrada, Camada calculo) {
        for (int i = 0; i < calculo.neuronios.length; i++) {
            double y_in = calculaYInNeuronio(entrada, calculo.neuronios[i]);
            double valorFinal = FuncaoSigmoid.sigmoid(y_in);
            calculo.neuronios[i].valorInicial = valorFinal;
            calculo.neuronios[i].y_in = y_in;
        }
    }

    /*
     * Esse método é responsável por calcular o somatório da multiplicação dos pesos e das entradas entre uma camada e um neuronio de outra camada subsequente.
     * Itera-se sobre todos os neurônios da camada de entrada (a camada que será responsável por fornecer os dados).
     * Para cada neurônio da camada de entrada, soma-se à variável "somatorio" o valor do neurônio (valor que está armazenado no atributo "valorInicial" de cada objeto do tipo Neuronio)
     * multiplicado pelo peso respectivo àquele valor (peso que está armazenado em um arranjo do tipo double na instancia neuronioSaida, que é um objeto do tipo Neuronio).
     * Após todas as iterações serem feitas, o valor da variável "somatorio" é atribuido a variável y_in, que por sua vez tem seu valor retornado pelo método.
     *
     * A variável y_in não é necessária nesse método, poderia ter sido diretamente retornado o valor da variável somatorio. Foi feito dessa maneira para ser visualizado e explicado
     * mais facilmente o que é o valor sendo calculado pelo método.
     * */
    private double calculaYInNeuronio(Camada entrada, Neuronio neuronioSaida) {
        double y_in;
        double somatorio = 0;
        for (int i = 0; i < entrada.neuronios.length; i++) {
            somatorio += (entrada.neuronios[i].valorInicial * neuronioSaida.pesos[i]);
        }
        y_in = somatorio;
        return y_in;
    }

    /*
     * Esse método foi implementado para calcular o erro de cada Neuronio da camada final (ou camada de saída).
     * Recebe como parâmetros um objeto do tipo Neuronio, um objeto do tipo Camada que é a camada anterior à camada final (ou seja, é a camada escondida) e o valor esperado para aquele Neuronio.
     * O parâmetro "valorEsperado" do tipo double é o que possibilita que o MultiLayer Perceptron seja treinado.
     *
     * */
    private void calculaErroCamadaSaida(Neuronio neuronio, Camada escondida, double valorEsperado) {
        /*
         * Calcula-se o erro do neuronio recebido como parametro, como pode ser visto abaixo, o erro é dado pela diferença entre o valor esperado e o valor do neuronio ao final da execução do algoritmo.
         * O termo de informação de erro é calculado pela multiplicação desse erro pela derivada da função sigmoid, com o valor y_in do neuronio passado como argumento (calculado durante as etapas Feed-Forward e armazenado como atributo do tipo double no Neuronio)
         * O método "derivadaSigmoide" está explicado em sua definição, na classe FuncaoSigmoid.
         * O termo de informação de erro, após ser calculado, é armazenado no atributo "delta" do Neuronio passado como parametro.
         * */
        double erro = valorEsperado - neuronio.valorInicial;
        double termoDeInfoErro = erro * FuncaoSigmoid.derivadaSigmoid(neuronio.y_in);
        neuronio.delta = termoDeInfoErro;

        /*
         * No laço abaixo, itera-se sobre os pesos do neuronio, calculando-se as correções necessárias baseando-se nos erros cometidos pelo neuronio.
         * Para cada peso do neuronio, calcula-se a correção necessária, que é equivalente a taxa de aprendizado multiplicada pelo termo de informação de erro
         * (calculado no passo anterior e armazenado no atributo delta do neuronio) e, enfim, multiplicado pelo valor do neuronio da camada anterior (nesse caso, a cama escondida) a qual o peso corresponde.
         * Em outras palavras, a correcao que deve ser feita no peso de um neuronio da camada final é baseada na taxa de aprendizado, no erro do neuronio e nos valores de cada um dos neuronios da camada anterior que
         * afetam o valor do neuronio sendo corrigido.
         *
         * Para cada peso do neuronio sendo iterado (cuja quantidade é igual ao número de neuronios na camada anterior), existe uma correção.
         * De modo que o elemento neuronio.correcoes[0] é a correção que deve ser feita ao elemento neuronio.pesos[0], e assim sucessivamente.
         * */
        for (int j = 0; j < neuronio.pesos.length; j++) {
            neuronio.correcoes[j] = taxaDeAprendizado * neuronio.delta * escondida.neuronios[j].valorInicial;
        }
    }

    /*
     * Esse método foi implementado para atualizar os pesos de todos os neuronios de uma camada.
     * Recebe como parâmetros uma Camada cujos Neuronios devem ter seus pesos atualizados e o nome do arquivo onde os pesos da Camada devem ser armazenados.
     * Itera-se sobre todos os neuronios da camada recebida como argumento. Para cada neuronio na camada, executa-se o método "atualizarPesos" passando o neuronio iterado como parametro.
     * O funcionamento do método "atualizarPesos" está explicado em sua definição.
     *
     * Após o laço, executa-se o método "salvarPesosCSV" da class Utils.Utils.FileUtils, passando a camada sendo atualizada e o nome do arquivo como parametros.
     * O funcionamento do método "salvarPesosCSV" está explicado em sua definição.
     * */
    private void atualizarPesosCamada(Camada camada, String nomeArquivo) {
        for (int i = 0; i < camada.tamanho; i++) {
            Neuronio neuronio = camada.neuronios[i];
            atualizarPesos(neuronio);
        }
        FileUtils.salvarPesosCSV(camada, nomeArquivo);
    }

    /*
     * Esse método atualiza os pesos de um objeto do tipo Neuronio que é recebido como parâmetro.
     * Itera-se sobre todos os pesos de um neuronio e atualiza-se o valor do peso para a soma entre o antigo valor, e a correção correspondente.
     * Essa é a ultima etapa do algoritmo de treinamento antes de salvar os valores dos pesos atualizados e iniciar uma nova época.
     * Como foi explicado no método "calculaErroCamadaSaida", cada peso de um neuronio possui uma correção correspondente que deve ser utilizada em sua atualização.
     * */
    private void atualizarPesos(Neuronio neuronio) {
        for (int i = 0; i < neuronio.pesos.length; i++) {
            neuronio.pesos[i] += neuronio.correcoes[i];
        }
    }

    /*
     * Esse método foi utilizado apenas uma vez e tem como objetivo inicializar os pesos de cada neuronio da camada escondida e da camada de saída (nesse caso, o segundo e terceiro item do arranjo camadas).
     * Recebe como parâmetro o arranjo contendo objetos do tipo Camada já inicializados, e dois elementos do tipo double que determinam o intervalo cujos valores dos pesos farão parte. Nesse caso, o intervalo seria denotado por [valorMinimo, valorMaximo].
     * Executa-se o método "inicializaPesosAleatorios" da classe Utils.Utils, passando como parâmetro o objeto do tipo Camada que deve ter seus pesos inicializados e o intervalo de valores.
     * O funcionamento do método "inicializaPesosAleatorios" está explicado em sua definição.
     *
     * Após a inicialização dos pesos, executa-se o método "salvarPesosCSV" da classe Utils.Utils.FileUtils para que os dados recém criados (os pesos de cada neuronio) sejam salvos em um arquivo .CSV.
     * O funcionamento do método "salvarPesosCSV" está explicado em sua definição.
     * Os nomes dos arquivos escolhidos para salvar os dados foram: PCE.csv e PCS.csv (Pesos Camada Escondida e Pesos Camada Saida, respectivamente).
     * */
    public void inicializaPesosAleatorios(double valorMinimo, double valorMaximo) {
        Utils.inicializaPesosAleatorios(camadas[1], valorMinimo, valorMaximo);
        Utils.inicializaPesosAleatorios(camadas[2], valorMinimo, valorMaximo);

        FileUtils.salvarPesosCSV(camadas[1], nomeArquivoPCE);
        FileUtils.salvarPesosCSV(camadas[2], nomeArquivoPCS);
    }

}
