package com.antonio.hundirlaflota.Modelos;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
public class Jugador1 extends Jugador {
    public Jugador1() {
        super();
        this.setNombre("maquina");
        this.setVer(true);
    }

    @OneToOne
    private Casilla ultTocado = null;
    @Getter
    private int estado = 0;


    public Casilla getUllTocado() {
        return ultTocado;
    }

    public void setUllTocado(Casilla ultidisparo) {
        this.ultTocado = ultidisparo;
    }


}
