package models;


public class Barbeiro extends Thread {
    private final int id_barbeiro;
    private Cliente cliente_atual;
    private Barbearia barbearia;

    private boolean cortando_cabelo;

    public Barbeiro(int id_barbeiro, Barbearia barbearia){
        this.id_barbeiro = id_barbeiro + 1;
        cliente_atual = null;
        this.barbearia = barbearia;
        cortando_cabelo = false;
        start();  
    }

    @Override
    public void run () {
        try {
            while(!isInterrupted()) {
                if(barbearia.estaAberta() || barbearia.temClienteNaFilaDeEspera() || cliente_atual != null) {
                    if(cliente_atual != null) {
                        System.out.println(cliente_atual.getId() + " cortar cabelo");
                        cortando_cabelo = true;
                        cortarCabelo();
                        cortando_cabelo = false;
                        barbearia.finalizarCliente_chamarProximo(this);
                    } 
                }else{
                    this.interrupt();
                }                                       
            }
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

    public void ecerrarBarbearia() {

    }

    public boolean estaCortandoCabelo(){
        return cortando_cabelo;
    }
}