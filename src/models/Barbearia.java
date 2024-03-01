package models;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Barbearia {
    private Semaphore cadeiras_de_espera;
    private Utensilios pentes_tesouras;
    
    private Queue<Cliente> clientes_em_espera;
    private Queue<Barbeiro> barbeiros_disponiveis;

    public Barbearia(int num_cadeiras_de_espera, int num_barbeiros) {
        cadeiras_de_espera = new Semaphore(num_cadeiras_de_espera);
        pentes_tesouras = new Utensilios(num_barbeiros/2);
        
        clientes_em_espera = new LinkedList<>();
        barbeiros_disponiveis = new LinkedList<>();
        for(int i = 0; i < num_barbeiros; i++) {
            barbeiros_disponiveis.add(new Barbeiro(this));
        }
    }

    public void clienteEntra(Cliente cliente) {
        if(barbeiros_disponiveis.isEmpty()) {
            if(this.cadeiras_de_espera.tryAcquire()) {
                System.out.println(cliente.getId() + " cadeira de espera"); 
                clientes_em_espera.add(cliente);                   
            }else{
                System.out.println(cliente.getId() + " foi embora"); 
            } 
        }else{
            Barbeiro barbeiro = barbeiros_disponiveis.poll();
            barbeiro.atenderCliente(cliente);
            barbeiro.start();
        }
    }

    public boolean temClienteNaFilaDeEspera() {
        return (!clientes_em_espera.isEmpty());
    }

    public void solicitarUtensilios() throws InterruptedException {
        pentes_tesouras.pegarUtensilios();
    }

    public void finalizarCliente_chamarProximo(Barbeiro barbeiro) {
        pentes_tesouras.liberarUtensilios();
        barbeiro.dispensarCliente();
        cadeiras_de_espera.release();
        if(!clientes_em_espera.isEmpty()){
            Cliente proximo_cliente = clientes_em_espera.poll();
            barbeiro.atenderCliente(proximo_cliente);
        }        
    }
}
