package src.presenteFacil.aeds3;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

import src.presenteFacil.model.Registro;

public class Arquivo<T extends Registro> {
    
    final int TAM_CABECALHO = 12;

    private RandomAccessFile arquivo;
    private String nomeArquivo;
    private Constructor<T> construtor;
    private HashExtensivel<ParIDEndereco> indiceDireto;

    public Arquivo(String na, Constructor<T> c) throws Exception {
        File d = new File(".\\data");
        if (!d.exists()) d.mkdir();

        d = new File(".\\data\\" + na);
        if (!d.exists()) d.mkdir();

        this.nomeArquivo = ".\\data\\" + na + "\\" + na + ".db";
        this.construtor = c;
        this.arquivo = new RandomAccessFile(this.nomeArquivo, "rw");

        if (arquivo.length() < TAM_CABECALHO) {
            arquivo.writeInt(0);
            arquivo.writeLong(-1);
        }

        indiceDireto = new HashExtensivel<>(
            ParIDEndereco.class.getConstructor(),
            4,
            ".\\data\\" + na + "\\" + na + ".d.db",
            ".\\data\\" + na + "\\" + na + ".c.db"
        );
    }

    public int create(T obj) throws Exception {
        arquivo.seek(0);
        int proximoID = arquivo.readInt() + 1;
        arquivo.seek(0);
        arquivo.writeInt(proximoID);
        obj.setID(proximoID);

        byte[] b = obj.toByteArray();

        long endereco = getDeleted(b.length);
        if (endereco == -1) {
            arquivo.seek(arquivo.length());
            endereco = arquivo.getFilePointer();
            arquivo.writeByte(' ');
            arquivo.writeShort(b.length);
            arquivo.write(b);
        } else {
            arquivo.seek(endereco);
            arquivo.writeByte(' ');
            arquivo.skipBytes(2);
            arquivo.write(b);
        }

        indiceDireto.create(new ParIDEndereco(proximoID, endereco));
        return obj.getID();
    }

    public T read(int id) throws Exception {
        ParIDEndereco pid = indiceDireto.read(id);
        if (pid != null) {
            arquivo.seek(pid.getEndereco());
            T obj = construtor.newInstance();
            byte lapide = arquivo.readByte();

            if (lapide == ' ') {
                short tam = arquivo.readShort();
                byte[] b = new byte[tam];
                arquivo.read(b);
                obj.fromByteArray(b);
                if (obj.getID() == id) return obj;
            }
        }
        return null;
    }

    public boolean delete(int id) throws Exception {
        ParIDEndereco pie = indiceDireto.read(id);
        if (pie != null) {
            arquivo.seek(pie.getEndereco());
            byte lapide = arquivo.readByte();

            if (lapide == ' ') {
                short tam = arquivo.readShort();
                byte[] b = new byte[tam];
                arquivo.read(b);
                T obj = construtor.newInstance();
                obj.fromByteArray(b);

                if (obj.getID() == id) {
                    if (indiceDireto.delete(id)) {
                        arquivo.seek(pie.getEndereco());
                        arquivo.write('*'); 
                        addDeleted(tam, pie.getEndereco());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Atualiza um registro existente.
     */
    public boolean update(T novoObj) throws Exception {
        ParIDEndereco pie = indiceDireto.read(novoObj.getID());
        if (pie == null) {
            return false; // Registro não encontrado no índice
        }

        // Lê o tamanho do registro antigo para comparar
        arquivo.seek(pie.getEndereco());
        byte lapide = arquivo.readByte();
        if (lapide == '*') {
            return false; // Registro marcado como excluído
        }
        short tamAntigo = arquivo.readShort();

        // Serializa o novo objeto para saber seu tamanho
        byte[] b_novo = novoObj.toByteArray();
        short tamNovo = (short) b_novo.length;

        // Se o novo registro for menor ou do mesmo tamanho, sobrescreve no mesmo lugar
        if (tamNovo <= tamAntigo) {
            arquivo.seek(pie.getEndereco() + 1); // Volta para a posição do tamanho
            arquivo.writeShort(tamNovo);         // ESCREVE O NOVO TAMANHO (A CORREÇÃO)
            arquivo.write(b_novo);               // Escreve os novos dados
        } 
        // Se o novo registro for maior, apaga o antigo e escreve o novo em outro lugar
        else {
            // 1. Marca o registro antigo como excluído
            arquivo.seek(pie.getEndereco());
            arquivo.writeByte('*');
            addDeleted(tamAntigo, pie.getEndereco());

            // 2. Encontra um novo local para o registro (reutilizado ou no fim do arquivo)
            long novoEndereco = getDeleted(tamNovo);
            if (novoEndereco == -1) {
                novoEndereco = arquivo.length();
            }
            
            // 3. Escreve o novo registro no novo local
            arquivo.seek(novoEndereco);
            arquivo.writeByte(' ');
            arquivo.writeShort(tamNovo);
            arquivo.write(b_novo);

            // 4. Atualiza o índice para apontar para o novo endereço
            indiceDireto.update(new ParIDEndereco(novoObj.getID(), novoEndereco));
        }
        return true;
    }

    public void addDeleted(int tamanhoEspaco, long enderecoEspaco) throws Exception {
        long anterior = 4;
        arquivo.seek(anterior);
        long endereco = arquivo.readLong();
        long proximo;
        int tamanho;

        if (endereco == -1) {
            arquivo.seek(4);
            arquivo.writeLong(enderecoEspaco);
            arquivo.seek(enderecoEspaco+3);
            arquivo.writeLong(-1);
        } else {
            do {
                arquivo.seek(endereco+1);
                tamanho = arquivo.readShort();
                proximo = arquivo.readLong();

                if (tamanho >= tamanhoEspaco) {
                    if (anterior == 4) arquivo.seek(anterior);
                    else arquivo.seek(anterior+3);
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco+3);
                    arquivo.writeLong(endereco);
                    break;
                }
                if (proximo == -1) {
                    arquivo.seek(endereco+3);
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco+3);
                    arquivo.writeLong(-1);
                    break;
                }
                anterior = endereco;
                endereco = proximo;
            } while (endereco != -1);
        }
    }

    public long getDeleted(int tamanhoNecessario) throws Exception {
        long anterior = 4;
        arquivo.seek(anterior);
        long endereco = arquivo.readLong();
        long proximo;
        int tamanho;

        while (endereco != -1) {
            arquivo.seek(endereco+1);
            tamanho = arquivo.readShort();
            proximo = arquivo.readLong();

            if (tamanho >= tamanhoNecessario) {
                if (anterior == 4) arquivo.seek(anterior);
                else arquivo.seek(anterior+3);
                arquivo.writeLong(proximo);
                break;
            }
            anterior = endereco;
            endereco = proximo;
        }
        return endereco;
    }

    public void close() throws Exception {
        arquivo.close();
    }
}