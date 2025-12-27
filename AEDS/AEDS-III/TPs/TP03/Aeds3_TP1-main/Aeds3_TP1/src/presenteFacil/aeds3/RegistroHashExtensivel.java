package src.presenteFacil.aeds3;

import java.io.IOException;

/**
 * Interface que define o contrato para registros usados em um
 * Hash Extensível.
 *
 * Garante que qualquer classe que a implemente possa:
 * - Ser identificada por uma chave de hash.
 * - Ter tamanho fixo em bytes (essencial para manipulação em arquivos).
 * - Ser convertida para um vetor de bytes (serialização).
 * - Ser reconstruída a partir de um vetor de bytes (desserialização).
 *
 * @param <T> Tipo da classe que implementa a interface.
 */
public interface RegistroHashExtensivel<T> {

    /**
     * Retorna a chave numérica que será usada como base
     * para a função de hash no diretório.
     * 
     * @return chave de hash do registro.
     */
    public int hashCode();

    /**
     * Retorna o tamanho FIXO do registro em bytes.
     * Esse valor deve ser constante para todos os objetos do mesmo tipo.
     *
     * @return tamanho do registro em bytes.
     */
    public short size();

    /**
     * Converte o registro em um vetor de bytes.
     * Usado para gravar o objeto em arquivos binários.
     *
     * @return vetor de bytes representando o registro.
     * @throws IOException caso ocorra erro na serialização.
     */
    public byte[] toByteArray() throws IOException;

    /**
     * Reconstrói o registro a partir de um vetor de bytes.
     * Usado para ler o objeto de arquivos binários.
     *
     * @param ba vetor de bytes que contém a representação do registro.
     * @throws IOException caso ocorra erro na desserialização.
     */
    public void fromByteArray(byte[] ba) throws IOException;
}
