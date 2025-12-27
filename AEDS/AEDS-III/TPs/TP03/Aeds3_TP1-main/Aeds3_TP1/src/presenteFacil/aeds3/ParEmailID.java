package src.presenteFacil.aeds3;

import java.io.*;

/**
 * Classe que representa um par (email, id) utilizado no índice hash extensível.
 * Implementa a interface RegistroHashExtensivel, o que permite que objetos
 * dessa classe sejam armazenados em um arquivo de hash de tamanho fixo.
 */
public class ParEmailID implements RegistroHashExtensivel<ParEmailID> {

    private String email;  // chave de busca (única para cada usuário)
    private int id;        // identificador do usuário correspondente no arquivo principal

    // Tamanho fixo do registro em bytes.
    // Isso facilita o armazenamento em estruturas de hash no disco.
    // Escolhido 64 como limite (deve suportar a maioria dos e-mails).
    private final short TAMANHO = 64;

    /** Construtor padrão (necessário para uso em estruturas genéricas). */
    public ParEmailID() {
        this.email = "";
        this.id = -1; // id inválido para indicar estado "vazio"
    }

    /** Construtor com dados. */
    public ParEmailID(String email, int id) {
        this.email = email;
        this.id = id;
    }

    /**
     * Código de hash baseado no email.
     * Usamos Math.abs() para garantir que o valor seja sempre positivo.
     */
    @Override
    public int hashCode() {
        return Math.abs(this.email.hashCode());
    }

    /** Método estático auxiliar: calcula hash diretamente a partir de um email. */
    public static int hash(String email) {
        return Math.abs(email.hashCode());
    }

    // Getters
    public String getEmail() {
        return this.email;
    }

    public int getId() {
        return this.id;
    }

    /** Retorna o tamanho fixo do registro (necessário para o hash extensível). */
    @Override
    public short size() {
        return this.TAMANHO;
    }

    /**
     * Serializa o objeto para um array de bytes de tamanho fixo (TAMANHO).
     * Se o conteúdo for menor que TAMANHO, preenche o restante com zeros (padding).
     */
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreve email e id no fluxo
        dos.writeUTF(this.email);
        dos.writeInt(this.id);

        // Array com os dados serializados
        byte[] dados = baos.toByteArray();

        // Cria o registro de tamanho fixo
        byte[] registro = new byte[TAMANHO];

        // Copia os dados para o registro fixo (se sobrar espaço, mantém zeros)
        for (int i = 0; i < dados.length && i < TAMANHO; i++) {
            registro[i] = dados[i];
        }

        return registro;
    }

    /**
     * Reconstrói o objeto a partir de um array de bytes lido do arquivo de hash.
     */
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        this.email = dis.readUTF();
        this.id = dis.readInt();
    }
}
