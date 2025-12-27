package src.presenteFacil.model;

import src.presenteFacil.aeds3.Arquivo;
import src.presenteFacil.aeds3.HashExtensivel;
import src.presenteFacil.aeds3.ParEmailID;

public class ArquivoUsuario extends Arquivo<Usuario> {

    private HashExtensivel<ParEmailID> indiceIndiretoEmail;

    public ArquivoUsuario() throws Exception {
        super("usuarios", Usuario.class.getConstructor());

        indiceIndiretoEmail = new HashExtensivel<>(
                ParEmailID.class.getConstructor(),
                4,
                ".\\data\\usuarios\\indiceEmail.d.db",
                ".\\data\\usuarios\\indiceEmail.c.db"  
        );
    }

    @Override
    public int create(Usuario u) throws Exception {
        int id = super.create(u); 
        indiceIndiretoEmail.create(new ParEmailID(u.getEmail(), id)); // cria no índice
        return id;
    }

    public Usuario read(String email) throws Exception {
        ParEmailID pei = indiceIndiretoEmail.read(ParEmailID.hash(email));
        if (pei == null) 
            return null;
        
        return read(pei.getId()); // usa o método read(int id) da classe pai
    }

    public boolean delete(String email) throws Exception {
        ParEmailID pei = indiceIndiretoEmail.read(ParEmailID.hash(email));
        if (pei != null) {
            return delete(pei.getId());
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario u = super.read(id);
        if (u != null) {
            if (super.delete(id)) {
                return indiceIndiretoEmail.delete(ParEmailID.hash(u.getEmail()));
            }
        }
        return false;
    }

    @Override
    public boolean update(Usuario novoUsuario) throws Exception {
        Usuario usuarioVelho = super.read(novoUsuario.getId());

        if (usuarioVelho == null) {
            return false;
        }

        if (super.update(novoUsuario)) {
            if (!novoUsuario.getEmail().equals(usuarioVelho.getEmail())) {
                indiceIndiretoEmail.delete(ParEmailID.hash(usuarioVelho.getEmail()));
                indiceIndiretoEmail.create(new ParEmailID(novoUsuario.getEmail(), novoUsuario.getId()));
            }
            return true;
        }
        return false;
    }

}