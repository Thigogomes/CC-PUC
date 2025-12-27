package src.presenteFacil.model;

import src.presenteFacil.aeds3.Arquivo;
import src.presenteFacil.aeds3.HashExtensivel;
import src.presenteFacil.aeds3.ParGtin13ID;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class ArquivoProduto extends Arquivo<Produto> {

    private HashExtensivel<ParGtin13ID> indiceIndiretoGtin13;

    public ArquivoProduto() throws Exception {
        super("produtos", Produto.class.getConstructor());
        
        indiceIndiretoGtin13 = new HashExtensivel<>(
                ParGtin13ID.class.getConstructor(),
                4,
                ".\\data\\produtos\\indiceGtin13.d.db",
                ".\\data\\produtos\\indiceGtin13.c.db"
        );
    }

    @Override
    public int create(Produto p) throws Exception {
        int id = super.create(p);
        indiceIndiretoGtin13.create(new ParGtin13ID(p.getGtin13(), id));
        return id;
    }

    public Produto read(String gtin13) throws Exception {
        ParGtin13ID pgi = indiceIndiretoGtin13.read(ParGtin13ID.hash(gtin13));
        if (pgi == null) {
            return null;
        }
        return super.read(pgi.getId());
    }

    @Override
    public boolean delete(int id) throws Exception {
        Produto p = super.read(id);
        if (p != null) {
            if (super.delete(id)) {
                return indiceIndiretoGtin13.delete(ParGtin13ID.hash(p.getGtin13()));
            }
        }
        return false;
    }

    @Override
    public boolean update(Produto novoProduto) throws Exception {
        Produto produtoAntigo = super.read(novoProduto.getID());
        if (produtoAntigo == null) {
            return false;
        }

        if (super.update(novoProduto)) {
            // Se o GTIN mudou, atualiza o índice secundário
            if (!novoProduto.getGtin13().equals(produtoAntigo.getGtin13())) {
                indiceIndiretoGtin13.delete(ParGtin13ID.hash(produtoAntigo.getGtin13()));
                indiceIndiretoGtin13.create(new ParGtin13ID(novoProduto.getGtin13(), novoProduto.getID()));
            }
            return true;
        }
        return false;
    }

    public List<Produto> listarTodos() throws Exception {
        // Faz varredura sequencial no arquivo para garantir que
        // todos os registros ativos presentes no .db sejam listados,
        // mesmo que alguma entrada no índice direto esteja ausente.
        List<Produto> produtos = new ArrayList<>();

        try (RandomAccessFile arq = new RandomAccessFile(".\\data\\produtos\\produtos.db", "r")) {
            if (arq.length() < 12) return produtos; // arquivo sem cabeçalho

            // Pula o cabeçalho: [int ultimoID][long cabecaListaExcluidos]
            arq.seek(0);
            arq.readInt();
            arq.readLong();

            while (arq.getFilePointer() < arq.length()) {
                byte lapide = arq.readByte();
                short tamanho = arq.readShort();

                if (tamanho < 0) break; // sanidade

                if (lapide == ' ') {
                    byte[] ba = new byte[tamanho];
                    arq.readFully(ba);
                    Produto p = new Produto();
                    p.fromByteArray(ba);
                    if (p.isAtivo()) produtos.add(p);
                } else {
                    // Registro excluído: avança o ponteiro
                    arq.skipBytes(tamanho);
                }
            }
        }

        return produtos;
    }
}