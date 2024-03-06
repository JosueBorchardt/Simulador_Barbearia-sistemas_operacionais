package main;

import models.Cliente;
import models.Barbearia;

import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int N_cadeiras_de_espera = 5;
        int M_barbeiros = 5;
        Barbearia barbearia = new Barbearia(N_cadeiras_de_espera, M_barbeiros);

        Queue<Cliente> lista_clientes = new LinkedList<>();

        for(int i = 0; i < 20; i++){
            try {
                Cliente new_Cliente = new Cliente(i, barbearia);
                lista_clientes.add(new_Cliente);
                new_Cliente.start();
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }            
        }
        
        for (Cliente cliente : lista_clientes) {
            cliente.join();
        }

        barbearia.imprimirRelatorioAtendimento();
    }
}
