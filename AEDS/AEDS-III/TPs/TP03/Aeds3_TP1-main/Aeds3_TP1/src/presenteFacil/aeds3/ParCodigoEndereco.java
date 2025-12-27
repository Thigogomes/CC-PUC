package src.presenteFacil.aeds3;

import java.io.*;

public class ParCodigoEndereco implements RegistroHashExtensivel<ParCodigoEndereco> {

    private String codigo;
    private long endereco; 

    private final short TAMANHO = 28;

    public ParCodigoEndereco() {
        this.codigo = "";
        this.endereco = -1L;
    }

    public ParCodigoEndereco(String codigo, long endereco) {
        this.codigo = codigo;
        this.endereco = endereco;
    }

    @Override
    public int hashCode() {
        return Math.abs(this.codigo.hashCode());
    }

    public static int hash(String codigo) {
        return Math.abs(codigo.hashCode());
    }

    public String getCodigo() {
        return this.codigo;
    }

    public long getEndereco() {
        return this.endereco;
    }

    @Override
    public short size() {
        return this.TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeUTF(this.codigo);
        dos.writeLong(this.endereco);

        byte[] dados = baos.toByteArray();

        byte[] registro = new byte[TAMANHO];

        for (int i = 0; i < dados.length && i < TAMANHO; i++) {
            registro[i] = dados[i];
        }

        return registro;
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        this.codigo = dis.readUTF();
        this.endereco = dis.readLong();
    }
}