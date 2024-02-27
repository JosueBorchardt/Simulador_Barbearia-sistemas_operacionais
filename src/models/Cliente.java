package models;

public class Cliente extends Thread {
    private final int id_cliente;
    private int tempo_de_corte;
    private Barbearia barbearia;
    
    public Cliente(int id_cliente) {
        this.id_cliente = id_cliente;
        this.tempo_de_corte = (int) (Math.random() * 3000 + 3000);
    }

    public long getId() {
        return id_cliente;
    }

    @Override
    public void run() {
        
    }

}
