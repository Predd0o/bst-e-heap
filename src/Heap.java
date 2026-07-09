import java.util.*;                                                        // importa classes utilitárias (não usadas diretamente aqui, mas mantém padrão)

//implementação de um Max Heap usando array
public class Heap {
    private int[] heap;                                                     // array que armazena os elementos do heap
    private int tail;                                                       // índice do último elemento presente no heap (-1 se vazio)

    public Heap(int capacidade){                                            // construtor: cria heap vazio com a capacidade informada
        this.heap = new int[capacidade];                                    // aloca o array com o tamanho pedido
        this.tail = -1;                                                     // -1 indica que não há elementos ainda
    }

    public Heap(int[] array){                                               // construtor: recebe um array pronto e transforma em heap (Build Heap)
        this.heap = array;                                                  // usa o próprio array recebido como array interno
        this.tail = this.heap.length - 1;                                   // tail aponta para o último índice ocupado (array cheio)
        this.buildHeap();                                                   // aplica heapify de baixo para cima para virar um Heap válido
    }

    public boolean isEmpty(){                                               // verifica se o heap está vazio
        return this.tail == -1;                                             // vazio quando tail é -1 (nenhum elemento inserido)
    }

    public int left(int index){                                            // retorna o índice do filho esquerdo de um nó
        return 2 * index + 1;                                               // fórmula: 2*index + 1
    }

    public int right(int index){                                           // retorna o índice do filho direito de um nó
        return 2 * (index + 1);                                             // fórmula: 2*(index + 1)
    }

    public int parent(int index){                                          // retorna o índice do pai de um nó
        return Math.floorDiv(index - 1, 2);                                 // fórmula: floorDiv(index - 1, 2)
    }

    private boolean isValidIndex(int index){                                // verifica se um índice está dentro dos limites do heap
        return index >= 0 && index <= tail;                                 // válido se estiver entre 0 e tail (inclusive)
    }

    private boolean isLeaf(int index){                                      // verifica se o nó no índice é uma folha
        return index > parent(tail) && index <= tail;                      // folha: está além do pai da última posição ocupada
    }

    private void swap(int i, int j){                                        // troca os valores armazenados nos índices i e j
        int aux = this.heap[i];                                             // guarda o valor de i temporariamente
        this.heap[i] = this.heap[j];                                        // i recebe o valor de j
        this.heap[j] = aux;                                                 // j recebe o valor antigo de i
    }

    private void resize(){                                                  // dobra o tamanho do array quando ele está cheio
        int novaCapacidade;
        if(this.heap.length == 0){                                          // se o array atual tem tamanho 0
            novaCapacidade = 1;                                            // nova capacidade deve ser 1
        } else {
            novaCapacidade = this.heap.length * 2;                         // caso contrário, dobra a capacidade
        }
        this.heap = Arrays.copyOf(this.heap, novaCapacidade);               // copia elementos antigos para o array maior
    }

    public void add(int n){                                                 // insere um novo elemento no heap, mantendo a propriedade de max heap
        if(tail >= (heap.length - 1))                                       // se não há espaço livre no array
            resize();                                                      // aumenta a capacidade do array

        tail += 1;                                                         // avança tail para a próxima posição livre
        this.heap[tail] = n;                                                // insere o elemento na última posição livre (mantém completude)

        int i = tail;                                                       // i começa na posição recém-inserida
        while(i > 0 && this.heap[parent(i)] < this.heap[i]){                 // sobe enquanto o pai for menor que o filho
            swap(i, parent(i));                                             // troca elemento com o pai (sobe na árvore)
            i = parent(i);                                                  // atualiza i para a posição do pai
        }
    }

    public int max(){                                                       // retorna (sem remover) o elemento do topo do heap
        if(isEmpty()) throw new RuntimeException("Heap vazio");            // não há topo em heap vazio
        return this.heap[0];                                                // a raiz (índice 0) é o maior em um max-heap
    }

    public boolean contains(int value){                                     // verifica se o valor existe no heap
        return indexOf(value) != -1;                                        // true se encontrar índice válido
    }

    private int indexOf(int value){                                         // busca o índice do valor no array do heap
        for(int i = 0; i <= tail; i++){
            if(this.heap[i] == value)                                       // achou valor
                return i;                                                   // retorna índice do valor
        }
        return -1;                                                          // não encontrou
    }

    public void remove(int value){                                         // remove o valor fornecido do heap, se existir
        if(isEmpty()) return;                                               // nada a fazer em heap vazio

        int index = indexOf(value);                                        // busca o índice do valor a ser removido

        if(index == -1) return;                                             // valor não encontrado -> nenhum elemento removido

        if(index == tail){                                                   // valor está na última posição ocupada
            this.tail -= 1;                                                 // basta reduzir tail
            return;
        }

        this.heap[index] = this.heap[tail];                                 // substitui o valor removido pelo último elemento
        this.tail -= 1;                                                     // reduz tail para remover a última posição

        while(index > 0 && this.heap[parent(index)] < this.heap[index]){    // corrige se o novo valor for maior que o pai
            swap(index, parent(index));                                     // sobe o valor até a posição correta
            index = parent(index);                                          // continua a partir do novo índice
        }

        heapify(index);                                                     // ajusta a subárvore caso o valor seja menor que os filhos
    }

    private int bestIndex(int index, int left, int right){                 // encontra, entre index, left e right, quem deve ficar no topo (maior valor)
        int melhor = index;                                                 // assume inicialmente que index já é o "melhor"

        if(isValidIndex(left) && this.heap[left] > this.heap[melhor])       // se o filho esquerdo for maior
            melhor = left;                                                  // atualiza melhor para left

        if(isValidIndex(right) && this.heap[right] > this.heap[melhor])     // se o filho direito for maior
            melhor = right;                                                 // atualiza melhor para right

        return melhor;                                                      // retorna o índice do "melhor" entre os três
    }

    private void heapify(int index){                                       // corrige a propriedade de heap a partir do índice informado, descendo na árvore
        if(isLeaf(index) || !isValidIndex(index))                          // condição de parada: folha ou índice inválido
            return;                                                         // nada a fazer

        int indexBest = bestIndex(index, left(index), right(index));      // compara index com seus filhos para achar quem deve ficar no topo

        if(indexBest != index){                                             // se o "melhor" não for o próprio nó
            swap(index, indexBest);                                        // troca o nó com o filho que deveria estar no topo
            heapify(indexBest);                                             // continua o heapify a partir da nova posição do valor
        }
    }

    private void buildHeap(){                                               // transforma um array qualquer em um Heap válido
        for(int i = parent(this.tail); i >= 0; i--)                        // começa no pai da última folha e sobe até a raiz
            heapify(i);                                                     // aplica heapify em cada índice
    }

    public void toMinHeap(){                                                // transforma o heap atual (max-heap) em um min-heap, in-place
        buildMinHeap();                                                     // usa heapify de min-heap para reorganizar o array
    }

    private void buildMinHeap(){                                            // transforma o array em min-heap
        for(int i = parent(this.tail); i >= 0; i--)                        // começa no pai da última folha e sobe até a raiz
            minHeapify(i);                                                  // aplica min-heapify em cada índice
    }

    private void minHeapify(int index){                                     // corrige a propriedade de min-heap a partir do índice informado
        if(isLeaf(index) || !isValidIndex(index))                          // condição de parada: folha ou índice inválido
            return;                                                         // nada a fazer

        int smallest = index;                                               // assume inicialmente que index é o menor
        int left = left(index);                                             // índice do filho esquerdo
        int right = right(index);                                           // índice do filho direito

        if(isValidIndex(left) && this.heap[left] < this.heap[smallest])     // se o filho esquerdo for menor
            smallest = left;                                                // atualiza smallest

        if(isValidIndex(right) && this.heap[right] < this.heap[smallest])   // se o filho direito for menor
            smallest = right;                                               // atualiza smallest

        if(smallest != index){                                              // se o menor não for o próprio nó
            swap(index, smallest);                                         // troca com o menor
            minHeapify(smallest);                                           // continua o processo a partir do filho
        }
    }

    public int size(){                                                      // retorna a quantidade de elementos presentes no heap
        return this.tail + 1;                                               // tail é o índice do último elemento, então size = tail + 1
    }

    public void printArray(){                                              // imprime o array interno do heap (apenas as posições ocupadas)
        for(int i = 0; i <= tail; i++)                                     // percorre do índice 0 até tail
            System.out.print(this.heap[i] + " ");                         // imprime cada elemento ocupado
        System.out.println();                                               // pula linha ao final
    }

    public static boolean isMaxHeapSequenceFromStdin(){                    // lê sequência da entrada padrão e verifica se forma um max-heap
        Scanner scanner = new Scanner(System.in);                           // usa Scanner para leitura de inteiros
        List<Integer> values = new ArrayList<>();                           // guarda os valores lidos

        while(scanner.hasNextInt())                                         // consome todos os inteiros da entrada
            values.add(scanner.nextInt());                                  // adiciona cada inteiro à lista

        return isMaxHeap(values);                                           // verifica a propriedade do max-heap
    }

    private static boolean isMaxHeap(List<Integer> values){                 // valida se a lista representa um max-heap completo/almost-completo
        int n = values.size();                                              // número de elementos lidos

        for(int i = 0; i < n; i++){                                        // para cada nó do array
            int left = 2 * i + 1;                                           // índice do filho esquerdo
            int right = 2 * i + 2;                                          // índice do filho direito

            if(left < n && values.get(i) < values.get(left))               // se o nó for menor que o filho esquerdo
                return false;                                               // então não é max-heap

            if(right < n && values.get(i) < values.get(right))             // se o nó for menor que o filho direito
                return false;                                               // então não é max-heap
        }

        return true;                                                        // todas as comparações passaram
    }
}