package models;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Barbearia {
    private Semaphore cadeiras_de_espera;
    private Utensilios pentes_tesouras;
    private boolean barbearia_aberta;
    
    private Queue<Cliente> clientes_em_espera;
    private Queue<Barbeiro> barbeiros_disponiveis;

    private int num_clientes_atendidos;
    private int num_clientes_desistiram;

    private LinkedList<Cliente> clientes_nao_atendidos;
    private LinkedList<Barbeiro> lista_de_barbeiros;

    public Barbearia(int num_cadeiras_de_espera, int num_barbeiros) {
        cadeiras_de_espera = new Semaphore(num_cadeiras_de_espera);
        pentes_tesouras = new Utensilios(num_barbeiros/2);
        barbearia_aberta = true;
        
        clientes_em_espera = new LinkedList<>();
        barbeiros_disponiveis = new LinkedList<>();
        lista_de_barbeiros = new LinkedList<>();
        clientes_nao_atendidos = new LinkedList<>();

        for(int i = 0; i < num_barbeiros; i++) {
            Barbeiro barbeiro = new Barbeiro(i, this);
            barbeiros_disponiveis.add(barbeiro);
            lista_de_barbeiros.add(barbeiro);
        }

        num_clientes_atendidos = 0;
        num_clientes_desistiram = 0;
    }

    public void clienteEntra(Cliente cliente) {
        if(barbeiros_disponiveis.isEmpty()) {
            if(this.cadeiras_de_espera.tryAcquire()) {
                System.out.println(cliente.getId() + " cadeira de espera"); 
                clientes_em_espera.add(cliente);                   
            }else{
                System.out.println(cliente.getId() + " foi embora");
                num_clientes_desistiram++;
                clientes_nao_atendidos.add(cliente);
            } 
        }else{
            Barbeiro barbeiro = barbeiros_disponiveis.poll();
            barbeiro.atenderCliente(cliente);
        }
    }

    public boolean temClienteNaFilaDeEspera() {
        return (!clientes_em_espera.isEmpty());
    }

    public void solicitarUtensilios() throws InterruptedException {
        pentes_tesouras.pegarUtensilios();
    }

    public void finalizarCliente_chamarProximo(Barbeiro barbeiro) throws InterruptedException {
        pentes_tesouras.liberarUtensilios();
        barbeiro.dispensarCliente();        
        num_clientes_atendidos++;
        if(temClienteNaFilaDeEspera()){
            cadeiras_de_espera.release();
            Cliente proximo_cliente = clientes_em_espera.poll(); 
            barbeiro.atenderCliente(proximo_cliente);
        }else{           
            barbeiros_disponiveis.add(barbeiro);
        }    
    }

    public void imprimirRelatorioAtendimento() throws InterruptedException {
        fecharBarbearia();
        for (Barbeiro barbeiro : lista_de_barbeiros) {
            barbeiro.join();
        }
        System.out.println("Número de cliente atendidos: " + num_clientes_atendidos);
        System.out.println("Números de clientes que destiram do atendimento: " + num_clientes_desistiram);
    }

        public boolean estaAberta() {
            return barbearia_aberta;
        }

        public void fecharBarbearia() {
            this.barbearia_aberta = false;
        }
}
