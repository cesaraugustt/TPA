package src.dominio;

import src.colecao.IColecao;
import src.listaencadeada.ListaEncadeada;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        IColecao<Aluno> lista = new ListaEncadeada<>();
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            System.out.println("\n=== Sistema de Gerenciamento de Alunos ===");
            System.out.println("1. Adicionar Aluno");
            System.out.println("2. Remover Aluno");
            System.out.println("3. Pesquisar Aluno");
            System.out.println("4. Imprimir todos os Alunos");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opcao: ");

            String entrada = scanner.nextLine().trim();
            int opcao;
            try {
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("\nEntrada invalida! Insira um numero de 1 a 5.");
                continue;
            }

            switch (opcao) {
                case 1:
                    adicionarAluno(scanner, lista);
                    break;
                case 2:
                    removerAluno(scanner, lista);
                    break;
                case 3:
                    pesquisarAluno(scanner, lista);
                    break;
                case 4:
                    imprimirLista(lista);
                    break;
                case 5:
                    System.out.println("\nSaindo do sistema...");
                    executando = false;
                    break;
                default:
                    System.out.println("\nOpcao incorreta! Insira um numero de 1 a 5.");
            }
        }

        scanner.close();
    }

    /**
     * Lê os dados do aluno inseridos pelo usuário e adiciona um novo Aluno à coleção.
     */
    private static void adicionarAluno(Scanner scanner, IColecao<Aluno> lista) {
        System.out.print("Matricula: ");
        int matricula = lerInteiro(scanner);
        if (matricula < 0) {
            System.out.println("\nOperacao cancelada.");
            return;
        }

        if (lista.pesquisar(Aluno.porMatricula(matricula)) != null) {
            System.out.println("\nMatricula " + matricula + " ja cadastrada. Operacao cancelada.");
            return;
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("\nNome nao pode ser vazio. Operacao cancelada.");
            return;
        }

        System.out.print("Nota (0-10): ");
        int nota = lerInteiro(scanner);
        if (nota < 0 || nota > 10) {
            System.out.println("\nNota invalida. Operacao cancelada.");
            return;
        }

        lista.adicionar(new Aluno(matricula, nome, nota));
        System.out.println("\nAluno adicionado com sucesso!");
    }

    /**
     * Lê a matrícula de um aluno e remove o registro correspondente da coleção.
     * A matrícula é utilizada como chave pelo equals().
     */
    private static void removerAluno(Scanner scanner, IColecao<Aluno> lista) {
        if (lista.quantidadeNos() == 0) {
            System.out.println("\nA lista esta vazia.");
            return;
        }

        System.out.print("Matricula do aluno a remover: ");
        int matricula = lerInteiro(scanner);
        if (matricula < 0) {
            System.out.println("\nOperacao cancelada.");
            return;
        }

        // Apenas a matrícula é necessária, pois equals() compara somente esse campo.
        boolean removido = lista.remover(Aluno.porMatricula(matricula));
        if (removido) {
            System.out.println("\nAluno removido com sucesso!");
        } else {
            System.out.println("\nAluno nao encontrado.");
        }
    }

    /**
     * Lê a matrícula de um aluno e pesquisa o registro correspondente na coleção.
     */
    private static void pesquisarAluno(Scanner scanner, IColecao<Aluno> lista) {
        if (lista.quantidadeNos() == 0) {
            System.out.println("\nA lista esta vazia.");
            return;
        }

        System.out.print("Matricula do aluno a pesquisar: ");
        int matricula = lerInteiro(scanner);
        if (matricula < 0) {
            System.out.println("\nOperacao cancelada.");
            return;
        }

        Aluno resultado = lista.pesquisar(Aluno.porMatricula(matricula));
        if (resultado != null) {
            System.out.println("\nAluno encontrado: " + resultado);
        } else {
            System.out.println("\nAluno nao encontrado.");
        }
    }

    /**
     * Imprime todos os alunos armazenados na coleção.
     */
    private static void imprimirLista(IColecao<Aluno> lista) {
        if (lista.quantidadeNos() == 0) {
            System.out.println("\nA lista esta vazia.");
            return;
        }
        System.out.println("\n--- Lista de Alunos (" + lista.quantidadeNos() + " aluno(s)) ---");
        System.out.println(lista);
    }

    /**
     * Lê uma linha do stdin e a interpreta como um inteiro não-negativo.
     * Retorna -1 se a entrada for inválida.
     */
    private static int lerInteiro(Scanner scanner) {
        String linha = scanner.nextLine().trim();
        try {
            int valor = Integer.parseInt(linha);
            if (valor < 0) {
                System.out.println("\nValor nao pode ser negativo.");
                return -1;
            }
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("\nEntrada invalida! Esperado um numero inteiro.");
            return -1;
        }
    }
}