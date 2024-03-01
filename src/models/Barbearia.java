package models;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

//import models.observers.BarbeiroObserver;

public class Barbearia {
    private Semaphore cadeiras_de_espera;
    private Semaphore cadeiras_de_barbear;
    private Utensilios pentes_tesouras;

    private Queue<Cliente> fila_clientes;
    private LinkedList<Barbeiro> barbeiros_disponiveis = new LinkedList<>();

    private int clientes_atendidos;

    public Barbearia(int numero_cadeiras_de_espera, int numero_de_barbeiros){
        cadeiras_de_espera = new Semaphore(numero_cadeiras_de_espera);
        cadeiras_de_barbear = new Semaphore(numero_de_barbeiros);
        pentes_tesouras = new Utensilios(numero_de_barbeiros/2);
        
        fila_clientes = new LinkedList<>();

        for(int i = 0; i < numero_de_barbeiros; i++) {
            barbeiros_disponiveis.add(new Barbeiro(i+1, this));
        }

        clientes_atendidos = 0;
    }

    public boolean clienteEntra(Cliente cliente) throws InterruptedException {
        System.out.println("Cliente " + cliente.getId() + " entrou na barbearia");
        
        if(cadeiras_de_barbear.tryAcquire()) {
            Barbeiro barbeiro = barbeiros_disponiveis.removeFirst();
            barbeiro.atenderCliente(cliente);
            barbeiro.start();
            System.out.println("Cliente " + cliente.getId() + " está sendo atendido por um barbeiro");
            return true;
        }
        
        if(cadeiras_de_espera.tryAcquire()){
            fila_clientes.add(cliente);
            System.out.println("Cliente " + cliente.getId() + " está aguardando por um barbeiro");
            return true;
        }
        
        System.out.println("Não há cadeiras disponíveis, cliente " + cliente.getId() + " foi embora");
        return false;
    }

    public void solicitarUtensilios() throws InterruptedException {
        pentes_tesouras.pegarUtensilios();
    }

    //@Override
    public void clienteAtendido(Barbeiro barbeiro, Cliente cliente) throws InterruptedException {
        pentes_tesouras.liberarUtensilios();
        
        cadeiras_de_barbear.release();        
        barbeiro.dispensarCliente();
        barbeiros_disponiveis.add(barbeiro);
        clientes_atendidos++;
        proximoCliente();
    }

    public void proximoCliente() throws InterruptedException {
        if(!fila_clientes.isEmpty()) {
            cadeiras_de_barbear.acquire();
            cadeiras_de_espera.release();
            Cliente proximo_cliente = fila_clientes.poll();
            Barbeiro barbeiro = barbeiros_disponiveis.removeFirst();
            barbeiro.atenderCliente(proximo_cliente);  
            barbeiro.start();         
        }
    }

    
}
