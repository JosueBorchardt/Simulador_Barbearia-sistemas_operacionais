package models;


public class Barbeiro extends Thread {
    private final int id_barbeiro;
    private Cliente cliente_atual;
    private Barbearia barbearia;

    public Barbeiro(int id_barbeiro, Barbearia barbearia){
        this.id_barbeiro = id_barbeiro;
        cliente_atual = null;
        this.barbearia = barbearia;
        //this.start();  
    }

    @Override
        public void run () {
            try {
                //while(true) {
                //if(cliente_atual != null) {
                System.out.println("Cliente " + cliente_atual.getId() + " está cortando cabelo");
                cortarCabelo();
                barbearia.clienteAtendido(this, cliente_atual);
                //}
                //}
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public void atenderCliente(Cliente cliente) {
        this.cliente_atual = cliente;
    }

    public void cortarCabelo() throws InterruptedException {
        barbearia.solicitarUtensilios();
        Thread.sleep(cliente_atual.getTempoDeCorte());
        barbearia.clienteAtendido(this, cliente_atual);       
    }

    public void dispensarCliente() {
        this.cliente_atual = null;
    }

}