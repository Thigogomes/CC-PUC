package src.presenteFacil.controller;

import src.presenteFacil.model.ArquivoListaProduto;
import src.presenteFacil.model.ArquivoProduto;
import src.presenteFacil.model.Lista;
import src.presenteFacil.model.Produto;
import src.presenteFacil.model.Usuario;
import src.presenteFacil.utils.ClearConsole;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ControladorProduto {

    private ArquivoProduto arqProdutos;
    private ArquivoListaProduto arqListaProduto;
    private Usuario usuario;

    public ControladorProduto() throws Exception {
        this.arqProdutos = new ArquivoProduto();
        this.arqListaProduto = new ArquivoListaProduto();
    }

    public void setUsuario(Usuario usuarioLogado){
        this.usuario = usuarioLogado;
    }

    public void cadastrarNovoProduto(Scanner scanner) {

        System.out.println("-------- PresenteFácil 2.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("> Início > Produtos > Cadastrar\n");

        try {
            System.out.print("GTIN-13: ");
            String gtin13 = scanner.nextLine();

            // --- Verificação 1: GTIN deve ter exatamente 13 dígitos numéricos ---
            if (!gtin13.matches("\\d{13}")) {
                System.out.println("\n-- ERRO: GTIN-13 inválido. Deve conter exatamente 13 dígitos numéricos. --\n");
                return;
            }

            // --- Verificação 2: GTIN já cadastrado ---
            Produto existente = arqProdutos.read(gtin13);
            if (existente != null) {
                System.out.println("\n-- ERRO: Já existe um produto com o GTIN-13 informado. --\n");
                return;
            }

            System.out.print("\nNome do Produto: ");
            String nome = scanner.nextLine();
            System.out.print("\nDescrição: ");
            String descricao = scanner.nextLine();

            Produto novoProduto = new Produto(gtin13, nome, descricao);
            arqProdutos.create(novoProduto);

            ClearConsole.clearScreen();

            System.out.println("\n-- Produto cadastrado com sucesso! --\n");

        } catch (Exception e) {
            System.err.println("\nOcorreu um erro ao cadastrar o produto: " + e.getMessage() + "\n");
        }
    }

    public void buscarProdutoPorGtin(Scanner scanner, Usuario usuarioLogado) {

        System.out.println("-------- PresenteFácil 2.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("> Início > Produtos > Buscar por GTIN\n");

        setUsuario(usuarioLogado);

        try {
            System.out.print("Digite o GTIN-13 do produto: ");
            String gtin13 = scanner.nextLine();
            Produto produto = arqProdutos.read(gtin13);

            ClearConsole.clearScreen();

            if (produto == null) {
                System.out.println("\n-- Nenhum produto encontrado com este GTIN-13. --\n");
            } else {
                exibirDetalhesProduto(scanner, produto);
            }
        } catch (Exception e) {
            System.err.println("\nErro ao buscar produto: " + e.getMessage() + "\n");
        }
    }

    public void listarTodosOsProdutos(Scanner scanner, Usuario usuarioLogado) {
        
        try {
            setUsuario(usuarioLogado);
            List<Produto> produtos = arqProdutos.listarTodos();

            if (produtos.isEmpty()) {
                System.out.println("\n-- Nenhum produto cadastrado. --\n");
                return;
            }

            produtos.sort(Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER));

            int paginaAtual = 0;
            final int ITENS_POR_PAGINA = 10;
            boolean sair = false;

            while (!sair) {
                System.out.println("------------ PresenteFácil 2.0 -----------");
                System.out.println("------------------------------------------");
                System.out.println("> Início > Produtos > Listagem\n");

                int totalPaginas = (int) Math.ceil((double) produtos.size() / ITENS_POR_PAGINA);
                System.out.println("Página " + (paginaAtual + 1) + " de " + totalPaginas);
                System.out.println("----------------------------------------\n");

                int inicio = paginaAtual * ITENS_POR_PAGINA;
                int fim = Math.min(inicio + ITENS_POR_PAGINA, produtos.size());

                for (int i = inicio; i < fim; i++) {
                    int numeroExibido = (i - inicio) + 1; // numeração reinicia a cada página
                    System.out.println("(" + numeroExibido + ") " + produtos.get(i).getNome());
                }

                System.out.println("\nEscolha uma opção para avançar ou voltar uma página:\n");
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
                            int indicePagina = escolha - 1; // índice relativo à página atual
                            if (indicePagina >= 0 && indicePagina < (fim - inicio)) {
                                Produto selecionado = produtos.get(inicio + indicePagina);
                                exibirDetalhesProduto(scanner, selecionado);
                            } else {
                                System.out.println("\n-- Número fora da página atual. --\n");
                            }
                        } catch (NumberFormatException nf) {
                            System.out.println("\n-- Opção inválida. --\n");
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("\nErro ao listar produtos: " + e.getMessage() + "\n");
        }
    }

    public void mostrarDetalhesProduto(Scanner scanner, Produto produto) throws Exception {
        exibirDetalhesProduto(scanner, produto);
    }

    private void exibirDetalhesProduto(Scanner scanner, Produto produto) throws Exception {
        
        while (true) {

            System.out.println("------------- PresenteFácil 2.0 -------------");
            System.out.println("---------------------------------------------");
            System.out.println("> Início > Produtos > Listagem > " + produto.getNome() + "\n");

            System.out.println("\n----------- Detalhes do Produto -----------\n");
            System.out.println(produto.toString());
            System.out.println("\n---------------------------------------------\n");
            
            Lista[] ListasDoUsuario = arqListaProduto.getListaByProdutoIdAndUsuario(produto.getId(), usuario.getId());
            Lista[] listasDeOutros = arqListaProduto.getListasByProdutoId(produto.getId());

            System.out.println("Aparece nas minhas listas:\n");
            for(int i = 0; i < ListasDoUsuario.length; i++) {
                System.out.println(" - " + ListasDoUsuario[i].getNome() + "\n");
            }

            System.out.println("\nAparece também em mais " + (listasDeOutros.length == 0 ? (listasDeOutros.length) : (listasDeOutros.length - 1)) + " listas" + " de outras pessoas.\n");

            System.out.println("(1) Alterar dados do produto");
            
            if (produto.isAtivo()) {
                System.out.println("(2) Inativar o produto");
            }
                System.out.println("(3) Reativar produto");

                System.out.println("(4) Reativar produto por GTIN");
            
            System.out.println("(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");

            String opcao = scanner.nextLine().trim().toUpperCase();

            ClearConsole.clearScreen();

            switch (opcao) {

                case "1":
                    alterarDadosProduto(scanner, produto);
                    // recarrega pelo ID atualizado (GTIN pode mudar)
                    produto = arqProdutos.read(produto.getID());
                    if (produto == null) return;
                    break;

                case "2":
                    if (produto.isAtivo()) {
                        inativarProduto(scanner, produto);
                    } else {
                        System.out.println("\n-- Opção inválida para produto inativo. --\n");
                    }
                    break;

                case "3":
                    if (!produto.isAtivo()) {
                        reativarProduto(scanner, produto);
                    } else {
                        System.out.println("\n-- Opção inválida para produto ativo. --\n");
                    }
                    break;

                case "4":
                    if (!produto.isAtivo()) {
                        reativarProdutoPorGtin(scanner);
                    } else {
                        System.out.println("\n-- Opção inválida para produto ativo. --\n");
                    }
                    break;

                case "R":
                    return;
                default:
                    System.out.println("\n-- Opção inválida. --\n");
            }
        }
    }

    private void alterarDadosProduto(Scanner scanner, Produto produto) {
        
        boolean sair = false;
        
        while (!sair) {

            try {
                System.out.println("\n-------- Alterar Dados do Produto --------\n");
                System.out.println("(1) Alterar descrição");
                System.out.println("(2) Alterar GTIN-13\n");
                System.out.println("(R) Retornar");
                System.out.print("\nOpção: ");
                String op = scanner.nextLine().trim().toUpperCase();

                ClearConsole.clearScreen();

                switch (op) {
                    case "1": {
                        System.out.println("\nDescrição atual: " + produto.getDescricao());
                        System.out.print("\nNova descrição: ");
                        String novaDescricao = scanner.nextLine();
                        if (!novaDescricao.trim().isEmpty()) {
                            produto.setDescricao(novaDescricao);
                            boolean ok = arqProdutos.update(produto);
                            if (ok) System.out.println("\n-- Descrição alterada com sucesso! --\n");
                            else System.out.println("\n-- Não foi possível alterar a descrição. --\n");
                        } else {
                            System.out.println("\n-- Nenhuma alteração realizada. --\n");
                        }

                        ClearConsole.clearScreen();

                        break;
                    }
                    case "2": {
                        System.out.println("\nGTIN-13 atual: " + produto.getGtin13());
                        System.out.print("\nNovo GTIN-13 (13 digitos): ");
                        String novoGtin = scanner.nextLine().trim();
                        if (novoGtin.isEmpty()) {
                            System.out.println("\n-- Nenhuma alteração realizada. --\n");
                            break;
                        }
                        if (!novoGtin.matches("\\d{13}")) {
                            System.out.println("\n-- GTIN-13 inválido. Deve conter 13 dígitos. --\n");
                            break;
                        }
                        try {
                            Produto existente = arqProdutos.read(novoGtin);
                            if (existente != null && existente.getID() != produto.getID()) {
                                System.out.println("\n-- Já existe um produto com esse GTIN-13. --\n");
                                break;
                            }
                        } catch (Exception ignored) { }

                        produto.setGtin13(novoGtin);
                        boolean ok = false;
                        try {
                            ok = arqProdutos.update(produto);
                        } catch (Exception e) {
                            System.err.println("\nErro ao atualizar GTIN: " + e.getMessage() + "\n");
                        }
                        if (ok) System.out.println("\n-- GTIN-13 alterado com sucesso! --\n");
                        else System.out.println("\n-- Não foi possível alterar o GTIN-13. --\n");
                        break;
                    }
                    case "R":
                        sair = true;
                        break;
                    default:
                        System.out.println("\n-- Opção inválida. --\n");
                }
            } catch (Exception e) {
                System.err.println("\nErro ao alterar dados do produto: " + e.getMessage() + "\n");
                return;
            }
        }
    }

    private void inativarProduto(Scanner scanner, Produto produto) {

        try {

            int qtdListas = 0;

            try {
                Lista[] listas = arqListaProduto.getListasByProdutoId(produto.getID());
                qtdListas = (listas != null) ? listas.length : 0;
            } 
            
            catch (Exception e) {
                // Se houver erro ao obter as listas, mantem qtdListas = 0 e segue o fluxo
            }

            System.out.println("\nEste produto participa de " + qtdListas + " lista(s).");
            System.out.print("\nVocê tem certeza que deseja inativar este produto? (S/N): ");
            String conf = scanner.nextLine().trim().toUpperCase();
            
            if (!"S".equals(conf)) {
                System.out.println("\n-- Operação cancelada. --\n");
                return;
            }

            produto.setAtivo(false);
            boolean ok = arqProdutos.update(produto);

            ClearConsole.clearScreen();

            if (ok) {
                System.out.println("\n-- Produto inativado com sucesso! --\n");
            } else {
                System.out.println("\n-- Não foi possível inativar o produto. --\n");
            }
        } catch (Exception e) {
            System.err.println("\nErro ao inativar produto: " + e.getMessage() + "\n");
        }
    }

    private void reativarProduto(Scanner scanner, Produto produto) {

        System.out.println("------------ PresenteFácil 2.0 ------------");
        System.out.println("-------------------------------------------");
        System.out.println("> Início > Produtos > Reativar\n");

        try {
            System.out.print("\nVocê tem certeza que deseja reativar este produto? (S/N): ");
            String conf = scanner.nextLine().trim().toUpperCase();

            if (!"S".equals(conf)) {
                System.out.println("\n-- Operação cancelada. --\n");
                return;
            }

            produto.setAtivo(true);
            boolean ok = arqProdutos.update(produto);

            ClearConsole.clearScreen();

            if (ok) {
                System.out.println("\n-- Produto reativado com sucesso! --\n");
            } 

            else {
                System.out.println("\n-- Não foi possível reativar o produto. --\n");
            }

        } catch (Exception e) {
            System.err.println("\nErro ao reativar produto: " + e.getMessage() + "\n");
        }
    }

    // Reativar produto diretamente via GTIN
    public void reativarProdutoPorGtin(Scanner scanner) {

        System.out.println("-------- PresenteFácil 2.0 --------");
        System.out.println("-----------------------------------");
        System.out.println("> Início > Produtos > Reativar\n");

        try {
            System.out.print("Digite o GTIN-13 do produto: ");
            String gtin13 = scanner.nextLine().trim();

            if (!gtin13.matches("\\d{13}")) {
                System.out.println("\n-- GTIN-13 inválido. Deve conter exatamente 13 dígitos numéricos. --\n");
                return;
            }

            Produto produto = arqProdutos.read(gtin13);
            if (produto == null) {
                System.out.println("\n-- Nenhum produto encontrado com este GTIN-13. --\n");
                return;
            }

            // Exibe informações do produto antes da confirmação
            System.out.println("\n-------- Detalhes do Produto --------");
            System.out.println(produto.toString());
            System.out.println("-------------------------------------\n");

            if (produto.isAtivo()) {
                System.out.println("\n-- Produto já está ativo. --\n");
                return;
            }

            reativarProduto(scanner, produto);

        } catch (Exception e) {
            System.err.println("\nErro ao reativar produto: " + e.getMessage() + "\n");
        }
    }
}
