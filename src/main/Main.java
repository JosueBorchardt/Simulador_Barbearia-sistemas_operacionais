package main;

import models.Cliente;
import models.Barbearia;

public class Main {
    public static void main(String[] args) {
        int N_cadeiras_de_espera = 5;
        int M_barbeiros = 5;
        Barbearia barbearia = new Barbearia(N_cadeiras_de_espera, M_barbeiros);

        for(int i = 0; i < 40; i++){
            try {
                Cliente new_Cliente = new Cliente(i, barbearia);
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            
        }        
    }
}
