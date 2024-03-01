package models;

import java.util.concurrent.Semaphore;

public class Utensilios {
    private Semaphore pentes;
    private Semaphore tesouras;

    public Utensilios(int num_pentes_tesouras) {
        pentes = new Semaphore(num_pentes_tesouras);
        tesouras = new Semaphore(num_pentes_tesouras);
    }

    public boolean utensiliosDiponiveis() {
        return (pentes.tryAcquire() && tesouras.tryAcquire());
    }

    public void pegarUtensilios() throws InterruptedException {
        pentes.acquire();
        tesouras.acquire();
    }

    public void liberarUtensilios() {
        pentes.release();
        tesouras.release();
    }    
}
