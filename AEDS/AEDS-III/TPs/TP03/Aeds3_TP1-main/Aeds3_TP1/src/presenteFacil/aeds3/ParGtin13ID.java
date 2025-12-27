package src.presenteFacil.aeds3;

import java.io.*;

public class ParGtin13ID implements RegistroHashExtensivel<ParGtin13ID> {

    private String gtin13;
    private int id;
    private final short TAMANHO = 32; // GTIN-13 (String) + ID (int)

    public ParGtin13ID() {
        this("", -1);
    }

    public ParGtin13ID(String gtin13, int id) {
        this.gtin13 = gtin13;
        this.id = id;
    }

    public String getGtin13() {
        return gtin13;
    }

    public int getId() {
        return id;
    }

    public static int hash(String gtin13) {
        return Math.abs(gtin13.hashCode());
    }

    @Override
    public int hashCode() {
        return Math.abs(this.gtin13.hashCode());
    }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(gtin13);
        dos.writeInt(id);

        byte[] dados = baos.toByteArray();
        byte[] registro = new byte[TAMANHO];
        System.arraycopy(dados, 0, registro, 0, Math.min(dados.length, TAMANHO));
        
        return registro;
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.gtin13 = dis.readUTF();
        this.id = dis.readInt();
    }
}