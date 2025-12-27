package src.presenteFacil.aeds3;

import java.io.*;

// Classe que representa o par (ID, Endereço) para ser usado em índices primários.
// Implementa a interface RegistroHashExtensivel, garantindo compatibilidade com HashExtensivel.
public class ParIDEndereco implements RegistroHashExtensivel<ParIDEndereco> {

    private int id;          // Identificador único do registro
    private long endereco;   // Endereço (posição) do registro no arquivo
    private final short TAMANHO = 12; // Tamanho fixo em bytes: int (4) + long (8) = 12 bytes

    // Construtor padrão: inicializa valores inválidos
    public ParIDEndereco() {
        this.id = -1;
        this.endereco = -1L; // 'L' indica literal do tipo long
    }

    // Construtor com parâmetros
    public ParIDEndereco(int id, long endereco) {
        this.id = id;
        this.endereco = endereco;
    }

    // Hash baseado diretamente no ID.
    // Isso garante acesso direto no índice primário.
    @Override
    public int hashCode() {
        return this.id;
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public long getEndereco() {
        return this.endereco;
    }

    // Retorna o tamanho fixo do registro
    @Override
    public short size() {
        return this.TAMANHO;
    }

    // Serialização: converte o objeto para um array de bytes
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreve os atributos na ordem: ID (int) e Endereço (long)
        dos.writeInt(this.id);
        dos.writeLong(this.endereco);

        return baos.toByteArray();
    }

    // Desserialização: recria o objeto a partir de um array de bytes
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        // Lê os atributos na mesma ordem em que foram gravados
        this.id = dis.readInt();
        this.endereco = dis.readLong();
    }
}
