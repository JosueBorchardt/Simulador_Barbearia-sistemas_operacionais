package models;

import java.util.concurrent.Semaphore;

public class Barbearia {
    private Semaphore cadeiras_de_espera;
    private Semaphore cadeiras_de_barbear;
    private Semaphore pentes_tesouras;

    public Barbearia(int numero_cadeiras_de_espera, int numero_de_barbeiros){
        cadeiras_de_espera = new Semaphore(numero_cadeiras_de_espera);
        cadeiras_de_barbear = new Semaphore(numero_de_barbeiros);
        pentes_tesouras = new Semaphore(numero_de_barbeiros/2);
    }

    public void clienteEntra() throws InterruptedException {
        System.out.println("Cliente entrou na barbearia");
        if(cadeiras_de_espera.tryAcquire()){
            if(cadeiras_de_barbear.tryAcquire()){
                cadeiras_de_barbear.acquire();
                System.out.println("Cliente está sendo atendido por um barbeiro");
            }else{
                cadeiras_de_espera.acquire();
                System.out.println("Cliente está aguardando por um barbeiro");
            }
        }else{
            System.out.println("Não há cadeiras disponíveis, cliente foi embora");
        }
    }

    public void clienteSai(){
        cadeiras_de_barbear.release();
    }
}
