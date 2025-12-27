package src.presenteFacil.controller;

import java.util.*;
import src.presenteFacil.model.*;
import src.presenteFacil.utils.ClearConsole;

public class ControladorListaProduto {
    private ArquivoListaProduto arqListaProduto; 
    private ArquivoProduto arqProduto;
    private ControladorProduto controladorProduto;
    private Usuario usuarioLogado;

    public ControladorListaProduto() throws Exception{
        this.arqListaProduto = new ArquivoListaProduto();
        this.arqProduto = new ArquivoProduto();
        this.controladorProduto = new ControladorProduto();
    }

    public void setUsuario(Usuario usuarioLogado){
        this.usuarioLogado = usuarioLogado;
    }

    public void gerenciarProdutoLista(Scanner scanner, Lista lista, Usuario usuarioLogado) throws Exception{

        setUsuario(usuarioLogado);
        String opcao;
        boolean continua = true;

        try{
            while(continua) {
                System.out.println("-------- PresenteFácil 2.0 --------"); 
                System.out.println("-----------------------------------"); 
                System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos \n");

                Produto[] produtos = mostrarProdutosPorListaProduto(lista);

                System.out.println();
                System.out.println("(A) Acrescentar produto");
                System.out.println("(R) Retornar ao menu anterior");
                System.out.println();
                System.out.print("\nOpção: ");

                opcao = scanner.nextLine().trim().toUpperCase();

                ClearConsole.clearScreen();

                switch (opcao) {
                    case "R":
                        System.out.println("\n-- Retornando ao menu anterior. --\n");
                        continua = false;
                        break;
                    case "A":
                        System.out.println("\n-- Há ser acresentada. --\n");
                        acresentarProdutoLista(scanner, lista);
                        continua = false;
                        break;
                    default:
                        if(isNumber(opcao)){ 
                            int indice = Integer.parseInt(opcao); 
                            System.out.println("\n[Selecionou a lista " + indice + "]\n"); 

                            if(produtos != null && indice > 0 && indice <= produtos.length){ 
                                mostrarDetalhesListaProduto(scanner, lista, produtos[indice - 1]); 
                            } else { 
                                System.out.println("\nOpção Inválida. Tente novamente.\n");
                            } 
                        }else{
                            System.out.println("\nOpção Inválida. Tente novamente.\n");
                        }
                }
            }
        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao gerenciar os produtos da lista: " + e.getMessage() + "\n");
        }
    }

    public void mostrarDetalhesListaProduto(Scanner scanner, Lista lista, Produto produto) throws Exception{
        try{
            ListaProduto listaProduto = arqListaProduto.readByListaIdAndProdutoId(lista.getId(), produto.getId());
            boolean continua = true;

            while (continua) {
                System.out.println("-------- PresenteFácil 2.0 --------"); 
                System.out.println("-----------------------------------"); 
                System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos > " + produto.getNome() +"\n");

                System.out.println("\n-------- Detalhes do Produto --------\n");
                System.out.println(produto.toString());
                System.out.println(listaProduto.toString());
                System.out.println("-------------------------------------\n");
                System.out.println("(1) Alterar a quantidade");
                System.out.println("(2) Alterar as observações");
                System.out.println("(3) Remover o produto desta lista");
                System.out.println("(R) Retornar ao menu anterior");
                System.out.print("\nOpcao: ");

                String opcao = scanner.nextLine().trim().toUpperCase();

                ClearConsole.clearScreen();

                switch (opcao) {
                    case "1":
                        mudarQuantidade(scanner, listaProduto);
                        break;
                    case "2":
                        mudarObservacoes(scanner, listaProduto);
                        break;
                    case "3":
                        boolean result = removerProdutoDaLista(scanner, listaProduto);
                        if(result){
                            continua = false;
                        }
                        break;
                    case "R":
                        return;
                    default:
                        System.out.println("\n-- Opção inválida. --\n");
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar mostrar Detalhes ListaProduto: " + e.getMessage() + "\n");
        }
    }

    public boolean removerProdutoDaLista(Scanner scanner, ListaProduto listaProduto) throws Exception{
        try{
            String opcao = "";
            boolean continua = true;

            while (continua) {
                System.out.print("Deseja deletar esse produto da lista? (S/N): ");
                opcao = scanner.nextLine().trim().toUpperCase();

                ClearConsole.clearScreen();

                if (opcao.equals("S")) {
                    boolean result = arqListaProduto.delete(listaProduto.getId());
                    if(result){
                        System.out.println("\n--- Produto deletado com sucesso! ---\n");
                    }else{
                        System.out.println("\n--- Erro ao deletar o produto! ---\n");
                    }
                    continua = false;
                    return result;
                } else if (opcao.equals("N")) {
                    System.out.println("\n--- Produto não foi deletado da lista. ---\n");
                    return false;
                } else {
                    System.out.println("Entrada inválida. Por favor, digite 'S' para sim ou 'N' para não.");
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro ao remover produto da lista: " + e.getMessage() + "\n");
        }
        return false;
    }

    public void buscarProdutosPorGTIN(Scanner scanner, Lista lista) throws Exception {
        try {
            System.out.println("-------- PresenteFácil 2.0 --------"); 
            System.out.println("-----------------------------------"); 
            System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos > Acrescentar produto > Buscar por GTIN \n");

            System.out.print("Digite o GTIN-13 do produto: ");
            String gtin13 = scanner.nextLine();
            boolean estaNaLista = false;
            Produto[] produtos = arqListaProduto.getProdutosByListaId(lista.getId());

            for(int i = 0; i < produtos.length; i++){
                if(gtin13.equals(produtos[i].getGtin13())){
                    estaNaLista = true;
                    break;
                }
            }

            if(estaNaLista){
                System.out.println("\n--- Projduto já está cadastrado na lista! ---\n");
                ClearConsole.clearScreen();
                return;
            }

            Produto produto = arqProduto.read(gtin13);

            if (produto == null) {
                ClearConsole.clearScreen();
                System.out.println("\n-- Nenhum produto encontrado com este GTIN-13. --\n");
            } 
            
            else {
                System.out.println("\n------------- Detalhes do Produto ---------------\n");
                System.out.println(produto);
                System.out.println("\n-------------------------------------------------\n");

                boolean continua = true;
                int quantidade = 1;

                while(continua){
                    System.out.print("Digite a quantidade: ");
                    quantidade = scanner.nextInt();

                    if(quantidade < 1){
                        System.out.println("\n--- A quantidade não pode ser menor ou igual a zero! ---\n");
                    }
                    continua = false;
                }
                scanner.nextLine();

                System.out.println();
                System.out.print("Observações (Opcional): ");
                String observacoes = scanner.nextLine();
                System.out.println();

                String opcao = "";
                continua = true;

                while (continua) {
                    System.out.print("\nDeseja acrescentar esse produto à lista? (S/N): ");
                    opcao = scanner.nextLine().trim().toUpperCase();

                    ClearConsole.clearScreen();

                    if (opcao.equals("S")) {
                        System.out.println("\nProduto adicionado à lista com sucesso!\n");
                        continua = false;

                    } else if (opcao.equals("N")) {
                        System.out.println("\nProduto não foi adicionado à lista.\n");
                        return;
                    } 
                    
                    else {
                        System.out.println("\nEntrada inválida. Por favor, digite 'S' para sim ou 'N' para não.\n");
                    }
                }

                ListaProduto novaListaProduto = new ListaProduto(lista.getId(), produto.getId(), quantidade, observacoes);
                arqListaProduto.create(novaListaProduto);
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar produto: " + e.getMessage() + "\n");
        }
    }  

    public void acresentarProdutoLista(Scanner scanner, Lista lista) throws Exception {

        System.out.println("-------- PresenteFácil 2.0 --------"); 
        System.out.println("-----------------------------------"); 
        System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos > Acrescentar produto \n");

        String opcao;
        boolean continua = true;

        try{
            while(continua) {
                System.out.println("(1) Buscar produtos por GTIN");
                System.out.println("(2) Listar todos os produtos");
                System.out.println();
                System.out.println("(R) Retornar ao menu anterior");
                System.out.println();
                System.out.print("\nOpção: ");

                opcao = scanner.nextLine().trim().toUpperCase();

                ClearConsole.clearScreen();

                switch (opcao) {
                    case "1": //busca por GTIN
                        buscarProdutosPorGTIN(scanner, lista);
                        continua = false;
                        break;
                    case "2": // listagem 
                        System.out.println("\n-- Há ser acrescentada. --\n");
                        listagemDeProdutos(scanner, lista);
                        continua = false;
                        break;
                    case "R":
                        System.out.println("\n-- Retornando ao menu anterior. --\n");
                        continua = false;
                        break;
                    default:
                        System.out.println("\nOpção Inválida. Tente novamente.\n");
                }
            }
        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao mostrar acresentar produto: " + e.getMessage() + "\n");
        }
    }

    public Produto[] mostrarProdutosPorListaProduto(Lista lista) throws Exception {
        try {
            Produto[] produtos = arqListaProduto.getProdutosByListaId(lista.getId());
            ListaProduto[] listaProdutos = arqListaProduto.readByListaId(lista.getId());

            if (produtos == null || produtos.length == 0) {
                System.out.println("\n-- Nenhum produto cadastrado. --\n");
                return null;
            }

            Arrays.sort(produtos, Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER));

            for (int i = 0; i < produtos.length; i++) {
                for (int j = 0; j < listaProdutos.length; j++) {
                    if (produtos[i].getId() == listaProdutos[j].getIdProduto()) {
                        System.out.println("(" + (i + 1) + ") " + produtos[i].getNome() + " " +
                            "(x" + listaProdutos[j].getQuantidade() + ")");
                    }
                }
            }

            return produtos;
        } catch (Exception e) {
            System.err.println("\nErro ao mostrar produtos por ListaProduto: " + e.getMessage() + "\n");
            return null;  
        }
    }

    public void listagemDeProdutos(Scanner scanner, Lista lista){ //coisas aqui
        try {
            List<Produto> produtos = arqProduto.listarTodos();
            if (produtos.isEmpty()) {
                System.out.println("\n-- Nenhum produto cadastrado. --\n");
                return;
            }

            int paginaAtual = 0;
            final int ITENS_POR_PAGINA = 10;
            boolean sair = false;

            produtos.sort(Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER));

            while (!sair){
                System.out.println("-------- PresenteFácil 2.0 --------"); 
                System.out.println("-----------------------------------"); 
                System.out.println("> Início > Minhas Listas > " + lista.getNome() + " > Produtos > Acrescentar produto > Listagem \n");

                int totalPaginas = (int) Math.ceil((double) produtos.size() / ITENS_POR_PAGINA);
                System.out.println("Pagina " + (paginaAtual + 1) + " de " + totalPaginas);
                System.out.println("----------------------------------\n");

                int inicio = paginaAtual * ITENS_POR_PAGINA;
                int fim = Math.min(inicio + ITENS_POR_PAGINA, produtos.size());

                for (int i = inicio; i < fim; i++) {
                    System.out.println("(" + (i + 1) + ") " + produtos.get(i).getNome());
                }

                System.out.println("\nEscolha uma opção para ver mais detalhes:\n");
                System.out.println("(P) Proxima página");
                System.out.println("(A) Página anterior");
                System.out.println("(R) Retornar ao menu anterior");
                System.out.print("\nOpção: ");

                String opcao = scanner.nextLine().trim().toUpperCase();

                ClearConsole.clearScreen();

                switch (opcao) {
                    case "P":
                        if (paginaAtual < totalPaginas - 1) paginaAtual++;
                        break;
                    case "A":
                        if (paginaAtual > 0) paginaAtual--;
                        break;
                    case "R":
                        sair = true;
                        break;
                    default:
                        try {
                            int escolha = Integer.parseInt(opcao);
                            int indiceEscolhido = escolha - 1;
                            if (indiceEscolhido >= inicio && indiceEscolhido < fim) {
                                Produto selecionado = produtos.get(indiceEscolhido); 
                                acrescentarPorListagemDeProdutos(scanner, lista, selecionado);
                            } else {
                                System.out.println("\n-- Número fora da página atual. --\n");
                            }
                        } catch (NumberFormatException nf) {
                            System.out.println("\n-- Opção inválida. --\n");
                        }
                        break;
                }
            }
        }  catch (Exception e) {
            System.err.println("\nErro ao listar produtos: " + e.getMessage() + "\n");
        }
    }

    public void acrescentarPorListagemDeProdutos(Scanner scanner, Lista lista, Produto produto){
        try {
            boolean estaNaLista = false;
            Produto[] produtos = arqListaProduto.getProdutosByListaId(lista.getId());

            for(int i = 0; i < produtos.length; i++){
                if(produto.getGtin13().equals(produtos[i].getGtin13())){
                    estaNaLista = true;
                    break;
                }
            }

            if(estaNaLista){
                System.out.println("\n--- Projduto já está cadastrado na lista! ---\n");
                return;
            }

            if (produto == null) {
                System.out.println("\n-- Nenhum produto encontrado com este GTIN-13. --\n");
            } else {
                System.out.println("\n-------------- Detalhes do Produto --------------\n");
                System.out.println(produto);
                System.out.println("\n---------------------------------------------------\n");

                boolean continua = true;
                int quantidade = 1;

                while(continua){
                    System.out.print("Digite a quantidade: ");
                    quantidade = scanner.nextInt();

                    if(quantidade < 1){
                        System.out.println("\n--- A quantidade não pode ser menor ou igual a zero! ---\n");
                    }
                    continua = false;
                }
                scanner.nextLine();

                System.out.println();
                System.out.print("Observações (Opcional): ");
                String observacoes = scanner.nextLine();
                System.out.println();

                String opcao = "";
                continua = true;

                while (continua) {
                    System.out.print("Deseja acrescentar esse produto à lista? (S/N): ");
                    opcao = scanner.nextLine().trim().toUpperCase();

                    ClearConsole.clearScreen();

                    if (opcao.equals("S")) {
                        System.out.println("\nProduto adicionado à lista com sucesso!\n");
                        continua = false;
                    } 
                    
                    else if (opcao.equals("N")) {
                        System.out.println("\nProduto não foi adicionado à lista.\n");
                        return;
                    } else {
                        System.out.println("\nEntrada inválida. Por favor, digite 'S' para sim ou 'N' para não.\n");
                    }
                }

                ListaProduto novaListaProduto = new ListaProduto(lista.getId(), produto.getId(), quantidade, observacoes);
                arqListaProduto.create(novaListaProduto);
            }
        } catch (Exception e) {
            System.err.println("\nErro ao acrescentar por listagem de produto: " + e.getMessage() + "\n");
        }
    }

    public void mudarQuantidade(Scanner scanner, ListaProduto listaProduto){
        boolean continua = true;

        while(continua){
            System.out.print("\nDigite a nova quantidade: ");
            int quantidade = scanner.nextInt();

            if(quantidade < 1){
                System.out.println("\n--- A quantidade não pode ser menor ou igual a zero! ---\n");
                ClearConsole.clearScreen();
            }

            listaProduto.setQuantidade(quantidade);
            continua = false;
        }
    }

    public void mudarObservacoes(Scanner scanner, ListaProduto listaProduto){

        System.out.print("\nDigite as novas observações: ");
        String observacoes = scanner.nextLine();
        if(!observacoes.equals(" ")){
            listaProduto.setObservacoes(observacoes);
        }

        ClearConsole.clearScreen();
    }

    public boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
