package models;


public class Barbeiro extends Thread {
    //private final int id_barbeiro;
    private Cliente cliente_atual;
    private Barbearia barbearia;

    public Barbeiro(Barbearia barbearia){
        cliente_atual = null;
        this.barbearia = barbearia;  
    }

    @Override
        public void run () {
            try {
                do {
                    System.out.println(cliente_atual.getId() + " cortar cabelo");
                    cortarCabelo();
                    barbearia.finalizarCliente_chamarProximo(this);
                }while(barbearia.temClienteNaFilaDeEspera());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public void atenderCliente(Cliente cliente) {
        cliente_atual = cliente;
        System.out.println(cliente_atual.getId() + " atendido por barbeiro");
    }

    public void dispensarCliente() {
        cliente_atual = null;
    }

    public void cortarCabelo() throws InterruptedException {
        barbearia.solicitarUtensilios();
        Thread.sleep(cliente_atual.getTempoDeCorte());              
    }
}